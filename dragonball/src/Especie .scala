import scala.util.{ Try, Success, Failure }


  trait Especie
  
  case object Humano extends Especie
  case class Saiyajin(poseeCola: Boolean) extends Especie
  case class Mono(cola: Boolean) extends Especie
  case class SuperSaiyajin(poseeCola: Boolean,nivel:Int) extends Especie
  case class Androide(bateria: Integer) extends Especie
  case object Namekusein extends Especie
  case object Monstruos extends Especie
  case class Fusionado(guerreroAnterior: Guerrero) extends Especie
