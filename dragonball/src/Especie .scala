trait Especie

case object Humano extends Especie
case class Saiyajins(poseeCola :Boolean) extends Especie
case class Mono(poseeCola : Boolean) extends Especie
case class SuperSaiyajins(poseeCola : Boolean, nivel : Integer)  extends Especie  //extends Saiyajins ????
case class Androides (bateria : Integer) extends Especie
case object Namukesins extends Especie
case object Monstruos extends Especie