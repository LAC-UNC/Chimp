package com.lac.petrinet.configuration.data;

/**
 * Clase utilizada para completar una instancia de MatrizIncidencia.
 * @author María Florencia Caro & Ignacio Furey
 */

public abstract class AbstractElemento {
	/**
	 * id de elemento.
	 */
	protected String id;
	/**
	 * Valor del elemento, depende del tipo de elemento. Puede ser un marcado
	 * inicial para una plza, un peso de arco o nada en caso de transicion.
	 */
	protected Integer valorElemento;
	/**
	 * Class contructor.
	 */
	public AbstractElemento() {
		this.valorElemento = new Integer(0);
		this.reset();
	}
	/**
	 * Define como debe agregarse este elemento de una red de petri a la matriz
	 * de incidencia.
	 * @param matriz Matriz a la cual se agregara el elmento.
	 */
	public abstract void agregarElementoAMatriz(
			MatrizIncidenciaMarcadoInicial matriz);
	/**
	 * Establece los valores de source y target, solo debe implementarse
	 * para elementos de tipo arco.
	 * @param source string con el id del elemento source del arco.
	 * @param target string con el id del elemento target del arco.
	 * @throws RuntimeException .
	 */
	public abstract void setSourceTarget(String source, String target) throws
		RuntimeException;

	/**
	 * Establece a null los valores de id y valorElemento.
	 */
	public void reset() {
		this.id = null;
		this.valorElemento = Integer.valueOf(0);
	}
	/**
	 * Setter de id.
	 * @param idNuevo valor a establecer en id.
	 */
	public void setId(final String idNuevo) {
		this.id = idNuevo;
	}
	/**
	 * Setter de valorElemento.
	 * @param valor valor a establecer en valorElemento.
	 */
	public void setValorElemento(final int valor) {
		this.valorElemento = Integer.valueOf(valor);
	}
}
