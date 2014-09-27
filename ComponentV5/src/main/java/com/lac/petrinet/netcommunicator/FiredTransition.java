package com.lac.petrinet.netcommunicator;

import java.util.Collection;

import com.lac.petrinet.components.Dummy;

public class FiredTransition extends Transition {
	
	public FiredTransition(ProcessorHandler processor, int trasitionId) {
		super(processor, trasitionId);
	}
	
	@Override
	public void communicate() {
		this.processor.fire(this.transitionId);
	}
}
