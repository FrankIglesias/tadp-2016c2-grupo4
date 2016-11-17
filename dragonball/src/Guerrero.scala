import scala.util.{ Try, Success, Failure }
import Tipos._
import ObjetoItem._
import scala.math._


abstract class EstadoGuerrero
case object Inconsciente extends EstadoGuerrero
case object Muerto extends EstadoGuerrero
case object Vivo extends EstadoGuerrero

case class Guerrero(
    estado: EstadoGuerrero = Vivo,
    listaDeMovimientos: List[Movimiento],
    listaDeItems: List[Item],
    ki: Int,
    kiMaximo: Int,
    especie: Especie) {  
  
  def tenesBalas(arma:Arma) = listaDeItems
  .find{item => item.asInstanceOf[Municion].armaAsociada.eq(arma.asInstanceOf[Arma])}
  .map{municion => municion.asInstanceOf[Municion].disminuirMunicion(1)}
  .isEmpty
  
  def quedateInconsiente() ={
    this.especie match {
      case SuperSaiyajin(p,_) => this.copy(estado = Inconsciente,especie = Saiyajin(p))
      case _ => this.copy(estado = Inconsciente)
    }
  }
  
  def disminuirElKi(kiADisminuir:Int)={
    this.copy(ki = this.ki - kiADisminuir)
  }
  
  def seLaBancaContra(kiAComparar:Int) = (if(kiAComparar > this.ki) this.disminuirElKi(20) else this.copy())
  
  def morite() = this.copy(ki = 0,estado = Muerto)
  
  def recibiExplosion(golpeDeLaExplosion:Int)={
    this.especie match{
      case Namekusein => this.disminuirElKi(min((this.ki - 1), golpeDeLaExplosion))
      case _ => this.disminuirElKi(golpeDeLaExplosion)
    }
  }
}
