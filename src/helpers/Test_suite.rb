require_relative '../../src/helpers/TADP_spy'
require_relative '../../src/helpers/TADP_method_tester'
require_relative '../../src/TAD_sPec'
module TestSuite

  def devolver_resultado_test(resultado, esperado, recibido)
    proc do
      puts("\n esperaba #{esperado} y recibi #{recibido}") if (resultado.eql? false)
      puts recibido.backtrace if (resultado.nil?)
      resultado
    end
  end

  def correr_metodo_test(test, metodo)
    test.send metodo
    TADsPec.deberia_list.all? { |resultado| resultado.call }
  end

  def remove_mock_methods(mocked_class)
    mock_methods = mocked_class.instance_methods.select { |symbol| symbol.to_s.start_with?('mock_') }
    mock_methods.each { |mock_method|
      metodo_a_modificar = mock_method.to_s.sub('mock_', '')
      mocked_class.send :define_method, (metodo_a_modificar.to_sym), (mocked_class.instance_method mock_method)
      mocked_class.send(:undef_method, mock_method) }
  end

  def espiar(objeto_espiado)
    TADPSpy.new(objeto_espiado)
  end

  def mayor_a(numero)
    proc do |objeto|
      devolver_resultado_test(objeto > numero, "\n ser mayor a #{objeto} ", numero)
    end
  end

  def menor_a(numero)
    proc do |objeto|
      devolver_resultado_test(objeto < numero, "\n ser menor a #{objeto} ", numero)
    end
  end

  def uno_de_estos(primero, *demas_valores)
    lista_parametros = []
    lista_parametros << primero << demas_valores
    lista_parametros.flatten!

    proc do |objeto|
      devolver_resultado_test((lista_parametros.include? objeto), "\n ser uno de a #{primero} ", lista_parametros)
    end
  end

  def entender(metodo)
    proc do |objeto|
      devolver_resultado_test(objeto.respond_to?(metodo), "\n alguno de #{objeto.methods} \n", metodo)
    end
  end

  def ser (matcher)
    if matcher.is_a? Proc
      matcher
    else
      proc { |valor_a_comparar| devolver_resultado_test(matcher==valor_a_comparar, valor_a_comparar, matcher) }
    end
  end


  def no(bloque)
    devolver_bloque_negado(bloque)
  end

  def haber_recibido(metodo)
    TADPMethodTester.new metodo
  end

  def explotar_con (error)
    proc do
    |objeto|
      begin
        objeto.call
      rescue Exception => ex
        resultado= ex.class.ancestors.include? (error)
        devolver_resultado_test(resultado, error, ex.class)
      end
    end
  end

  def is_a_dynamic_matcher?(name)
    name.start_with?('ser_')|| name.start_with?('tener_')
  end

  def dynamic_variable(dynamic_name, value)
    dynamic_name= ('@' + dynamic_name).to_sym
    if value.is_a? Proc
      proc do |objeto|
        value.call(objeto.instance_variable_get(dynamic_name))
      end
    else
      proc do |objeto|
        devolver_resultado_test(objeto.instance_variable_get(dynamic_name)==value, objeto.instance_variable_get(dynamic_name), value)
      end
    end

  end

  def dynamic_method(dynamic_name)
    proc do |objeto|
      resultado= objeto.send(dynamic_name.to_sym)
      devolver_resultado_test(resultado, true, resultado)
    end
  end

  def method_missing(symbol, *args)
    name = symbol.to_s
    if self.is_a_dynamic_matcher?(name)
      dynamic_name = name.sub('tener_', '').sub('ser_', '')
      return self.dynamic_variable(dynamic_name, args[0]) if name.start_with? 'tener_'
      return self.dynamic_method(dynamic_name) if name.start_with? 'ser_'
    end
  end
end
