import scala.util.{ Try, Success, Failure }
import Tipos._
import ObjetoItem._
import scala.math._
import Especie._
import scala.collection.mutable.MutableList



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
    Try(listaDeMovimientosConocidos.maxBy{movimiento => unCriterio(movimiento,(generarDueloNuevo(this)(otroGuerrero)))})
  }
  
  //Con que criterio el contraatacante elige que movimiento usar??
  def pelearRoundSegunUnCriterio(unMovimiento : Movimiento)(otroGuerrero : Guerrero)(criterio:Criterio) : Duelo = 
  {
    val ataque : Duelo = unMovimiento(generarDueloNuevo(this)(otroGuerrero))
    val movimientoDeContraataque : Try[Movimiento] = defensor(ataque).movimentoMasEfectivoContra(atacante(ataque))(masDanioHace)
    movimientoDeContraataque.getOrElse(ataque).asInstanceOf[Movimiento](generarDueloNuevo(defensor(ataque))(atacante(ataque)))
  }
  
  def pelearRound(unMovimiento : Movimiento)(otroGuerrero : Guerrero) = pelearRoundSegunUnCriterio(unMovimiento)(otroGuerrero)(meDejaConElMayorKi)
  
  def planDeAtaqueContra(guerrero:Guerrero,cantidadDeRounds:Int)(criterio:Criterio){
    var plan : MutableList[Movimiento] = MutableList()
    desarrollarPlanDeAtaque(guerrero,cantidadDeRounds,criterio, plan)
  }
  
  def desarrollarPlanDeAtaque(guerrero:Guerrero,cantidad:Int,criterio:Criterio, planDeAtaque:MutableList[Movimiento]):MutableList[Movimiento]={
    cantidad match{
      case 0 => planDeAtaque
      case _ => {
        val movi = ((movimentoMasEfectivoContra(guerrero)(criterio)).getOrElse(this.listaDeMovimientos.head))
        val estado = pelearRound(movi)(guerrero)
        planDeAtaque += movi
        atacante(estado).desarrollarPlanDeAtaque(defensor(estado),cantidad - 1,criterio,planDeAtaque)      
      }
    }
  }
    
  }
