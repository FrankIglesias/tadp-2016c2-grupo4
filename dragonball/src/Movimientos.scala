import Especies.Humano
import Tipos._

trait Movimiento

case object DejarseFajar extends Movimiento {
  def apply(duelo: Duelo): Duelo = {
    duelo match {
      case (g,_) if g.especie == Humano => ???
      case (Humano(g),_) => ???
    }
  }
}
case class CargarKi(val ki: Int) extends Movimiento
case class UsarItem(val item: Items) extends Movimiento
case class Convertirse(val estado: Especie) extends Movimiento //Si mas adelante nos dicen que se convierte en un chocolate solo cambiamos este movimiento y le agregamos un case mas
case class Fusion(val otroGuerrero: Guerrero) extends Movimiento
case class Magia(val magia: TipoDeMagia) extends Movimiento
case class UsarAtaque(val ataque: Ataque) extends Movimiento
case class ComerseAlOponente(val oponente: Guerrero) extends Movimiento
