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

import com.lac.petrinet.configuration.data.Etiqueta;

/**
 * Clase encargada de leer las etiquetas de transicion desde el archivo de descripcion
 * de la red de Petri y por cada una de ellas genera una instancia de la clase guardando
 * la informacion de las etiquetas en una lista.
 * @author María Florencia Caro & Ignacio Furey
 */
public class ManejadorEtiquetas {
	/**
	 * Lista de etiquetas.
	 */
	private ArrayList<Etiqueta> etiquetas;
	/**
	 * Constructor.
	 * @param pathARedPetri path al archivo PNML a analizar.
	 */
	public ManejadorEtiquetas(final String pathARedPetri) {
		this.etiquetas = new ArrayList<Etiqueta>();
		this.leerEtiquetasDesdePNML(pathARedPetri);
	}
	/**
	 * Getter.
	 * @return Array list de etiquetas.
	 */
	public ArrayList<Etiqueta> getEtiquetas() {
		return this.etiquetas;
	}
	/**
	 * Lee los valores desde el PNML y completa el atributo Etiquetas con
	 * objetos de tipo Etiqueta.
	 * @param pathARedPetri Path al archivo PNML de la red a analizar.
	 */
	private void leerEtiquetasDesdePNML(final String pathARedPetri) {
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
		Etiqueta etiqueta = null;
		String idTransicion = "";
		String etiquetaAParsear = "";
		String primerValor = "";
		String segundoValor = "";
		for (int temp = 0; temp < listaTransiciones.getLength(); temp = temp + 1) {
			final Node transition = listaTransiciones.item(temp);
			if (transition.getNodeType() == Node.ELEMENT_NODE) {
				final Element transicion = (Element) transition;
				idTransicion = transicion.getAttribute("id");
				final NodeList texto = transicion.getElementsByTagName("text");
				if (texto.item(0) != null) {
					String[] aux;
					final String delimiter = ",";
					etiquetaAParsear = texto.item(0).getTextContent();
					aux = etiquetaAParsear.split(delimiter);
					primerValor = aux[0].replace("<", "");
					primerValor = primerValor.replace(" ", "");
					segundoValor = aux[1].replace(">", "");
					segundoValor = segundoValor.replace(" ", "");
				} else {
					primerValor = "A";
					segundoValor = "N";
				}
				etiqueta = new Etiqueta(idTransicion, primerValor,
						segundoValor);
				this.etiquetas.add(etiqueta);
			}
		}
	}
}
