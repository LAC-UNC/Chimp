package manualTests.integration;

import java.net.URISyntaxException;

import com.lac.petrinet.components.Dummy;
import com.lac.petrinet.configuration.providers.ConfigurationFileTest;
import com.lac.petrinet.configuration.providers.PNMLConfigurationReader;
import com.lac.petrinet.core.PetriNet;
import com.lac.petrinet.exceptions.PetriNetException;

public class FullIntegrationNoMutualExclusion {	
	public static class Talker extends Dummy {	
		protected Talker(String tName) {
			super(tName);
		}

		@Override
		protected void execute() {
			System.out.println("Empece " + Thread.currentThread().getId());
			for(int i = 0; i < 1000000000; i++);
			System.out.println("Termine " + Thread.currentThread().getId());
			return;
		}
	}
	
	public static void main(String[] args) {
		PNMLConfigurationReader pnmlConfigurator = new PNMLConfigurationReader();

		try {
			PetriNet pn = pnmlConfigurator.loadConfiguration(getJarpath() + "pnml/noMutualExclusion.pnml");
			
			FullIntegrationNoMutualExclusion.Talker talker = new FullIntegrationNoMutualExclusion.Talker("t1");
			
			pn.assignDummy("t0", talker);
			
			pn.startListening();
			/*pn.nextCicle();
			pn.nextCicle();
			pn.nextCicle();
			pn.nextCicle();
			pn.nextCicle();
			pn.nextCicle();
			pn.nextCicle();
			pn.nextCicle();*/
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
