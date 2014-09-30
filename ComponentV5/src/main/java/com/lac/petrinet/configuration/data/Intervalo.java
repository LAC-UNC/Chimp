package com.lac.petrinet.configuration.data;

/**
 * Clase que representa un intervalo de tiempo de una transicion, reune toda la
 * informacion de un intervalo.
 * @author Maria Florencia Caro & Ignacio Furey
 */

public class Intervalo {
	/**
	 * Id de la transicion.
	 */
	private String idTransicion;
	/**
	 * Minimo valor del intervalo de tiempo de la transicion.
	 */
	private String primerValor;
	/**
	 * Maximo valor del intervalo de tiempo de la transicion.
	 */
	private String segundoValor;

	/**
	 * Constructor.
	 * @param idTransicionIntervalo Id de la transicion.
	 * @param primerValorIntervalo minimo valor del intervalo de tiempo.
	 * @param segundoValorIntervalo maximo valor del intervalo de tiempo.
	 */
	public Intervalo(final String idTransicionIntervalo, final String primerValorIntervalo,
		final String segundoValorIntervalo) {
		this.idTransicion = idTransicionIntervalo;
		this.primerValor = primerValorIntervalo;
		this.segundoValor = segundoValorIntervalo;
	}
	/**
	 * Getter.
	 * @return idTransicion Id de la transicion asociada al intervalo de tiempo.
	 * */
	public String getIdTransicion() {
		return this.idTransicion;
	}
	/**
	 * Getter.
	 * @return primerValor Primer valor del intervalo de tiempo de una transicion.
	 */
	public String getPrimerValor() {
		return this.primerValor;
	}
	/**
	 * Getter.
	 * @return segundoValor Segundo valor del intervalo de tiempo de una transicion.
	 */
	public String getSegundoValor() {
		return this.segundoValor;
	}
}
