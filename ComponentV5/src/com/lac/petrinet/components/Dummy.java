package com.lac.petrinet.components;

import java.util.concurrent.Callable;

import com.lac.petrinet.netcommunicator.Transition;

public abstract class Dummy implements Callable<Void> {
	
	private Transition transition;
	
	@Override
	public Void call() throws Exception {
		execute();
		transition.fire();
		return null;
	}
	
	abstract protected void execute();

	public void setTransition(Transition transition) {
		this.transition = transition;
	}
	
	
	
}
