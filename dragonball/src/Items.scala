
import Tipos._
import Movimiento._
class Item

class Arma extends Item {
  object ArmaRoma  extends Arma {
    def apply(duelo:Duelo) = {
      defensor(duelo).especie match {
        case Androide(_) => defensor(duelo).copy()
        case otro if defensor(duelo).ki < 300 => defensor(duelo).copy(estado = Inconsciente)
      }
    }
  }
  
  object ArmaFilosa  extends Arma{
    def apply(duelo:Duelo) = ???
  }
  object ArmaDeFuego extends Arma
}