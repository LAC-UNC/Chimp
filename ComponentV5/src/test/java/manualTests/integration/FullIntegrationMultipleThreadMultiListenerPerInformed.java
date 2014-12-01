package manualTests.integration;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import com.lac.petrinet.components.Dummy;
import com.lac.petrinet.configuration.providers.ConfigurationFileTest;
import com.lac.petrinet.configuration.providers.PNMLConfigurationReader;
import com.lac.petrinet.core.PetriNet;
import com.lac.petrinet.exceptions.PetriNetException;
import com.lac.petrinet.netcommunicator.InformedTransition;


public class FullIntegrationMultipleThreadMultiListenerPerInformed {

	public static class Talker extends Dummy {
		private String msg;
		
		public Talker(String tName, String message) throws PetriNetException{
			super(tName);
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

			FullIntegrationMultipleThreadMultiListenerPerInformed.Talker dumb0 = new FullIntegrationMultipleThreadMultiListenerPerInformed.Talker("t1", "----START----");
			FullIntegrationMultipleThreadMultiListenerPerInformed.Talker dumb1 = new FullIntegrationMultipleThreadMultiListenerPerInformed.Talker("t3", "Dumb1");
			FullIntegrationMultipleThreadMultiListenerPerInformed.Talker dumb2 = new FullIntegrationMultipleThreadMultiListenerPerInformed.Talker("t4", "Dumb2");
			FullIntegrationMultipleThreadMultiListenerPerInformed.Talker dumb3 = new FullIntegrationMultipleThreadMultiListenerPerInformed.Talker("t5", "Dumb3");
			
			List<List<InformedTransition>> transitionListForListeners = new ArrayList<List<InformedTransition>>();
			List<InformedTransition> t0List = new ArrayList<InformedTransition>();
			t0List.add(pn.getInformed("t0"));
			List<InformedTransition> t1List = new ArrayList<InformedTransition>();
			t0List.add(pn.getInformed("t1"));
			transitionListForListeners.add(t0List);
			transitionListForListeners.add(t1List);
			
			
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
