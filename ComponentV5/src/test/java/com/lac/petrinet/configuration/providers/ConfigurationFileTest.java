package com.lac.petrinet.configuration.providers;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.lac.petrinet.configuration.PNData;
import com.lac.petrinet.core.PetriNet;
import com.lac.petrinet.exceptions.PetriNetException;
import com.lac.petrinet.netcommunicator.FiredTransition;
import com.lac.petrinet.netcommunicator.InformedTransition;

public class ConfigurationFileTest {

	String configurationFilePath ;
	String pnmlFilePath;
	File tempFolder;
	// Immutable list of files names 
	List<String> fileNames = Arrays.asList(
			"/archivosConfiguracion/automaticos0.txt",
			"/archivosConfiguracion/config.txt",
			"/archivosConfiguracion/cotas0.txt",
			"/archivosConfiguracion/incidencia0.txt",
			"/archivosConfiguracion/marcado0.txt",
			"/archivosConfiguracion/prioridades0.txt",
			"/archivosConfiguracion/prioridadesDistribuidas.txt",
			"/archivosConfiguracion/relacion0.txt",
			"/archivosConfiguracion/sinInforme0.txt"
			);
	
	List<String> informedTransitionsName = Arrays.asList(
			"t0",
			"t1"
			);
	
	List<String> firedTransitionsName = Arrays.asList(
			"t1",
			"t2"
			);
	
	@BeforeSuite
	private void pathAndFolderConfiguration() throws URISyntaxException, IOException{
		// source of the sample pnml
		pnmlFilePath = getJarpath() + "/pnml/simplePnml.pnml";
		// destination folder of the new created files. 		
//		Path tempFolderPath = Files.createTempDirectory("ConfigTempFolder");
//		tempFolder = tempFolderPath.toFile();
//		tempFolder.deleteOnExit();
//		configurationFilePath = tempFolder.getPath();
		
	}
	
	@Test
	public void configurationFileCreatingTest() throws PetriNetException, URISyntaxException {
		
		// creation of the configuration files
		PNMLConfigurationReader pnmlConfigurator = new PNMLConfigurationReader();
		pnmlConfigurator.loadConfiguration(pnmlFilePath);
		
		String configFolderParentPath = getJarpath();
		
		// test if the files are created
		for(String fileName : fileNames){
			Assert.assertTrue((new File( configFolderParentPath + fileName)).exists());
		}
	}
	
	@Test
	public void configurationFileContentTest() throws IOException, URISyntaxException, PetriNetException{
		// creation of the configuration files
		PNMLConfigurationReader pnmlConfigurator = new PNMLConfigurationReader();
		pnmlConfigurator.loadConfiguration(pnmlFilePath);
		
		String configFolderParentPath = getJarpath();
		for(String fileName : fileNames){
			String actualString = FileUtils.readFileToString((new File(configFolderParentPath +  fileName)));
			String expectedString = FileUtils.readFileToString(new File( getJarpath() + fileName));
			Assert.assertEquals(actualString, expectedString);
		}
	}

	@Test
	public void allInformedExistsTest() throws PetriNetException{
		PNMLConfigurationReader pnmlConfigurator = new PNMLConfigurationReader();
		PetriNet petriNet = pnmlConfigurator.loadConfiguration(pnmlFilePath);
		
		for(String transitionName : informedTransitionsName){
			Assert.assertNotNull(petriNet.getInformed(transitionName), "Informed transition doesn't exist for Name: " + transitionName);  
		}
	}
	
