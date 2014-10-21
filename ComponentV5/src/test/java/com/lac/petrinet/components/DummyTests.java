package com.lac.petrinet.components;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.lac.petrinet.components.Dummy;
import com.lac.petrinet.core.PetriNet;
import com.lac.petrinet.exceptions.PetriNetException;

import static org.mockito.Mockito.*;

public class DummyTests {
	//mock creation
	private PetriNet mockedPN = mock(PetriNet.class);

	public class DummyClass extends Dummy {
		public DummyClass(String tName) throws PetriNetException{
			super(DummyTests.this.mockedPN, tName);
		}
		@Override
		protected void execute() {
			return;
		}
	}
	
	@BeforeMethod
	public void PnMocker(){
		when(mockedPN.containFired("SomeTransition")).thenReturn(true);
	}
	
	@Test
	public void fireTransitionAfterExecutionTest() throws Exception{
		DummyClass dummyObj = new DummyClass("SomeTransition");

		dummyObj.call();
		verify(mockedPN).fire("SomeTransition");
	}
}
