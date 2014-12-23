package com.lac.petrinet.netcommunicator;

public abstract class Transition {

	protected int transitionId;
	protected ProcessorHandler processor;
	
	public Transition(ProcessorHandler processor, int trasitionId) {
		this.setProcessor(processor);
		this.setTransitionId(trasitionId);
	}
	
	public abstract boolean communicate();
	
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
