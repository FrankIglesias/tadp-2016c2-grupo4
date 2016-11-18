
import scala.util.Try

object Tipos {
  type Duelo = (Guerrero,Guerrero)
  type Movimiento = Duelo => Duelo
  type Digerir = Guerrero => Guerrero
  type Criterio = (List[Movimiento], Duelo) => Movimiento 
  type AtaqueDeEnegia = Duelo => (Integer, Integer)
  def atacante(duelo:Duelo) : Guerrero = duelo._1
  def defensor(duelo:Duelo) : Guerrero = duelo._2
  
  def generarDueloNuevo(atacante:Guerrero)(defensor:Guerrero): Duelo=(atacante.copy(),defensor.copy())

}