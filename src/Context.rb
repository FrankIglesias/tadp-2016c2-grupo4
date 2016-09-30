require_relative '../src/helpers/TestSuite'

class TestContex

  def self.correr(clase, lista)
    lista_resultado = []
    lista_test = crear_lista_test(clase, lista)
    print "\nLos test de la suite #{clase}:"
    run_test_suite_tests(clase, lista_test, lista_resultado)
    lista_resultado
  end

  def self.crear_lista_test(clase, lista)
    if lista.length > 0
      lista_test = lista
    else
      lista_test = (clase.instance_methods false).select { |m| m.to_s.start_with?('testear_que_') }
    end
  end


  def self.deberia_list
    @deberia
  end

  def self.deberia_init
    @deberia = []
  end

  def self.run_test_suite_tests(clase, lista_metodos_test, lista_resultados)
    lista_metodos_test.each do |test_metodo|
      lista_resultados << clase.instance_eval do
        begin
          test = self.new
          test.singleton_class.send(:include, TestSuite)
          test.deberia_init
          analizado = test.analizar_resultado(test, test_metodo.to_sym)
          print "\n El resultado del test: #{test_metodo} -> fue: #{analizado.to_s.upcase}"
          test.remove_mock_methods(test.class)
          TestSuite.instance_methods.each { |metodo| test.singleton_class.send(:undef_method, metodo) }
          analizado
        rescue Exception => ex
          print "\n El resultado del test #{test_metodo} -> fue: EXPLOSIVO = #{ex}"
          TestSuite.instance_methods.each { |metodo| test.singleton_class.send(:undef_method, metodo) }
          (nil)
        end
      end
    end
  end
end

