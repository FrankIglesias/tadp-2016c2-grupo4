require_relative '../../src/helpers/test_suite'

class TADPMethodTester
  attr_accessor :metodo, :devolver_resultado_proc

  def initialize(metodo)
    self.metodo = metodo
    self.devolver_resultado_proc = (TestSuite.instance_method :devolver_resultado_test)
                                   .bind(nil).to_proc
  end

  def call(algo)
    devolver_resultado_proc.call(algo.spying_object.lista_metodos_llamados
                                     .any? { |tester| tester.call(metodo) },
                                 "\n Uno de #{algo.spying_object
                                                  .lista_metodos_llamados}",
                                 metodo)
  end

  def veces(numero)
    define_singleton_method :call do |espia|
      variable = espia.spying_object.lista_metodos_llamados
                      .select { |tester| tester.call(metodo) }
      resultado = variable.length == numero
      devolver_resultado_proc.call(resultado,
                                   "\n que el metodo haya sido llamado #{variable
                                                                         .length} veces",
                                   numero)
    end
    self
  end

  def con_argumentos(*args)
    define_singleton_method :call do |espia|
      metodos_que_cumplen_argumentos = espia.spying_object.lista_metodos_llamados
                                            .select { |tester| tester.call(metodo, args) }
      devolver_resultado_proc.call(metodos_que_cumplen_argumentos
                             .empty?, "\n la funcion esperaba #{args}", args)
    end
    self
  end
end
