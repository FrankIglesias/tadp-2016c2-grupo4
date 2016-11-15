
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
      def apply(duelo:Duelo) = ???
    }
    
    case class UsarItem(itemAUsar:Item) extends Movimiento {
      def apply(duelo:Duelo) : Duelo = atacante(duelo)
        .listaDeItems
        .find {item => item.equals(itemAUsar)}
        .fold(duelo.copy())(item => item(duelo))
    }
    
    case object Convertirse extends Movimiento {
      def apply(estado: Especie, duelo: Duelo) : Duelo = ???
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