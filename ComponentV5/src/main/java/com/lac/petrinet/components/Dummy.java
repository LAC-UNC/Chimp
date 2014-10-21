package com.lac.petrinet.components;

import java.util.concurrent.Callable;

import com.lac.petrinet.core.PetriNet;
import com.lac.petrinet.exceptions.PetriNetException;

public abstract class Dummy implements Callable<Void> {
	
	protected String transitionName;
	private PetriNet petriNet;  
	
	abstract protected void execute() throws PetriNetException;
	
	protected Dummy(PetriNet pn, String tName) throws PetriNetException{
		this.petriNet = pn;
		if(pn.containFired(tName))
			this.transitionName = tName;
		else
			throw new PetriNetException("There is no fired transition named: " + tName);
	}
	
	@Override
	public Void call() throws PetriNetException {
		execute();
		petriNet.fire(transitionName);
		return null;
	}

	public void setTransitionName(String transitionName) {
		this.transitionName = transitionName;
	}

	public void setPetriNet(PetriNet petriNet) {
		this.petriNet = petriNet;
	}
}
