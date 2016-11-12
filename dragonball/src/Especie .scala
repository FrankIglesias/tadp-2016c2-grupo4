import scala.util.{ Try, Success, Failure }


  trait Especie
  
  case object Humano extends Especie
  case class Saiyajins(poseeCola: Boolean) extends Especie
  case class Mono(cola: Boolean) extends Especie
  case class SuperSaiyajin(poseeCola: Boolean, nivel: Integer) extends Especie
  case class Androide(bateria: Integer) extends Especie
  case object Namukesins extends Especie
  case class Monstruos(comemeToda: ((Guerrero, Guerrero) => Try[Guerrero])) extends Especie {
    val funcion = comemeToda
  }
  case class Fusionado(guerreroAnterior: Guerrero) extends Especie
