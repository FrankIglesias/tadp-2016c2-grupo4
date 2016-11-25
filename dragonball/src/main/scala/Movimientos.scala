
import Tipos._
import ObjetoItem._
import TiposDeAtaque._

object TodosLosMovimientos{
    
  
    case object DejarseFajar extends Movimiento {
      def apply(duelo: Duelo): Duelo = (aumentarCantidadDeFajadas(atacante(duelo)),defensor(duelo))
    }
  
    
    case object CargarKi extends Movimiento {
      def apply(duelo:Duelo) : Duelo = {(aumentarElKiSegunTipo(atacante(duelo)),defensor(duelo))
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
          case Monstruo(_) if kiEsMenor(atacante(duelo),defensor(duelo)) =>(
             atacante(duelo)
            .especie
            .asInstanceOf[Monstruo]
            .maneraDeDigerir(atacante(duelo),defensor(duelo)),defensor(duelo).morite())
          case _ => duelo
        }
      }
      
      def kiEsMenor(atacante:Guerrero, defensor:Guerrero) = { 
        atacante.dameElPoder>defensor.dameElPoder 
        }
    }
    
    case class UsarItem(itemAUsar:Item) extends Movimiento {
      def apply(duelo:Duelo) : Duelo = atacante(duelo)
        .listaDeItems
        .find {item => item.equals(itemAUsar)}
        .fold(duelo)(item => item(duelo))
    }
    
    case class Convertirse(especieAConvertirse:Especie) extends Movimiento {
      def apply(duelo: Duelo) : Duelo = { 
        especieAConvertirse match {
          case Mono(_) => (convertirseEnMono(atacante(duelo)),defensor(duelo))
          case SuperSaiyajin(_,_) => (convertirseEnSS(atacante(duelo)),defensor(duelo))
          case _ => duelo
        }
      }
    }
    
    def convertirseEnMono(guerrero:Guerrero) : Guerrero = {
      guerrero.especie match {
        case Saiyajin(c) if (c && guerrero.listaDeItems.contains(FotodeLaLuna)) => guerrero.copy(especie = Mono(c), kiMaximo = guerrero.kiMaximo * 3, ki = guerrero.kiMaximo * 3)
        case _ => guerrero
      }
    }
      
    def convertirseEnSS(guerrero:Guerrero): Guerrero = {
     guerrero.especie match {
       case Saiyajin(c) if (guerrero.ki >= guerrero.kiMaximo / 2) => guerrero.copy(especie = SuperSaiyajin(c, 1),kiMaximo = guerrero.kiMaximo * 5)
       case SuperSaiyajin(cola, nivel) if (guerrero.ki >= guerrero.kiMaximo / 2) => guerrero.copy(especie = SuperSaiyajin(cola, nivel + 1),kiMaximo = guerrero.kiMaximo * 5 * (nivel + 1))
       case _ => guerrero
     }
    }
    
    case class Fusion(otroGuerrero:Guerrero) extends Movimiento {
      def apply(duelo: Duelo)  : Duelo = {
         (atacante(duelo).especie,otroGuerrero.especie) match{
           case (especieDelAtacante,especieDelOtro) if contieneUnaDeLasEspeciesNecesarias(especieDelAtacante,especieDelOtro) =>
             (atacante(duelo).copy(
               especie = Fusionado(atacante(duelo)), 
               ki = atacante(duelo).ki + otroGuerrero.ki, 
               kiMaximo = atacante(duelo).kiMaximo + otroGuerrero.kiMaximo,
               listaDeMovimientosConocidos = atacante(duelo).listaDeMovimientosConocidos ::: otroGuerrero.listaDeMovimientosConocidos)
               ,defensor(duelo))
           case _ => duelo
         }
      }
      
      def contieneUnaDeLasEspeciesNecesarias(especie_1:Especie, especie_2:Especie) = { 
        val listaDeEspecies = List(Humano,Namekusein,Saiyajin(true),Saiyajin(false))
        listaDeEspecies.contains(especie_1)  && listaDeEspecies.contains(especie_2)
        }
    }
    
    case class Magia(magia: (Duelo=>Duelo)) extends Movimiento {
      def apply(duelo: Duelo) : Duelo = {
        atacante(duelo).especie match {
          case Namekusein | Monstruo(_) => magia(duelo)
          case _ if atacante(duelo).listaDeItems.count{item => item.eq(EsferaDeDragon)} >= 7 => removeEsferasYGeneraDuelo(duelo,magia)
          case _ => duelo
        }
      }
      
      def removeEsferasYGeneraDuelo(duelo:Duelo,magia:(Duelo=>Duelo)) = {
        val atacanteSinBolas = atacante(duelo).copy(listaDeItems = atacante(duelo).listaDeItems.dropWhile { item => item.eq(EsferaDeDragon)})
        magia(atacanteSinBolas,defensor(duelo))
      }
    }
    
    case class Atacar(ataque: Ataque) extends Movimiento {
       def apply(duelo:Duelo) = ataque(duelo)
    }
    
    case object MovimientoNulo extends Movimiento {
      def apply(duelo:Duelo)=duelo
    }
}
