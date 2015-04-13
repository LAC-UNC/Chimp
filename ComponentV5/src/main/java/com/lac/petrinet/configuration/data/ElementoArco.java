package com.lac.petrinet.configuration.data;

/**
 * Clase utilizada para completar una instancia de MatrizIncidencia, en este
 * caso, se completan los valores definidos por los arcos de la red de petri.
 * @author Mar� Florencia Caro & Ignacio Furey
 */

public class ElementoArco extends AbstractElemento implements Cloneable{
	public enum TipoArco{
		INHIBIDOR, LECTOR, REGULAR
	}
	
	/**
	 * plaza o transicion de inicio del arco.
	 */
	private String source;
	/**
	 * plaza o transicion de fin del arco.
	 */
	private String target;
	
	private TipoArco tipo;
	/**
	 * Class contructor.
	 */
	public ElementoArco() {
		super();
		this.tipo = TipoArco.REGULAR;
		this.reset();
	}
	/* (non-Javadoc)
	 * @see externo.Elemento#agregarPlazaAMatriz(java.lang.String)
	 */
	@Override
	public void agregarElementoAMatriz(final MatricesPN matriz) {
//		matriz.agregarArco(this.id, this.source, this.target,
//			this.valorElemento.toString());
		matriz.agregarArco(this);
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
		this.tipo = TipoArco.REGULAR;
		this.valorElemento = Integer.valueOf(1);
	}
	
	
	public ElementoArco clone(){
		ElementoArco nuevoArco = new ElementoArco();
		nuevoArco.setId(this.id);
		nuevoArco.setTipo(this.tipo);
		nuevoArco.setSource(this.source);
		nuevoArco.setTarget(this.target);
		nuevoArco.setValorElemento(this.valorElemento);
		
		return nuevoArco;
	}

	
	
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public TipoArco getTipo() {
		return tipo;
	}
	public void setTipo(TipoArco tipo) {
		this.tipo = tipo;
	}
}
