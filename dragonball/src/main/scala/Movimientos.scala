
import Tipos._
import ObjetoItem._
import TiposDeAtaque._

object TodosLosMovimientos{
    
  
    case object DejarseFajar extends Movimiento {
      def apply(duelo: Duelo): Duelo = generarDueloNuevo(
          aumentarCantidadDeFajadas(atacante(duelo)))(
          defensor(duelo))
    }
  
    
    case object CargarKi extends Movimiento {
      def apply(duelo:Duelo) : Duelo = {
        generarDueloNuevo(aumentarElKiSegunTipo(atacante(duelo)))(defensor(duelo))
        }
    
      def aumentarElKiSegunTipo(guerrero:Guerrero) : Guerrero = {
        guerrero.especie match {
          case SuperSaiyajin(_,n) => guerrero.copy(ki = guerrero.dameElPoder + (150 * n)) 
          case Androide(_) => guerrero.copy()
          case _ => guerrero.copy(ki = guerrero.dameElPoder + 100)
        }
      }
    }
    
    case object ComerseAlOponente extends Movimiento {
      def apply(duelo:Duelo) = {
        atacante(duelo).especie match {
          case Monstruo(_) if kiEsMenor(atacante(duelo),defensor(duelo)) => generarDueloNuevo(
             atacante(duelo)
            .especie
            .asInstanceOf[Monstruo]
            .maneraDeDigerir(defensor(duelo)))(defensor(duelo).morite())
          case _ => duelo.copy()
        }
      }
      
      def kiEsMenor(atacante:Guerrero, defensor:Guerrero) = { 
        atacante.dameElPoder>defensor.ki 
        }
    }
    
    case class UsarItem(itemAUsar:Item) extends Movimiento {
      def apply(duelo:Duelo) : Duelo = atacante(duelo)
        .listaDeItems
        .find {item => item.equals(itemAUsar)}
        .fold(duelo.copy())(item => item(duelo))
    }
    
    case object Convertirse extends Movimiento {
      def apply(duelo: Duelo) : Duelo = { 
        atacante(duelo).especie match {
          case Mono(_) => generarDueloNuevo(convertirseEnMono(atacante(duelo)))(defensor(duelo))
          case SuperSaiyajin(_,_) =>  generarDueloNuevo(convertirseEnSS(atacante(duelo)))(defensor(duelo))
          case _ => duelo.copy()
        }
      }
    }
    
    def convertirseEnMono(guerrero:Guerrero) : Guerrero = {
      guerrero.especie match {
        case Saiyajin(c) if (c && guerrero.listaDeItems.contains(FotodeLaLuna)) => guerrero.copy(especie = Mono(c), kiMaximo = guerrero.kiMaximo * 3, ki = guerrero.kiMaximo * 3)
        case _ => guerrero.copy()
      }
    }
      
    def convertirseEnSS(guerrero:Guerrero): Guerrero = {
     guerrero.especie match {
       case Saiyajin(c) if (guerrero.ki >= guerrero.kiMaximo / 2) => guerrero.copy(especie = SuperSaiyajin(c, 1))
       case SuperSaiyajin(cola, nivel) if (guerrero.ki >= guerrero.kiMaximo / 2) => guerrero.copy(especie = SuperSaiyajin(cola, nivel + 1))
       case _ => guerrero.copy()
     }
    }
    
    case class Fusion(otroGuerrero:Guerrero) extends Movimiento {
      def apply(duelo: Duelo)  : Duelo = {
         atacante(duelo).especie match {
         case (Humano | Saiyajin(_) | Namekusein) if contieneAEstaEspecie(List(Humano,Saiyajin(true),Saiyajin(false),Namekusein),otroGuerrero.especie) =>
               generarDueloNuevo(atacante(duelo).copy(
               especie = Fusionado(atacante(duelo)), 
               ki = atacante(duelo).ki + otroGuerrero.ki, 
               kiMaximo = atacante(duelo).kiMaximo + defensor(duelo).kiMaximo))(defensor(duelo))
         case _ => duelo.copy()
        }
      }
      
      def contieneAEstaEspecie(listaDeEspecies: List[Especie], especie:Especie) = { 
        listaDeEspecies.contains(especie) 
        }
    }
    
    case class Magia(magia: (Duelo=>Duelo)) extends Movimiento {
      def apply(duelo: Duelo) : Duelo = {
        atacante(duelo).especie match {
          case Namekusein | Monstruo(_) => magia(duelo)
          case _ if atacante(duelo).listaDeItems.count{item => item.eq(EsferaDeDragon)} >= 7 => removeEsferasYGeneraDuelo(duelo,magia)
          case _ => duelo.copy()
        }
      }
      
      def removeEsferasYGeneraDuelo(duelo:Duelo,magia:(Duelo=>Duelo)) = {
        atacante(duelo).listaDeItems.dropWhile { item => item.eq(EsferaDeDragon)}
        magia(duelo)
      }
    }
    
    case class Atacar(ataque: Ataque) extends Movimiento {
       def apply( duelo:Duelo) = ataque(duelo)
    }
}
