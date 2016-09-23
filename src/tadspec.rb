class TADsPec

  def self.search_all_test_suites
    @unit_test_clases = (Object.constants).map { |constant| Object.const_get(constant) }
    @unit_test_clases = @unit_test_clases.select { |constant| constant.is_a? Class }
    @unit_test_clases = @unit_test_clases.select { |klass| (klass.instance_methods false).any? { |method| method.to_s.start_with?('testear_que_') } }
  end

  def self.add_test_class(clase)
    @unit_test_clases << clase
  end

  def self.agregar_suites(clase)

    if clase.is_a? Class
      add_test_class(clase)
    else
      search_all_test_suites
    end

  end

  def self.iniciar_entorno
    deberia_proc = proc { |algo| TestContex.deberia_array << (algo.run(self)) }
    mockear_proc = proc { |symbol, &block| self.class.send :alias_method ,("mock_" + symbol.to_s).to_sym, symbol
      self.define_singleton_method symbol,block }
    Object.send :include, TestSuite
    Proc.send :include, TestSuite
    Object.send :define_method, :deberia, deberia_proc
    Proc.send :define_method, :deberia, deberia_proc
    Object.send :define_method , :mockear, mockear_proc
  end

  def self.remove_mock_methods
    mock_method_classes = (Object.constants).map { |constant| Object.const_get(constant) }
    mock_method_classes = mock_method_classess.select { |constant| constant.is_a? Class }
    mock_method_classes = mock_method_classes.select { |klass| (klass.instance_methods false).any? { |method| method.to_s.start_with?('mock_')}}
  end

  def self.remover_metodos_peligrosos
    Object.send :remove_method, (:deberia)
    Proc.send :remove_method, (:deberia)
    ##remove_mock_methods
  end

  def self.remover_modulo_test
    TestSuite.instance_methods.each { |m| undef_method(m) }
  end

  def self.testear (clase = nil, *args)
    @unit_test_clases = []
    @test_totales = []
    iniciar_entorno
    agregar_suites clase

    ## CORRER TESTS
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

class TestContex
  def self.correr(clase, lista)
    @var = []
    @object = clase
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
          analizado  = analizar_resultado(test , m.to_sym)
          print "\n El resultado del test: #{m} -> fue: #{analizado.to_s.upcase}"
          analizado
        rescue Exception => a
          print "\n El resultado del test #{m} -> fue: EXPLOSIVO = #{a}"
          (nil)
        end
      end
    end
  end

end

class TADPBlock
  def initialize block
    self.define_singleton_method(:run, block)
  end
end

class TADPObject
  def initialize algo
    @object= algo
  end

  def run algo
    @object.eql? algo
  end
end

class TADPErrorBloc
  def initialize error
    @tipo_error=error
  end

  def run algo

    begin
      algo.call
    rescue Exception => ex
      ex.class.ancestors.include? (@tipo_error)
    end

  end
end

class TADPSpy

  attr_accessor :spying_object

  def new objeto
    spying_object  = objeto.clone
    @method_list = spying_object.methods
    espiar_metodos
  end

  def espiar_metodos

    @method_list.each do |m|
      viejo_metodo = (m.to_s + '_viejo').to_sym
      spying_object.define_singleton_method.send :alias  , viejo_metodo, m
      spying_object.define_singleton_method.send :define_method, m do
        @lista_metodos << m.to_s
        self.define_singleton_method.send viejo_metodo
    end

    end
  end
  

  def deberia algo
    TestContex.deberia_array << algo.run(self)
  end

  def method_missing(symbol,*args)
    puts spying_object.methods.include? :viejo?
      super(symbol,*args)
  end

end

module TestSuite

  def analizar_resultado(objeto, metodo)

    objeto.send metodo
    TestContex.deberia_array.all? {|resultado| resultado.eql? true}

  end

  def espiar algo

    TADPSpy.new algo

  end

  def haber_recibido algo

    TADPBlock.new (proc do
      |x| x.method_list.include? algo
    end)

  end

  def mayor_a algo
    proc do
    |x|
      x > algo
    end
  end

  def menor_a algo
    proc do
    |x|
      x<algo
    end
  end

  def uno_de_estos (primero, *algo)
    proc do |x|
      if primero.is_a? Array
        primero.include? x
      else
        (primero.eql? x) || (algo.include? x)
      end
    end
  end

  def entender symbol
    TADPBlock.new (
                      proc do
                      |x|
                        x.respond_to? symbol
                      end)
  end

  def ser (algo)
    if algo.send(:is_a?, Proc)
      TADPBlock.new algo
    else
      TADPObject.new algo
    end
  end

  def explotar_con algo
    TADPErrorBloc.new algo
  end

  def method_missing(symbol, *args)
    if symbol.to_s.start_with? "ser_"

      TADPBlock.new (proc { |x|
        @string = symbol.to_s
        @string[0..3]= ''
        x.send(@string.to_sym) })

    else
      if symbol.to_s.start_with? "tener_"
        string = symbol.to_s
        string[0..5]=''
        string = ('@' + string)
        if args[0].is_a? Proc
          TADPBlock.new (proc { |x|
            args[0].call(x.instance_variable_get(string.to_sym)) })
        else
          TADPBlock.new (proc { |x|
            x.instance_variable_get(string.to_sym) == args[0] })
        end
      else
        super(symbol, *args)
      end
    end
  end
end
=begin
class A
  attr_accessor :algo
  def hola?
    true
  end
  def testear_que_hola1
    a = A.new
    a.algo=3
    a.deberia tener_algo 4
  end
  def testear_que_hola2
    a = A.new
    a.deberia ser_hola?
    a.algo=3
    a.deberia tener_algo 4
  end
  def testear_que_hola3
    a = A.new
    a.algo=3
    a.deberia tener_algo 3
    a.deberia ser_hola?
  end
end
class B
  def hola
    false
  end
  def testear_que_no_mockea
    b = B.new
    b.deberia ser_hola
  end
  def testear_que_mockea
    b = B.new
  b.mockear :hola do
    true
  end
    b.deberia ser_hola
  end
end
=end

class PersonaTest
  attr_accessor :edad
  def viejo?
    self.edad > 29
  end

  def testear_que_se_use_la_edad
    pato = PersonaTest.new
    pato.edad=  30
    pato = espiar(pato)
    pato.viejo?
   ## pato.deberia haber_recibido (:edad)
  end
end


