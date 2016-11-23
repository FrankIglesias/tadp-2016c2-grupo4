
import Tipos._
import TodosLosMovimientos._
import ObjetoItem._

object DragonBallBuilder {
  def generarGuerrero(
    estado: EstadoGuerrero = Vivo,
    listaDeMovimientosConocidos: List[Movimiento],
    listaDeItems: List[Item],
    ki: Int,
    kiMaximo: Int,
    especie: Especie,
    cantidadDeFajadas: Int = 0)={Guerrero(estado,listaDeMovimientosConocidos,listaDeItems,ki,kiMaximo,especie,cantidadDeFajadas)}

  val dejarseFajar = DejarseFajar
  //val cargar100DeKi = CargarKi(100)
  val cargarki = CargarKi
  
  val goku = generarGuerrero(
      Vivo,
      List(dejarseFajar,cargarki),
      List(),
      20,
      1000,
      Saiyajin(false),
      0)
      
  val vegeta = generarGuerrero(
      Vivo,
      List(dejarseFajar,cargarki),
      List(),
      10,
      1000,
      Saiyajin(false),
      0)
 
}