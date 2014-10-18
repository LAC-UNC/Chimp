package petrinet.support.procesador;

public abstract interface ProcesadorPetri
{
  public abstract boolean consultarDisparoTransicion(int paramInt);
  
  public abstract void encolar(int paramInt1, int paramInt2);
}
