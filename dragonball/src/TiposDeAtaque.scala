
import Tipos._

object TiposDeAtaque {
  class Ataque{ def apply(duelo:Duelo) = duelo.copy()}
  
  
  class AtaqueDeEnergia extends Ataque()
  class AtaqueFisico extends Ataque()
  
  case object MuchosGolpesNinja extends AtaqueFisico(){
    override def apply(duelo:Duelo) = atacante(duelo).especie match {
      case Humano if defensor(duelo).especie.eq(Androide) => {generarDueloNuevo(
          atacante(duelo).disminuirElKi(10))(
          defensor(duelo))
      }
    }
  }
}