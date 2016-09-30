require_relative '../src/Context'

class TADsPec
  def self.deberia_init
    @deberia = []
  end

  def self.deberia_list
    @deberia
  end

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
    deberia_proc = proc { |bloque| TADsPec.deberia_list << (bloque.call(self)) }
    mockear_proc = proc { |symbol, &block| self.send :alias_method, ('mock_'+symbol.to_s).to_sym, symbol
    self.send :define_method, symbol, block }
    Object.send :define_method, :deberia, deberia_proc
    Proc.send :define_method, :deberia, deberia_proc
    Class.send :define_method, :mockear, mockear_proc
  end


  def self.remover_deberia_mockear
    Object.send :remove_method, :deberia
    Proc.send :remove_method, :deberia
    Class.send :remove_method, :mockear
  end

  def self.testear (clase = nil, *args)

    asignar_deberia_y_mockear
    lista_test_totales =[]
    suits(clase).each do |unit_test|
      lista_test_totales << TestContex.correr(unit_test, args)
      puts "\n"
    end

    remover_deberia_mockear
    generar_reporte(lista_test_totales.flatten)

  end

  def self.suits(clase)
    obtener_suites(clase, lista = [])
  end

  def self.generar_reporte(resultados_tests)
    print "\n Se corrieron #{resultados_tests.count} tests de los cuales: "
    print "\n #{resultados_tests.count true} Pasaron!"
    print "\n #{resultados_tests.count false} Fallaron!"
    print "\n #{resultados_tests.count nil} Explotaron! \n"
    resultados_tests
  end
end