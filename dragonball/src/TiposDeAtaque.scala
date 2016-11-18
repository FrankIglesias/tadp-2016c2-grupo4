
import Tipos._
import scala.math._

object TiposDeAtaque {
  
  class Ataque{ 
    def apply(duelo:Duelo) = duelo.copy() 
    }
  
  case class AtaqueFisico() extends Ataque()
  
  object MuchosGolpesNinja extends AtaqueFisico(){
    override def apply(duelo:Duelo) = atacante(duelo).especie match {
      case Humano if defensor(duelo).especie.eq(Androide) => {generarDueloNuevo(
          atacante(duelo).disminuirElPoder(10))(
          defensor(duelo))
      }
      case _ => definirQuienEsElDeMenorKiYFajarlo(duelo)
    }
    def definirQuienEsElDeMenorKiYFajarlo(duelo:Duelo) ={generarDueloNuevo(
        atacante(duelo).seLaBancaContra(defensor(duelo).dameElPoder))(
        defensor(duelo).seLaBancaContra(atacante(duelo).dameElPoder))}
  }
  
  object Explotar extends AtaqueFisico(){
    override def apply(duelo:Duelo) = atacante(duelo).especie match{
      case Monstruo(_) => explotaComoElMonstruoQueSos(duelo)
      case Androide(bateria) => explotaComoElRobotQueSos(duelo,bateria)
      case _ => duelo.copy()
    }
  }
  
  
  def explotaComoElMonstruoQueSos(duelo:Duelo) = {generarDueloNuevo(
    atacante(duelo).morite())(
    defensor(duelo).recibiExplosion(atacante(duelo).dameElPoder * 2))
    }
  
  def explotaComoElRobotQueSos(duelo:Duelo,bateria:Int) = {generarDueloNuevo(
    atacante(duelo).morite())(
    defensor(duelo).recibiExplosion(bateria * 3))
    }
  
  case class AtacarDeEnergia() extends Ataque()

  
  class Onda(cantidadDeKiNecesario:Int) extends AtacarDeEnergia(){
   override def apply(duelo:Duelo) ={
      defensor(duelo).especie match {
        case Monstruo(_) if tieneSuficientePoder(atacante(duelo)) => {generarDueloNuevo(
            atacante(duelo).disminuirElPoder(cantidadDeKiNecesario))(
            defensor(duelo).disminuirElPoder(cantidadDeKiNecesario/2))
            }
        case _ if tieneSuficientePoder(atacante(duelo)) => {generarDueloNuevo(
            atacante(duelo).disminuirElPoder(cantidadDeKiNecesario))(
            defensor(duelo).recibirDanioDeEnergia(2*cantidadDeKiNecesario))
            }
        case _ => duelo.copy()
      }
    }
    
    def tieneSuficientePoder(guerrero:Guerrero) = {
      guerrero.especie match {
      case Androide(b) => b >= cantidadDeKiNecesario 
      case _ =>  guerrero.dameElPoder >= cantidadDeKiNecesario
      }
    }
  }
  
  object Genkidama extends AtacarDeEnergia(){
    override def apply(duelo:Duelo) ={generarDueloNuevo(
            atacante(duelo))(
            defensor(duelo).recibirDanioDeEnergia(pow(10,(atacante(duelo).cantidadDeFajadas)).toInt))}
  }
}
