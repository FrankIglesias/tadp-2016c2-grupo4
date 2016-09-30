module TestSuite

  def espiar(objeto_espiado)
    TADPSpy.new(objeto_espiado)
  end

  def mayor_a(numero)
    proc do |objeto|
      TADResult.new(objeto > numero, "\n ser mayor a #{objeto} ", numero)
    end
  end

  def menor_a(numero)
    proc do |objeto|
      TADResult.new(objeto < numero, "\n ser menor a #{objeto} ", numero)
    end
  end

  def uno_de_estos(primero, *demas_valores)
    lista_parametros = []
    lista_parametros << primero << demas_valores
    lista_parametros.flatten!

    proc do |objeto|
      TADResult.new((lista_parametros.include? objeto), "\n ser uno de a #{primero} ", lista_parametros)
    end
  end

  def entender(metodo)
    proc do |objeto|
      TADResult.new(objeto.respond_to?(metodo), "\n alguno de #{objeto.methods} \n", metodo)
    end
  end

  def ser (matcher)
    if matcher.is_a? Proc
      matcher
    else
      proc { |valor_a_comparar| TADResult.new(matcher==valor_a_comparar, valor_a_comparar, matcher) }
    end
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
        TADResult.new resultado, error, ex.class
      end
    end
  end

  def is_a_dynamic_matcher?(name)
    name.start_with?('ser_')|| name.start_with?('tener_')
  end

  def dynamic_variable(dynamic_name, value)
    if value.is_a? Proc
      proc do |objeto|
        value.call(objeto.instance_variable_get(dynamic_name.to_sym))
      end
    else
      proc do |objeto|
        dynamic_name= ('@' + dynamic_name).to_sym
        TADResult.new(objeto.instance_variable_get(dynamic_name.to_sym)==value, objeto.instance_variable_get(dynamic_name.to_sym), value)
      end
    end

  end

  def dynamic_method(dynamic_name)
    proc do |objeto|
      resultado= objeto.send(dynamic_name.to_sym)
      TADResult.new(resultado, true, resultado)
    end
  end

  def method_missing(symbol, *args)
    name = symbol.to_s
    if self.is_a_dynamic_matcher?(name)
      dynamic_name = name.sub('tener_', '').sub('ser_', '')
      return self.dynamic_variable(dynamic_name, args[0]) if name.start_with? 'tener_'
      return self.dynamic_method(dynamic_name) if name.start_with? 'ser_'
    else
      super(symbol, *args)
    end
  end
end
