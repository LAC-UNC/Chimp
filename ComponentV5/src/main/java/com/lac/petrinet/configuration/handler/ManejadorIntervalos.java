package com.lac.petrinet.configuration.handler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.lac.petrinet.configuration.data.Intervalo;

/**
 * Clase encargada de leer los intervalos de tiempo de cada transicion desde el 
 * archivo de descripcion de la red de Petri con tiempo y por cada uno de ellos genera 
 * una instancia Intervalo guardando la informacion de los intervalos en una lista.
 * @author María Florencia Caro & Ignacio Furey
 */
public class ManejadorIntervalos {
	/**
	 * Lista de intervalos.
	 */
	private ArrayList<Intervalo> intervalos;
	/**
	 * Constructor.
	 * @param pathARedPetri path al archivo PNML a analizar.
	 */
	public ManejadorIntervalos(final String pathARedPetri) {
		this.intervalos = new ArrayList<Intervalo>();
		this.leerIntervalosDesdePNML(pathARedPetri);
	}
	/**
	 * Getter.
	 * @return Array list de intervalos.
	 */
	public ArrayList<Intervalo> getIntervalos() {
		return this.intervalos;
	}
	/**
	 * Lee los valores desde el PNML y completa el atributo Intervalo con
	 * objetos de tipo Intervalo.
	 * @param pathARedPetri Path al archivo PNML de la red a analizar.
	 */
	private void leerIntervalosDesdePNML(final String pathARedPetri) {
  		final File xmlFile = new File(pathARedPetri);
		final DocumentBuilderFactory dbFactory =
			DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		}
		Document doc = null;
		try {
			doc = dBuilder.parse(xmlFile);
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}
		doc.getDocumentElement().normalize();
		
		//Nodo "transition"
		final NodeList listaTransiciones =
			doc.getElementsByTagName("transition");
		Intervalo intervalo = null;
		String idTransicion = "";
		String primerValor = "";
		String segundoValor = "";
		for (int temp = 0; temp < listaTransiciones.getLength(); temp = temp + 1) {
			final Node transition = listaTransiciones.item(temp);
			if (transition.getNodeType() == Node.ELEMENT_NODE) {
				final Element transicion = (Element) transition;
				idTransicion = transicion.getAttribute("id");
				final NodeList listaIntervalos = transicion.getElementsByTagName("cn");
				for (int j = 0; j < listaIntervalos.getLength(); j = j + 2) {
					primerValor = listaIntervalos.item(j).getTextContent();
					segundoValor = listaIntervalos.item(j + 1).getTextContent();
				}	
				intervalo = new Intervalo(idTransicion, primerValor,
						segundoValor);
				this.intervalos.add(intervalo);
			}
		}
	}
}
