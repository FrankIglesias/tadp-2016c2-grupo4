require '../src/TADPClasses'
require '../src/TestSuite'

class TADsPec

  def self.obtener_todas_las_clases
    var = (Object.constants).map { |constant| Object.const_get(constant) }
    var.select { |constant| constant.is_a? Class }
  end

  def self.search_all_test_suites(lista_suit)
    lista_suit = obtener_todas_las_clases
   lista_suit.select { |klass| (klass.instance_methods false).any? { |method| method.to_s.start_with?('testear_que_') } }
  end

  def self.agregar_suites(clase,lista_suit)
    if clase.is_a? Class
      lista_suit << clase
    else
      search_all_test_suites(lista_suit)
    end
  end

  def self.iniciar_entorno
    deberia_proc = proc { |algo| TestContex.deberia_array << (algo.call(self)) }
    mockear_proc = proc { |symbol, &block| self.send :alias_method, ('mock_'+symbol.to_s).to_sym, symbol
    self.send :define_method, symbol, block }
    #Object.send :include, ModuleRemover
    #Proc.send :include, TestSuite
    Object.send :define_method, :deberia, deberia_proc
    Proc.send :define_method, :deberia, deberia_proc
    Class.send :define_method, :mockear, mockear_proc
  end

  def self.remove_mock_methods
    mock_method_classes = obtener_todas_las_clases
    mock_method_classes = mock_method_classes.select { |klass| (klass.instance_methods).any? { |method| method.to_s.start_with?('mock_') } }
    mock_method_classes.each { |mocked_class|
      mock_methods = mocked_class.instance_methods.select { |symbol| symbol.to_s.start_with?('mock_') }
      mock_methods.each { |mock_method|
        metodo_a_modificar = mock_method.to_s.sub('mock_', '')
        mocked_class.send :define_method, (metodo_a_modificar.to_sym), (mocked_class.instance_method mock_method)
        mocked_class.send(:undef_method, mock_method) }
    }
  end


  def self.remover_metodos_peligrosos(lista_unit)
    Object.send :remove_method, :deberia
    Proc.send :remove_method, :deberia
    Class.send :remove_method, :mockear
    remove_mock_methods
    #remover_modulo_test(lista_unit)
  end


  def self.remover_modulo_test(lista_unit)
    lista_unit.each do |unit|
      unit.send(:uninclude,TestSuite)
    end
  end

  def self.testear (clase = nil, *args)
    lista_suits = []
    lista_test_totales = []
    iniciar_entorno
    agregar_suites(clase,lista_suits)

    lista_suits.each do |unit_test|
      unit_test.send :include, TestSuite
      lista_test_totales << TestContex.correr(unit_test, args)
      puts "\n"
    end

    remover_metodos_peligrosos(lista_suits)
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

  def self.correr(clase, lista)
    lista_resultado = []
    clase.class.send(:include, TestSuite)
    if lista.length > 0
      lista_test = lista
    else
      lista_test = (clase.instance_methods false).select { |m| m.to_s.start_with?('testear_que_') }
    end
    print "\nLos test de la suite #{clase}:"
    run_test_suite_tests(clase,lista_test,lista_resultado)
    lista_resultado
  end

  def self.deberia_array
    @deberia
  end

  def self.deberia_init
    @deberia = []
  end

  def self.run_test_suite_tests(clase,lista_metodos_test,lista_resultados)
    lista_metodos_test.each do |m|
      lista_resultados << clase.instance_eval do
        begin
          TestContex.deberia_init
          test = self.new
          analizado = analizar_resultado(test, m.to_sym)
          print "\n El resultado del test: #{m} -> fue: #{analizado.to_s.upcase}"
        ##  eliminar_mock_methods
        ##  TestSuite.methods.each { |suite_method| self.undef_method suite_method }
          analizado
        rescue Exception => a
          print "\n El resultado del test #{m} -> fue: EXPLOSIVO = #{a}"
          (nil)
        end
      end
    end
  end

end




