package com.lac.petrinet.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.lac.petrinet.configuration.data.Etiqueta;
import com.lac.petrinet.configuration.data.Intervalo;
import com.lac.petrinet.configuration.data.MatrizIncidenciaMarcadoInicial;
import com.lac.petrinet.configuration.handler.ManejadorEtiquetas;
import com.lac.petrinet.configuration.handler.ManejadorIntervalos;
import com.lac.petrinet.configuration.handler.ManejadorXMLMatrizYMarcado;
import com.lac.petrinet.configuration.handler.ProcesaXML;


/**
 * Clase abstracta para el manejo de redes de petri.
 * @author María Florencia Caro & Ignacio Furey
 */

public class PNData {
	/**
	 * Vectores de invariantes de plaza de la red de petri.
	 */
	protected ArrayList<ArrayList<String>> invariantesPlaza;

	/**
	 * Vectores de invariantes de transicion de la red de petri.
	 */
	protected ArrayList<ArrayList<String>> invariantesTransicion;
	/**
	 * Intancia con los datos de la matriz de incidencia y el marcado inicial.
	 */
	protected MatrizIncidenciaMarcadoInicial matrizIncidenciaMarcadoInicial;
	/**
	 * Un manejador de etiquetas.
	 */
	private ManejadorEtiquetas manejadorEtiquetas;
	/**
	 * Un manejador de intervalos.
	 */
	private ManejadorIntervalos manejadorIntervalos;
	/**
	 * Getter.
	 * @return invariantesPlaza
	 */
	public final ArrayList<ArrayList<String>> getInvariantesPlaza() {
		return this.invariantesPlaza;
	}
	/**
	 * Getter.
	 * @return the invariantesTransicion
	 */
	public final ArrayList<ArrayList<String>> getInvariantesTransicion() {
		return this.invariantesTransicion;
	}
	/**
	 * Getter.
	 * @return etiquetas
	 */
	public final ArrayList<Etiqueta> getEtiquetas() {
		return this.manejadorEtiquetas.getEtiquetas();
	}
	/**
	 * Getter.
	 * @return intervalos
	 */
	public final ArrayList<Intervalo> getIntervalos() {
		return this.manejadorIntervalos.getIntervalos();
	}
	/**
	 * Llama a los metodos
	 * actualizarDatosRedPetri y generarMatrizIncidenciaYMarcado.
	 * @param pathARedPetri Path de la Red de Petri a cargar.
	 * @param pathAXMLProcesos Path al archivo de procesos.
	 */
	public final void cargarRed(final String filePNML) {
		this.generarMatrizIncidenciaYMarcado(filePNML);

		this.manejadorEtiquetas = new ManejadorEtiquetas(filePNML);
		this.manejadorIntervalos = new ManejadorIntervalos(filePNML);
	}
	//INTERACCION CON MANEJADOR MATRIZ Y MARCADO
	/**
	 * Genera el objeto matrizIncidenciaMarcadoInicial.
	 * @param path Path de la Red de Petri a editar
	 */
	private void generarMatrizIncidenciaYMarcado(final String path) {
		final ManejadorXMLMatrizYMarcado manejador =
			new ManejadorXMLMatrizYMarcado();
		new ProcesaXML(manejador, path);
		this.matrizIncidenciaMarcadoInicial = manejador.getMatriz();
	}
	/**
	 * Getter.
	 * @return Matriz de Incidencia Positiva
	 */
	public int[][] getMatrizIncidenciaPositiva() {
		return this.matrizIncidenciaMarcadoInicial.getMatrizPositiva();
	}
	/**
	 * Getter.
	 * @return Matriz de Incidencia Negativa
	 */
	public int[][] getMatrizIncidenciaNegativa() {
		return this.matrizIncidenciaMarcadoInicial.getMatrizNegativa();
	}
	/**
	 * Getter.
	 * @return Marcado Inicial
	 */
	public final int[] getMarcadoInicial() {
		return this.matrizIncidenciaMarcadoInicial.getMarcado();
	}
	/**
	 * Getter.
	 * @return Marcado Transiciones
	 */
	public final HashMap<String, Integer[]> getPlazas() {
		return this.matrizIncidenciaMarcadoInicial.getPlazas();
	}
	/**
	 * Getter.
	 * @return Marcado Transiciones
	 */
	public final HashMap<String, Integer> getTransiciones() {
		return this.matrizIncidenciaMarcadoInicial.getTransiciones();
	}
	/**
	 * Getter.
	 * @return String con la descripcion de las matrices de incidencia y el
	 * marcado inicial de la red.
	 */
	public final String matrizIncidenciaMarcadoInicialComoString() {
		return this.matrizIncidenciaMarcadoInicial.matrizYMarcadoComoString();
	}
	/**
	 * Getter.
	 * @return Vector de disparos automaticos
	 */
	public final int[] getVectorDisparosAtomaticos() {
		//Se genera vector con tamaño igual a cantidad de transiciones.
		final int[] disparosAutomaticos = 
				new int[this.manejadorEtiquetas.getEtiquetas().size()];
		//Se genera un iterator con el array list de etiquetas.
		final Iterator<Etiqueta> etiquetasIt = 
				this.manejadorEtiquetas.getEtiquetas().iterator();
		System.out.println("Orden de transiciones");
		//Mientras haya otra etiqueta ==> hay otra transicion.
		while (etiquetasIt.hasNext()) {
			//salvo el valor de la proxima etiqueta.
			final Etiqueta actual = etiquetasIt.next();
			//Obtengo el valor con el idTransicion como key. Ese valor sera
			//el indice del vector
			final int indice = this.matrizIncidenciaMarcadoInicial.
					getTransiciones().get(actual.getIdTransicion()).intValue();
			/*System.out.println("id: " + actual.getIdTransicion()
					+ " colum: " + indice);*/
			//guardo el valor que represente la etiqueta de 
			//Disparo = 0/Automatico = 1 en la posicion del vector indicada
			//por indice.
			if (actual.getPrimerValor().equals("A")) {
				disparosAutomaticos[indice] = 1;
			} else {
				disparosAutomaticos[indice] = 0;
			}
		}
		return disparosAutomaticos;
	}
	/**
	 * Getter.
	 * @return Vector de disparos automatios.
	 */
	public final int[] getVectorDisparosSinInforme() {
		//Se genera vector con tamaño igual a cantidad de transiciones.
		final int[] disparosSinInforme = 
				new int[this.manejadorEtiquetas.getEtiquetas().size()];
		//Se genera un iterator con el array list de etiquetas.
		final Iterator<Etiqueta> etiquetasIt = 
				this.manejadorEtiquetas.getEtiquetas().iterator();
		//Mientras haya otra etiqueta ==> hay otra transicion.
		while (etiquetasIt.hasNext()) {
			//salvo el valor de la proxima etiqueta.
			final Etiqueta actual = etiquetasIt.next();
			//Obtengo el valor con el idTransicion como key. Ese valor sera
			//el indice del vector
			final int indice = this.matrizIncidenciaMarcadoInicial.
					getTransiciones().get(actual.getIdTransicion()).intValue();
			//guardo el valor que represente la etiqueta de 
			//Disparo = 0/Automatico = 1 en la posicion del vector indicada
			//por indice.
			if (actual.getSegundoValor().equals("N")) {
				disparosSinInforme[indice] = 1;
			} else {
				disparosSinInforme[indice] = 0;
			}
		}
		return disparosSinInforme;
	}
}
