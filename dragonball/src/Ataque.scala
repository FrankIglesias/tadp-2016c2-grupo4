
import Tipos._

object ObjetoAtaque {
  
}

class Ataque {
  
}

class AtacarConEnergia(duelo: Duelo, ataque: AtaqueDeEnergia) extends Ataque {
   def apply(duelo:Duelo) = {
     defensor(duelo).especie match {
       case Androide(_) => cambiarKiYGenerarDuelo(ataque(duelo))
       case _ => cambiarKiYGenerarDuelo(negarSegundoParametro(ataque(duelo)))
     }
     
   }
   def negarSegundoParametro(delta:(Integer,Integer)){
     (delta._1,-delta._2)
   }
   def cambiarKiYGenerarDuelo(delta : (Integer, Integer)) {
     generarDueloNuevo(atacante(duelo).copy(atacante(duelo).ki - delta._1 min atacante(duelo).kiMaximo))(defensor(duelo).copy(ki = defensor(duelo).ki + delta._2 min defensor(duelo).kiMaximo))
   }
}

class AtaqueFisico {
  def apply(duelo:Duelo) = duelo.copy()
}

trait Ataques {
  
  case class Onda(cantidadDeKi:Integer) {
    def apply(duelo:Duelo => (Integer,Integer)){
     (cantidadDeKi,cantidadDeKi); 
    }
  }
  
}