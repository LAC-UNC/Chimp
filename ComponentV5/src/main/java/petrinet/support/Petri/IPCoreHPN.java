package petrinet.support.Petri;

import java.util.ArrayList;
import java.util.List;

import petrinet.support.procesadorPNVirtual.ColaTransiciones;

public class IPCoreHPN
implements Runnable
{
	private List<PetriNet> redes = null;
	private int[][] matriz_prioridades = null;
	private ColaTransiciones colaSalida;

	public IPCoreHPN(int[][] _prioridades_distribuidas, int totalTransiciones)
	{
		this.matriz_prioridades = _prioridades_distribuidas;
		this.redes = new ArrayList<PetriNet>();
		this.colaSalida = new ColaTransiciones(totalTransiciones);
	}

	public void addPetriNet(PetriNet _petriNet)
	{
		this.redes.add(_petriNet);
	}

	private int[][] generarDisparoDistribuido(int[][] _sensibilizados_distribuidos)
	{
		int[][] temp = PetriNet.producto(this.matriz_prioridades, _sensibilizados_distribuidos);


		int[][] seleccion = new int[_sensibilizados_distribuidos.length][1];
		for (int i = 0; i < temp.length; i++) {
			if (temp[i][0] == 1)
			{
				seleccion[i][0] = 1;
				break;
			}
		}
		return PetriNet.producto(PetriNet.transpuesta(this.matriz_prioridades), seleccion);
	}

	private synchronized void simularOneStep()
	{
		int[][] sensibilizados_distribuidos = new int[1][1];
		int[][] temp = new int[1][1];
		for (int i = 0; i < this.redes.size(); i++)
		{
			temp = ((PetriNet)this.redes.get(i)).getSensibilizadosBordeCorrelacionados();
			if (i == 0) {
				sensibilizados_distribuidos = (int[][])temp.clone();
			} else {
				for (int j = 0; j < sensibilizados_distribuidos.length; j++) {
					sensibilizados_distribuidos[j][0] &= temp[j][0];
				}
			}
		}

		//TODO: agregar condicion de inhibidores a los sensibilizados Aqui  (sensibilizados_distribuidos)! 
		int[][] disparo_distribuido = generarDisparoDistribuido(sensibilizados_distribuidos);
		for (int i = 0; i < this.redes.size(); i++) {
			((PetriNet)this.redes.get(i)).setDisparoDistribuido(disparo_distribuido);
		}
		boolean cambioEstado = false;
		for (int i = 0; i < this.redes.size(); i++)
		{
			int retorno = ((PetriNet)this.redes.get(i)).disparar();
			if (retorno >= 0)
			{
				this.colaSalida.encolar(retorno);
				cambioEstado = true;
			}
		}
		if (!cambioEstado) {
			try
			{
				wait();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public int[][] getPrioridadesDistribuidas()
	{
		return this.matriz_prioridades;
	}

	public void setPrioridadesDistribuidas(int[][] _prioridades_distribuidas)
	{
		this.matriz_prioridades = _prioridades_distribuidas;
	}

	public synchronized void encolarDisparo(int _red, int _transicion)
	{
		((PetriNet)this.redes.get(_red)).getColaEntrada().disparo(_transicion);
		((PetriNet)this.redes.get(_red)).actualizar();
		notifyAll();
	}

	public List<PetriNet> getListaRedes()
	{
		return this.redes;
	}

	public void run()
	{
		for (;;)
		{
			simularOneStep();
		}
	}

	public ColaTransiciones getColaSalida()
	{
		return this.colaSalida;
	}
}

