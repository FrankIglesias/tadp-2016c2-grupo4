class Module
  def uninclude(mod)
    mod.instance_methods.each do |method|
      undef_method(method)
    end
  end
end


class TADsPec
  def self.obtener_todas_las_clases
    var = (Object.constants).map { |constant| Object.const_get(constant) }
    var = var.select { |constant| constant.is_a? Class }
  end

  def self.search_all_test_suites
    @unit_test_clases = obtener_todas_las_clases
    @unit_test_clases = @unit_test_clases.select { |klass| (klass.instance_methods false).any? { |method| method.to_s.start_with?('testear_que_') } }
  end

  def self.agregar_suites(clase)
    if clase.is_a? Class
      @unit_test_clases << clase
    else
      search_all_test_suites
    end
  end

  def self.iniciar_entorno
    deberia_proc = proc { |algo| TestContex.deberia_array << (algo.call(self)) }
    mockear_proc = proc { |symbol, &block| self.send :alias_method, ('mock_'+symbol.to_s).to_sym, symbol
    self.send :define_method, symbol, block }
    #Object.send :include, ModuleRemover
    #Proc.send :include, TestSuite
    Object.send :define_method, :deberia, deberia_proc
    Proc.send :define_method, :deberia, deberia_proc
    Class.send :define_method, :mockear, mockear_proc
  end

  def self.remove_mock_methods
    mock_method_classes = obtener_todas_las_clases
    mock_method_classes = mock_method_classes.select { |klass| (klass.instance_methods).any? { |method| method.to_s.start_with?('mock_') } }
    mock_method_classes.each { |mocked_class|
      mock_methods = mocked_class.instance_methods.select { |symbol| symbol.to_s.start_with?('mock_') }
      mock_methods.each { |mock_method|
        metodo_a_modificar = mock_method.to_s.sub('mock_', "")
        mocked_class.send :define_method, (metodo_a_modificar.to_sym), (mocked_class.instance_method mock_method)
        mocked_class.send(:undef_method, mock_method) }
    }
  end


  def self.remover_metodos_peligrosos
    Object.send :remove_method, :deberia
    Proc.send :remove_method, :deberia
    Class.send :remove_method, :mockear
    remove_mock_methods
    ## remover_modulo_test
  end


  def self.remover_modulo_test
    Object.uninclude TestSuite
    Proc.send :uninclude, TestSuite
  end

  def self.testear (clase = nil, *args)
    @unit_test_clases = []
    @test_totales = []
    iniciar_entorno
    agregar_suites clase

    @unit_test_clases.each do |unit_test|
      unit_test.send :include, TestSuite
      @test_totales << TestContex.correr(unit_test, args)
      puts "\n"
    end

    remover_metodos_peligrosos
    generar_reporte(@test_totales.flatten)

  end

  def self.generar_reporte(i)
    print "\n Se corrieron #{i.count} tests de los cuales: "
    print "\n #{i.count true} Pasaron!"
    print "\n #{i.count false} Fallaron!"
    print "\n #{i.count nil} Explotaron! \n"
    i
  end
end

class TADResult

  attr_accessor :resultado, :esperado, :recibido

  def initialize resultado, esperado= nil, recibido= nil
    self.resultado= resultado
    self.esperado= esperado
    self.recibido= recibido
  end

  def analizar_resultados
    if (self.resultado==false)
      puts("esperaba #{esperado} y recibi #{recibido}")
    end
    if (self.resultado==nil)
      puts recibido.backtrace
    end
    self.resultado
  end
end


class TestContex

  def self.correr(clase, lista)
    @var = []
    @object = clase
    @object.class.send(:include, TestSuite)
    if lista.length > 0
      @test_methods = lista
    else
      @test_methods = (@object.instance_methods false).select { |m| m.to_s.start_with?('testear_que_') }
    end
    print "\nLos test de la suite #{@object}:"
    run_test_suite_tests
    @var
  end

  def self.deberia_array
    @deberia
  end

  def self.deberia_init
    @deberia = []
  end

  def self.run_test_suite_tests
    @test_methods.each do |m|
      @var << @object.instance_eval do
        begin
          TestContex.deberia_init
          test = self.new
          analizado = analizar_resultado(test, m.to_sym)
          print "\n El resultado del test: #{m} -> fue: #{analizado.to_s.upcase}"
        ##  eliminar_mock_methods
        ##  TestSuite.methods.each { |suite_method| self.undef_method suite_method }
          analizado
        rescue Exception => a
          print "\n El resultado del test #{m} -> fue: EXPLOSIVO = #{a}"
          (nil)
        end
      end
    end
  end

end


class TADPObject
  def initialize algo
    @object= algo
  end

  def run algo
    resultado= @object.eql? algo
    TADResult.new resultado, @object, algo
  end
end

class TADPMethodHistory
  attr_accessor :method, :params

  def initialize method, *params
    self.method= method
    self.params = *params
  end

  def se_llamo symbol, *params
    if params.length==0
      self.method ==symbol
    else
      (self.method == symbol) && (self.params.eql? (params))
    end
  end
