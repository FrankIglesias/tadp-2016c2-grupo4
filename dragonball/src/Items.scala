
import Tipos._
import TodosLosMovimientos._

object ObjetoItem{
  class Item(var nombre:String){
    def apply(duelo:Duelo) = duelo.copy()
  }
  
  class Arma(nombre:String) extends Item(nombre) {
    object ArmaRoma  extends Arma(nombre) {
      override def apply(duelo:Duelo) = {generarDueloNuevo(atacante(duelo))(matcheaDefensorArmaRoma(defensor(duelo)))}
      def matcheaDefensorArmaRoma(defensor:Guerrero)={
        defensor.especie match {
          case Androide(_) => defensor.copy()
          case otro if defensor.ki < 300 => defensor.copy(estado = Inconsciente)
        }
      }
    }
    
    object ArmaFilosa  extends Arma(nombre){
      override def apply(duelo:Duelo) = {generarDueloNuevo(atacante(duelo))(analizaEfectoAlDefensor(atacante(duelo).ki,defensor(duelo)))}
      def analizaEfectoAlDefensor(kiDelAtacante:Int, defensor:Guerrero) : Guerrero = {
        defensor.especie match {
          case Saiyajin(true) => defensor.copy(ki = 1, especie = Saiyajin(false))
          case Mono(_) => defensor.copy(especie = Saiyajin(true),estado = Inconsciente, ki = defensor.ki - kiDelAtacante/100)
          case _ => defensor.copy(ki = defensor.ki - kiDelAtacante/100) 
        }
      }
    }
    
    object ArmaDeFuego extends Arma(nombre){
      override def apply(duelo:Duelo) = {
        if (atacante(duelo).tenesBalas(this)){
          atacante(duelo).disminuirMunicion(this,1)
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
  }
  
  class Municion(
      var nombreDelArma:String,
      var cantidad:Int) extends Item(nombreDelArma){
    
    def aumentarMunicion(cantidadAAumentar:Int) = {
       cantidad = cantidad + cantidadAAumentar
    }
    
    def disminuirMunicion(cantidadADisminuir:Int) = {
       cantidad = cantidad - cantidadADisminuir
    }
  }
}