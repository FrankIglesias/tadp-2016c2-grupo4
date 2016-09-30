
class TADPMethodTester
  attr_accessor :metodo

  def initialize (metodo)
    self.metodo = metodo
  end

  def call (algo)
    TADPResult.new(algo.spying_object.lista_metodos_llamados.any? { |tester| tester.se_llamo self.metodo }, "\n Uno de #{algo.spying_object.lista_metodos_llamados}", metodo)
  end

  def veces (numero)
    self.define_singleton_method :call do |espia|
      variable = espia.spying_object.lista_metodos_llamados.select { |tester| tester.se_llamo self.metodo }
      resultado= variable.length ==numero
      TADPResult.new resultado, "\n que el metodo haya sido llamado #{variable.length} veces", numero
    end
    self
  end

  def con_argumentos (*args)
    self.define_singleton_method :call do
    |espia|
      metodos_que_cumplen_argumentos = espia.spying_object.lista_metodos_llamados.select { |tester| tester.se_llamo(metodo, args) }
      TADPResult.new(metodos_que_cumplen_argumentos.length>0, "\n que el metodo haya recibido #{metodos_que_cumplen_argumentos.map { |metodo_llamado| metodo_llamado.params }}", args)
    end
    self
  end
end