end

class TADPSpy
  attr_accessor :spying_object

  def initialize objeto
    self.spying_object = objeto.clone
    @method_list = spying_object.class.instance_methods false
    spying_object.singleton_class.send :attr_accessor, :lista_metodos
    spying_object.send :lista_metodos=, []
    espiar_metodos
  end

  def espiar_metodos
    @method_list.each do |m|
      self.spying_object.class.mockear m do
      |*args|
        viejo_metodo = ('mock_'+ m.to_s).to_sym
        self.lista_metodos << TADPMethodHistory.new(m, args)
        self.send viejo_metodo, *args
      end

    end
  end

  def method_missing (symbol, *args)
    if spying_object.class.instance_methods.include? symbol
      spying_object.send symbol, *args
    else
      super(symbol, *args)
    end
  end
end

class TADPMethodTester
  attr_accessor :metodo

  def initialize metodo
    self.metodo = metodo
  end

  def call algo
    resultado= algo.spying_object.lista_metodos.any? { |x| x.se_llamo self.metodo }
    TADResult.new resultado, "Uno de #{algo.spying_object.lista_metodos}", metodo
  end

  def veces numero
    self.define_singleton_method :call do |x|
      variable = x.spying_object.lista_metodos.select { |m| m.se_llamo self.metodo }
      resultado= variable.length ==numero
      TADResult.new resultado, "que el metodo haya sido llamado #{variable.length} veces", numero
    end
    self
  end

  def con_argumentos *args
    self.define_singleton_method :call do
    |x|
      variable = x.spying_object.lista_metodos.select { |m| m.se_llamo metodo, args }
      resultado= variable.length>0
      TADResult.new resultado, "que el metodo haya recibido #{variable.map { |m| m.params }}", args
    end
    self
  end
end

module TestSuite

  def analizar_resultado(objeto, metodo)
    begin
      objeto.send metodo
      TestContex.deberia_array.all? { |resultado| resultado.analizar_resultados }
    rescue Exception => ex
      puts ex.backtrace
      raise
    end
  end

  def eliminar_mock_methods
    metodos_mockeados = self.instance_methods.select { |symbol| symbol.to_s.start_with?('mock_') }
    metodos_mockeados.each { |mock_method|
      metodo_a_modificar = mock_method.to_s.sub('mock_', "")
      self.send :define_method, (metodo_a_modificar.to_sym), (self.instance_method mock_method)
      self.send :undef, mock_method }
  end

  def espiar(objeto_espiado)
    TADPSpy.new(objeto_espiado)
  end

  def mayor_a(numero)
    proc do |objeto|
      TADResult.new(objeto > numero, "ser mayor a #{objeto} ", numero)
    end
  end

  def menor_a(numero)
    proc do |objeto|
      TADResult.new(objeto < numero, "ser menor a #{objeto} ", numero)
    end
  end

  def uno_de_estos(primero, *demas_valores)
    lista_parametros = []
    lista_parametros << primero << demas_valores
    lista_parametros.flatten!

    proc do |objeto|
      TADResult.new((lista_parametros.include? objeto), "ser uno de a #{primero} ", lista_parametros)
    end
  end

  def entender(metodo)
    proc do |objeto|
      TADResult.new(objeto.respond_to?(metodo), "alguno de #{objeto.methods} \n", metodo)
    end
  end

  def ser (matcher)
    if matcher.is_a? Proc
      matcher
    else
      proc { |valor_a_comparar| TADResult.new(matcher==valor_a_comparar, valor_a_comparar,matcher) }
    end
  end


  def haber_recibido(algo)
    TADPMethodTester.new algo
  end

  def explotar_con (algo)
    proc do
    |objeto|
      begin
        objeto.call
      rescue Exception => ex
        resultado= ex.class.ancestors.include? (algo)
        TADResult.new resultado, algo, ex.class
      end
    end
      end

  def is_a_dynamic_matcher?(name)
    name.start_with?('ser_')|| name.start_with?('tener_')
  end

  def dynamic_variable(dynamic_name, value)
    if value.is_a? Proc
      proc do |objeto|
        value.call(objeto.instance_variable_get(dynamic_name.to_sym))
      end
    else
      proc do |objeto|
        dynamic_name= ('@' + dynamic_name).to_sym
        TADResult.new(objeto.instance_variable_get(dynamic_name.to_sym)==value, objeto.instance_variable_get(dynamic_name.to_sym), value)
      end
    end

  end

  def dynamic_method(dynamic_name)
    proc do |objeto|
      resultado= objeto.send(dynamic_name.to_sym)
      TADResult.new(resultado, true, resultado)
    end
  end

  def method_missing(symbol, *args)

    name = symbol.to_s
    if self.is_a_dynamic_matcher?(name)
      dynamic_name = name.sub('tener_', "").sub('ser_', "")

      if name.start_with? 'tener_'
      self.dynamic_variable(dynamic_name, args[0])
      else  if name.start_with? 'ser_'
        self.dynamic_method(dynamic_name)
            end
      end

    else
      super(symbol, *args)
    end
  end
end



