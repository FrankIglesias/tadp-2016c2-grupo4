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

    def deberia(bloque)
      self.instance_exec(&bloque)
    end

    def ser(algo)
      if block_given?
        yield
      else
        proc{ self.eql? algo }
      end
    end
  end


    def mayor_a algo
      proc{self > algo}
    end

    def menor_a algo
      proc{self < algo}
    end

