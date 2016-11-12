
trait Item
trait Arma extends Item




case class ArmaRoma()  extends Arma
case class ArmaFilosa()  extends Arma
case class ArmaDeFuego(var municiones: Integer) extends Arma
