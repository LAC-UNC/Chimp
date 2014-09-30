package com.lac.petrinet.configuration.data;

/**
 * Clase utilizada para completar una instancia de MatrizIncidencia, en este
 * caso, se completan los valores definidos por las transiciones de la red de petri.
 * @author María Florencia Caro & Ignacio Furey
 */

public class Transicion extends AbstractElemento {
	/**
	 * class constructor.
	 */
	public Transicion() {
		super();
	}
	/* (non-Javadoc)
	 * @see externo.Elemento#agregarElementoAMatriz()
	 */
	@Override
	public void agregarElementoAMatriz(final MatrizIncidenciaMarcadoInicial matriz) {
		matriz.agregarTransicion(id);
		this.reset();
	}
	/* (non-Javadoc)
	 * @see externo.Elemento#setSourceTraget(java.lang.String, java.lang.String)
	 */
	@Override
	public void setSourceTarget(final String source, final String target) {
		throw new RuntimeException("Una transicion no deberia tener " +
			"campos source ni target");
	}
}
