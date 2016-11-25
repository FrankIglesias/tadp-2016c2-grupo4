import Tipos._

trait ResultadoPelea{
  def map(f: Movimiento) : ResultadoPelea
}

case class Pelien(duelo:Duelo) extends ResultadoPelea{
  def map(f: Movimiento) : ResultadoPelea = {
    val resultado = atacante(duelo).pelearRound(f)(defensor(duelo))
    if(atacante(resultado).estado.eq(Muerto)){
      HayUnGanador(defensor(resultado))
    }
    if(defensor(resultado).estado.eq(Muerto)){
      HayUnGanador(atacante(resultado))
    }
    else{
      Pelien(atacante(duelo),defensor(duelo))
    }
  }
}

case class HayUnGanador(guerrero:Guerrero) extends ResultadoPelea{
  def map(f: Movimiento) : ResultadoPelea = this
}