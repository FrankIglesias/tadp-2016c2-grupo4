
import Tipos._
import ObjetoItem._

object TodosLosMovimientos{
    
  
  trait Movimiento{
  
    case class DejarseFajar() extends Movimiento {
      def apply(duelo: Duelo): Duelo = duelo.copy()
    }
    
    case class CargarKi(kiACargar:Int) extends Movimiento {
      def apply(duelo:Duelo) : Duelo ={ generarDueloNuevo(aumentarElKiSegunTipo(atacante(duelo)))(defensor(duelo))}
      
    
      def aumentarElKiSegunTipo(guerrero:Guerrero) : Guerrero = {
        guerrero.especie match {
          case SuperSaiyajin(_,n) => guerrero.copy(ki = guerrero.ki + (150 * n)) 
          case Androide(_) => guerrero.copy()
          case _ => guerrero.copy(ki = guerrero.ki + 100)
        }
      }
    }
    
    case class ComerseAlOponente() extends Movimiento{
      def apply(duelo:Duelo) = {
        atacante(duelo).especie match {
          case Monstruo(_) if kiEsMenor(atacante(duelo),defensor(duelo)) => generarDueloNuevo(
             atacante(duelo)
            .especie
            .asInstanceOf[Monstruo]
            .maneraDeDigerir(defensor(duelo)))(defensor(duelo).copy(estado = Muerto))
          case _ => duelo.copy()
        }
      }
      def kiEsMenor(atacante:Guerrero, defensor:Guerrero) ={atacante.ki>defensor.ki}
    }
    
    case class UsarItem(itemAUsar:Item) extends Movimiento {
      def apply(duelo:Duelo) : Duelo = atacante(duelo)
        .listaDeItems
        .find {item => item.equals(itemAUsar)}
        .fold(duelo.copy())(item => item(duelo))
    }
    
    case object Convertirse extends Movimiento {
      def apply(especie: Especie, duelo: Duelo) : Duelo = { 
        especie match {
          case Mono(_) => generarDueloNuevo(convertirseEnMono(atacante(duelo)))(defensor(duelo))
          case SuperSaiyajins(_,_) =>  generarDueloNuevo(convertirseEnSS(atacante(duelo)))(defensor(duelo))
          case _ => duelo.copy()
        }
      }
    }
    
    def convertirseEnMono(guerrero:Guerrero) : Guerrero = {
      guerrero.especie match {
        case Saiyajin(c) => if (c == true && listaDeItems.contains(FotodeLaLuna)) copy(especie = Mono(true), kiMaximo = guerrero.kiMaximo * 3, ki = guerrero.kiMaximo * 3) else guerrero.copy()
        case _ => guerrero.copy()
    }
      
    def convertirseEnSS(guerrero:Guerrero): Guerrero = {
     guerreo.especie match {
       case Saiyajin(c) => if (ki >= guerrero.kiMaximo / 2) this.copy(especie = SuperSaiyajin(c, 1)) else guerrero.copy()
       case SuperSaiyajin(cola, nivel) => if (ki >= kiMaximo / 2) this.copy(especie = SuperSaiyajin(cola, nivel + 1))else guerrero.copy()
       case _ => guerrero.copy()
     }
    
    
    case object Fusion extends Movimiento {
      def apply(otroGuerrero: Guerrero, duelo: Duelo)  : Duelo = ???
    }
    case object Magia extends Movimiento {
      def apply(magia: TipoDeMagia, duelo: Duelo) : Duelo = ???
    }
    
    //case object Atacar extends Movimiento {
    //  def apply(ataque:Ataque, duelo:Duelo) = ???
    //}

  }
}