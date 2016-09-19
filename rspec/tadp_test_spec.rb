require 'rspec'
require  '../src/tadspec'

describe 'TADSPEC tests' do


  before do
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

      def testear_que_algo
        a = A.new
        a.hello.deberia ser 25
      end



    end

    class B
      attr_accessor :hello,:o

      def initialize
        @hello = 25
        @o="holas"
      end

      def hola?
        true
      end

      def testear_que_holis
        b = A.new
        b.deberia ser_hola?
      end

    end

  end


  it 'test_sencillo ' do
    expect(TADsPec.testear).to eq([true,true,true])
  end

  it 'testear solo la unit que pido' do
    expect(TADsPec.testear A).to eq([true,true])
  end

  it 'testear solo la unit que pido con el metodo que pido' do
    expect(TADsPec.testear A ,:testear_que_algo ).to eq([true])
  end

end