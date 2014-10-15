package manualTests.integration;

import java.net.URISyntaxException;

import com.lac.petrinet.configuration.providers.ConfigurationFileTest;
import com.lac.petrinet.configuration.providers.PNMLConfigurationReader;
import com.lac.petrinet.core.PetriNet;
import com.lac.petrinet.exceptions.PetriNetException;

public class FullIntegrationSimplePetriNet {

	public static void main(String[] args) {
		PNMLConfigurationReader pnmlConfigurator = new PNMLConfigurationReader();
		try {
			PetriNet pn = pnmlConfigurator.loadConfiguration(getJarpath() + "pnml/simplePnml.pnml");
		} catch (PetriNetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static String getJarpath() throws URISyntaxException {
		final String uri;
		uri = ConfigurationFileTest.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
		return uri;
	}

}
