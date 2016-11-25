
import TodosLosMovimientos._
import TiposDeAtaque._
import ObjetoItem._

object Tipos {
  type Duelo = (Guerrero,Guerrero)
  type Movimiento = Duelo => Duelo
  
  type Digerir = Duelo => Guerrero
  type Criterio = (Movimiento,Duelo) => Integer
  type PlanDeAtaque = List[Movimiento]
  type ResultadoPelea = (EstadoGuerrero,EstadoGuerrero)
  
  def atacante(duelo:Duelo) : Guerrero = duelo._1
  def defensor(duelo:Duelo) : Guerrero = duelo._2

  
  def analizarMovimientoYEjecutar(movimiento:Movimiento,duelo:Duelo):Duelo = {
    movimiento match{
      case DejarseFajar => movimiento(aumentarCantidadDeFajadas(atacante(duelo)),defensor(duelo))
      case Atacar(Genkidama) => movimiento(atacante(duelo),defensor(duelo))
      case _ => movimiento(resetearCantidadDeFajadas(atacante(duelo)),defensor(duelo))    
    }
  }
  
  def aumentarCantidadDeFajadas(guerrero:Guerrero) ={
    guerrero.copy(cantidadDeFajadas = guerrero.cantidadDeFajadas + 1)
  }
  
  def resetearCantidadDeFajadas(guerrero:Guerrero) = {
    guerrero.copy(cantidadDeFajadas = 0)
  }
  
  def disminuirMunicion(item:Item,municion:Municion)={
    if(item.eq(municion)){
      municion.copy(cantidadDeBalas = municion.cantidadDeBalas - 1)
    }else{
      item
    }
  }
  


  val menosDanioHace: Criterio = (movimiento,duelo) => defensor(duelo).dameElPoder- defensor(atacante(duelo).utilizarMovimiento(movimiento)(defensor(duelo))).dameElPoder
  val masDanioHace: Criterio = (movimiento,duelo) => defensor(duelo).dameElPoder+ defensor(atacante(duelo).utilizarMovimiento(movimiento)(defensor(duelo))).dameElPoder
  val meDejaConElMayorKi : Criterio = (movimiento,duelo) => atacante(atacante(duelo).utilizarMovimiento(movimiento)(defensor(duelo))).dameElPoder - defensor(atacante(duelo).utilizarMovimiento(movimiento)(defensor(duelo))).dameElPoder



}