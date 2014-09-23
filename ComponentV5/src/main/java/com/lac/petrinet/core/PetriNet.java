package com.lac.petrinet.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.lac.petrinet.components.Dummy;
import com.lac.petrinet.netcommunicator.FiredTransition;
import com.lac.petrinet.netcommunicator.InformedTransition;

public class PetriNet {

	Map<String,InformedTransition> informedTransitions = new HashMap<String, InformedTransition>(); 
	Map<String, FiredTransition> firedTransitions = new HashMap<String, FiredTransition>(); 
	
	public Collection<Dummy> listenAll(){
		throw new UnsupportedOperationException();
	}
	
	public void assignDummy(String transition, Dummy dummy){
		throw new UnsupportedOperationException();
	}
	
	public void fire(String transition){
		throw new UnsupportedOperationException();
	}
	
	public void addInformed(InformedTransition informedTransition){
		throw new UnsupportedOperationException();	
	}
	
	public void addFired(FiredTransition fireTransition){
		throw new UnsupportedOperationException();
	}
}
