
import scala.util.Try

object Tipos {
  type Duelo = (Guerrero,Guerrero)
  type Movimiento = Duelo => Duelo
}