
import scala.util.Try
import scala.collection.mutable.MutableList

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


  val menosDanioHace: Criterio = (movimiento,duelo) => defensor(duelo).dameElPoder- defensor(atacante(duelo).utilizarMovimiento(movimiento)(defensor(duelo))).dameElPoder
  val masDanioHace: Criterio = (movimiento,duelo) => defensor(duelo).dameElPoder+ defensor(atacante(duelo).utilizarMovimiento(movimiento)(defensor(duelo))).dameElPoder
  val meDejaConElMayorKi : Criterio = (movimiento,duelo) => atacante(atacante(duelo).utilizarMovimiento(movimiento)(defensor(duelo))).dameElPoder - defensor(atacante(duelo).utilizarMovimiento(movimiento)(defensor(duelo))).dameElPoder



}