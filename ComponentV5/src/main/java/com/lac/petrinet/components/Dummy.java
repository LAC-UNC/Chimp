package com.lac.petrinet.components;

import java.util.concurrent.Callable;

import com.lac.petrinet.core.PetriNet;

public abstract class Dummy implements Callable<Void> {
	
	private String transitionName;
	private PetriNet petriNet;  
	
	abstract protected void execute();
	
	@Override
	public Void call() throws Exception {
		throw new UnsupportedOperationException();
	}

	public void setTransitionName(String transitionName) {
		this.transitionName = transitionName;
	}

	public void setPetriNet(PetriNet petriNet) {
		this.petriNet = petriNet;
	}
}
