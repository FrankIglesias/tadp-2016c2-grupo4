
import Tipos._

object ObjetoAtaque {
  
}

class Ataque {
  
}

class AtaqueDeEnergia(duelo: Duelo, ataque: (Duelo => (Integer, Integer))) extends Ataque {
   def apply(duelo:Duelo) = {
     defensor(duelo).especie match {
       case Androide(_) => cambiarKiYGenerarDuelo(ataque(duelo))
       case _ => cambiarKiYGenerarDuelo(-ataque(duelo))
     }
   }
   
   def cambiarKiYGenerarDuelo(delta : (Integer, Integer)) {
     generarDueloNuevo(atacante(duelo).copy(atacante(duelo).ki - delta._1 min atacante(duelo).kiMaximo))(defensor(duelo).copy(ki = defensor(duelo).ki + delta._2 min defensor(duelo).kiMaximo))
   }
}

class AtaqueFisico {
  def apply(duelo:Duelo) = duelo.copy()
}

trait Ataques {
  
  def KameHameHa(duelo:Duelo => Integer) {
     return juntarKi
  }
  
}