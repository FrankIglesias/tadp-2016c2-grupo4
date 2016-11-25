import scala.util.{ Try, Success, Failure }
import Tipos._
import TodosLosMovimientos._
import ObjetoItem._
import scala.math._
import scala.collection.mutable.MutableList




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
  
  def planDeAtaqueContra(guerrero:Guerrero,cantidadDeRounds:Int)(criterio:Criterio){
    var plan : PlanDeAtaque = MutableList()
    desarrollarPlanDeAtaque(guerrero,cantidadDeRounds,criterio, plan)
  }
  
  def desarrollarPlanDeAtaque(guerrero:Guerrero,cantidad:Int,criterio:Criterio, planDeAtaque:PlanDeAtaque):PlanDeAtaque ={
    cantidad match{
      case 0 => planDeAtaque
      case _ => {
        val movi = ((movimentoMasEfectivoContra(guerrero)(criterio)).getOrElse(this.listaDeMovimientosConocidos.head))
        val estado = pelearRound(movi)(guerrero)
        planDeAtaque += movi
        atacante(estado).desarrollarPlanDeAtaque(defensor(estado),cantidad - 1,criterio,planDeAtaque)      
      }
    }
  }
  
  def pelearContra(guerrero:Guerrero)(plan:PlanDeAtaque): ResultadoPelea={
    this.estado match{
      case Muerto => (Muerto,guerrero.estado)
      case _ if guerrero.estado.eq(Muerto) => (this.estado,Muerto)
      case _ if plan.isEmpty => (this.estado,guerrero.estado)
      case _ => {
        val movimiento = plan.head
        val nuevoPlan = plan.tail
        val nuevoDuelo = this.pelearRound(movimiento)(guerrero)
        atacante(nuevoDuelo).pelearContra(guerrero)(nuevoPlan)
      }
    }
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
