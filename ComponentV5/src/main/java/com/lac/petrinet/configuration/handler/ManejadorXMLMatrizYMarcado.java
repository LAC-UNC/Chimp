package com.lac.petrinet.configuration.handler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.lac.petrinet.configuration.data.AbstractElemento;
import com.lac.petrinet.configuration.data.Arco;
import com.lac.petrinet.configuration.data.MatrizIncidenciaMarcadoInicial;
import com.lac.petrinet.configuration.data.Plaza;
import com.lac.petrinet.configuration.data.Transicion;

/**
 * Genera una instancia de la clase MatrizIncidenciaMarcadoInicial y a medida que lee
 * informacion le agrega una instancia de transicion, arco o plaza segun
 * corresponda.
 * @author María Florencia Caro & Ignacio Furey
 */

public class ManejadorXMLMatrizYMarcado extends DefaultHandler {
	/**
	 * matriz.
	 */
	private MatrizIncidenciaMarcadoInicial matriz;
	/**
	 * Elemento actual.
	 */
	private AbstractElemento actual;
	/**
	 * Plaza de la red.
	 */
	private AbstractElemento plaza;
	/**
	 * Transicion de la red.
	 */
	private AbstractElemento transicion;
	/**
	 * Arco de la red.
	 */
	private AbstractElemento arco;
	/**
	 * Variable booleana.
	 */
	private boolean bText;
	/**
	 * Variable booleana.
	 */
	private boolean bAPeso;
	/**
	 * Variable booleana.
	 */
	private boolean bTMarcadoInicial;
	/**
	 * Constructor.
	 */
	public ManejadorXMLMatrizYMarcado() {
		this.matriz = new MatrizIncidenciaMarcadoInicial();
		this.plaza = new Plaza();
		this.transicion = new Transicion();
		this.arco = new Arco();
		this.bText = false;
		this.bAPeso = false;
		this.bTMarcadoInicial = false;
	}
	@Override
	public void startDocument() throws SAXException {
	}
	@Override
	public void endDocument() throws SAXException {
		this.matriz.crearMatriz();
	}
	@Override
	public void startElement(final String uri, final String localName, final String name,
							final Attributes attributes) throws SAXException {

		switch (localName) {
			case "place":
				this.actual = this.plaza;
				this.actual.setId(attributes.getValue("id"));
				break;

			case "transition":
				this.actual = this.transicion;
				this.actual.setId(attributes.getValue("id"));
				break;

			case "arc":
				this.actual = this.arco;
				this.actual.setId(attributes.getValue("id"));
				this.actual.setSourceTarget(attributes.getValue("source"),
						attributes.getValue("target"));
				break;

			case "initialMarking":
				this.bTMarcadoInicial = true;
				break;

			case "inscription":
				this.bAPeso = true;
				break;

			case "text":
				this.bText = true;
				break;
			default:
				break;
		}
	}

	@Override
	public void characters(final char[] ch, final int start, final int length)
		throws SAXException {

		//Si la etiqueta actual es "texto" y ya se paso por la etiqueta
		//"initialMarking" 
		//(de una plaza) o "inscription" (de un arco), entonces se
		//establece el valor 
		//del elemento actual.
		if (this.bText) {
			if (this.bAPeso || this.bTMarcadoInicial) {
				//Se setea el valor del elemento actual
				this.actual.setValorElemento(Integer.parseInt(String.valueOf(ch, start, length)));
				//se limpian las banderas para proximas etiquetas
				this.bText = false;
				this.bAPeso = false;
				this.bTMarcadoInicial = false;
			}
			//si bText es true, pero bAPeso y bTMarcadoInicial son falso, se cambia bTexto a false
			else {
				this.bText = false;
			}
		}
	}

	@Override
	public void endElement(final String uri, final String localName, final String name)
		throws SAXException {

		if (localName.equals("place") || localName.equals("transition") ||
				localName.equals("arc")) {
			this.actual.agregarElementoAMatriz(this.matriz);
		}
	}
	/**
	 * Retorna la instancia de MatrizIncidencia. Deberia llamarse luego de 
	 * haberse hecho el parseo del archivo XML con ProcesaXML.
	 * @return La matriz de incidencia de la red descripta por el archivo XML.
	 */
	public MatrizIncidenciaMarcadoInicial getMatriz() {
		return this.matriz;
	}
}

