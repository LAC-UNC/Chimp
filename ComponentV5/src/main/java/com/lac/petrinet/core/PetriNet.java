package com.lac.petrinet.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.lac.petrinet.components.Dummy;
import com.lac.petrinet.netcommunicator.FiredTransition;
import com.lac.petrinet.netcommunicator.InformedTransition;

public class PetriNet {

	Map<String,InformedTransition> informedTransitions = new HashMap<String, InformedTransition>(); 
	Map<String, FiredTransition> firedTransitions = new HashMap<String, FiredTransition>(); 
	
	public void startListening(int numberOfCicles){
		Iterator<Entry<String, InformedTransition>> it;
		while(true) { // As far as I know, this method can't be tested because of this infinite cycle.
		    it = informedTransitions.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry<String, InformedTransition> pairs = (Entry<String, InformedTransition>)it.next();
		        ((InformedTransition) pairs.getValue()).communicate();
		    }
		}
	}
	
	public void assignDummy(String transition, Dummy dumb){
		informedTransitions.get(transition).addDummy(dumb);
	}
	
	public void fire(String transition){
		firedTransitions.get(transition).communicate();
	}
	
	public void addInformed(String name, InformedTransition informedTransition){
		informedTransitions.put(name, informedTransition);
	}
	
	public void addFired(String name, FiredTransition firedTransition){
		firedTransitions.put(name, firedTransition);
	}
}
