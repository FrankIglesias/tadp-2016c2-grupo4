
import Tipos._

object ObjetoItem{
  
 class Item{
   def apply(duelo:Duelo) = duelo.copy()
 }
  
 class Arma() extends Item{}
 
 case class ArmaRoma()  extends Arma {
   override def apply(duelo:Duelo) = {(atacante(duelo),matcheaDefensorArmaRoma(defensor(duelo)))}
   def matcheaDefensorArmaRoma(defensor:Guerrero)={
     defensor.especie match {
       case Androide(_) => defensor.copy()
       case otro if defensor.dameElPoder < 300 => defensor.quedateInconsiente()
     }
   }
 }
   
 case class ArmaFilosa()  extends Arma{
   override def apply(duelo:Duelo) = {(atacante(duelo),analizaEfectoAlDefensor(atacante(duelo).dameElPoder,defensor(duelo)))}
   def analizaEfectoAlDefensor(kiDelAtacante:Int, defensor:Guerrero) : Guerrero = {
     defensor.especie match {
       case Saiyajin(true) => defensor.copy(ki = 1, especie = Saiyajin(false))
       case Mono(_) => defensor.copy(especie = Saiyajin(true),estado = Inconsciente).disminuirElPoder(kiDelAtacante/100)
       case _ => defensor.disminuirElPoder(kiDelAtacante/100)
     }
   }
 }
  
 case class ArmaDeFuego() extends Arma{
   override def apply(duelo:Duelo) = {
     atacante(duelo).encontrarBalas(this).fold(duelo)(
         {municion => 
           (municion.asInstanceOf[Municion]
           .disminuir1Bala(atacante(duelo)),matcheaDefensorArmaDeFuego(defensor(duelo)))})
  }
   
   def matcheaDefensorArmaDeFuego(defensor:Guerrero) :Guerrero ={
     defensor.especie match {
       case Humano => defensor.disminuirElPoder(20)
       case Namekusein if defensor.estado.equals(Inconsciente) => defensor.disminuirElPoder(10)
       case _ => defensor
     }
   }
 }
  
 case object SemillaDeErmitanio extends Item{
    override def apply(duelo:Duelo) = {(aumentarPoderAlMango(atacante(duelo)),defensor(duelo))}
    def aumentarPoderAlMango(alguien:Guerrero) = {
      alguien.especie match{
        case Androide(b) => alguien //Supongo que no deberia tener efectos en androide por que no tienen una "maxima bateria"
        case _ => alguien.copy(estado = Vivo,ki = alguien.kiMaximo)
      }
    }
  }
  
 case class Municion(
      armaAsociada: ArmaDeFuego,
      cantidadDeBalas:Int) extends Item{
   
   def disminuir1Bala(guerrero:Guerrero) ={
     guerrero.copy(listaDeItems = guerrero.listaDeItems.map{item=> disminuirMunicion(item,this)})
   }
 }
 
   object FotodeLaLuna extends Item
   object EsferaDeDragon extends Item
}