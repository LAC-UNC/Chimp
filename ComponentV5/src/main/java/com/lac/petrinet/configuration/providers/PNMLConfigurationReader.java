package com.lac.petrinet.configuration.providers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.lac.petrinet.configuration.ConfigurationReader;
import com.lac.petrinet.configuration.PNData;
import com.lac.petrinet.core.PetriNet;
import com.lac.petrinet.exceptions.PetriNetException;
import com.lac.petrinet.netcommunicator.FiredTransition;
import com.lac.petrinet.netcommunicator.InformedTransition;
import com.lac.petrinet.netcommunicator.ProcessorHandler;
import com.lac.petrinet.netcommunicator.ProcessorHandlerImpl;

/**
 * Read the PNML file and create all the transition instances that are inform or fired, and stored it.
 * 
 */
public class PNMLConfigurationReader implements ConfigurationReader {
	
	/**
	 * Nombre de archivo para la matriz de incidencia.
	 */
	private static final String INCIDENCIA = "incidencia";
	/**
	 * Nombre de archivo para el marcado inicial.
	 */
	private static final String MARCADO = "marcado";
	/**
	 * Nombre de archivo para para la matriz de relaciones.
	 */
	private static final String RELACION = "relacion";
	/**
	 * Nombre de archivo para las cotas de plazas.
	 */
	private static final String COTAS = "cotas";
	/**
	 * Nombre de archivo para los diparos automaticos.
	 */
	private static final String AUTOMATICOS = "automaticos";
	/**
	 * Nombre de archivo para la matriz de prioridades.
	 */
	private static final String PRIORIDADES = "prioridades";
	/**
	 * Nombre de archivo para la matriz de prioridades distribuidas.
	 */
	private static final String PRIORIDADESDISTRIBUIDAS =
			"prioridadesDistribuidas";
	/**
	 * Nombre de archivo para las transiciones sin informe.
	 */
	private static final String SININFORME = "sinInforme";

	private PNData pnData;
	
	public PNData getPnData() {
		return this.pnData;
	}
	
