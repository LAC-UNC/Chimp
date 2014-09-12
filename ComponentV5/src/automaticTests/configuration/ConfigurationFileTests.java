package automaticTests.configuration;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.lac.petrinet.configuration.PNData;
import com.lac.petrinet.configuration.providers.PNMLConfigurationReader;
import com.lac.petrinet.exceptions.PetriNetException;

public class ConfigurationFileTests {

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
	
	@BeforeSuite
	private void pathAndFolderConfiguration() throws URISyntaxException, IOException{
		// source of the sample pnml
		pnmlFilePath = getJarpath() + "/automaticTests/resources/pnml/barcos-conContrainst.pnml";
		// destination folder of the new created files. 		
		Path tempFolderPath = Files.createTempDirectory("ConfigTempFolder");
		tempFolder = tempFolderPath.toFile();
		tempFolder.deleteOnExit();
		configurationFilePath = tempFolder.getPath();
		
	}
	
	@Test
	public void configurationFileCreatingTest() throws PetriNetException {
		
		// creation of the configuration files
		PNData pnData = new PNData();
		pnData.cargarRed(pnmlFilePath);
		PNMLConfigurationReader pnmlConfigurator = new PNMLConfigurationReader();
		pnmlConfigurator.generarArchivosConfiguracion(pnData, configurationFilePath);
		
		// test if the files are created
		for(String fileName : fileNames){
			Assert.assertTrue((new File(tempFolder.getPath()+fileName)).exists());
		}
	}
	
	@Test
	public void configurationFileContentTest() throws IOException, URISyntaxException{
		// creation of the configuration files
		PNData pnData = new PNData();
		pnData.cargarRed(pnmlFilePath);
		PNMLConfigurationReader pnmlConfigurator = new PNMLConfigurationReader();
		pnmlConfigurator.generarArchivosConfiguracion(pnData, configurationFilePath);
		
		for(String fileName : fileNames){
			String actualString = FileUtils.readFileToString((new File(tempFolder.getPath()+ fileName)));
			String expectedString = FileUtils.readFileToString(new File( getJarpath() + "automaticTests/resources" + fileName));
			Assert.assertEquals(actualString, expectedString);
		}
	}

	private String getJarpath() throws URISyntaxException {
		final String uri;
		uri = ConfigurationFileTests.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
		return uri;
	}
	
}
