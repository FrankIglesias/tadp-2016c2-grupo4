
import Tipos._

object Criterios {
  
  case class Criterio()
  {
     def apply(listaMovimientos: List[Movimiento], duelo : Duelo) : Movimiento = ???
  }
  
    class movimientoMasDanino extends Criterio
    {
      
      def max(t1: (Int, Movimiento), t2: (Int, Movimiento)): (Int, Movimiento) = if (t1._1 > t2._2) t1 else t2
      def aplicarDuelo(mov: Movimiento) : (Duelo, Movimiento) = 
      {
        return (mov(duelo), mov)
      }
      
      def sacarCoef(t1: (duelo :Duelo, mov: Movimiento)): (Int,  Movimiento) = 
      {
        return (defensor(duelo).ki/defensorInicial.ki, mov)
      }
      
      def apply(listaMovimientos: List[Movimiento], duelo : Duelo) : Movimiento = {
        val atacanteInicial = atacante(duelo)
        val defensorInicial = defensor(duelo)
        
        var movMasEfectivo = listaMovimientos.map(mov => aplicarDuelo(mov)).map((duelo, mov) => sacarCoef((duelo, mov))).reduceLeft(max)
        
        return listaMovimientos(movMasEfectivo._2)
      }
    }
  
}

