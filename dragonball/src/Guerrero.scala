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
  
private def cargar(delta: Int) = copy(ki = ki + delta min kiMaximo)
 
 
def cargameElKi() = 
   this.especie match{
     case SuperSaiyajins(_, nivel) => cargar(150 * nivel)
     case Androides(_) => cargar(0)
     case _ => cargar(100)
}
  

// def aplicarEfecto(???) : Guerrero = ???
 
def realizarMovimiento(movimiento: Movimiento , oponente: Try[Guerrero]) : Guerrero = 
   movimiento match{
  
     case movimiento: DejarseFajar => this.copy()
     case movimiento: CargarKi => this.cargameElKi()
     case movimiento: UsarItem => this.aplicarEfecto(movimiento.item)
     case movimiento: ComerseAlOponente => this.cometeAlOtro(movimiento.oponente)
     case movimiento: UsarAtaque => this.aplicarEfecto(movimiento.ataque)
     case movimiento: Convertirse => this.analizaConversion(movimiento.estado)
     case movimiento: Fusion => this.fusionate(movimiento.otroGuerrero)
     case movimiento: Magia => this.aplicarEfecto(movimiento.magia)
     
   }
 }