package com.lac.petrinet.netcommunicator;

import java.util.Collection;

import com.lac.petrinet.components.Dummy;

public abstract class Transition {

	@SuppressWarnings("unused")
	private int transitionId;
	@SuppressWarnings("unused")
	private ProcessorHandler processor  ;
	
	public abstract Collection<Dummy> communicate();
	
}
