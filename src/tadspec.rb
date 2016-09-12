  class TADsPec

    def self.search_instance
      @@test_instances= (Object.constants.select{|x| eval(x.to_s).is_a? Class}).map {|x| eval(x.to_s)}
      @@test_instances= @@test_instances.select{|y| (y.instance_methods false).any? {|metodo| metodo.to_s.start_with? "testear_que"}}
    end

    ## un getter mas copado necesitariamos
  def self.test_instances
    @@test_instances
  end

    def self.testear (*args)
      if args.length>0
        args.each do
        |unit_test|
          TestContex.new.correr_simple(unit_test)
        end
      else
        search_instance
        @@test_instances.each do |unit_test|
        TestContex.new.correr_simple(unit_test)
        end
      end
    end
  end

  class TestContex
     def correr_simple(block)
       block = method_to_block(block)
       self.instance_eval(&block)
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
                 @string=symbol.to_s
                 @string[0..5]=''
               @var =  x.send(@string.to_sym)
               1.deberia ser args
             }
                           )
      else
              super(symbol, *args)
      end
    end
    end
end
    ############# zona peligrosa
  class Object
    include TestSuite
    def deberia algo
    algo.run(self)
    end
  end
  class Proc
    include TestSuite
    def deberia algo
      algo.run(self)
    end
  end

  module ModuloPrincipalTesting

    def method_missing(symbol,*args)
      meta = symbol.to_s.partition('_')[2]
      head = symbol.to_s.partition('_')[0]
      super(symbol,*args) unless self.methods(false).include? (meta + '?').to_sym or self.instance_variables.include? ('@' + meta.to_sym)
      MissingMethodManager.new(head,meta,self,args[0])
    end

    class MissingMethodManager
      def new(head, meta,obj, arg)
        @obj = obj
        @arg = args[0]
        if head.equal? "ser"
          @meta = meta + '?'.to_sym
          ejecutar_metodo_bool
        else
          @meta = ('@' + meta).to_sym
          comparar_variable
        end
      end
      def ejecutar_metodo_bool
        @obj.send(@meta,@arg)
      end

      def comparar_variable
        @obj.instance_variable_get(@meta).eql? @arg
      end

    end

    def evaluar(algo)
      self.instance_eval(&algo)
    end

    def ser(algo)
      proc do
      if algo.is_a? Proc
        self.instance_eval(&algo)
      else
        self.eql? algo
      end
      end
    end

    def entender (algo)
     proc  do
       self.class.instance_methods.include? algo
     end
    end

    def mayor_a algo
      proc {self > algo}
    end

    def menor_a algo
      proc{self < algo}
    end
  end
  class A
    attr_accessor :hello
    def hola?
      true
    end
  end
  puts A.new.deberia ser_hola?
  puts holita.deberia tener_hello 1
=begin


    unitTest = (Object.constants).map {|c| Object.const_get(c)}
    unitTest = unitTest.select {|k| k.is_a? Class}
    unitTest = unitTest.select {|c| (c.instance_methods false).any? {|m| m.to_s.start_with?('testear_que_')}}
    unitTest.each do |ut|
      ut.send :include, ModuloPrincipalTesting
    end
=end






  