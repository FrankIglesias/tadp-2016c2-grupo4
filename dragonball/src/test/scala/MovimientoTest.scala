import org.scalatest.FunSuite
import scala.util._
import DragonBallBuilder._
import Tipos._

class SetSuite extends FunSuite {

  test("An empty Set should have size 0") {
    (Set.empty.size == 0)
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
}