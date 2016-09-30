require '../src/TADPClasses'
require '../src/TestSuite'

class TADsPec

  def self.obtener_todas_las_clases
    var = (Object.constants).map { |constant| Object.const_get(constant) }
    var.select { |constant| constant.is_a? Class }
  end

  def self.search_all_test_suites
    obtener_todas_las_clases.select { |klass| (klass.instance_methods false).any? { |method| method.to_s.start_with?('testear_que_') } }
  end

  def self.obtener_suites(clase, lista_suit)
    (clase.is_a? Class) ? lista_suit << clase : lista_suit = search_all_test_suites
  end

  def self.asignar_deberia_y_mockear
    deberia_proc = proc { |algo| TestContex.deberia_list << (algo.call(self)) }
    mockear_proc = proc { |symbol, &block| self.send :alias_method, ('mock_'+symbol.to_s).to_sym, symbol
    self.send :define_method, symbol, block }
    Object.send :define_method, :deberia, deberia_proc
    Proc.send :define_method, :deberia, deberia_proc
    Class.send :define_method, :mockear, mockear_proc
  end


  def self.remover_deberia_mockear(lista_unit)
    Object.send :remove_method, :deberia
    Proc.send :remove_method, :deberia
    Class.send :remove_method, :mockear
  end

  def self.testear (clase = nil, *args)
    lista_suits = []
    lista_test_totales = []
    asignar_deberia_y_mockear
    obtener_suites(clase, lista_suits)
    lista_suits.each do |unit_test|
      lista_test_totales << TestContex.correr(unit_test, args)
      puts "\n"
    end

    remover_deberia_mockear(lista_suits)
    generar_reporte(lista_test_totales.flatten)

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
    ##clase.send(:include, TestSuite)
    if lista.length > 0
      lista_test = lista
    else
      lista_test = (clase.instance_methods false).select { |m| m.to_s.start_with?('testear_que_') }
    end
    print "\nLos test de la suite #{clase}:"
    run_test_suite_tests(clase, lista_test, lista_resultado)
    remove_mock_methods (clase)
  ## TestSuite.instance_methods.each { |metodo| clase.send(:undef_method, metodo) if clase.new.respond_to? metodo }
    lista_resultado
  end

  def self.deberia_list
    @deberia
  end

  def self.deberia_init
    @deberia = []
  end

  def self.run_test_suite_tests(clase, lista_metodos_test, lista_resultados)
    lista_metodos_test.each do |m|
      lista_resultados << clase.instance_eval do
        begin
          TestContex.deberia_init
          test = self.new

          analizado = TestContex.analizar_resultado(test, m.to_sym)
          print "\n El resultado del test: #{m} -> fue: #{analizado.to_s.upcase}"
          TestSuite.instance_methods.each { |metodo| test..send(:undef_method, metodo)}
          analizado
        rescue Exception => a
          print "\n El resultado del test #{m} -> fue: EXPLOSIVO = #{a}"
          TestSuite.instance_methods.each { |metodo| self.send(:undef_method, metodo)}
          (nil)
        end

      end
    end
  end

end




