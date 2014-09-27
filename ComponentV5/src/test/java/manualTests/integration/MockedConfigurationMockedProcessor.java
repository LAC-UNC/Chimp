package manualTests.integration;

import static org.mockito.Mockito.mock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lac.petrinet.commonfake.DummyClass;
import com.lac.petrinet.components.Dummy;
import com.lac.petrinet.core.PetriNet;
import com.lac.petrinet.netcommunicator.FiredTransition;
import com.lac.petrinet.netcommunicator.InformedTransition;
import com.lac.petrinet.netcommunicator.ProcessorHandler;

public class MockedConfigurationMockedProcessor {
	public static int sharedCounter = 0; //Shared resource
	public static String last = ""; //Shared resource
	
	private static ProcessorHandler mockedProcessor = mock(ProcessorHandler.class);
	
	public static class Counter extends Dummy {
		private String msg;
		
		public Counter(PetriNet pn, String tName, String message){
			super(pn, tName);
			this.msg = message;
		}
		
		@Override
		protected void execute() {
			MockedConfigurationMockedProcessor.sharedCounter++;
			MockedConfigurationMockedProcessor.last = this.msg;
			System.out.println(this.msg);
			return;
		}
	}
	
	public static void main() {
		//This is some similar to what a Chimp user can do.
		PetriNet pn = loadConfiguration();
		
		MockedConfigurationMockedProcessor.Counter dumb1 = new MockedConfigurationMockedProcessor.Counter(pn,"someFired11", "OneDummy");
		MockedConfigurationMockedProcessor.Counter dumb2 = new MockedConfigurationMockedProcessor.Counter(pn,"someFired12", "OtherDummy");
		MockedConfigurationMockedProcessor.Counter dumb3 = new MockedConfigurationMockedProcessor.Counter(pn,"someFired13", "TheBestDummy");
			
		pn.assignDummy("someInformed1", dumb1);
		pn.assignDummy("someInformed2", dumb2);
		pn.assignDummy("someInformed3", dumb3);
		
		pn.startListening(3);
		
		
	}
	
	public static PetriNet loadConfiguration() {
		PetriNet p = new PetriNet();
		ExecutorService threadPool = Executors.newCachedThreadPool();
		
		InformedTransition it1 = new InformedTransition(MockedConfigurationMockedProcessor.mockedProcessor, 1, threadPool);
		InformedTransition it2 = new InformedTransition(MockedConfigurationMockedProcessor.mockedProcessor, 2, threadPool);
		InformedTransition it3 = new InformedTransition(MockedConfigurationMockedProcessor.mockedProcessor, 3, threadPool);
		FiredTransition ft1 = new FiredTransition(MockedConfigurationMockedProcessor.mockedProcessor, 11);
		FiredTransition ft2 = new FiredTransition(MockedConfigurationMockedProcessor.mockedProcessor, 12);
		FiredTransition ft3 = new FiredTransition(MockedConfigurationMockedProcessor.mockedProcessor, 13);
		
		p.addInformed("someInformed1", it1);
		p.addInformed("someInformed2", it2);
		p.addInformed("someInformed3", it3);
		p.addFired("someFired1", ft1);
		p.addFired("someFired2", ft2);
		p.addFired("someFired3", ft3);
		
		return p;
	}
}
