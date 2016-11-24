
import Tipos._
import TodosLosMovimientos._

object ObjetoItem{
  
 class Item{
   def apply(duelo:Duelo) = duelo.copy()
 }
  
 class Arma() extends Item{}
 
 case class ArmaRoma()  extends Arma {
   override def apply(duelo:Duelo) = {generarDueloNuevo(atacante(duelo))(matcheaDefensorArmaRoma(defensor(duelo)))}
   def matcheaDefensorArmaRoma(defensor:Guerrero)={
     defensor.especie match {
       case Androide(_) => defensor.copy()
       case otro if defensor.dameElPoder < 300 => defensor.quedateInconsiente()
     }
   }
 }
   
 case class ArmaFilosa()  extends Arma{
   override def apply(duelo:Duelo) = {generarDueloNuevo(atacante(duelo))(analizaEfectoAlDefensor(atacante(duelo).dameElPoder,defensor(duelo)))}
   def analizaEfectoAlDefensor(kiDelAtacante:Int, defensor:Guerrero) : Guerrero = {
     defensor.especie match {
       case Saiyajin(true) => defensor.copy(ki = 1, especie = Saiyajin(false))
       case Mono(_) => defensor.copy(especie = Saiyajin(true),estado = Inconsciente, ki = defensor.dameElPoder - kiDelAtacante/100)
       case _ => defensor.copy(ki = defensor.dameElPoder - kiDelAtacante/100) 
     }
   }
 }
  
 case class ArmaDeFuego() extends Arma{
   override def apply(duelo:Duelo) = {
     atacante(duelo).encontrarBalas(this) match{
       case Some(municion) => generarDueloNuevo(atacante(duelo).disminuirBalas(municion.asInstanceOf[Municion]))(matcheaDefensorArmaDeFuego(defensor(duelo)))
       case None => duelo.copy()
   }
  }
   
   def matcheaDefensorArmaDeFuego(defensor:Guerrero) :Guerrero ={
     defensor.especie match {
       case Humano => defensor.copy(ki = defensor.dameElPoder - 20)
       case Namekusein if defensor.estado.equals(Inconsciente) => defensor.copy(ki = defensor.dameElPoder - 10)
       case _ => defensor.copy()
     }
   }
 }
  
 case class SemillaDeErmitanio() extends Item{
    override def apply(duelo:Duelo) = {generarDueloNuevo(aumentarPoderAlMango(atacante(duelo)))(defensor(duelo))}
    def aumentarPoderAlMango(alguien:Guerrero) = alguien.aumentarElPoder(alguien.kiMaximo)
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