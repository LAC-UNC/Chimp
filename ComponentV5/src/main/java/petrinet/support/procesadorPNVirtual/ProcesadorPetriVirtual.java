package petrinet.support.procesadorPNVirtual;

import java.io.File;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import org.apache.commons.io.FilenameUtils;

import petrinet.support.IOFiles.ManejadorTXT;
import petrinet.support.IOFiles.ManejadorXMLConfig;
import petrinet.support.Petri.IPCoreHPN;
import petrinet.support.Petri.PetriNet;
import petrinet.support.procesador.ProcesadorPetri;

public class ProcesadorPetriVirtual
implements ProcesadorPetri
{
	/*private static final String INCIDENCIA = "incidencia";
   private static final String MARCADO = "marcado";
   private static final String RELACION = "relacion";
   private static final String COTAS = "cotas";
   private static final String AUTOMATICOS = "automaticos";
   private static final String PRIORIDADES = "prioridades";
   private static final String PRIORIDADESDISTRIBUIDAS = "prioridadesDistribuidas";*/
	private String fileNameIncidencia;
	private String fileNameInhibidores;
	private String fileNameMarcado;
	private String fileNameRelacion;
	private String fileNameCotas;
	private String fileNameAutomaticos;
	private String fileNamePrioridades;
	private String fileNamePrioridadesDistribuidas;
	private String folderPath;
	IPCoreHPN ipCore = null;

	public ProcesadorPetriVirtual(String filePath, int cantidadTransiciones) throws Exception
	{
		this.folderPath = obtenerFolderPath(filePath);
		try
		{
			this.fileNameIncidencia = (this.folderPath + 
					ManejadorXMLConfig.obtenerValorConfig(filePath, "incidencia"));
			this.fileNameInhibidores = (this.folderPath + 
					ManejadorXMLConfig.obtenerValorConfig(filePath, "inhibidores"));
			this.fileNameMarcado = (this.folderPath + 
					ManejadorXMLConfig.obtenerValorConfig(filePath, "marcado"));
			this.fileNameRelacion = (this.folderPath + 
					ManejadorXMLConfig.obtenerValorConfig(filePath, "relacion"));
			this.fileNameAutomaticos = (this.folderPath + 
					ManejadorXMLConfig.obtenerValorConfig(filePath, "automaticos"));
			this.fileNameCotas = (this.folderPath + 
					ManejadorXMLConfig.obtenerValorConfig(filePath, "cotas"));
			this.fileNamePrioridades = (this.folderPath + 
					ManejadorXMLConfig.obtenerValorConfig(filePath, "prioridades"));
			this.fileNamePrioridadesDistribuidas = (this.folderPath + 
					ManejadorXMLConfig.obtenerValorConfig(filePath, "prioridadesDistribuidas"));
		}
		catch (Exception e)
		{
			throw new Exception("Error leyendo archivos de configuracion",e);
//			e.printStackTrace();
//			System.exit(1);
		}
		try
		{
			this.ipCore = buildIPCoreHPN(cantidadTransiciones);
		}
		catch (Exception e)
		{
			throw new Exception("Error generando el IP Core",e);
		}
		new Thread(this.ipCore).start();
	}

	private IPCoreHPN buildIPCoreHPN(int cantidadTransiciones)
			throws Exception
	{
		int i = 0;
		IPCoreHPN ipCore = null;

		ipCore = new IPCoreHPN(ManejadorTXT.getMatrix(this.fileNamePrioridadesDistribuidas), cantidadTransiciones);

		File fileIncidencia = new File(this.fileNameIncidencia.replace("X", String.valueOf(i)));
		File fileinhibidores = new File(this.fileNameInhibidores.replace("X", String.valueOf(i)));
		File fileMarcado = new File(this.fileNameMarcado.replace("X", String.valueOf(i)));
		File fileRelacion = new File(this.fileNameRelacion.replace("X", String.valueOf(i)));
		File fileCotas = new File(this.fileNameCotas.replace("X", String.valueOf(i)));
		File fileAutomaticos = new File(this.fileNameAutomaticos.replace("X", String.valueOf(i)));
		File filePrioridades = new File(this.fileNamePrioridades.replace("X", String.valueOf(i)));
		while ((fileIncidencia.exists()) && (fileMarcado.exists()) && (fileRelacion.exists()) && 
				(fileinhibidores.exists()) &&(fileCotas.exists()) && (fileAutomaticos.exists()) && 
				(filePrioridades.exists()))
		{
			PetriNet petriNet = PetriNet.createPetriNet(this.fileNameIncidencia.replace("X", String.valueOf(i)), 
					this.fileNameMarcado.replace("X", String.valueOf(i)), 
					this.fileNameRelacion.replace("X", String.valueOf(i)), 
					this.fileNameCotas.replace("X", String.valueOf(i)), 
					this.fileNameAutomaticos.replace("X", String.valueOf(i)), 
					this.fileNamePrioridades.replace("X", String.valueOf(i)),
					this.fileNameInhibidores.replace("X", String.valueOf(i)));

			ipCore.addPetriNet(petriNet);

			i++;
			fileIncidencia = new File(this.fileNameIncidencia.replace("X", String.valueOf(i)));
			fileMarcado = new File(this.fileNameMarcado.replace("X", String.valueOf(i)));
			fileRelacion = new File(this.fileNameRelacion.replace("X", String.valueOf(i)));
			fileCotas = new File(this.fileNameCotas.replace("X", String.valueOf(i)));
			fileAutomaticos = new File(this.fileNameAutomaticos.replace("X", String.valueOf(i)));
			filePrioridades = new File(this.fileNamePrioridades.replace("X", String.valueOf(i)));
		}
		return ipCore;
	}

	private String obtenerFolderPath(String original)
	{
		return FilenameUtils.separatorsToSystem(original).substring(0, FilenameUtils.separatorsToSystem(original).lastIndexOf(FilenameUtils.separatorsToSystem("/"))+1);

		//     StringTokenizer token = new StringTokenizer(original, "\\");
		//     String path = new String("");
		//     while (token.countTokens() > 1) {
		//       path = path + token.nextToken() + "\\";
		//     }
		//     return path;
	}

	public boolean consultarDisparoTransicion(int transicion)
	{
		return this.ipCore.getColaSalida().desencolar(transicion);
	}

	public void encolar(int subred, int transicion)
	{
		this.ipCore.encolarDisparo(0, transicion);
	}
}
