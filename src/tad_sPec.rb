require_relative '../src/helpers/test_suite.rb'

class TADsPec
  class << self
    attr_accessor :deberia_list
  end

  def self.obtener_todas_las_clases
    var = Object.constants.map { |constant| Object.const_get(constant) }
    var.select { |constant| constant.is_a? Class }
  end

  def self.search_all_test_suites
    obtener_todas_las_clases.select do |klass|
      klass.instance_methods
           .any? do |method|
             method.to_s
                   .start_with?'testear_que_'
           end
    end
  end

  def self.obtener_suites(clase)
    clase.is_a? Class ? [clase] : search_all_test_suites
  end

  def self.asignar_deberia_y_mockear
    deberia_proc = proc { |bloque| TADsPec.deberia_list << bloque.call(self) }
    mockear_proc = proc do |symbol, &block|
      send :alias_method, ('mock_' + symbol.to_s).to_sym, symbol
      send :define_method, symbol, block
    end
    Object.send :define_method, :deberia, deberia_proc
    Proc.send :define_method, :deberia, deberia_proc
    Class.send :define_method, :mockear, mockear_proc
  end

  def self.remover_deberia_mockear
    Object.send :remove_method, :deberia
    Proc.send :remove_method, :deberia
    Class.send :remove_method, :mockear
  end

  def self.correr(clase, lista)
    lista_resultado = []
    lista_test = crear_lista_test(clase, lista)
    print "\nLos test de la suite #{clase}:"
    run_test_suite_tests(clase, lista_test, lista_resultado)
    lista_resultado
  end

  def self.crear_lista_test(clase, lista)
    if lista!empty? > 0
      lista
    else
      clase.instance_methods.select { |m| m.to_s.start_with?('testear_que_') }
    end
  end

  def self.run_test_suite_tests(clase, lista_metodos_test, lista_resultados)
    lista_metodos_test.each do |test_metodo|
      lista_resultados << clase.instance_eval do
        begin
          test = new
          test.singleton_class.send(:include, TestSuite)
          TADsPec.deberia_list = []
          analizado = test.correr_metodo_test(test, test_metodo.to_sym)
          print "\n El resultado del test:
                #{test_metodo} -> fue: #{analizado.to_s.upcase}"
          test.remove_mock_methods(test.singleton_class)
          TestSuite.instance_methods.each do |metodo|
            test.singleton_class
                .send(:undef_method, metodo)
          end
          analizado
        rescue StandardError => ex
          print "\n El resultado del test #{test_metodo} -> fue: EXPLOSIVO = #{ex}"
          TestSuite.instance_methods.each do |metodo|
            test.singleton_class
                .send(:undef_method, metodo)
          end
          (nil)
        end
      end
    end
  end

  def self.testear(clase = nil, *args)
    asignar_deberia_y_mockear
    lista_test_totales = []
    obtener_suites(clase).each do |unit_test|
      lista_test_totales << correr(unit_test, args)
      puts "\n"
    end
    remover_deberia_mockear
    generar_reporte(lista_test_totales.flatten)
  end

  def self.generar_reporte(resultados_tests)
    print "\n Se corrieron #{resultados_tests.count} tests de los cuales: "
    print "\n #{resultados_tests.count true} Pasaron!"
    print "\n #{resultados_tests.count false} Fallaron!"
    print "\n #{resultados_tests.count nil} Explotaron! \n"
    resultados_tests
  end
end
