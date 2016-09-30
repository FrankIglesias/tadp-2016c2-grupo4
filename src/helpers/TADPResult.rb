
class TADPResult

  attr_accessor :resultado, :esperado, :recibido

  def initialize (resultado, esperado= nil, recibido= nil)
    self.resultado= resultado
    self.esperado= esperado
    self.recibido= recibido
  end

  def analizar_resultados

    puts("\n esperaba #{esperado} y recibi #{recibido}") if (self.resultado.eql? false)
    puts recibido.backtrace if (self.resultado.nil?)
    self.resultado
  end
end