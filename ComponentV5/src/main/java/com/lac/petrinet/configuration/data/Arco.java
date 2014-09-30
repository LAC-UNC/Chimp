package com.lac.petrinet.configuration.data;

/**
 * Clase utilizada para completar una instancia de MatrizIncidencia, en este
 * caso, se completan los valores definidos por los arcos de la red de petri.
 * @author Mará Florencia Caro & Ignacio Furey
 */

public class Arco extends AbstractElemento {
	/**
	 * plaza o transicion de inicio del arco.
	 */
	private String source;
	/**
	 * plaza o transicion de fin del arco.
	 */
	private String target;
	/**
	 * Class contructor.
	 */
	public Arco() {
		super();
		this.reset();
	}
	/* (non-Javadoc)
	 * @see externo.Elemento#agregarPlazaAMatriz(java.lang.String)
	 */
	@Override
	public void agregarElementoAMatriz(final MatrizIncidenciaMarcadoInicial matriz) {
		matriz.agregarArco(this.id, this.source, this.target,
			this.valorElemento.toString());
		this.reset();
	}
	/* (non-Javadoc)
	 * @see externo.Elemento#setSourceTraget(java.lang.String, java.lang.String)
	 */
	@Override
	public void setSourceTarget(final String newSource, final String newTarget) {
		this.source = newSource;
		this.target = newTarget;
	}
	/* (non-Javadoc)
	 * @see externo.Elemento#setSourceTraget(java.lang.String, java.lang.String)
	 */
	@Override
	public void reset() {
		super.reset();
		this.source = null;
		this.target = null;
		this.valorElemento = Integer.valueOf(1);
	}
}
