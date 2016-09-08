  class TADsPec
    attr_accessor :test_instance

    def self.search_instance
      @test_instance = instance_methods.grep(/testear_que_/)
    end

  end


    ############# zona peligrosa
  class Object
    def deberia(bloque)
      self.instance_eval(&bloque)
    end

  end

  class Object

    def mayor_a(algo)
      proc do
        self > algo
      end
    end

    def menor_a(algo)
      proc do
        self < algo
      end
    end

    def ser(bloque, algo)
      proc do
        self.instance_eval(&bloque)
      end
    end

    def ser(algo)
      proc do
        self.eql? algo
      end
    end

  end
  


