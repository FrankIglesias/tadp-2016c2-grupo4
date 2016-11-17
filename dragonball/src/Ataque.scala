
import Tipos._

object ObjetoAtaque {
  
}

class Ataque {
  
}

class AtacarConEnergia(duelo: Duelo, ataque: AtaqueDeEnergia) extends Ataque {
   def apply(duelo:Duelo) = {
     defensor(duelo).especie match {
       case Androide(_) => cambiarKiYGenerarDuelo(negarSegundoParametro(ataque(duelo)))
       case _ => cambiarKiYGenerarDuelo(ataque(duelo))
     }
     
   }
   
   def negarSegundoParametro(delta:(Integer,Integer)):(Integer, Integer) = {
     return (delta._1,-delta._2)
   }
   
   //TODO lo de la bateria del androide, esta solo con ki 
   def cambiarKiYGenerarDuelo(delta : (Integer, Integer)) {
     generarDueloNuevo(atacante(duelo).copy(ki = atacante(duelo).ki - delta._1 min atacante(duelo).kiMaximo))(defensor(duelo).copy(ki = defensor(duelo).ki + delta._2 min defensor(duelo).kiMaximo))
   }
}

class AtaqueFisico {
  def apply(duelo:Duelo) = duelo.copy()
}

trait Ataques {
  
  case class Onda(cantidadDeKi:Integer) {
    def apply(duelo:Duelo): (Integer,Integer) = 
    {
      defensor(duelo).especie match {
        case Mounstro(_) => return (cantidadDeKi,cantidadDeKi / 2);
        case _ => return (cantidadDeKi, cantidadDeKi * 2)
      }
     
    }
    
    case class Genkidama(cantidadDeKiExterno: Integer) {
       def apply(duelo:Duelo): (Integer,Integer) = 
      {
        return (cantidadDeKiExterno, 10*cuantoTeDejasteFajar(atacante(duelo)))
      }
       //TODO cuantos rounds seguidos se dejo fajar
       def cuantoTeDejasteFajar(guerrero: Guerrero) : Integer = ???
    }
    
    
  }
  
}