import org.scalatest.FunSuite
import DragonBallBuilder._
import Tipos._
import TodosLosMovimientos._

class GuerreroTest extends FunSuite {
  test("goku busca el movimiento que lo deje con mayor ki frente a vegeta"){
    val movimiento = goku.movimentoMasEfectivoContra(vegeta)(meDejaConElMayorKi)
    
    assert(movimiento.getOrElse(MovimientoNulo).eq(cargarki))
  }
  
  test("goku pelea un round contra vegetta, utiliza un ataque de onda corto y vegeta responde cargandoseElKi"){
    val duelo = goku.pelearRound(ataqueDeOndacorta)(vegeta)
    assert(defensor(duelo).ki == (vegeta.ki) - 4 /*Es lo que saca el ataque de onda corta*/ + 100 /*Es el resultado de cargarse el ki*/)
  }
  
  test("goku desarrolla un plan de ataque con el objetivo de generarle el mayor danio posible a vegetta en 2 turnos"){
    val plan = gokuCon2Movimientos.planDeAtaqueContra(vegeta, 2)(masDanioHace)
    assert(plan.contains(genkidama) && plan.contains(usarEspadaSencilla)) 
  }
  
  test("goku lucha contra vegeta y lo mata"){
    val plan = gokuCon2Movimientos.planDeAtaqueContra(vegeta, 2)(masDanioHace)
    val resultadoDeLaPelea = gokuCon2Movimientos.pelearContra(vegeta)(plan)
    assert(resultadoDeLaPelea == HayUnGanador(gokuCon2Movimientos))
  }
}