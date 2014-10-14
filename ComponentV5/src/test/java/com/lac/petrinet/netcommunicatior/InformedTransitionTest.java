package com.lac.petrinet.netcommunicatior;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.testng.annotations.Test;

import com.lac.petrinet.netcommunicator.InformedTransition;
import com.lac.petrinet.netcommunicator.ProcessorHandler;
import com.lac.petrinet.commonfake.DummyClass;

import static org.testng.AssertJUnit.*;


public class InformedTransitionTest {
	
	private ProcessorHandler mockedProcessor = mock(ProcessorHandler.class);
	private ExecutorService threadPool = Executors.newCachedThreadPool();
	
	@Test
	public void addDummyToTransition() {
		InformedTransition it = new InformedTransition(mockedProcessor, 7170, threadPool);
		DummyClass dumb = new DummyClass("someTransition");
		
		assertFalse(it.contains(dumb));
		it.addDummy(dumb);
		assertTrue(it.contains(dumb));
	}
	
	@Test
	public void transitionShouldListenTheProcessorWhenCommunicate() {
		InformedTransition it = new InformedTransition(mockedProcessor, 717, threadPool);
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
		InformedTransition it = new InformedTransition(mockedProcessor, 7, threadPool);
		DummyClass dumb = new DummyClass("someTransition");
		
		when(mockedProcessor.listen(7)).thenReturn(true);
		
		it.addDummy(dumb);
		it.communicate();
		
		verify(dumb.mockedResource).SomeResourceAction();
	}
}