
import Tipos._
import TodosLosMovimientos._
import ObjetoItem._
import scala.math._




trait EstadoGuerrero
case object Inconsciente extends EstadoGuerrero
case object Muerto extends EstadoGuerrero
case object Vivo extends EstadoGuerrero

case class Guerrero(
    estado: EstadoGuerrero = Vivo,
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
  
  def encontrarBalas(armaDeFuego:ArmaDeFuego)={
    listaDeItems
    .filter{item => item.isInstanceOf[Municion]}
    .find{item => item.asInstanceOf[Municion].armaAsociada.eq(armaDeFuego)}
  }

  def quedateInconsiente() ={
    this.especie match {
      case SuperSaiyajin(p,_) => this.copy(estado = Inconsciente,especie = Saiyajin(p))
      case Fusionado(guerrero) => guerrero.copy(estado = Inconsciente)
      case _ => this.copy(estado = Inconsciente)
    }
  }
  
  def disminuirElPoder(poderADisminuir:Int) = {
    especie match {
      case Androide(b) => (this.copy(especie = Androide(bateria = max(b - poderADisminuir,0)))).analizaSiEstoyMuerto()
      case _ => (this.copy(ki = max(ki - poderADisminuir,0))).analizaSiEstoyMuerto()
    }
  }
  

  
  def aumentarElPoder(poderAAumentar:Int)={
    especie match {
      case Androide(b) => this.copy(especie = Androide(bateria = b + poderAAumentar))
      case _ => this.copy(ki = min(ki + poderAAumentar,this.kiMaximo))
    }
  }

  
  def seLaBancaContra(kiAComparar:Int) = (if(kiAComparar > this.dameElPoder) this.disminuirElPoder(20) else this.copy())
  
  def analizaSiEstoyMuerto() = especie match {
      case Androide(b) if b==0 => this.morite()
      case _ if this.ki == 0 => this.morite()
      case _ => this
    }
  
  def morite()={
    especie match {
      case Androide(_) => this.copy(especie = Androide(bateria = 0),estado = Muerto)
      case Fusionado(guerrero) => guerrero.especie match{
        case Androide(_) => guerrero.copy(especie = Androide(bateria = 0),estado = Muerto)
        case _ => guerrero.copy(ki = 0, estado = Muerto)        
      }
      case _ => this.copy(ki = 0, estado = Muerto)
    }
  }
  
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
    Option(listaDeMovimientosConocidos.maxBy{movimiento => unCriterio(movimiento,(this,otroGuerrero))})
  }
  
  def pelearRound(unMovimiento : Movimiento)(otroGuerrero : Guerrero) : Duelo = 
  {
    val resultadoDeMiAtaque = this.utilizarMovimiento(unMovimiento)(otroGuerrero)
    defensor(resultadoDeMiAtaque).contraAtacar(atacante(resultadoDeMiAtaque))
  }
  
  def contraAtacar(unGuerrero:Guerrero)={
    val resultadoDelContraAtaque = this.movimentoMasEfectivoContra(unGuerrero)(meDejaConElMayorKi)
    resultadoDelContraAtaque
        .fold((this,unGuerrero))(this.utilizarMovimiento(_)(unGuerrero))
  }
  
  def planDeAtaqueContra(guerrero:Guerrero,cantidadDeRounds:Int)(criterio:Criterio):PlanDeAtaque = {
    val planInicial : PlanDeAtaque = List()
    List.range(0,cantidadDeRounds).foldLeft(planInicial,(this,guerrero))(
        (semillaAnterior : (PlanDeAtaque,(Guerrero,Guerrero)),_) => {
          val (planAcumulado,(atacante,defensor)) = semillaAnterior
          val nuevoMovimiento : Movimiento = atacante.movimentoMasEfectivoContra(defensor)(criterio).getOrElse(MovimientoNulo)
          (planAcumulado:+ nuevoMovimiento , atacante.pelearRound(nuevoMovimiento)(defensor))
          })._1
  }
  
  
  def pelearContra(guerrero:Guerrero)(plan:PlanDeAtaque): ResultadoPelea={
    val resultadoInicial : ResultadoPelea =  Pelien(this,guerrero)
    plan.foldLeft(resultadoInicial)((resultadoAnterior,movimiento) => resultadoAnterior.map(movimiento))
  }
  
  
 def utilizarMovimiento(movimiento:Movimiento)(oponente:Guerrero):Duelo={
  this.listaDeMovimientosConocidos.find {m => m.eq(movimiento) }
  .fold(
      (this,oponente))(
      {movi => this.estado match{
         case Muerto => ((this),(oponente))
         case Inconsciente =>{
           movi match{
             case UsarItem(semilla) if semilla.eq(SemillaDeErmitanio) => analizarMovimientoYEjecutar(movi,((this),(oponente)))
             case _ => ((this),(oponente))
           }
         }
         case _ => analizarMovimientoYEjecutar(movi,((this),(oponente))) 
       }
      })
  }
}
