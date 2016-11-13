
import Tipos._
import Movimiento._
class Item{
  def apply(duelo:Duelo) = duelo.copy()
}

class Arma extends Item {
  object ArmaRoma  extends Arma {
    def apply(duelo:Duelo) = {generarDueloNuevo(atacante(duelo))(matcheaDefensorArmaRoma(defensor(duelo)))}
    def matcheaDefensorArmaRoma(defensor:Guerrero)={
      defensor.especie match {
        case Androide(_) => defensor.copy()
        case otro if defensor.ki < 300 => defensor.copy(estado = Inconsciente)
      }
    }
  }
  
  object ArmaFilosa  extends Arma{
    def apply(duelo:Duelo) = {generarDueloNuevo(atacante(duelo))(analizaEfectoAlDefensor(atacante(duelo).ki,defensor(duelo)))}
    def analizaEfectoAlDefensor(kiDelAtacante:Int, defensor:Guerrero) : Guerrero = {
      defensor.especie match {
        case Saiyajin(true) => defensor.copy(ki = 1, especie = Saiyajin(false))
        case Mono(_) => defensor.copy(especie = Saiyajin(true),estado = Inconsciente, ki = kiDelAtacante/100)
        case _ => defensor.copy(ki = kiDelAtacante/100) 
      }
    }
  }
  
  object ArmaDeFuego extends Arma{
    def apply(duelo:Duelo) = ???
  }
}