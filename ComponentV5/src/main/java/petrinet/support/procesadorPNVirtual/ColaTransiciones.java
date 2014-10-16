package petrinet.support.procesadorPNVirtual;

public class ColaTransiciones
{
  int[] valores;
  
  public ColaTransiciones(int cantidadTransiciones)
  {
    this.valores = new int[cantidadTransiciones];
    for (int i = 0; i < cantidadTransiciones; i++) {
      this.valores[i] = 0;
    }
  }
  
  public synchronized boolean desencolar(int transicion)
  {
    if(this.valores[transicion] > 0){
    	this.valores[transicion]--;
    	return true;
    }
    else{
    	return false;
    }
  }
  
  public synchronized void encolar(int transicion)
  {
      this.valores[transicion]++;
  }
}
