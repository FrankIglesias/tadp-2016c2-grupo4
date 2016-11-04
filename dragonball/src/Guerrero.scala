import scala.util.{ Try, Success, Failure }


abstract class EstadoGuerrero
case object Inconsciente extends EstadoGuerrero
case object Muerto extends EstadoGuerrero
case object Vivo extends EstadoGuerrero

case class Guerrero(
    estado: EstadoGuerrero = Vivo,
    listaDeMovimientos: List[(Unit => Guerrero)],
    listaDeItems: List[Items],
    ki: Int,
    kiMaximo: Int,
    especie: Especie) {

  def cargar(delta: Int) = copy(ki = ki + delta min kiMaximo)

  def dejarseFajar(): Guerrero = copy()
  
  def cargarKi(): Guerrero = {
    especie match {
      case SuperSaiyajins(_, nivel) => cargar(150 * nivel)
      case Androides(_) => cargar(0)
      case _ => cargar(100)
    }
  }

  def usarItem(item: Items, guerrero: Guerrero): Guerrero = {
    item.asInstanceOf[Armas].usarCon(guerrero)
  }
  def comerseAlOponente(guerrero: Guerrero) = {
    especie match {
      case m: Monstruos => if (guerrero.ki < ki) Try(this).flatMap { guerrero => m.funcion(this, guerrero) }
      case _ => // should explode
    }
  }
  def convertirseEnMono(): Guerrero = {
    especie match {
      case Saiyajins(c) => if (c == true && listaDeItems.contains(FotodeLaLuna)) copy(especie = Mono(true), kiMaximo = kiMaximo * 3, ki = kiMaximo * 3) else copy()
      case _ => copy() // SHOULD EXPLODE
    }

  }
  def convertirseEnSS(): Guerrero = {
    especie match {
      case Saiyajins(c) => if (ki >= kiMaximo / 2) this.copy(especie = SuperSaiyajins(c, 1)) else copy()
      case SuperSaiyajins(cola, nivel) => if (ki >= kiMaximo / 2) this.copy(especie = SuperSaiyajins(cola, nivel + 1))else copy()
      case _ => copy()
    }
  }
 def fusionarse(guerrero : Guerrero) : Guerrero = {
 especie match {
 case Humano | Saiyajins(_) | Namukesins => copy(especie = Fusionado(this), ki = ki +guerrero.ki, kiMaximo = kiMaximo + guerrero.kiMaximo)
 case _ => copy()
 }
 }
 def magia(guerrero : Guerrero,f : (Guerrero=>Try[Guerrero])) : Guerrero = {
 especie match {
 case  Namukesins  | Monstruos(_) =>Try(this).flatMap { guerrero => f(guerrero) } 
 case _ => if(listaDeItems.filter { item => equals(EsferaDeDragon)}.size == 7) Try(this).flatMap { guerrero => f(guerrero) } 
 }
 copy()
 }
