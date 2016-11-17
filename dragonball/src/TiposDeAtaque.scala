
import Tipos._

object TiposDeAtaque {
  
  class Ataque{ 
    def apply(duelo:Duelo) = duelo.copy() 
    }
  
  case class AtaqueFisico() extends Ataque()
  
  object MuchosGolpesNinja extends AtaqueFisico(){
    override def apply(duelo:Duelo) = atacante(duelo).especie match {
      case Humano if defensor(duelo).especie.eq(Androide) => {generarDueloNuevo(
          atacante(duelo).disminuirElKi(10))(
          defensor(duelo))
      }
      case _ => definirQuienEsElDeMenorKiYFajarlo(duelo)
    }
    def definirQuienEsElDeMenorKiYFajarlo(duelo:Duelo) ={generarDueloNuevo(
        atacante(duelo).seLaBancaContra(defensor(duelo).ki))(
        defensor(duelo).seLaBancaContra(atacante(duelo).ki))}
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
    defensor(duelo).recibiExplosion(atacante(duelo).ki * 2))
    }
  
  def explotaComoElRobotQueSos(duelo:Duelo,bateria:Int) = {generarDueloNuevo(
    atacante(duelo).morite())(
    defensor(duelo).recibiExplosion(bateria * 3))
    }
  
  case class AtacarDeEnergia() extends Ataque() {
   def apply(duelo:Duelo) = {
     defensor(duelo).especie match {
       case Androide(_) => cambiarKiYGenerarDuelo(negarSegundoParametro(ataque(duelo)))
       case _ => cambiarKiYGenerarDuelo(ataque(duelo))
     } 
   }

   
   def cambiarKiYGenerarDuelo(delta : (Integer, Integer)) {
     generarDueloNuevo(atacante(duelo).copy(ki = atacante(duelo).ki - delta._1 min atacante(duelo).kiMaximo))(defensor(duelo).copy(ki = defensor(duelo).ki + delta._2 min defensor(duelo).kiMaximo))
   }
  }
  
  class Onda(cantidadDeKiNecesario:Int) extends AtacarDeEnergia()
  object Genkidama extends AtacarDeEnergia()

}
