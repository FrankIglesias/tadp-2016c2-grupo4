class TADPMethodHistory
  attr_accessor :method, :params

  def initialize (method, *params)
    self.method= method
    self.params = *params
  end

  def se_llamo (symbol, *params)
    if params.length==0
      self.method ==symbol
    else
      (self.method == symbol) && (self.params.eql? (params))
    end
  end
end

class TADPSpy
  attr_accessor :spying_object

  def initialize (objeto)
    self.spying_object = objeto
    lista_metodos_llamados_a_espiar = self.spying_object.class.instance_methods false
    self.spying_object.instance_variable_set(:@lista_metodos_llamados, [])
    self.spying_object.define_singleton_method(:lista_metodos_llamados, proc do @lista_metodos_llamados end)
    espiar_metodos(lista_metodos_llamados_a_espiar)
  end

  def espiar_metodos(lista_metodos_a_espiar)
    lista_metodos_a_espiar.each do |metodo|
      self.spying_object.singleton_class.mockear metodo do
      |*args|
        viejo_metodo = ('mock_'+ metodo.to_s).to_sym
        self.lista_metodos_llamados << TADPMethodHistory.new(metodo, args)
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

  def initialize (metodo)
    self.metodo = metodo
  end

  def call (algo)
    TADResult.new(algo.spying_object.lista_metodos_llamados.any? { |tester| tester.se_llamo self.metodo }, "\n Uno de #{algo.spying_object.lista_metodos_llamados}", metodo)
  end

  def veces (numero)
    self.define_singleton_method :call do |x|
      variable = x.spying_object.lista_metodos_llamados.select { |tester| tester.se_llamo self.metodo }
      resultado= variable.length ==numero
      TADResult.new resultado, "\n que el metodo haya sido llamado #{variable.length} veces", numero
    end
    self
  end

  def con_argumentos (*args)
    self.define_singleton_method :call do
    |espia|
      metodos_que_cumplen_argumentos = espia.spying_object.lista_metodos_llamados.select { |tester| tester.se_llamo(metodo, args) }
      TADResult.new(metodos_que_cumplen_argumentos.length>0, "\n que el metodo haya recibido #{metodos_que_cumplen_argumentos.map { |m| m.params }}", args)
    end
    self
  end
end
class TADResult

  attr_accessor :resultado, :esperado, :recibido

  def initialize (resultado, esperado= nil, recibido= nil)
    self.resultado= resultado
    self.esperado= esperado
    self.recibido= recibido
  end

  def analizar_resultados

    puts("\n esperaba #{esperado} y recibi #{recibido}") if (self.resultado.eql? false)
    puts recibido.backtrace if (self.resultado.nil?)
    self.resultado
  end
end