	/**
	 * Read the values from PNML file y create the transitions instance adding it to the transitions attribute. 
	 * @param pathARedPetri Path to PNML file
	 * @param processorHandler 
	 * @throws PetriNetException 
	 */
	private PetriNet createPNFromPNML(final String pathARedPetri, ProcessorHandler processorHandler) throws PetriNetException {
		PetriNet petriNet = new PetriNet();
		ExecutorService threadPool = Executors.newCachedThreadPool();
		int transitionID = 0;
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
				final Element name = (Element) transicion.getElementsByTagName("name").item(0);
				idTransicion = name.getElementsByTagName("text").item(0).getTextContent();
				final Element label = (Element) transicion.getElementsByTagName("label").item(0);
				final NodeList texto = label.getElementsByTagName("text");
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

				if(primerValor.compareToIgnoreCase("d") == 0 ){
					FiredTransition newTransition = new FiredTransition(processorHandler, transitionID);
					petriNet.addFired(idTransicion, newTransition);
				}else if(primerValor.compareToIgnoreCase("a") != 0 ){
					throw new PetriNetException("Transition has invalid etiquete for fire. Transition id: " + idTransicion);
				}
				if(segundoValor.compareToIgnoreCase("i") == 0 ){
					InformedTransition newTransition = new InformedTransition(processorHandler, transitionID,threadPool);
					petriNet.addInformed(idTransicion, newTransition);
				}else if(segundoValor.compareToIgnoreCase("n") != 0){
					throw new PetriNetException("Transition has invalid etiquete for inform. Transition id: " + idTransicion);
				}
				transitionID++;
			}
		}
		return petriNet;
	}
	
	@Override
	public PetriNet loadConfiguration(String pnmlFilepath) throws PetriNetException {
		//create temporal folder for configuration files
		String configFolderParentPath;
		try {
			configFolderParentPath = getJarpath();
			while(configFolderParentPath.contains(".jar")){
				configFolderParentPath = configFolderParentPath.substring(0,  configFolderParentPath.lastIndexOf("/"));
			}
		} catch (URISyntaxException e) {
			throw new PetriNetException(e.getMessage(), e); 
		}
		// create configuration files for processor and Transitions
		this.pnData = new PNData();
		this.pnData.cargarRed(pnmlFilepath);
		String configFileFolderPath = generateConfigFiles(this.pnData, configFolderParentPath);
		// create processorHandler
		ProcessorHandler processorHandler = new ProcessorHandlerImpl(pathForPNNVHack(configFileFolderPath), this.pnData.getTransiciones().size());
		// Create PetriNet and assign processorHandler to its.
		PetriNet petriNet = createPNFromPNML(pnmlFilepath, processorHandler);
		petriNet.setPNData(this.pnData);
		return petriNet;
	}
	
	/**
	 * Genera todos los archivos de configuracion para el procesador de petri.
	 * @param infoRed instancia de Herramienta red de petri con informacion
	 * de la red.
	 * @param pathArchivos path del proyecto.
	 * @return path to the folder that contain all the configuration files. 
	 */
	private String generateConfigFiles( final PNData infoRed, final String exitFilePath) {
		String pathConfig ;
		if(exitFilePath.endsWith("/") || exitFilePath.endsWith("\\")){
			pathConfig =  exitFilePath + "archivosConfiguracion";
		}
		else{
			pathConfig = exitFilePath + "\\archivosConfiguracion";
		}
		final File folderConfig = new File(pathConfig);
		folderConfig.mkdirs();
		////Se genera la matriz de prioridades distribuidas con un vector nulo,
		//por que no existen transiciones distribuidas.
		generarVectorCero(1,
				pathConfig + "\\" + PRIORIDADESDISTRIBUIDAS + ".txt",
				"\n");
		//Matriz de incidencia
		generarArchivoMatriz(infoRed.getMatrizIncidenciaPositiva(),
				infoRed.getMatrizIncidenciaNegativa(),
				pathConfig + "\\" + INCIDENCIA + "0.txt");
		//Marcado Inicial
		generarArchivoVectorVertical(infoRed.getMarcadoInicial(),
				pathConfig + "\\" + MARCADO + "0.txt");
		//Se genera la matriz de relaciones con un vector nulo, de modo que
		//no existan relaciones.
		generarVectorCero(infoRed.getMatrizIncidenciaPositiva()[0].length,
				pathConfig + "\\" + RELACION + "0.txt",
				" ");
		//Se genera las cotas de plazas con un vector nulo, de modo que
		//no existan limitaciones de tokens en las plazas.
		generarVectorCero(infoRed.getMatrizIncidenciaPositiva().length,
				pathConfig + "\\" + COTAS + "0.txt",
				"\n");
		//Se genera el vector de disparos automaticos.
		generarArchivoVectorVertical(infoRed.getVectorDisparosAtomaticos(),
				pathConfig + "\\" + AUTOMATICOS + "0.txt");
		//Se genera el vector de disparos sin informe.
		generarArchivoVectorVertical(infoRed.getVectorDisparosSinInforme(),
				pathConfig + "\\" + SININFORME + "0.txt");
		//Se genera una matriz identidad de prioridades.
		generarMatrizIdentidad(infoRed.getMatrizIncidenciaPositiva()[0].length,
				pathConfig + "\\" + PRIORIDADES + "0.txt");
		//Se genera el archivo de configuracion con los nombres del resto de los
		//archivos.
		generarArchivoConfig(pathConfig + "\\config.txt");
		return pathConfig + "\\config.txt";
	}
	
	/**
	 * Generador del archivo matriz de configuracion del procesador.
	 * @param matrizPositiva matriz de incidencia positiva
	 * @param matrizNegativa matriz de incidencia negativa
	 * @param filePath path donde crear el archivo
	 */
	protected void generarArchivoMatriz(final int[][] matrizPositiva,
		final int[][] matrizNegativa, final String filePath) {
		File matrizFile = null;
		BufferedWriter bw = null;
        try {
        	matrizFile = new File(filePath);
        	matrizFile.createNewFile();
        	bw = new BufferedWriter(new FileWriter(matrizFile));
			for (int f = 0; f < matrizPositiva.length; f = f + 1) {
				for (int c = 0; c < matrizPositiva[0].length; c = c + 1) {
					final int valor = matrizPositiva[f][c] - matrizNegativa[f][c];
					bw.write(String.valueOf(valor) + " ");
				}
				bw.newLine();
			}
        } catch (java.io.IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	/**
	 * Crea un archivo cuyo path sera filePath con un vector vertical con los
	 * valores definidos en valores.
	 * @param valores vector con los valores.
	 * @param filePath path del archivo a crear.
	 */
	protected void generarArchivoVectorVertical(final int[] valores, final String filePath) {
		File marcadoFile = null;
		BufferedWriter bw = null;
        try {
        	marcadoFile = new File(filePath);
        	marcadoFile.createNewFile();
			bw = new BufferedWriter(new FileWriter(marcadoFile));

			for (int f = 0; f < valores.length; f = f + 1) {
				final int valor = valores[f];
				bw.write(String.valueOf(valor));
				bw.newLine();
			}
        } catch (java.io.IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * Genera un vector con todos sus valores a cero.
	 * @param length Longitud
	 * @param filePath path del archivo a crear.
	 * @param separador separador entre valores. Normalmente espacio blanco o
	 * nueva linea.
	 */
	protected void generarVectorCero(final int length, final String filePath,
			final String separador) {
		File relacionesFile = null;
		BufferedWriter bw = null;
        try {
        	relacionesFile = new File(filePath);
        	relacionesFile.createNewFile();
			bw = new BufferedWriter(new FileWriter(relacionesFile));
			for (int f = 0; f < length; f = f + 1) {
				bw.write('0' + separador);
			}
        } catch (java.io.IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}	
	/**
	 * Generar matriz Identidad.
	 * @param length Longitud
	 * @param filePath path del archivo a crear.
	 */
	protected void generarMatrizIdentidad(final int length, final String filePath) {
		File matrizFile = null;
		BufferedWriter bw = null;
        try {
        	matrizFile = new File(filePath);
        	matrizFile.createNewFile();
			bw = new BufferedWriter(new FileWriter(matrizFile));
			for (int f = 0; f < length; f = f + 1) {
				for (int c = 0; c < length; c = c + 1) {
					final int valor;
					if (f == c) {
						valor = 1;
					} else {
						valor = 0;
					}
					bw.write(String.valueOf(valor) + " ");
				}
				bw.newLine();
			}
        } catch (java.io.IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}	
	/**
	 * Generar archivo config.
	 * @param filePath Path al archivo
	 */
	protected void generarArchivoConfig(final String filePath) {
		File matrizFile = null;
		BufferedWriter bw = null;
        try {
        	matrizFile = new File(filePath);
        	matrizFile.createNewFile();
			bw = new BufferedWriter(new FileWriter(matrizFile));

			bw.write("<" + INCIDENCIA + ">"  + INCIDENCIA + "X.txt" +
					"</" + INCIDENCIA + ">");
			bw.newLine();
			bw.write("<" +  MARCADO + ">"  +  MARCADO + "X.txt" +
					"</" +  MARCADO + ">");
			bw.newLine();
			bw.write("<" + COTAS + ">"  + COTAS + "X.txt" +
					"</" + COTAS + ">");
			bw.newLine();
			bw.write("<" + AUTOMATICOS + ">"  + AUTOMATICOS + "X.txt" +
					"</" + AUTOMATICOS + ">");
			bw.newLine();
			bw.write("<" + PRIORIDADES + ">"  + PRIORIDADES + "X.txt" +
					"</" + PRIORIDADES + ">");
			bw.newLine();
			bw.write("<" + PRIORIDADESDISTRIBUIDAS + ">" +
					PRIORIDADESDISTRIBUIDAS + ".txt" +
					"</" + PRIORIDADESDISTRIBUIDAS + ">");
			bw.newLine();
			bw.write("<" + SININFORME + ">"  + SININFORME + "X.txt" +
					"</" + SININFORME + ">");
			bw.newLine();
			bw.write("<" + RELACION + ">"  + RELACION + "X.txt" +
					"</" + RELACION + ">");
        } catch (java.io.IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * This method is a hack in order to work around the way taht PNNV expect the path to the configuration files folder. 
	 * @return
	 */
	private String pathForPNNVHack(String path){
		return path.replace("/", "\\");
	}
	
	private String getJarpath() throws URISyntaxException {
		final String uri;
		uri = PNMLConfigurationReader.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
		return uri;
	}
	
	
}
