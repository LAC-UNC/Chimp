package com.lac.petrinet.netcommunicator;

import java.util.Collection;

import com.lac.petrinet.components.Dummy;

public abstract class Transition {

	@SuppressWarnings("unused")
	protected int transitionId;
	@SuppressWarnings("unused")
	protected ProcessorHandler processor;
	
	public Transition(ProcessorHandler processor, int trasitionId) {
		this.setProcessor(processor);
		this.setTransitionId(trasitionId);
	}
	
	public abstract void communicate();
	
	protected void setTransitionId(int transitionId){
		this.transitionId = transitionId;
	}
	
	protected void setProcessor(ProcessorHandler processor){
		this.processor = processor;
	}
	
	public int getTransitionId() {
		return transitionId;
	}
}
