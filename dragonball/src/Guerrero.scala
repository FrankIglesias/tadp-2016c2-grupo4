import scala.util.{ Try, Success, Failure }


abstract class EstadoGuerrero
case object Inconsciente extends EstadoGuerrero
case object Muerto extends EstadoGuerrero
case object Vivo extends EstadoGuerrero

case class Guerrero(
    estado: EstadoGuerrero = Vivo,
    listaDeMovimientos: List[Movimiento],
    listaDeItems: List[Items],
    ki: Int,
    kiMaximo: Int,
    especie: Especie) {

  
  
}

  

// def aplicarEfecto(???) : Guerrero = ???
 
