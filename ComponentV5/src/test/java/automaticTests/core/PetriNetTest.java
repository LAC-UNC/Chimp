package automaticTests.core;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;
import static org.testng.AssertJUnit.assertSame;

import org.testng.annotations.Test;

import com.lac.petrinet.commonfake.DummyClass;
import com.lac.petrinet.core.PetriNet;
import com.lac.petrinet.netcommunicator.FiredTransition;
import com.lac.petrinet.netcommunicator.InformedTransition;
import com.lac.petrinet.netcommunicator.ProcessorHandler;

public class PetriNetTest {
	
	private InformedTransition itmocked = mock(InformedTransition.class);
	private FiredTransition ftmocked = mock(FiredTransition.class);
	private ProcessorHandler mockedProcessor = mock(ProcessorHandler.class);
	
	@Test
	public void addInformedTransitionToPetriNet() {
		PetriNet p = new PetriNet();
		
		p.addInformed("someInformed", itmocked);
		assertSame(p.getInformed("someInformed"), itmocked) ;
	}
	
	@Test
	public void addFiredTransitionToPetriNet() {
		PetriNet p = new PetriNet();
		
		p.addFired("someFired", ftmocked);
		assertSame(p.getFired("someFired"), ftmocked) ;
	}
	
	@Test
	public void fireAFiredFromPetriNet() {
		PetriNet p = new PetriNet();
		
		//I mock the processor instead the transition to make the test the most implementation agnostic as possible
		p.addFired("someFired", new FiredTransition(mockedProcessor, 213));
		p.fire("someFired");
		
		verify(mockedProcessor).fire(213);
	}
	
	@Test
	public void assignDummyToInformedFromPetriNet() {
		PetriNet p = new PetriNet();
		InformedTransition it = new InformedTransition(mockedProcessor, 216);
		DummyClass dumb = new DummyClass(p,"someFired");
		
		p.addInformed("someInformed", it);
		p.addFired("someFired", new FiredTransition(mockedProcessor, 27));
		
		assertFalse(it.contains(dumb));
		p.assignDummy("someInformed", dumb);
		assertTrue(it.contains(dumb));
	}
	
	@Test
	public void startListeningShouldListenToTheTransitionOnTheProcessor() {
		PetriNet p = new PetriNet();
		InformedTransition it1 = new InformedTransition(mockedProcessor, 1);
		InformedTransition it2 = new InformedTransition(mockedProcessor, 2);
		InformedTransition it3 = new InformedTransition(mockedProcessor, 3);
		FiredTransition ft1 = new FiredTransition(mockedProcessor, 11);
		FiredTransition ft2 = new FiredTransition(mockedProcessor, 12);
		FiredTransition ft3 = new FiredTransition(mockedProcessor, 13);
		DummyClass dumb1 = new DummyClass(p,"someFired11");
		DummyClass dumb2 = new DummyClass(p,"someFired12");
		DummyClass dumb3 = new DummyClass(p,"someFired13");
		
		p.addInformed("someInformed1", it1);
		p.addInformed("someInformed2", it2);
		p.addInformed("someInformed3", it3);
		p.addFired("someFired1", ft1);
		p.addFired("someFired2", ft2);
		p.addFired("someFired3", ft3);
		
		p.assignDummy("someInformed1", dumb1);
		p.assignDummy("someInformed1", dumb2);
		p.assignDummy("someInformed1", dumb3);
		
		p.startListening(1);
		
		verify(mockedProcessor).listen(1);
		verify(mockedProcessor).listen(2);
		verify(mockedProcessor).listen(3);
	}
}
