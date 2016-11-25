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
  
  test("un androide ataca a goku con un revolveru que no le pasa nada y el androide pierde una bala"){
    val resultado = terminator.utilizarMovimiento(usarRevolver)(goku)
    assert(atacante(resultado)
        .listaDeItems
        .find {municion => municion.isInstanceOf[Municion]}  
        .getOrElse(cartuchoDeRevolver)
        .asInstanceOf[Municion].cantidadDeBalas == 5)
    }
  
  test("krillin, inconsiente, se come una semilla de ermitanio y se recupera"){
    val resultado = krillin.utilizarMovimiento(usarSemillaDeErmitanio)(goku)
    assert(atacante(resultado).estado == Vivo)
  }
  
  test("cell se come a terminator y aprende a disparar un revolver"){
    val resultado = cell.utilizarMovimiento(comerseAlOponente)(terminator)
    assert(atacante(resultado)
        .listaDeMovimientosConocidos
        .filter {movimiento => movimiento.eq(usarRevolver)}
        .size == 1)    
  }
  
  test("goku se convierte en mono"){
    val resultado = gokuConCola.utilizarMovimiento(convertirseEnMono)(cell)
    assert(atacante(resultado).especie == Mono(true))
  }
  
  test("goku como un SS de nivel 4, se convierte en un SS de nivel 5"){
    val resultado = gokuSS.utilizarMovimiento(convertirseEnSS)(cell)
    assert(atacante(resultado).especie == SuperSaiyajin(true,5))
  }
  
  test("goku se fusiona con krillin"){
    val resultado = gokuDeChico.utilizarMovimiento(fusionarseConKrillin)(cell)
    assert(atacante(resultado).especie == Fusionado(gokuDeChico))
  }
  
  test("un mago malvado hace una magia loca que intercambie el valor del ki con goku"){
    val resultado = magoMalvado.utilizarMovimiento(intercambiarKi)(goku)
    assert(atacante(resultado).ki == goku.ki && defensor(resultado).ki == magoMalvado.ki && atacante(resultado).listaDeItems.isEmpty)
  }
  
  test("krillin ataca con muchos golpes a terminator y se lastima los deditos"){
    val resultado = krillinVivo.utilizarMovimiento(muchosGolpes)(terminator)
    assert(atacante(resultado).ki == (krillinVivo.ki - 10))
  }
  test("terminator sobre cargado explota y deja en 1 de vida a piccolo"){
    val resultado = terminatorSobrecargado.utilizarMovimiento(explota)(piccolo)
    assert(defensor(resultado).ki == 1)
  }
  
  test("terminator lanza un ataque de onda que le regenera 100 de bateria al terminator comun"){
    val resultado = terminatorSobrecargado.utilizarMovimiento(ondaDe50energia)(terminator)
    assert(defensor(resultado).dameElPoder == terminator.dameElPoder + 100)
  }
  
  test("goku destruido lanza un ataque de Genkidama contra vegetta y lo mata"){
    val resultado = gokuDestruido.utilizarMovimiento(genkidama)(vegeta)
    assert(defensor(resultado).estado == Muerto)
  }
  
  
}
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
 