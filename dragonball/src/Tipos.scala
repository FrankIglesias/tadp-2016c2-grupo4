
import scala.util.Try

object Tipos {
  type Duelo = (Guerrero,Guerrero)
  type Movimiento = Duelo => Duelo
  type Digerir = Guerrero => Guerrero
  type Criterio = (Movimiento,Duelo) => Integer
  
  def atacante(duelo:Duelo) : Guerrero = duelo._1
  def defensor(duelo:Duelo) : Guerrero = duelo._2

  def generarDueloNuevo(atacante:Guerrero)(defensor:Guerrero): Duelo=(atacante.copy(),defensor.copy())

  val menosDanioHace: Criterio = (movimiento,duelo) => defensor(duelo).dameElPoder- defensor(movimiento(duelo)).dameElPoder
  val masDanioHace: Criterio = (movimiento,duelo) => defensor(duelo).dameElPoder + defensor(movimiento(duelo)).dameElPoder
 
}