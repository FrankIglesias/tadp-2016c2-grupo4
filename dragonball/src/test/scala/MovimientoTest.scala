import org.scalatest.FunSuite
import scala.util._
import DragonBallBuilder._
import Tipos._
import ObjetoItem._

class SetSuite extends FunSuite {

  test("An empty Set should have size 0") {
    assert(Set.empty.size == 0)
  }

  test("Invoking head on an empty Set should produce NoSuchElementException") {
    assertThrows[NoSuchElementException] {
      Set.empty.head
    }
  }
}

class MovimientoTest extends FunSuite {
  test("Un movimiento no genera efecto lateral"){
    val resultado = goku.utilizarMovimiento(cargarki)(vegeta)
    
    assert(atacante(resultado).ki==(goku.ki + 100))
    //assert(goku.ki == 20)
    //assert(false)
  }
  
  test("goku malvado ataca con un espada sencilla a goku cauzandole 2 de daño"){
    val resultado = gokuMalvado.utilizarMovimiento(usarEspadaSencilla)(goku)
    assert(defensor(resultado).ki==(goku.ki - 2))
  }
  
  test("goku ataca con una espada sencilla pero no tiene efecto porque no tiene la espada"){
    val resultado = goku.utilizarMovimiento(usarEspadaSencilla)(gokuMalvado)
    assert(defensor(resultado).ki==(gokuMalvado.ki))
  }
  
  test("vegeta deja inconsiente con una espada oxidada a goku"){
    val resultado = vegeta.utilizarMovimiento(usarEspadaOxidada)(goku)
    assert(defensor(resultado).estado == Inconsciente)
  }
  
  test("un androide ataca a goku con un revolver a goku que no le pasa nada y el androide piede una bala"){
    val resultado = terminator.utilizarMovimiento(usarRevolver)(goku)
    assert(atacante(resultado)
        .listaDeItems
        .find {municion => municion.isInstanceOf[Municion]}
        .getOrElse(cartuchoDeRevolver)
        .asInstanceOf[Municion].cantidadDeBalas ==5)
    }
}