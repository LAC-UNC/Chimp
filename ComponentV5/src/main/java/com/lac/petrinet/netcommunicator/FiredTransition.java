package com.lac.petrinet.netcommunicator;

public class FiredTransition extends Transition {
	
	public FiredTransition(ProcessorHandler processor, int trasitionId) {
		super(processor, trasitionId);
	}
	
	@Override
	public boolean communicate() {
		this.processor.fire(this.transitionId);
		return true;
	}
}
