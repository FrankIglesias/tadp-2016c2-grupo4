
import scala.util.Try
import scala.collection.mutable.MutableList
import TodosLosMovimientos.DejarseFajar

object Tipos {
  type Duelo = (Guerrero,Guerrero)
  type Movimiento = Duelo => Duelo
  type Digerir = Guerrero => Guerrero
  type Criterio = (Movimiento,Duelo) => Integer
  type PlanDeAtaque = MutableList[Movimiento]
  type ResultadoPelea = (EstadoGuerrero,EstadoGuerrero)
  
  def atacante(duelo:Duelo) : Guerrero = duelo._1
  def defensor(duelo:Duelo) : Guerrero = duelo._2

  def generarDueloNuevo(atacante:Guerrero)(defensor:Guerrero): Duelo=(atacante.copy(),defensor.copy())
  
  def analizarMovimientoYEjecutar(movimiento:Movimiento,duelo:Duelo):Duelo = {
    movimiento match{
      case DejarseFajar => movimiento(generarDueloNuevo(aumentarCantidadDeFajadas(atacante(duelo)))(defensor(duelo)))
      case _ => movimiento(generarDueloNuevo(resetearCantidadDeFajadas(atacante(duelo)))(defensor(duelo)))    
    }
  }
  
  def aumentarCantidadDeFajadas(guerrero:Guerrero) ={
        guerrero.copy(cantidadDeFajadas = guerrero.cantidadDeFajadas + 1)
      }
  
  def resetearCantidadDeFajadas(guerrero:Guerrero) = {
    guerrero.copy(cantidadDeFajadas = 0)
  }

  val menosDanioHace: Criterio = (movimiento,duelo) => defensor(duelo).dameElPoder- defensor(atacante(duelo).utilizarMovimiento(movimiento)(defensor(duelo))).dameElPoder
  val masDanioHace: Criterio = (movimiento,duelo) => defensor(duelo).dameElPoder+ defensor(atacante(duelo).utilizarMovimiento(movimiento)(defensor(duelo))).dameElPoder
  val meDejaConElMayorKi : Criterio = (movimiento,duelo) => atacante(atacante(duelo).utilizarMovimiento(movimiento)(defensor(duelo))).dameElPoder - defensor(atacante(duelo).utilizarMovimiento(movimiento)(defensor(duelo))).dameElPoder



}