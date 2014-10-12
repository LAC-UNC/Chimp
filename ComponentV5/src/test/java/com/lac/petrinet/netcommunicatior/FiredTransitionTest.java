package com.lac.petrinet.netcommunicatior;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.testng.annotations.Test;

import com.lac.petrinet.netcommunicator.FiredTransition;
import com.lac.petrinet.netcommunicator.ProcessorHandler;

public class FiredTransitionTest {
	
	private ProcessorHandler mockedProcessor = mock(ProcessorHandler.class);
	
	@Test
	public void fireTransitionOnRealProcessorWhenCommunicate() {
		FiredTransition ft = new FiredTransition(mockedProcessor, 89);
		ft.communicate();
		verify(mockedProcessor).fire(89);
	}
}
