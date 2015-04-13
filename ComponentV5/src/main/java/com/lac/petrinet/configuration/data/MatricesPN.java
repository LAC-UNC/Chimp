package com.lac.petrinet.configuration.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.lac.petrinet.configuration.data.ElementoArco.TipoArco;

/**
 * En esta clase estan contenidos todos los datos necesarios para obtener la
 * matriz de incidencia de una red y la matriz de inhibidores.
 * Ademas, esta clase es capaz de armar las matrices a partir de Elementos de 
 * tipo Plaza, Transicion y Arco que se van"agregando" con sus caracteristicas.
 * @author Maria Florencia Caro & Ignacio Furey
 */

public class MatricesPN {
	/**
	 * Matriz de incidencia positiva. Pesos de arcos
	 * de Transiciones a Plazas.
	 */
	private int[][] positiva;
	/**
	 * Matriz de incidencia negativa. Pesos de arcos
	 * de Plazas a Transiciones.
	 */
	private int[][] negativa;


	private int[][] inhibidores;
	
	private int[][] lectores;

	/**
	 * Marcado inicial de las plazas.
	 */
	private int[] marcadoInicial;
	/**
	 * Plazas, String=key/idPlaza, Integer[0]=posicion fila en matriz,
	 * Integer[1]=marcado de plaza.
	 */
	private HashMap<String, Plaza> plazas;
	/**
	 * Transiciones, String=key/idTransicion, Integer=posicion columna
	 * en matriz.
	 */
	private HashMap<String, Integer> transiciones;
	/**
	 * Arcos, String=key/idArco, String[0]= idSource, String[1]= idTarget,
	 * String[2]=peso del arco.
	 */
	private HashMap<String, ElementoArco> arcos;


