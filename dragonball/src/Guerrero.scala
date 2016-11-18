import scala.util.{ Try, Success, Failure }
import Tipos._
import ObjetoItem._
import scala.math._
import Especie._



abstract class EstadoGuerrero
case object Inconsciente extends EstadoGuerrero
case object Muerto extends EstadoGuerrero
case object Vivo extends EstadoGuerrero

case class Guerrero(
    estado: EstadoGuerrero = Vivo,
    listaDeMovimientos: List[Movimiento],
    listaDeMovimientosConocidos: List[Movimiento],
    listaDeItems: List[Item],
    ki: Int,
    kiMaximo: Int,
    especie: Especie,
    cantidadDeFajadas: Int) {
  
  def dameElPoder : Int = {especie match{
    case Androide(bateria) => bateria
    case _ => ki
    }
  }
  
    
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
  
  def disminuirElPoder(poderADisminuir:Int) = {
    especie match {
      case Androide(b) => this.copy(especie = Androide(bateria = max(b - poderADisminuir,0)))
      case _ => this.copy(ki = ki - poderADisminuir min 0)
    }
  }
    
  
  def aumentarElPoder(poderAAumentar:Int)={
    especie match {
      case Androide(b) => this.copy(especie = Androide(bateria = b + poderAAumentar))
      case _ => this.copy(ki = ki - poderAAumentar)
    }
  }
  
  def seLaBancaContra(kiAComparar:Int) = (if(kiAComparar > this.dameElPoder) this.disminuirElPoder(20) else this.copy())
  
  def morite() = this.copy(ki = 0,estado = Muerto)
  
  def recibiExplosion(golpeDeLaExplosion:Int)={
    this.especie match{
      case Namekusein => this.disminuirElPoder(min((this.dameElPoder - 1), golpeDeLaExplosion))
      case _ => this.disminuirElPoder(golpeDeLaExplosion)
    }
  }
  
  def recibirDanioDeEnergia(danio:Int)={
    especie match{
      case Androide(_) => this.aumentarElPoder(danio)
      case _ => this.disminuirElPoder(danio)
    }
  }
  
  def movimentoMasEfectivoContra(otroGuerrero : Guerrero)(unCriterio : Criterio) = 
  {
    Try(
        listaDeMovimientosConocidos.maxBy{movimiento => unCriterio(movimiento,(generarDueloNuevo(this)(otroGuerrero)))})
  }
  
  //Con que criterio el contraatacante elige que movimiento usar??
  def pelearRound(unMovimiento : Movimiento)(otroGuerrero : Guerrero) : Duelo = 
  {
    val ataque : Duelo = unMovimiento(generarDueloNuevo(this)(otroGuerrero))
    val movimientoDeContraataque : Movimiento = movimentoMasEfectivoContra(this)(???)
    
    return movimientoDeContraataque(ataque)
  }
  
  

}
