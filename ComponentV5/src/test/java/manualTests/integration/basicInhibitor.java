package manualTests.integration;

import java.net.URISyntaxException;

import org.apache.commons.io.FilenameUtils;

import com.lac.petrinet.components.Dummy;
import com.lac.petrinet.configuration.providers.ConfigurationFileTest;
import com.lac.petrinet.configuration.providers.PNMLConfigurationReader;
import com.lac.petrinet.core.PetriNet;
import com.lac.petrinet.exceptions.PetriNetException;

public class basicInhibitor {
	public static class Buffer {
		private int[] buf;
		private int actualIndex;
		
		Buffer(int size) {
			this.buf = new int[3];
			this.actualIndex = 0;
		}
		
		void putItem(int item) {
			System.out.println("putItem -> " + actualIndex);
			this.buf[actualIndex] = item;
			actualIndex++;
			return;
		}
		
		int getItem() {
			actualIndex--;
			System.out.println("getItem -> " + actualIndex);
			int item = this.buf[actualIndex];
			return item;
		}
	}
	
	public static class Consumidor1 extends Dummy {
		
		protected Consumidor1(String tName) {
			super(tName);
		}

		@Override
		protected void execute() {
			System.out.println("Consumidor1 - Disparada correcta");
			return;
		}
	}
	
	public static class Consumidor2 extends Dummy {
		
		protected Consumidor2(String tName) {
			super(tName);
		}

		@Override
		protected void execute() {
			System.out.println("Consumidor2 - Disparada INHIBIDA !!");
			return;
		}
	}
	
	
	public static void main(String[] args) {
		PNMLConfigurationReader pnmlConfigurator = new PNMLConfigurationReader();

		try {
			PetriNet pn = pnmlConfigurator.loadConfiguration( FilenameUtils.separatorsToSystem(getJarpath() + "pnml/inhibitorBasic.pnml"));
			
			Consumidor1 c1 = new Consumidor1("t9");
			Consumidor2 c2 = new Consumidor2("t8");
			
			pn.assignDummy("t7", c1);
			pn.assignDummy("t6", c2);
			
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