	/**
	 * Contador para los numeros de transiciones agregados.
	 */
	private int numTransicion;
	/**
	 * Contador para los numeros de plazas agregados.
	 */
	private int numPlaza;
	/**
	 * Class contructor.
	 */
	public MatricesPN() {
		this.plazas = new HashMap<String, Plaza>();
		this.transiciones = new HashMap<String, Integer>();
		this.arcos = new HashMap<String, ElementoArco>();
		this.numTransicion = 0;
		this.numPlaza = 0;
	}
	/**
	 * Arma un String con los valores de las matrices y el marcado inicial
	 * de manera legible por personas.
	 * @return La matriz y el marcado en formato String.
	 */
	public final String matrizYMarcadoComoString() {
		String matrizYMarcado = new String("");
		//guarda matriz positiva
		matrizYMarcado = matrizYMarcado.concat("Matriz positiva:\n");
		//plazas - filas
		for (int f = 0; f < this.plazas.size(); f = f + 1) {
			String fila = new String("");
			//transiciones - columnas
			for (int c = 0; c < this.transiciones.size(); c = c + 1) {
				fila = fila + Integer.toString(this.positiva[f][c]) + "\t";
			}
			matrizYMarcado = matrizYMarcado.concat(fila + "\n");
		}
		//guarda matriz negativa
		matrizYMarcado = matrizYMarcado.concat("\nMatriz negativa:\n");
		//plazas - filas
		for (int f = 0; f < this.plazas.size(); f = f + 1) {
			String fila = new String("");
			//trnasiciones - columnas
			for (int c = 0; c < this.transiciones.size(); c = c + 1) {
				fila = fila + Integer.toString(this.negativa[f][c]) + "\t";
			}
			matrizYMarcado = matrizYMarcado.concat(fila + "\n");
		}
		//guarda marcado inicial
		matrizYMarcado = matrizYMarcado.concat("\nMarcado inicial:\n");
		for (int i = 0; i < this.plazas.size(); i = i + 1) {
			matrizYMarcado = matrizYMarcado.concat(this.marcadoInicial[i] + "\n");
		}
		return matrizYMarcado;
	}
	/**
	 * Agrega un id de transicion y le asigna un numero de columna de la matriz.
	 * @param idTransicion el id de transicion a agregar.
	 */
	public void agregarTransicion(final String idTransicion) {
		//Se agrega en el Map de transiciones una nueva Key=idTransicion
		//asociada a un Value= numero de transicion/columna de matriz.
		this.transiciones.put(idTransicion, this.numTransicion);
		//incrementa el valor del contador de transiciones
		this.numTransicion = this.numTransicion + 1;
	}
	/**
	 * Agrega un id de plaza con su marcado inicial y le asigna un numero de
	 * columna de la matriz.
	 * @param idPlaza el id de la plaza a agregar
	 * @param marcadoInicialNuevo marcado inicial de la plaza
	 */
	public void agregarPlaza(final String idPlaza, final int marcadoInicialNuevo) {
		//Se agrega en el Map de plazas una nueva Key=idPlaza asociada a
		//un Value= {numero de Plaza/fila de matriz, marcado inicial}.
		this.plazas.put(idPlaza, new Plaza( this.numPlaza,marcadoInicialNuevo));
		//incrementa el valor del contador de plazas
		this.numPlaza = this.numPlaza + 1;
	}
	/**
	 * Agrega un id de arco con los source, target y peso correspondientes y
	 * le asigna un numero.
	 * de columna de la matriz.
	 * @param idArco el id del arco a agregar
	 * @param idSource elemento source del arco
	 * @param idTarget elemento target del arco
	 * @param valorPeso peso del arco
	 */
	public void agregarArco(ElementoArco arco) {
		//Se agrega en el Map de arcos una nueva Key=idArco asociada a un
		//Value= {idSource, idTarget, valor peso del arco}.
		this.arcos.put(arco.id,  arco.clone());
	}
	/**
	 * Metodo que completa la matriz de incidencia positiva y negativa y el
	 * marcado inicial de una red segun los componentes que se cargaron hasta
	 * el momento. Este metodo debe ser llamado una vez se termine de cargar
	 * todos los compenentes de la red de petri de la que se desea obtener la
	 * matriz.
	 * @param idsMap 
	 */
	public void crearMatrizPositivaNegativa(Map<String, String> idsMap) {
		replaceBrokenArcIds(idsMap);
		this.positiva = new int[this.plazas.size()][this.transiciones.size()];
		this.negativa = new int[this.plazas.size()][this.transiciones.size()];
		this.marcadoInicial = new int [this.plazas.size()];
		//Pone a cero todos los valores de los arreglos
		for (int f = 0; f < this.plazas.size(); f = f + 1) {
			for (int c = 0; c < this.transiciones.size(); c = c + 1) {
				this.positiva[f][c] = 0;
				this.negativa[f][c] = 0;
			}
			this.marcadoInicial[f] = 0;
		}
		//En primer lugar, completamos el marcado inicial

		for(Entry<String, Plaza> plazaEntry : this.plazas.entrySet()){
			//Guarda el marcado en el vector marcadoInicial, en la posicion
			//indicada por numero de Plaza/fila en matriz.
			this.marcadoInicial[ plazaEntry.getValue().getPosicionFilaMatriz()] =
					plazaEntry.getValue().getMarcadoPlaza();
		}

		//Luego se completan la matriz de incidencia positiva y negativa
		for(Entry<String, ElementoArco> arcosValue : this.arcos.entrySet()){
			// si es un brazo inhibidor no debo incluirlo en estas matrices, ya que tendra su propia matriz.
			if(!(arcosValue.getValue().getTipo() == TipoArco.REGULAR) )
				continue;
			//Se determina si el peso del arco debe ir 
			//en la matriz positiva en caso que el idSource coincida con una transicion
			//o en la matriz negativa, si el idSource coincide con una plaza.

			int peso = arcosValue.getValue().getValorElemento().intValue();
			//Si existe en transiciones una key con
			//el valor de idSource, implica que el peso
			//debe ir en la matriz positiva.
			if (this.transiciones.containsKey(arcosValue.getValue().getSource())) {
				int posicionTarget = this.plazas.get(arcosValue.getValue().getTarget()).getPosicionFilaMatriz();
				int posicionSource = this.transiciones.get(arcosValue.getValue().getSource());
				//FILA = numero de Plaza de la plaza indicada por el idTarget del arco actual.
				//COLUMNA = numero de transicion de la transicion indicada por el idSource del arco atual.
				//Se carga la posicion de la matriz de incidencia positiva.
				this.positiva[posicionTarget][posicionSource] = peso;
			}
			//Si existe en plazas una key con el valor de idSource,
			//implica que el peso debe ir en la matriz negativa.
			else if (this.plazas.containsKey(arcosValue.getValue().getSource())) {
				int posicionTarget = this.plazas.get(arcosValue.getValue().getSource()).getPosicionFilaMatriz();
				int posicionSource = this.transiciones.get(arcosValue.getValue().getTarget());
				//FILA = numero de Plaza de la plaza indicada por el idSource del arco actual.
				//COLUMNA = numero de transicion de la transicion indicada por el idTarget del arco atual
				//Se carga la posicion de la matriz de incidencia negativa.
				this.negativa[posicionTarget][posicionSource] = peso;
			}
			else {
				System.out.println("no se encontro idSource: " +
						arcosValue.getValue().getSource() + ", del arco: " + arcosValue.getKey());
			}
		}

	}

