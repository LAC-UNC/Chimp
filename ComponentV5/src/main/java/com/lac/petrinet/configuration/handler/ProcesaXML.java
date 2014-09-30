package com.lac.petrinet.configuration.handler;

import java.io.FileInputStream;
import java.io.IOException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Clase que procesa un XML mediante el handler SAX.
 * @author María Florencia Caro & Ignacio Furey
 */

public class ProcesaXML {
/**
 * Metodo que procesa el archivo XML.
 * @param manejador
 * @param filePath
 */
	public ProcesaXML(final DefaultHandler manejador, final String filePath) {

		try {
			// Creamos la factoria de parseadores por defecto
			final XMLReader reader = XMLReaderFactory.createXMLReader();
			// Añadimos nuestro manejador al reader
			reader.setContentHandler(manejador); 
			// Procesamos el xml
			reader.parse(new InputSource(new FileInputStream(filePath)));
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
