package com.lac.petrinet.configuration.data;

/**
 * Clase que representa una etiqueta, reune toda la informacion de una etiqueta.
 * @author Maria Florencia Caro & Ignacio Furey
 */
public class Etiqueta {
	/**
	 * id de la transicion.
	 */
	private String idTransicion;
	/**
	 * primer valor de la etiqueta.
	 */
	private String primerValor;
	/**
	 * segundo valor de la etiqueta.
	 */
	private String segundoValor;
	/**
	 * Constructor.
	 * @param idTransicionNuevo Id de la transicion.
	 * @param primerValorNuevo Primer valor de la etiqueta.
	 * @param segundoValorNuevo Segundo valor de la etiqueta.
	 */
	public Etiqueta(final String idTransicionNuevo, final String primerValorNuevo,
		final String segundoValorNuevo) {
		this.idTransicion = idTransicionNuevo;
		this.primerValor = primerValorNuevo;
		this.segundoValor = segundoValorNuevo;
	}
	/**
	 * Getter.
	 * @return idTransicion Id de la transicion asociada a la etiqueta
	 */
	public String getIdTransicion() {
		return this.idTransicion;
	}
	/**
	 * Getter.
	 * @return primerValor Primer valor de la etiqueta.
	 */
	public String getPrimerValor() {
		return this.primerValor;
	}
	/**
	 * Getter.
	 * @return segundoValor Segundo valor de la etiqueta.
	 */
	public String getSegundoValor() {
		return this.segundoValor;
	}
}
