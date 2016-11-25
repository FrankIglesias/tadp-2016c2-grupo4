
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

  val espadaSencilla = new ArmaFilosa()
  val espadaOxidada = new ArmaRoma()
  val revolver = new ArmaDeFuego()
  val semillaDeErmitanio = SemillaDeErmitanio
  
  val cartuchoDeRevolver = new Municion(revolver,6)
  
  val dejarseFajar = DejarseFajar
  val cargarki = CargarKi
  val usarEspadaSencilla = new UsarItem(espadaSencilla)
  val usarEspadaOxidada = new UsarItem(espadaOxidada)
  val usarRevolver = new UsarItem(revolver)
  val usarSemillaDeErmitanio = new UsarItem(semillaDeErmitanio)
 
  
  val goku = generarGuerrero(
      Vivo,
      List(dejarseFajar,cargarki,usarEspadaSencilla),
      List(),
      20,
      1000,
      Saiyajin(false),
      0)
      
  val gokuMalvado = generarGuerrero(
      Vivo,
      List(dejarseFajar,cargarki,usarEspadaSencilla),
      List(espadaSencilla),
      200,
      1000,
      Saiyajin(false),
      0)
      
  val vegeta = generarGuerrero(
      Vivo,
      List(dejarseFajar,cargarki,usarEspadaOxidada),
      List(espadaOxidada),
      10,
      1000,
      Saiyajin(false),
      0)

val terminator = generarGuerrero(
      Vivo,
      List(usarRevolver),
      List(revolver,cartuchoDeRevolver),
      10,
      1000,
      Androide(60),
      0)
val krillin = generarGuerrero(
      Inconsciente,
      List(usarSemillaDeErmitanio),
      List(semillaDeErmitanio),
      10,
      20,
      Humano,
      0)
      
      
 
}