
import Tipos._
import TodosLosMovimientos._


object ObjetoItem{
  
 class Item{
   def apply(duelo:Duelo) = duelo.copy()
 }
  
 class Arma extends Item{}
 
 class ArmaRoma  extends Arma {
   override def apply(duelo:Duelo) = {generarDueloNuevo(atacante(duelo))(matcheaDefensorArmaRoma(defensor(duelo)))}
   def matcheaDefensorArmaRoma(defensor:Guerrero)={
     defensor.especie match {
       case Androide(_) => defensor.copy()
       case otro if defensor.ki < 300 => defensor.copy(estado = Inconsciente)
     }
   }
 }
   
 class ArmaFilosa  extends Arma{
   override def apply(duelo:Duelo) = {generarDueloNuevo(atacante(duelo))(analizaEfectoAlDefensor(atacante(duelo).ki,defensor(duelo)))}
   def analizaEfectoAlDefensor(kiDelAtacante:Int, defensor:Guerrero) : Guerrero = {
     defensor.especie match {
       case Saiyajin(true) => defensor.copy(ki = 1, especie = Saiyajin(false))
       case Mono(_) => defensor.copy(especie = Saiyajin(true),estado = Inconsciente, ki = defensor.ki - kiDelAtacante/100)
       case _ => defensor.copy(ki = defensor.ki - kiDelAtacante/100) 
     }
   }
 }
  
 class ArmaDeFuego extends Arma{
   override def apply(duelo:Duelo) = {
     if (atacante(duelo).tenesBalas(this)){
       generarDueloNuevo(atacante(duelo))(matcheaDefensorArmaDeFuego(defensor(duelo)))
     }
     else{
       super.apply(duelo)
     }
   }
   def matcheaDefensorArmaDeFuego(defensor:Guerrero) :Guerrero ={
     defensor.especie match {
       case Humano => defensor.copy(ki = defensor.ki - 20)
       case Namekusein if defensor.estado.equals(Inconsciente) => defensor.copy(ki = defensor.ki - 10)
     }
   }
 }
  
 class SemillaDeErmitaño extends Item{
    override def apply(duelo:Duelo) = {generarDueloNuevo(aumentarKiAlMango(atacante(duelo)))(defensor(duelo))}
    def aumentarKiAlMango(alguien:Guerrero) = alguien.copy(ki = alguien.kiMaximo)
  }
  
 class Municion(
      var armaAsociada: ArmaDeFuego,
      var cantidadDeBalas:Int) extends Item{
    
    def aumentarMunicion(cantidadAAumentar:Int) = {cantidadDeBalas = cantidadDeBalas + cantidadAAumentar}
    
    def disminuirMunicion(cantidadADisminuir:Int) = {cantidadDeBalas = cantidadDeBalas - cantidadADisminuir
    }
  }
}