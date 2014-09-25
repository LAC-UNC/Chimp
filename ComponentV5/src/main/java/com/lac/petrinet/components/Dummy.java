package com.lac.petrinet.components;

import java.util.concurrent.Callable;

import com.lac.petrinet.core.PetriNet;

public abstract class Dummy implements Callable<Void> {
	
	protected String transitionName;
	private PetriNet petriNet;  
	
	abstract protected void execute();
	
	protected Dummy(PetriNet pn, String tName){
		this.petriNet = pn;
		this.transitionName = tName;
	}
	
	@Override
	public Void call() throws Exception {
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
