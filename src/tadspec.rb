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
    def ser(algo)
      proc do
        eql? algo
      end
    end
  end





