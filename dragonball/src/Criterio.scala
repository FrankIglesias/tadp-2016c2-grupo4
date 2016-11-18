
import Tipos._

object Criterios {
  
  case class Criterio()
  {
     def apply(listaMovimientos: List[Movimiento], duelo : Duelo) : Movimiento = ???
  }
  
    class movimientoMasDanino extends Criterio
    {
      
      def min(t1: (Int, Int), t2: (Int, Int)): (Int, Int) = if (t1._1 < t2._2) t1 else t2
      
      def apply(listaMovimientos: List[Movimiento], duelo : Duelo) : Movimiento = {
        val atacanteInicial = atacante(duelo)
        val defensorInicial = defensor(duelo)
        var i : Integer = 0
        
        var movMasEfectivo = listaMovimientos.map(mov => mov(duelo)).map(duelito => (defensor(duelito).ki/defensorInicial.ki, i + 1)).reduceLeft(min)
        
        return listaMovimientos(movMasEfectivo._2 - 1)
      }
    }
  
}

