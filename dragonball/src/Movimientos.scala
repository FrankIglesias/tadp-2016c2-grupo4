
import Tipos._

trait Movimiento

case object DejarseFajar extends Movimiento {
  def apply(duelo: Duelo): Duelo = duelo.copy()
}

case object CargarKi extends Movimiento {
  def apply(ki:Int, duelo:Duelo) : Duelo =
    duelo match {
      case (atacante, defensor)=> duelo.copy(aumentarElKiSegunTipo(atacante),defensor.copy())
  }

  def aumentarElKiSegunTipo(guerrero:Guerrero) : Guerrero = {
    guerrero.especie match {
      case SuperSaiyajin(_,n) => guerrero.copy(ki = guerrero.ki + (150 * n)) 
      case Androide(_) => guerrero.copy()
      case _ => guerrero.copy(ki = guerrero.ki + 100)
    }
  }
}

case object UsarItem extends Movimiento {
  def apply(item: Item, duelo:Duelo) : Duelo = 
    item match {
      case _ :Arma => matcheaSegunElTipoDeArma(item,duelo)
      case _ :Item => aplicaItem(item,duelo)
    }
  
  
  def matcheaSegunElTipoDeArma(item:Item,duelo:Duelo) : Duelo = ???
  def aplicaItem(item:Item, duelo:Duelo) : Duelo = ???
}


case object Convertirse extends Movimiento {
  def apply(estado: Especie, duelo: Duelo) : Duelo = ???
}

case object Fusion extends Movimiento {
  def apply(otroGuerrero: Guerrero, duelo: Duelo)  : Duelo = ???
}
case object Magia extends Movimiento {
  def apply(magia: TipoDeMagia, duelo: Duelo) : Duelo = ???
}

case object Atacar extends Movimiento {
  def apply(ataque:Ataque, duelo:Duelo) = ???
}

case object ComerseAlOponente extends Movimiento{
  def apply(duelo:Duelo) = ???
}