require 'rspec'
require  '../src/tadspec'

describe 'TADSPEC tests' do


  it 'test_sencillo ' do

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

    #TADsPec.testear
    expect(TADsPec.testear).to eq([true])

  end


end