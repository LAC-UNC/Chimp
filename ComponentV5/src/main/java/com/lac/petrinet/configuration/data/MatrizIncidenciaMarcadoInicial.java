package com.lac.petrinet.configuration.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * En esta clase estan contenidos todos los datos necesarios para obtener la
 * matriz de incidencia de una red.
 * Ademas, esta clase es capaz de armar la matriz de incidencia y marcado de
 * una red a partir de Elementos de tipo Plaza, Transicion y Arco que se van
 * "agregando" con sus caracteristicas.
 * @author María Florencia Caro & Ignacio Furey
 */

public class MatrizIncidenciaMarcadoInicial {
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
	/**
	 * Marcado inicial de las plazas.
	 */
	private int[] marcadoInicial;
	/**
	 * Plazas, String=key/idPlaza, Integer[0]=posicion fila en matriz,
	 * Integer[1]=marcado de plaza.
	 */
	private HashMap<String, Integer[]> plazas;
	/**
	 * Transiciones, String=key/idTransicion, Integer=posicion columna
	 * en matriz.
	 */
	private HashMap<String, Integer> transiciones;
	/**
	 * Arcos, String=key/idArco, String[0]= idSource, String[1]= idTarget,
	 * String[2]=peso del arco.
	 */
	private HashMap<String, String[]> arcos;


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
	public MatrizIncidenciaMarcadoInicial() {
		this.plazas = new HashMap<String, Integer[]>();
		this.transiciones = new HashMap<String, Integer>();
		this.arcos = new HashMap<String, String[]>();
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
		this.plazas.put(idPlaza, new Integer[]{Integer.valueOf(this.numPlaza),
			Integer.valueOf(marcadoInicialNuevo), });
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
	public void agregarArco(final String idArco, final String idSource, final String idTarget,
			final String valorPeso) {
		//Se agrega en el Map de arcos una nueva Key=idArco asociada a un
		//Value= {idSource, idTarget, valor peso del arco}.
		this.arcos.put(idArco, new String[]{idSource, idTarget, valorPeso});
	}
	/**
	 * Metodo que completa la matriz de incidencia positiva y negativa y el
	 * marcado inicial de una red segun los componentes que se cargaron hasta
	 * el momento. Este metodo debe ser llamado una vez se termine de cargar
	 * todos los compenentes de la red de petri de la que se desea obtener la
	 * matriz.
	 * @param idsMap 
	 */
	public void crearMatriz(Map<String, String> idsMap) {
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
		//se crea un iterator con el HashMap de las plazas para poder recorrerlo
		final Iterator<Map.Entry<String, Integer[]>> it1 = 
				this.plazas.entrySet().iterator();
		//siempre que haya un elemento disponible
		while (it1.hasNext()) {
			//obtiene proximo elemento de tipo <String, Integer[]> =
			//<idPlaza, {numero de Plaza/fila en matriz, 
			//marcado inicial de plaza>.
			final Map.Entry<String, Integer[]> e1 = it1.next();
			//Guarda el marcado en el vector marcadoInicial, en la posicion
			//indicada por numero de Plaza/fila en matriz.
			this.marcadoInicial[ (e1.getValue()[0]).intValue() ] =
					(e1.getValue()[1]).intValue();
		}
		//Luego se completan la matriz de incidencia positiva y negativa
		//Se crea un iterador con el HashMap de los arcos par apoder recorrerlo
		final Iterator<Map.Entry<String, String[]>> it2 = this.arcos.entrySet().iterator();
		//siempre que haya un elemento disponible
		while (it2.hasNext()) {
			//obtiene proximo elemento de tipo <String, String[]> =
			//<idArco, {idSource, idTarget, peso del arco}>
			final Map.Entry<String, String[]> e2 = it2.next();
			//Luego determina si el peso del arco debe ir en la matriz positiva,
			//en caso que el idSource.
			//coincida con una transicion, o en la matriz negativa, si el 
			//idSource coincide con una plaza.
			if (this.transiciones.containsKey(e2.getValue()[0])) {
				//Si existe en transiciones una key con
				//el valor de idSource, implica que el peso
				//debe ir en la matriz positiva.

				//FILA = numero de Plaza (posicion 0 del vector value del 
				//HashMap plazas) de la plaza indicada por el idTarget del arco
				//actual.
				final int fila = this.plazas.get(e2.getValue()[1])[0].intValue();
				//COLUMNA = numero de transicion (value del HashMap 
				//transiciones) de la transicion indicada por el idSource del 
				//arco atual.
				final int columna = this.transiciones.get(e2.getValue()[0]);
				//PESO = valor entero de la posicion 2 del vector value del
				//arco actual
				final int peso = Integer.parseInt(e2.getValue()[2]);

				//Con los ultimos valores, se carga la posicion de la matriz de
				//incidencia positiva.
				this.positiva[fila][columna] = peso;
			}
			else {
				if (this.plazas.containsKey(e2.getValue()[0])) {
					//Si existe en plazas una key con el valor de idSource,
					//implica que el peso debe ir en la matriz negativa.

					//FILA = numero de Plaza (posicion 0 del vector value del
					//HashMap plazas) de la plaza indicada por el idSource del
					//arco actual.
					final int fila = this.plazas.get(e2.getValue()[0])[0].intValue();
					//COLUMNA = numero de transicion (value del HashMap
					//transiciones) de la transicion indicada por el idTarget
					//del arco atual
					final int columna = this.transiciones.get(e2.getValue()[1]);
					//PESO = valor entero de la posicion 2 del vector value del
					//arco actual.
					final int peso = Integer.parseInt(e2.getValue()[2]);

					//Con los ultimos valores, se carga la posicion de la
					//matriz de incidencia positiva.
					this.negativa[fila][columna] = peso;
				}
				else {
					System.out.println("no se encontro idSource: " +
						e2.getValue()[0] + ", del arco: " + e2.getKey());
				}
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
	public final HashMap<String, Integer[]> getPlazas() {
		return this.plazas;
	}
	/**
	 * Getter Transiciones ransiciones.
	 * @return Transiciones.
	 */
	public final HashMap<String, Integer> getTransiciones() {
		return this.transiciones;
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
			String[] arco = arcos.get(key);
			arco[0] = idsMaps.get(arco[0]); 
			arco[1] = idsMaps.get(arco[1]); 
		}

	}
}
