  class TADsPec
    attr_accessor :test_instance

    def initialize
      @test_instance = []
    end

    def self.search_instance
      @test_instance = instance_methods.grep(/testear_que_/)
    end

    def self.testear
      search_instance
      @test_instance.each do |unit_test|
        TestContex.new.correr_simple(unit_test)
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

    ############# zona peligrosa
  class Object

    def deberia(algo)
      self.evaluar algo
    end
  end

  module ModuloPrincipalTesting

    def method_missing(symbol,*args)

      meta = symbol.to_s.partition('_')[2]
      head = symbol.to_s.partition('_')[0]
      super unless self.methods(false).include? (meta + '?').to_sym or self.instance_variables.include? ('@' + meta.to_sym)
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

    def uno_de_estos (algo, *parametros)
      if(algo.is_a? Array)
        proc {algo.include? self}
      else
        proc {if(algo.eql? self)
                true
              parametros.include? self
              end}
      end
    end


end


    unitTest = (Object.constants).map {|c| Object.const_get(c)}
    unitTest = unitTest.select {|k| k.is_a? Class}
    unitTest = unitTest.select {|c| (c.instance_methods false).any? {|m| m.to_s.start_with?('testear_que_')}}

    unitTest.each do |ut|
      ut.send :include, ModuloPrincipalTesting
    end







  