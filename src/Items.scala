abstract class Items

abstract class Armas extends Items{
  def usarCon: (Guerrero => Guerrero)
}
object FotodeLaLuna extends Items
object EsferaDeDragon extends Items

case object ArmaRoma  extends {
  def usarCon(guerrero: Guerrero): Guerrero = {
    guerrero.especie match {
      case Androides(_) => guerrero.copy()
      case _ => if (guerrero.ki < 300) guerrero.copy(estado = Inconsciente) else guerrero.copy()
    }
  }
}
case object ArmaFilosa  extends {
  def usarCon(guerrero: Guerrero): Guerrero = {

    /*Las armas filosas reducen el ki del oponente 
en un punto por cada 100 del atacante. 
Además, si un Saiyajin con cola es atacado 
por un arma filosa, la pierde. Esto causa 
que su ki se reduzca automáticamente a 1 
y, si estaba convertido en Mono Gigante, 
vuelva a su forma normal y quede inconsciente.
*/
    //val guerreroReducido = guerrero.copy(estado, ki, especie)
    guerrero
  }
}

case class ArmaDeFuego(var municiones: Integer) extends Items{
  def usarCon(guerrero: Guerrero): Guerrero = {
    if (municiones > 0) {
      municiones = municiones - 1
      guerrero.especie match {
        case Namukesins => guerrero.copy(ki = guerrero.ki - 10)
        case Humano => guerrero.copy(ki = guerrero.ki - 20)
        case _ => guerrero.copy()
      }
   }
         guerrero.copy()
  }
}

case object SemillaDeHermitanio extends Items {
  def usarCon(guerrero: Guerrero): Guerrero = {
  guerrero.copy(ki = guerrero.kiMaximo)
  }
}