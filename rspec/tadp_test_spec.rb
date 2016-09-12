require 'rspec'
require  '../src/tadspec'

describe 'TADSPEC tests' do


  it '1 deberia ser 1 ' do
    expect(1.deberia ser 1).to eq(true)
  end

  it '1 no  deberia ser un string ' do
    expect(1.deberia ser "a").to eq(false)
  end

  it '1 deberia ser menor a 2 ' do
    expect(1.deberia ser menor_a 2).to eq(true)
  end

  it '1  no deberia ser menor a 0 ' do
    expect(1.deberia ser menor_a 0).to eq(false)
  end

  it '1 deberia ser mayor a 0 ' do
    expect(1.deberia ser mayor_a 0).to eq(true)
  end

  it '1  no deberia ser mayor a 3 ' do
    expect(1.deberia ser mayor_a 3).to eq(false)
  end

  it '1  debera ser uno_de_estos array ' do
    expect(1.deberia ser uno_de_estos [1,2,3]).to eq(true)
  end

  it '1  no deberia ser uno_de_estos array' do
    expect(1.deberia ser uno_de_estos [2,3,4]).to eq(false)
  end

  it '1  deberia ser ser uno_de_estos multiples argumentos ' do
    expect(1.deberia ser uno_de_estos 1,2,3).to eq(true)
  end

  it '1  no deberia ser uno_de_estos multiples argumentos' do
    expect(1.deberia ser uno_de_estos 2,3,4).to eq(false)
  end

  it '1  no deberia entender mensaje' do
    expect(1.deberia entender :>).to eq(true)
  end

  it '1  no deberia no entender mensaje' do
    expect(1.deberia ser entender :sarlompa).to eq(false)
  end

  it 'deberia dar true' do
    hola = proc{1/0}
    expect(hola.deberia explotar_con ZeroDivisionError).to eq(true)
  end

  it 'deberia dar true porque lo catchie afuera' do
    hola = proc{1/0}
    begin
      hola.deberia explotar_con NoMethodError
    rescue ZeroDivisionError
      @variable = true
    end
      expect(@variable).to eq(true)
  end

  it 'TADspec levanta todas las clases test' do
    class A
      def testear_que_saraza
      end
    end
    TADsPec.search_instance
    expect(TADsPec.test_instances.length).to eq(1)
  end

end