	/**
	 *Metodo que completa la matriz de inhibidores  de una red segun los 
	 *componentes que se cargaron hasta el momento. Este metodo debe ser 
	 *llamado una vez se termine de cargar todos los compenentes de la 
	 *red de petri de la que se desea obtener la matriz.
	 * @param idsMap 
	 */
	public void crearMatrizInhibidores(Map<String, String> idsMap) {
		replaceBrokenArcIds(idsMap);
		this.inhibidores = new int[this.plazas.size()][this.transiciones.size()];
		//Pone a cero todos los valores de los arreglos
		for (int f = 0; f < this.plazas.size(); f = f + 1) {
			for (int c = 0; c < this.transiciones.size(); c = c + 1) {
				this.inhibidores[f][c] = 0;
			}
		}
		//Luego se completan la matriz de inhibidores
		for(Entry<String, ElementoArco> arcosValue : this.arcos.entrySet()){
			// si es un brazo inhibidor no debo incluirlo en estas matrices, ya que tendra su propia matriz.
			if(!(arcosValue.getValue().getTipo() == TipoArco.INHIBIDOR))
				continue;

			int peso = arcosValue.getValue().getValorElemento().intValue();
			//Si no existe en la plazas el source del brazo entonces hay un error. 
			if (this.plazas.containsKey(arcosValue.getValue().getSource())) {
				int posicionTarget = this.plazas.get(arcosValue.getValue().getSource()).getPosicionFilaMatriz();
				int posicionSource = this.transiciones.get(arcosValue.getValue().getTarget());
				//FILA = numero de Plaza de la plaza indicada por el idTarget del arco actual.
				//COLUMNA = numero de transicion de la transicion indicada por el idSource del arco atual.
				this.inhibidores[posicionTarget][posicionSource] = peso;
			}
			else {
				System.out.println("no se encontro idSource: " +
						arcosValue.getValue().getSource() + ", del arco: " + arcosValue.getKey());
			}
		}

	}
	/**
	 *Metodo que completa la matriz de brazos lectores  de una red segun los 
	 *componentes que se cargaron hasta el momento. Este metodo debe ser 
	 *llamado una vez se termine de cargar todos los compenentes de la 
	 *red de petri de la que se desea obtener la matriz.
	 * @param idsMap 
	 */
	public void crearMatrizLectores(Map<String, String> idsMap) {
		replaceBrokenArcIds(idsMap);
		this.lectores = new int[this.plazas.size()][this.transiciones.size()];
		//Pone a cero todos los valores de los arreglos
		for (int f = 0; f < this.plazas.size(); f = f + 1) {
			for (int c = 0; c < this.transiciones.size(); c = c + 1) {
				this.lectores[f][c] = 0;
			}
		}
		//Luego se completan la matriz de inhibidores
		for(Entry<String, ElementoArco> arcosValue : this.arcos.entrySet()){
			// si es un brazo inhibidor no debo incluirlo en estas matrices, ya que tendra su propia matriz.
			if(!(arcosValue.getValue().getTipo() == TipoArco.LECTOR))
				continue;

			int peso = arcosValue.getValue().getValorElemento().intValue();
			//Si no existe en la plazas el source del brazo entonces hay un error. 
			if (this.plazas.containsKey(arcosValue.getValue().getSource())) {
				int posicionTarget = this.plazas.get(arcosValue.getValue().getSource()).getPosicionFilaMatriz();
				int posicionSource = this.transiciones.get(arcosValue.getValue().getTarget());
				//FILA = numero de Plaza de la plaza indicada por el idTarget del arco actual.
				//COLUMNA = numero de transicion de la transicion indicada por el idSource del arco atual.
				this.lectores[posicionTarget][posicionSource] = peso;
			}
			else {
				System.out.println("no se encontro idSource: " +
						arcosValue.getValue().getSource() + ", del arco: " + arcosValue.getKey());
			}
		}

	}


	/**
	 * Getter.
	 * @return La matriz positiva de la red.
	 */
	public final int[][] getMatrizPositiva() {
		return this.positiva;
	}
	/**
	 * Getter.
	 * @return La matriz negativa de la red.
	 */
	public final int[][] getMatrizNegativa() {
		return this.negativa;
	}
	/**
	 * Getter.
	 * @return El marcado inicial de la red.
	 */
	public final int[] getMarcado() {
		return this.marcadoInicial;
	}
	/**
	 * Getter.
	 * @return Plazas
	 */
	public final HashMap<String, Plaza> getPlazas() {
		return this.plazas;
	}
	/**
	 * Getter Transiciones ransiciones.
	 * @return Transiciones.
	 */
	public final HashMap<String, Integer> getTransiciones() {
		return this.transiciones;
	}
	
	public int[][] getMatrizInhibidores() {
		return inhibidores;
	}
	
	public int[][] getMatrizLectores() {
		return lectores;
	}
	/**
	 * Imprime todas las transiciones, mostrando su  nombre y numero de columna.
	 */
	public void mostrarColumnasTransicion() {
		final Iterator<String> tr = this.transiciones.keySet().iterator();
		while (tr.hasNext()) {
			final String tra = tr.next();
			System.out.println(
					"Transicion: " + tra + " columna: " +
							this.transiciones.get(tra));
		}
	}

	/**
	 * for new pnml standar, the id is a random auto incremental number now, and we manage that the id is the 
	 * transition name. so, in the idsMaps it is the relation between the id and the transition name, we just replace the 
	 * ids from the source and target of the Arc elements for the name of the transitions and places. 
	 * @param idsMaps
	 */
	private void replaceBrokenArcIds(Map<String,String> idsMaps){
		for(String key : arcos.keySet() ){
			ElementoArco arco = arcos.get(key);
			String sourceId =  idsMaps.get(arco.getSource());
			String targetId = idsMaps.get(arco.getTarget()); 
			if(sourceId != null && targetId != null){
				arco.setSourceTarget(idsMaps.get(arco.getSource()), idsMaps.get(arco.getTarget()));  
			}
		}

	}
}
