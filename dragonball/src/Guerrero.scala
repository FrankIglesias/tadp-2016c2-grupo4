abstract class EstadoGuerrero
case object Inconsciente extends EstadoGuerrero
case object Muerto extends EstadoGuerrero
case object Vivo extends EstadoGuerrero

case class Guerrero(
    val estado: EstadoGuerrero,
    //val listaDeMovimientos: List[(Unit =>Guerrero)], la lista puede variar segun el objeto instanciado??
    val ki: Int,
    val kiMaximo: Int,
    val especie: Especie) {

  def dejarseFajar(): Guerrero = this.copy()
  def cargarKi(): Guerrero = {
    especie match {
      case SuperSaiyajins(_, _) => copy(ki = 150 /*anterior.ki*anterior.especie.nivel*/ )
      case Androides(_) => this.copy()
      case _ => this.copy(ki = 100)
    }
  }
  def usarItem(item: Items, guerro: Guerrero): Guerrero = {
    usarItem
  }
}
