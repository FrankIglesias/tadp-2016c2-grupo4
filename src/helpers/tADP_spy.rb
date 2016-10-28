
class TADPSpy
  attr_accessor :spying_object

  def initialize(objeto)
    spying_object = objeto
    listita = self.spying_object.class.instance_methods false
    spying_object.instance_variable_set(:@lista_metodos_llamados, [])
    spying_object.define_singleton_method(:lista_metodos_llamados, proc { @lista_metodos_llamados })
    espiar_metodos(lista_metodos_llamados_a_espiar)
  end

  def espiar_metodos(listita)
    listita.each do |metodo|
     spying_object.singleton_class.mockear metodo do |*args|
        se_llamo= proc do |symbol, params|
          args.length==0? metodo == symbol : (metodo == symbol) && (args.eql? (params))
        end
        viejo_metodo = ('mock_'+ metodo.to_s).to_sym
        lista_metodos_llamados << se_llamo
        send(viejo_metodo, *args)
      end

    end
  end

  def method_missing(symbol, *args)
    if spying_object.class.instance_methods.include? symbol
      spying_object.send symbol, *args
    else
      super(symbol, *args)
    end
  end
end
