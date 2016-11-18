
import scala.util.Try
import scala.collection.mutable.MutableList

object Tipos {
  type Duelo = (Guerrero,Guerrero)
  type Movimiento = Duelo => Duelo
  type Digerir = Guerrero => Guerrero
  type Criterio = (Movimiento,Duelo) => Integer
  type PlanDeAtaque = MutableList[Movimiento]
  
  def atacante(duelo:Duelo) : Guerrero = duelo._1
  def defensor(duelo:Duelo) : Guerrero = duelo._2

  def generarDueloNuevo(atacante:Guerrero)(defensor:Guerrero): Duelo=(atacante.copy(),defensor.copy())

  val menosDanioHace: Criterio = (movimiento,duelo) => defensor(duelo).dameElPoder- defensor(movimiento(duelo)).dameElPoder
  val masDanioHace: Criterio = (movimiento,duelo) => defensor(duelo).dameElPoder + defensor(movimiento(duelo)).dameElPoder
  val meDejaConElMayorKi : Criterio = (movimiento,duelo) => atacante(movimiento(duelo)).dameElPoder - defensor(movimiento(duelo)).dameElPoder
}