package com.lac.petrinet.configuration.handler;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.lac.petrinet.configuration.data.AbstractElemento;
import com.lac.petrinet.configuration.data.ElementoArco;
import com.lac.petrinet.configuration.data.MatricesPN;
import com.lac.petrinet.configuration.data.ElementoPlaza;
import com.lac.petrinet.configuration.data.Transicion;

/**
 * Genera una instancia de la clase MatrizIncidenciaMarcadoInicial y a medida que lee
 * informacion le agrega una instancia de transicion, arco o plaza segun
 * corresponda.
 * @author Mar�a Florencia Caro & Ignacio Furey
 */

public class ManejadorXMLMatrizYMarcado extends DefaultHandler {
	
	/**
	 * matriz Incidencia Marcado Inicial.
	 */
	private MatricesPN matriz;
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
	private boolean isText;
	/**
	 * Variable booleana.
	 */
	private boolean isAPeso;
	/**
	 * Variable booleana.
	 */
	private boolean isTMarcadoInicial;
	
	private boolean isActualTransition;
	
	private boolean isActualArco;
	
	private boolean isPlace;
	
	private Map<String, String> idsMap ;
	
	private String tempId;
	private boolean isType;
	/**
	 * Constructor.
	 */
	public ManejadorXMLMatrizYMarcado() {
		this.matriz = new MatricesPN();
		this.plaza = new ElementoPlaza();
		this.transicion = new Transicion();
		this.arco = new ElementoArco();
		this.isText = false;
		this.isAPeso = false;
		this.isTMarcadoInicial = false;
		this.isActualTransition = false;
		this.isActualArco = false;
		this.isPlace = false;
		this.isType = false;
		this.idsMap = new HashMap<String,String>();
		this.tempId=null;
		
	}
	@Override
	public void startDocument() throws SAXException {
	}
	@Override
	public void endDocument() throws SAXException {
		this.matriz.crearMatrizPositivaNegativa(idsMap);
		this.matriz.crearMatrizInhibidores(idsMap);
	}
	@Override
	public void startElement(final String uri, final String localName, final String name,
							final Attributes attributes) throws SAXException {

		// localname es el nobmbre del tag. Aqui debemos obtener todo los valores internos la propio tag. 
		switch (localName) {
			case "place":
				this.actual = this.plaza;
				this.tempId = attributes.getValue("id");
				this.isPlace = true;
				break;

			case "transition":
				this.actual = this.transicion;
				this.tempId = attributes.getValue("id");
				isActualTransition = true;
				break;

			case "arc":
				this.actual = this.arco;
				this.actual.setId(attributes.getValue("id"));
				this.actual.setSourceTarget(attributes.getValue("source"),
						attributes.getValue("target"));
				this.isActualArco = true;
				break;

			case "initialMarking":
				this.isTMarcadoInicial = true;
				break;

			case "inscription":
				this.isAPeso = true;
				break;

			case "text":
				this.isText = true;
				break;
			case "type":
				this.isType = true;
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
		if (this.isText) {
			if (this.isAPeso || this.isTMarcadoInicial) {
				//Se setea el valor del elemento actual
				this.actual.setValorElemento(Integer.parseInt(String.valueOf(ch, start, length)));
				//se limpian las banderas para proximas etiquetas
				this.isText = false;
				this.isAPeso = false;
				this.isTMarcadoInicial = false;
			}
			else if(isActualTransition || isPlace ){ 
				String humanId = new String(ch,start,length);
				this.actual.setId(humanId);
				this.isActualTransition = false;
				isPlace = false;
				this.isText=false;
				idsMap.put(tempId, humanId);
			}
			//si bText es true, pero bAPeso y bTMarcadoInicial son falso, se cambia bTexto a false
			else {
				this.isText = false;
			}
		}
		else if(this.isType && this.isActualArco == true){
			((ElementoArco)this.actual).setInhibitor(true);
			this.isType = false;
			this.isActualArco = false;
			
		}
	}

	@Override
	public void endElement(final String uri, final String localName, final String name)
		throws SAXException {

		if (localName.equals("place") || localName.equals("transition") ) {
			this.actual.agregarElementoAMatriz(this.matriz);
		}
		else if(localName.equals("arc")){
				this.actual.agregarElementoAMatriz(this.matriz);	
		}
	}
	/**
	 * Retorna la instancia de MatrizIncidencia. Deberia llamarse luego de 
	 * haberse hecho el parseo del archivo XML con ProcesaXML.
	 * @return La matriz de incidencia de la red descripta por el archivo XML.
	 */
	public MatricesPN getMatriz() {
		return this.matriz;
	}
}

