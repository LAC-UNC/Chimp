package manualTests.integration;

import java.net.URISyntaxException;

import com.lac.petrinet.components.Dummy;
import com.lac.petrinet.configuration.providers.ConfigurationFileTest;
import com.lac.petrinet.configuration.providers.PNMLConfigurationReader;
import com.lac.petrinet.core.PetriNet;
import com.lac.petrinet.exceptions.PetriNetException;

public class FullIntegrationMultipleThreadPerInformed {

	public static class Talker extends Dummy {
		private String msg;
		
		public Talker(PetriNet pn, String tName, String message){
			super(pn, tName);
			this.msg = message;
		}
		
		@Override
		protected void execute() {
			System.out.println(this.msg);
			
			return;
		}
	}
	
	public static void main(String[] args) {
		PNMLConfigurationReader pnmlConfigurator = new PNMLConfigurationReader();
		

		try {
			PetriNet pn = pnmlConfigurator.loadConfiguration(getJarpath() + "pnml/multipleThreadPerInformedBucledWithDI.pnml");
			
			FullIntegrationMultipleThreadPerInformed.Talker dumb0 = new FullIntegrationMultipleThreadPerInformed.Talker(pn,"t1", "----START----");
			FullIntegrationMultipleThreadPerInformed.Talker dumb1 = new FullIntegrationMultipleThreadPerInformed.Talker(pn,"t3", "Dumb1");
			FullIntegrationMultipleThreadPerInformed.Talker dumb2 = new FullIntegrationMultipleThreadPerInformed.Talker(pn,"t4", "Dumb2");
			FullIntegrationMultipleThreadPerInformed.Talker dumb3 = new FullIntegrationMultipleThreadPerInformed.Talker(pn,"t5", "Dumb3");
			
			pn.assignDummy("t0", dumb0);
			pn.assignDummy("t1", dumb1);
			pn.assignDummy("t1", dumb2);
			pn.assignDummy("t1", dumb3);
			
			pn.startListening();
			/*pn.nextCicle();
			pn.nextCicle();
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