	@Test
	public void allFiredExistsTest() throws PetriNetException{
		PNMLConfigurationReader pnmlConfigurator = new PNMLConfigurationReader();
		PetriNet petriNet = pnmlConfigurator.loadConfiguration(pnmlFilePath);
		
		for(String transitionName : firedTransitionsName){
			Assert.assertNotNull(petriNet.getFired(transitionName), "Fired transition doesn't exist for Name: " + transitionName);  
		}
	}
	
	
	@Test
	public void incidenceMatrixPositionRelationTest() throws PetriNetException{
		// This is the the class that has the incidence matrix, and We will test against that matrix the corresponding value
		// of the incidence vector for each Transition
		PNData pnData = new PNData();
		pnData.cargarRed(pnmlFilePath);
		
		// we need to load the configuration in order to have the transitions created and use the id number associated to each of them.
		PNMLConfigurationReader pnmlConfigurator = new PNMLConfigurationReader();
		PetriNet petriNet = pnmlConfigurator.loadConfiguration(pnmlFilePath);
		
		Map<String, int[]> expectedIncidenceMap = getExpectedIncidenceMatrixMap();
		
		for(String transitionName :   expectedIncidenceMap.keySet()){
			
			// checking the fired transitions. 
			FiredTransition fired = petriNet.getFired(transitionName);
			// we don't care if the transition is not a fired one, because there is another test that checks if all the fired exists.
			if(fired != null){
				int idFired = fired.getTransitionId();
				for(int i  = 0 ; i< pnData.getMatrizIncidenciaNegativa().length ; i ++){
					int result = pnData.getMatrizIncidenciaPositiva()[i][idFired] -  pnData.getMatrizIncidenciaNegativa()[i][idFired]  ; 
					Assert.assertEquals(result, expectedIncidenceMap.get(transitionName)[i], "Incidence matrix does not match. "
							+ "Probably transition Id is incorrect. Transition name: " + transitionName+ " , position: " + 
							i);
				}
			}
			// checking the informed transitions.
			InformedTransition informed = petriNet.getInformed(transitionName);
			// we don't care if the transition is not a fired one, because there is another test that checks if all the fired exists.
			if(informed != null){
				int idInformed = informed.getTransitionId();
				for(int i  = 0 ; i< pnData.getMatrizIncidenciaNegativa().length ; i ++){
					int result = pnData.getMatrizIncidenciaPositiva()[i][idInformed] - pnData.getMatrizIncidenciaNegativa()[i][idInformed]  ; 
					Assert.assertEquals(result, expectedIncidenceMap.get(transitionName)[i], "Incidence matrix does not match. "
							+ "Probably transition Id is incorrect.Transition name: " + transitionName + " , position: " + 
							i);
				}
			}
		}
		
		
	}
	
	
	private Map<String, int[]> getExpectedIncidenceMatrixMap(){
		Map<String, int[]> incidenceMatrix = new HashMap<String,int[]>();
		incidenceMatrix.put("SalirCanal1", new int[] {1,0,1,0,0,0,0,0,0,0,-1,0,0,0,0,0,0,0,0,0,0,1});
		incidenceMatrix.put("SalirCanal3",  new int[] {0,1,0,0,1,0,0,0,0,0,0,0,0,-1,0,0,0,0,0,1,0,0});
		incidenceMatrix.put("SolicitarCanal1",  new int[] {0,-1,-1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,-1,-1});
		incidenceMatrix.put("SolicitarCanal1Der",  new int[] {0,0,-1,0,0,0,1,0,0,0,1,0,0,0,0,0,-1,0,0,0,1,0});
		incidenceMatrix.put("SolicitarCanal2",  new int[] {0,0,0,-1,0,1,0,0,0,0,0,1,0,0,0,-1,0,0,0,-1,0,0});
		incidenceMatrix.put("SolicitarCanal2Der",  new int[] {0,0,0,-1,0,0,0,0,1,0,0,0,1,0,0,0,0,0,-1,0,0,-1});
		incidenceMatrix.put("SolicitarCanal3",  new int[] {0,0,0,0,-1,0,0,1,0,0,0,0,0,1,0,0,0,-1,0,0,1,0});
		incidenceMatrix.put("SolicitarCanal3Der",  new int[] {-1,0,0,0,-1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,-1,-1,0});
		incidenceMatrix.put("SolicitarDarsena1",  new int[] {0,0,1,0,0,-1,0,0,0,-1,0,0,0,0,0,1,0,0,0,0,0,0});
		incidenceMatrix.put("SolicitarDarsena2",  new int[] {0,0,0,1,0,0,-1,0,0,0,0,0,-1,0,0,0,1,0,0,1,0,0});
		incidenceMatrix.put("SolicitarDarsena3",  new int[] {0,0,0,1,0,0,0,-1,0,0,0,-1,0,0,0,0,0,1,0,0,0,1});
		incidenceMatrix.put("SolicitarDarsena4",  new int[] {0,0,0,0,1,0,0,0,-1,0,0,0,0,0,-1,0,0,0,1,0,0,0});
		
		return incidenceMatrix;
	}
	
	private String getJarpath() throws URISyntaxException {
		final String uri;
		uri = ConfigurationFileTest.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
		return uri;
	}
	
}
