package automaticTests.netcommunicatior;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.concurrent.ExecutorService;

import org.testng.annotations.Test;


import com.lac.petrinet.components.Dummy;
import com.lac.petrinet.core.PetriNet;
import com.lac.petrinet.netcommunicator.InformedTransition;
import com.lac.petrinet.netcommunicator.ProcessorHandler;

import static org.testng.AssertJUnit.*;


public class InformedTransitionTest {
	
	private ProcessorHandler mockedProcessor = mock(ProcessorHandler.class);
	
	private class SomeResource {
		public void SomeResourceAction() {
			return;
		}
		
		public void SomeResourceOtherAction() {
			return;
		}
	}
	
	private SomeResource mockedResource = mock(SomeResource.class);
	
	private class DummyClass extends Dummy {
		public DummyClass(String tName){
			super(mock(PetriNet.class), tName);
		}
		@Override
		protected void execute() {
			mockedResource.SomeResourceAction();
			return;
		}
	}
	
	@SuppressWarnings("unchecked")
	private Collection<Dummy> getDummiesFromInformedTransition(InformedTransition obj) {
		Field field;
		try {
			field = InformedTransition.class.getDeclaredField("dummies");
			field.setAccessible(true);
			return (Collection<Dummy>) field.get(obj);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Test
	public void addDummyToTransition() {
		InformedTransition it = new InformedTransition(mockedProcessor, 7170);
		DummyClass dumb = new DummyClass("someTransition");
		
		assertFalse(this.getDummiesFromInformedTransition(it).contains(dumb));
		it.addDummy(dumb);
		assertTrue(this.getDummiesFromInformedTransition(it).contains(dumb));
	}
	
	@Test
	public void transitionShouldListenTheProcessorWhenCommunicate() {
		InformedTransition it = new InformedTransition(mockedProcessor, 717);
		it.communicate();
		verify(mockedProcessor).listen(717);
	}
	
	/*@Test
	public void transitionShouldNotExecuteTaskWhenIsNotTriggered() {
		InformedTransition it = new InformedTransition(mockedProcessor, 71);
		DummyClass dumb = new DummyClass("someTransition");
		
		when(mockedProcessor.listen(71)).thenReturn(false);
		
		it.addDummy(dumb);
		it.communicate();
		
		verify(mockedResource, never()).SomeResourceAction();
	}*/
	
	@Test
	public void transitionShouldExecuteTaskWhenIsTriggered() {
		InformedTransition it = new InformedTransition(mockedProcessor, 7);
		DummyClass dumb = new DummyClass("someTransition");
		
		when(mockedProcessor.listen(7)).thenReturn(true);
		
		it.addDummy(dumb);
		it.communicate();
		
		verify(mockedResource).SomeResourceAction();
	}
}
