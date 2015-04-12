package com.lac.petrinet.components;

import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;

import com.lac.petrinet.core.PetriNet;
import com.lac.petrinet.exceptions.PetriNetException;

public abstract class Dummy implements Callable<Void> {
	
	protected String transitionName;
	private PetriNet petriNet; 
	private Semaphore syncronizer;
	
	abstract protected void execute() throws PetriNetException;
	
	protected Dummy(String tName){
		this.transitionName = tName;
		this.petriNet = null;
	}
	
	@Override
	public Void call() throws PetriNetException {
		if(this.petriNet != null) {
			if(this.syncronizer != null){
				this.syncronizer.release();
			}

			execute();
			petriNet.fire(transitionName);
			return null;
		}
		else
			throw new PetriNetException("There is no petrinet assigned for this Dummy");
	}

	public void setPetriNet(PetriNet petriNet) throws PetriNetException {
		if(petriNet.containFired(this.transitionName))
			this.petriNet = petriNet;
		else
			throw new PetriNetException("There is no fired transition named: " + this.transitionName);
	}

	public String getTransitionName() {
		return this.transitionName;
	}
	
	public PetriNet getPetriNet() {
		return this.petriNet;
	}
	
	public void setSyncronizer(Semaphore s) {
		this.syncronizer = s;
	}
	
	protected void trigger(String event) throws PetriNetException{
		petriNet.fire(event);
	}
}
