import scala.util.{Try,Success,Failure}
object ModosDeDigestion{
type ModoDigestion =  (Guerrero,  Guerrero) => Guerrero
}

abstract class Especie 
case object Humano extends Especie
case class Saiyajins(poseeCola :Boolean) extends Especie
case class Mono(cola : Boolean) extends Especie
case class SuperSaiyajins(poseeCola : Boolean, nivel : Integer)  extends Especie 
case class Androides (bateria : Integer)  extends Especie
case object Namukesins extends Especie
case class Monstruos(comemeToda  :((Guerrero, Guerrero)=> Try[Guerrero])) extends Especie{
  val funcion = comemeToda
}
case class Fusionado (guerreroAnterior : Guerrero) extends Especie