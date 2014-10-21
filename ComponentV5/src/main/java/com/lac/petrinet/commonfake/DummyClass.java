package com.lac.petrinet.commonfake;

import static org.mockito.Mockito.mock;

import com.lac.petrinet.components.Dummy;
import com.lac.petrinet.core.PetriNet;
import com.lac.petrinet.exceptions.PetriNetException;

public class DummyClass extends Dummy {
	public class SomeResource {
		public void SomeResourceAction() {
			return;
		}
		
		public void SomeResourceOtherAction() {
			return;
		}
	}
	
	public SomeResource mockedResource = mock(SomeResource.class);
	
	public DummyClass(String tName) throws PetriNetException{
		super(mock(PetriNet.class), tName);
	}
	
	public DummyClass(PetriNet pn, String tName) throws PetriNetException{
		super(pn, tName);
	}
	
	@Override
	protected void execute() {
		mockedResource.SomeResourceAction();
		return;
	}
}