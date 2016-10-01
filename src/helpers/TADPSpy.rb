
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
      self.spying_object.singleton_class.mockear metodo do |*args|
        se_llamo=
            proc do |symbol, params|
          args.length==0? metodo == symbol : (metodo == symbol) && (args.eql? (params))
        end
        viejo_metodo = ('mock_'+ metodo.to_s).to_sym
        self.lista_metodos_llamados << se_llamo
        self.send(viejo_metodo, *args)
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
