import scala.util.{ Try, Success, Failure }
import Tipos._
import ObjetoItem._

abstract class EstadoGuerrero
case object Inconsciente extends EstadoGuerrero
case object Muerto extends EstadoGuerrero
case object Vivo extends EstadoGuerrero

case class Guerrero(
    estado: EstadoGuerrero = Vivo,
    listaDeMovimientos: List[Movimiento],
    listaDeItems: List[Item],
    ki: Int,
    kiMaximo: Int,
    especie: Especie) {  
  
  def tenesBalas(arma:Arma) = listaDeItems
  .find{item => item.asInstanceOf[Municion].nombreDelArma.eq(arma.nombre)}
  .map{municion => municion.asInstanceOf[Municion].disminuirMunicion(1)}
  .isEmpty
}
