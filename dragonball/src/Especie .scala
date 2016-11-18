import scala.util.{ Try, Success, Failure }
import Tipos._

  object Especie {
  
  case object Humano extends Especie
  case class Saiyajin(poseeCola: Boolean) extends Especie
  case class Mono(cola: Boolean) extends Especie
  case class SuperSaiyajin(poseeCola: Boolean,nivel:Int) extends Especie
  case class Androide(bateria: Integer) extends Especie
  case object Namekusein extends Especie
  case class Monstruo(maneraDeDigerir : Digerir) extends Especie
  case class Fusionado(guerreroAnterior: Guerrero) extends Especie

  }