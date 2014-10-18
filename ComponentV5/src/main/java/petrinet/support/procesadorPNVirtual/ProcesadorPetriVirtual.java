 package petrinet.support.procesadorPNVirtual;
 
 import java.io.File;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import petrinet.support.IOFiles.ManejadorTXT;
import petrinet.support.IOFiles.ManejadorXMLConfig;
import petrinet.support.Petri.IPCoreHPN;
import petrinet.support.Petri.PetriNet;
import petrinet.support.procesador.ProcesadorPetri;
 
 public class ProcesadorPetriVirtual
   implements ProcesadorPetri
 {
   private static final String INCIDENCIA = "incidencia";
   private static final String MARCADO = "marcado";
   private static final String RELACION = "relacion";
   private static final String COTAS = "cotas";
   private static final String AUTOMATICOS = "automaticos";
   private static final String PRIORIDADES = "prioridades";
   private static final String PRIORIDADESDISTRIBUIDAS = "prioridadesDistribuidas";
   private String fileIncidencia;
   private String fileMarcado;
   private String fileRelacion;
   private String fileCotas;
   private String fileAutomaticos;
   private String filePrioridades;
   private String filePrioridadesDistribuidas;
   private String folderPath;
   IPCoreHPN ipCore = null;
   
   public ProcesadorPetriVirtual(String filePath, int cantidadTransiciones)
   {
     this.folderPath = obtenerFolderPath(filePath);
     try
     {
       this.fileIncidencia = (this.folderPath + 
         ManejadorXMLConfig.obtenerValorConfig(filePath, "incidencia"));
       this.fileMarcado = (this.folderPath + 
         ManejadorXMLConfig.obtenerValorConfig(filePath, "marcado"));
       this.fileRelacion = (this.folderPath + 
         ManejadorXMLConfig.obtenerValorConfig(filePath, "relacion"));
       this.fileAutomaticos = (this.folderPath + 
         ManejadorXMLConfig.obtenerValorConfig(filePath, "automaticos"));
       this.fileCotas = (this.folderPath + 
         ManejadorXMLConfig.obtenerValorConfig(filePath, "cotas"));
       this.filePrioridades = (this.folderPath + 
         ManejadorXMLConfig.obtenerValorConfig(filePath, "prioridades"));
       this.filePrioridadesDistribuidas = (this.folderPath + 
         ManejadorXMLConfig.obtenerValorConfig(filePath, "prioridadesDistribuidas"));
     }
     catch (Exception e)
     {
       JOptionPane.showMessageDialog(null, "No se cargaron los path de los archivos de configuracion", 
       
         "ERROR", 0);
       System.exit(1);
     }
     try
     {
       this.ipCore = buildIPCoreHPN(cantidadTransiciones);
     }
     catch (Exception e)
     {
       JOptionPane.showMessageDialog(null, "Error generando el IP Core", 
         "ERROR", 0);
       System.exit(1);
     }
     new Thread(this.ipCore).start();
   }
   
   private IPCoreHPN buildIPCoreHPN(int cantidadTransiciones)
     throws Exception
   {
     int i = 0;
     IPCoreHPN ipCore = null;
     
     ipCore = new IPCoreHPN(ManejadorTXT.getMatrix(this.filePrioridadesDistribuidas), cantidadTransiciones);
     
     File fileIncidencia = new File(this.fileIncidencia.replace("X", String.valueOf(i)));
     File fileMarcado = new File(this.fileMarcado.replace("X", String.valueOf(i)));
     File fileRelacion = new File(this.fileRelacion.replace("X", String.valueOf(i)));
     File fileCotas = new File(this.fileCotas.replace("X", String.valueOf(i)));
     File fileAutomaticos = new File(this.fileAutomaticos.replace("X", String.valueOf(i)));
     File filePrioridades = new File(this.filePrioridades.replace("X", String.valueOf(i)));
     while ((fileIncidencia.exists()) && (fileMarcado.exists()) && (fileRelacion.exists()) && 
       (fileCotas.exists()) && (fileAutomaticos.exists()) && (filePrioridades.exists()))
     {
       PetriNet petriNet = PetriNet.createPetriNet(this.fileIncidencia.replace("X", String.valueOf(i)), 
         this.fileMarcado.replace("X", String.valueOf(i)), 
         this.fileRelacion.replace("X", String.valueOf(i)), 
         this.fileCotas.replace("X", String.valueOf(i)), 
         this.fileAutomaticos.replace("X", String.valueOf(i)), 
         this.filePrioridades.replace("X", String.valueOf(i)));
       
       ipCore.addPetriNet(petriNet);
       
       i++;
       fileIncidencia = new File(this.fileIncidencia.replace("X", String.valueOf(i)));
       fileMarcado = new File(this.fileMarcado.replace("X", String.valueOf(i)));
       fileRelacion = new File(this.fileRelacion.replace("X", String.valueOf(i)));
       fileCotas = new File(this.fileCotas.replace("X", String.valueOf(i)));
       fileAutomaticos = new File(this.fileAutomaticos.replace("X", String.valueOf(i)));
       filePrioridades = new File(this.filePrioridades.replace("X", String.valueOf(i)));
     }
     return ipCore;
   }
   
   private String obtenerFolderPath(String original)
   {
     StringTokenizer token = new StringTokenizer(original, "\\");
     String path = new String("");
     while (token.countTokens() > 1) {
       path = path + token.nextToken() + "\\";
     }
     return path;
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
