package manualTests.integration;

import java.net.URISyntaxException;

import com.lac.petrinet.components.Dummy;
import com.lac.petrinet.configuration.providers.ConfigurationFileTest;
import com.lac.petrinet.configuration.providers.PNMLConfigurationReader;
import com.lac.petrinet.core.PetriNet;
import com.lac.petrinet.exceptions.PetriNetException;

public class FullIntegrationSimplePetriNet {

	public static int sharedCounter = 0; //Shared resource
	public static String last = ""; //Shared resource
	
	public static class Counter extends Dummy {
		private String msg;
		
		public Counter(PetriNet pn, String tName, String message){
			super(pn, tName);
			this.msg = message;
		}
		
		@Override
		protected void execute() {
			FullIntegrationSimplePetriNet.sharedCounter++;
			FullIntegrationSimplePetriNet.last = this.msg;
			System.out.println(this.msg);
			
			return;
		}
	}
	
	public static void main(String[] args) {
		PNMLConfigurationReader pnmlConfigurator = new PNMLConfigurationReader();
		

		try {
			PetriNet pn = pnmlConfigurator.loadConfiguration(getJarpath() + "pnml/simplePnml.pnml");
			System.out.println(pn.getInformed("t0").toString());
			
			FullIntegrationSimplePetriNet.Counter dumb1 = new FullIntegrationSimplePetriNet.Counter(pn,"t1", "Dumb1");
			FullIntegrationSimplePetriNet.Counter dumb2 = new FullIntegrationSimplePetriNet.Counter(pn,"t2", "Dumb2");
			
			pn.assignDummy("t0", dumb1);
			pn.assignDummy("t1", dumb2);
			
			pn.nextCicle();
			pn.nextCicle();
			pn.nextCicle();
			pn.nextCicle();
			pn.nextCicle();
			pn.nextCicle();
			pn.nextCicle();
			pn.nextCicle();
			pn.nextCicle();
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
