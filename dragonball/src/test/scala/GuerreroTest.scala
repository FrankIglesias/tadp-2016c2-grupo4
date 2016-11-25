import org.scalatest.FunSuite
import DragonBallBuilder._
import Tipos._
import TodosLosMovimientos._

class GuerreroTest extends FunSuite {
  test("goku busca el movimiento que lo deje con mayor ki frente a vegeta"){
    val movimiento = goku.movimentoMasEfectivoContra(vegeta)(meDejaConElMayorKi)
    
    assert(movimiento.getOrElse(MovimientoNulo).eq(cargarki))
  }
  
}