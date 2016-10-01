require_relative '../../src/helpers/TestSuite'


class TADPMethodTester
  attr_accessor :metodo, :devolver_resultado_proc

  def initialize (metodo)
    self.metodo = metodo
    self.devolver_resultado_proc= (TestSuite.instance_method :devolver_resultado_test).bind(nil).to_proc
  end

  def call (algo)
    self.devolver_resultado_proc.call(algo.spying_object.lista_metodos_llamados.any? { |tester| tester.call(self.metodo) }, "\n Uno de #{algo.spying_object.lista_metodos_llamados}", metodo)
  end

  def veces (numero)
    self.define_singleton_method :call do |espia|
      variable = espia.spying_object.lista_metodos_llamados.select { |tester| tester.call(self.metodo) }
      resultado= variable.length ==numero
      self.devolver_resultado_proc.call(resultado, "\n que el metodo haya sido llamado #{variable.length} veces", numero)
    end
    self
  end

  def con_argumentos (*args)
    self.define_singleton_method :call do
    |espia|
      metodos_que_cumplen_argumentos = espia.spying_object.lista_metodos_llamados.select { |tester| tester.call(metodo, args) }
      self.devolver_resultado_proc.call(metodos_que_cumplen_argumentos.length>0, "\n la funcion esperaba #{args}", args)
    end
    self
  end
end
