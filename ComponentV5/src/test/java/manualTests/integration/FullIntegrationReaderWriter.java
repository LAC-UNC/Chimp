package manualTests.integration;

import java.net.URISyntaxException;

import com.lac.petrinet.components.Dummy;
import com.lac.petrinet.configuration.providers.ConfigurationFileTest;
import com.lac.petrinet.configuration.providers.PNMLConfigurationReader;
import com.lac.petrinet.core.PetriNet;
import com.lac.petrinet.exceptions.PetriNetException;

public class FullIntegrationReaderWriter {

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
	
	public static class Reader extends Dummy {
		private Buffer b;
		
		public Reader(PetriNet pn, String tName, Buffer buffer){
			super(pn, tName);
			this.b = buffer;
		}
		
		@Override
		protected void execute() {
			System.out.println("----READER----");
			System.out.println("READ: " + this.b.getItem());
			
			return;
		}
	}
	
	public static class Writer extends Dummy {
		private Buffer b;
		private int counter;
		
		public Writer(PetriNet pn, String tName, Buffer buffer){
			super(pn, tName);
			this.b = buffer;
			this.counter = 0;
		}
		
		@Override
		protected void execute() {
			System.out.println("----WRITER----");
			this.b.putItem(this.counter);
			System.out.println("WRITE: " + this.counter);
			this.counter++;
			
			return;
		}
	}
	
	public static void main(String[] args) {
		PNMLConfigurationReader pnmlConfigurator = new PNMLConfigurationReader();
		Buffer sharedBuffer = new FullIntegrationReaderWriter.Buffer(3); //Shared resource

		try {
			PetriNet pn = pnmlConfigurator.loadConfiguration(getJarpath() + "pnml/readerWriter.pnml");
			
			FullIntegrationReaderWriter.Reader r1 = new FullIntegrationReaderWriter.Reader(pn,"t2", sharedBuffer);
			FullIntegrationReaderWriter.Writer w1 = new FullIntegrationReaderWriter.Writer(pn,"t1", sharedBuffer);
			
			pn.assignDummy("t0", w1);
			pn.assignDummy("t3", r1);
			
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
