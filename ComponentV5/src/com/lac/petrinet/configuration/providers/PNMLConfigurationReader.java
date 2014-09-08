package com.lac.petrinet.configuration.providers;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.lac.petrinet.configuration.ConfigurationReader;
import com.lac.petrinet.configuration.TransitionGroup;
import com.lac.petrinet.exceptions.PetriNetException;
import com.lac.petrinet.netcommunicator.Transition;

/**
 * Read the PNML file and create all the transition instances that are inform or fired, and stored it.
 * 
 */
public class PNMLConfigurationReader implements ConfigurationReader {
	
	private TransitionGroup transitions;
	
	/**
	 * Constructor.
	 * @param pathARedPetri
	 * @throws PetriNetException 
	 */
	public PNMLConfigurationReader()  {
		this.transitions = new TransitionGroup();
	}

	/**
	 * Read the values from PNML file y create the transitions instance adding it to the transitions attribute. 
	 * @param pathARedPetri Path to PNML file
	 * @throws PetriNetException 
	 */
	private TransitionGroup createTransitionsFromPNML(final String pathARedPetri) throws PetriNetException {
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
				
//				Transition t = new Transition(idTransicion, 0);
				Transition t = new Transition(1, 0);
				if(primerValor.compareToIgnoreCase("d") == 0 && primerValor.compareToIgnoreCase("a") != 0){
					this.transitions.getFiredTransitions().put(idTransicion, t);
				}else{
					throw new PetriNetException("Transition has invalid etiquete for fire. Transition id: " + idTransicion);
				}
				if(segundoValor.compareTo("i") == 0 && primerValor.compareToIgnoreCase("n") != 0){
					this.transitions.getInformedTransitions().put(idTransicion, t);
				}else{
					throw new PetriNetException("Transition has invalid etiquete for inform. Transition id: " + idTransicion);
				}
			}
		}
		return this.transitions;
	}
	
	public TransitionGroup getTransitions(){
		return this.transitions;
	}
	
	public TransitionGroup updateTransitions(String pathARedPetri) throws PetriNetException{
		return createTransitionsFromPNML(pathARedPetri);
	}

	@Override
	public TransitionGroup getConfiguration(String path) throws PetriNetException {
		createTransitionsFromPNML(path);
		return this.transitions;
		
	}
	
}
