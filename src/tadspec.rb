  class TADsPec

    attr_accessor :unitTest

    def self.search_instance

      @unitTest = (Object.constants).map {|c| Object.const_get(c)}
      @unitTest = @unitTest.select {|k| k.is_a? Class}
      @unitTest = @unitTest.select {|c| (c.instance_methods false).any? {|m| m.to_s.start_with?('testear_que_')}}
    end

    def self.testear (*args)

      Object.send :include, TestSuite
      Proc.send :include, TestSuite

      search_instance

      ############# zona peligrosa

      Object.send :define_method ,:deberia do |algo|
        algo.run(self)
      end

      Proc.send :define_method ,:deberia do |algo|
        algo.run(self)
      end

      ############ fin de zona peligrosa
      # no me gusta esto, estamos repitiendo codigo
      if args.length>0
        args.each do
        |unit_test|
          unit_test.send :include, TestSuite
          TestContex.correr(unit_test)
        end
        remover_modulo_test
        remover_metodos_peligrosos
      else
        @unitTest.each do |unit_test|
          unit_test.send :include, TestSuite
          TestContex.correr(unit_test)
        end
        #remover_modulo_test
        remover_metodos_peligrosos
      end
    end

    def self.remover_metodos_peligrosos
      Object.send :remove_method, (:deberia)
      Proc.send :remove_method, (:deberia)
    end

    def self.remover_modulo_test
     TestSuite.instance_methods.each{|m| undef_method(m)}
    end

  end

  class TestContex
    attr_accessor :object, :met

    def self.correr(clase)
      @object = clase
      @met = (@object.instance_methods false).select {|m| m.to_s.start_with?('testear_que_')}
      correr_simple
    end

    def self.correr_simple

      @met.each do |m|
        var = @object.class_eval{
          test = self.new
          puts "El resultado del test fue: #{test.send m.to_sym}"
        }
      end

    end

    def method_to_block(block)
      proc{self.instance_eval(method(block.to_sym))}
    end
  end

  class TADPBlock
    def initialize block
      self.define_singleton_method(:run,block)
    end
  end

  class TADPObject
    attr_accessor :object
    def initialize algo
      @object= algo
    end
    def run algo
      @object.eql? algo
    end
  end

  class TADPErrorBloc

    def initialize error
      @tipo_error=error
    end
    def run algo
      begin
      algo.call
      rescue @tipo_error
        true
      end
    end
  end

  module TestSuite

    def mayor_a algo
      proc do
         |x| x>algo
      end
    end

    def menor_a algo
      proc do
      |x| x<algo
      end
    end

    def uno_de_estos (primero,*algo)
      proc do |x|
        if primero.is_a? Array
          primero.include? x
        else
          (primero.eql? x) || (algo.include? x) end
      end
    end

    def entender symbol
      TADPBlock.new (
      proc do
        |x| x.respond_to? symbol
      end)
    end

    def ser (algo)
      if algo.send(:is_a?, Proc)
       TADPBlock.new algo
      else
        TADPObject.new algo
      end
    end

    def explotar_con algo
      TADPErrorBloc.new algo
    end

    def method_missing(symbol,*args)
      if symbol.to_s.start_with? "ser_"

        TADPBlock.new (proc {|x|
        @string = symbol.to_s
        @string[0..3]= ''
        x.send(@string.to_sym) })

      else if symbol.to_s.start_with? "tener_"

             TADPBlock.new (proc {|x|
             @string = symbol.to_s
             @string[0..5]=''
             @string= ('@' + @string)
             x.instance_variable_get(@string) == args[0]})

           else
              super(symbol, *args)
           end
      end
    end

  end


  class A
    attr_accessor :hello,:o

    def initialize
      @hello = 25
      @o="holas"
    end

    def hola?
      true
    end

    def testear_que_holis
      a = A.new
      a.deberia ser_hola?
    end

  end





=begin

  puts A.new.deberia ser_hola?
  puts holita.deberia tener_hello 1

    unitTest = (Object.constants).map {|c| Object.const_get(c)}
    unitTest = unitTest.select {|k| k.is_a? Class}
    unitTest = unitTest.select {|c| (c.instance_methods false).any? {|m| m.to_s.start_with?('testear_que_')}}
    unitTest.each do |ut|
      ut.send :include, ModuloPrincipalTesting
    end
=end






  