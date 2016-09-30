
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
    lista_metodos_a_espiar = spying_object.class.instance_methods false
    spying_object.singleton_class.send :attr_accessor, :lista_metodos
    spying_object.send :lista_metodos=, []
    espiar_metodos(lista_metodos_a_espiar)
  end

  def espiar_metodos(lista_metodos_a_espiar)
    lista_metodos_a_espiar.each do |m|
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
    TADResult.new resultado, "\n Uno de #{algo.spying_object.lista_metodos}", metodo
  end

  def veces numero
    self.define_singleton_method :call do |x|
      variable = x.spying_object.lista_metodos.select { |m| m.se_llamo self.metodo }
      resultado= variable.length ==numero
      TADResult.new resultado, "\n que el metodo haya sido llamado #{variable.length} veces", numero
    end
    self
  end

  def con_argumentos *args
    self.define_singleton_method :call do
    |x|
      variable = x.spying_object.lista_metodos.select { |m| m.se_llamo metodo, args }
      resultado= variable.length>0
      TADResult.new resultado, "\n que el metodo haya recibido #{variable.map { |m| m.params }}", args
    end
    self
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
      puts("\n esperaba #{esperado} y recibi #{recibido}")
    end
    if (self.resultado==nil)
      puts recibido.backtrace
    end
    self.resultado
  end
end