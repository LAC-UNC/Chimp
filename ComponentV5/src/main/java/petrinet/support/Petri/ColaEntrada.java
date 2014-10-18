 package petrinet.support.Petri;
 
 public class ColaEntrada
 {
   private int[][] contadores;
   
   public ColaEntrada(int _transiciones)
   {
     this.contadores = new int[_transiciones][1];
   }
   
   public void disparo(int _transicion)
   {
     if (_transicion <= this.contadores.length) {
       this.contadores[_transicion][0] += 1;
     }
   }
   
   public void restarDisparo(int[][] _disparo_final)
   {
     for (int i = 0; i < _disparo_final.length; i++) {
       if ((_disparo_final[i][0] == 1) && (this.contadores[i][0] > 0)) {
         this.contadores[i][0] -= 1;
       }
     }
   }
   
   public int[][] getEstadoCola()
   {
     int[][] retorno = new int[this.contadores.length][1];
     for (int i = 0; i < this.contadores.length; i++) {
       if (this.contadores[i][0] > 0) {
         retorno[i][0] = 1;
       }
     }
     return retorno;
   }
 }

