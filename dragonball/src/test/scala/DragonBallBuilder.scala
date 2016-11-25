
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
  val fotoDeLaLuna = FotodeLaLuna
  
  val cartuchoDeRevolver = new Municion(revolver,6)
  
  val dejarseFajar = DejarseFajar
  val cargarki = CargarKi
  val usarEspadaSencilla = new UsarItem(espadaSencilla)
  val usarEspadaOxidada = new UsarItem(espadaOxidada)
  val usarRevolver = new UsarItem(revolver)
  val usarSemillaDeErmitanio = new UsarItem(semillaDeErmitanio)
  val comerseAlOponente = ComerseAlOponente
  val convertirseEnMono = new Convertirse(Mono(true))
  val convertirseEnSS = new Convertirse(SuperSaiyajin(true,200)) //No importa que nivel le ponga siempre va a aumentar 1 al actual nivel de SS
 
  val digerirComoCell: Digerir = {duelo:Duelo => 
    defensor(duelo).especie match{
      case Androide(_) => 
        atacante(duelo).copy(listaDeMovimientosConocidos =  atacante(duelo).listaDeMovimientosConocidos ::: defensor(duelo).listaDeMovimientosConocidos)
      case _ =>  atacante(duelo)
    }
  }
  
  val digerirComoMajinBu : Digerir = {duelo:Duelo =>
    atacante(duelo).copy(listaDeMovimientosConocidos = defensor(duelo).listaDeMovimientosConocidos)
  }
  
  val gokuSS = generarGuerrero(
      Vivo,
      List(dejarseFajar,cargarki,usarEspadaSencilla,convertirseEnMono,convertirseEnSS),
      List(fotoDeLaLuna),
      501,
      1000,
      SuperSaiyajin(true,4),
      0)
  
  
  val gokuConCola = generarGuerrero(
      Vivo,
      List(dejarseFajar,cargarki,usarEspadaSencilla,convertirseEnMono),
      List(fotoDeLaLuna),
      20,
      1000,
      Saiyajin(true),
      0)
  
  val goku = generarGuerrero(
      Vivo,
      List(dejarseFajar,cargarki,usarEspadaSencilla,convertirseEnMono),
      List(fotoDeLaLuna),
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
      
val cell =generarGuerrero(
    Vivo,
    List(comerseAlOponente),
    List(),
    200,
    25,
    Monstruo(digerirComoCell),
    0)
    
val majinBu =generarGuerrero(
    Vivo,
    List(comerseAlOponente),
    List(),
    12,
    25,
    Monstruo(digerirComoCell),
    0)
      
      
 
}