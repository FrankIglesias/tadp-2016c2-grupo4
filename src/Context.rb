require_relative '../src/helpers/TestSuite'

class TestContex

  def self.analizar_resultado(objeto, metodo)
    objeto.send metodo
    deberia_list.all? { |resultado| resultado.analizar_resultados }
  end


  def self.remove_mock_methods (mocked_class)
    mock_methods = mocked_class.instance_methods.select { |symbol| symbol.to_s.start_with?('mock_') }
    mock_methods.each { |mock_method|
      metodo_a_modificar = mock_method.to_s.sub('mock_', '')
      mocked_class.send :define_method, (metodo_a_modificar.to_sym), (mocked_class.instance_method mock_method)
      mocked_class.send(:undef_method, mock_method) }
  end

  def self.correr(clase, lista)
    lista_resultado = []
    lista_test = crear_lista_test(clase,lista)
    print "\nLos test de la suite #{clase}:"
    run_test_suite_tests(clase, lista_test, lista_resultado)
    lista_resultado
  end

  def self.crear_lista_test(clase,lista)
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
          TestContex.deberia_init
          test = self.new
          test.singleton_class.send(:include, TestSuite)
          analizado = TestContex.analizar_resultado(test, test_metodo.to_sym)
          print "\n El resultado del test: #{test_metodo} -> fue: #{analizado.to_s.upcase}"
          TestSuite.instance_methods.each { |metodo| test.singleton_class.send(:undef_method, metodo)}
          TestContex.remove_mock_methods(test.class)
          analizado
        rescue Exception => ex
          print "\n El resultado del test #{test_metodo} -> fue: EXPLOSIVO = #{ex}"
          TestSuite.instance_methods.each { |metodo| test.singleton_class.send(:undef_method, metodo)}
          (nil)
        end
      end
    end
  end
end

