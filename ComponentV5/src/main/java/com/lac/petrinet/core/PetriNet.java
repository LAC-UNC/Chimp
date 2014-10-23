package com.lac.petrinet.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.lac.petrinet.components.Dummy;
import com.lac.petrinet.configuration.PNData;
import com.lac.petrinet.exceptions.PetriNetException;
import com.lac.petrinet.netcommunicator.FiredTransition;
import com.lac.petrinet.netcommunicator.InformedTransition;

public class PetriNet {

	Map<String,InformedTransition> informedTransitions = new HashMap<String, InformedTransition>(); 
	Map<String, FiredTransition> firedTransitions = new HashMap<String, FiredTransition>(); 
	PNData pnData;
	
	public void startListening(){
		while(true) { // As far as I know, this method can't be tested because of this infinite cycle.
			this.nextCicle();
		}
	}
	
	public void startListening(int numberOfCicles){
		while(numberOfCicles-- > 0) {
			this.nextCicle();
		}
	}
	
	/**
	 * Check only 1 time the communicate() of all the informed transitions. 
	 */
	public void nextCicle(){
		Iterator<Entry<String, InformedTransition>> it;
	    it = informedTransitions.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, InformedTransition> pairs = (Entry<String, InformedTransition>)it.next();
	        ((InformedTransition) pairs.getValue()).communicate();
	    }
		
		return;
	}
	
	public void assignDummy(String transition, Dummy dumb) throws PetriNetException{
		InformedTransition it = informedTransitions.get(transition);
		dumb.setPetriNet(this);
		
		if(it == null)
			throw new PetriNetException("There is no informed transition named: " + transition);
		
		if(!this.sharePlaceVertically(it.getTransitionId(), this.getFired(dumb.getTransitionName()).getTransitionId()))
			throw new PetriNetException("The informed transition " + transition + " is not separed by only a place with the fired transition " + dumb.getTransitionName());
		
		it.addDummy(dumb);
	}
	
	public boolean sharePlaceVertically(int tAbove, int tBelow) {
		// TODO Auto-generated method stub
		int[][] pos = this.pnData.getMatrizIncidenciaPositiva();
		int[][] neg = this.pnData.getMatrizIncidenciaNegativa();
		
		for(int i = 0; i<pos.length; i++){
			if(pos[i][tAbove] > 0 && neg[i][tBelow] > 0) {
				return true;
			}
		}
		
		return false;
	}
	
	public void fire(String transition) throws PetriNetException{
		FiredTransition ft = firedTransitions.get(transition);
		if(ft == null)
			throw new PetriNetException("There is no fired transition named: " + transition);
		
		ft.communicate();
	}
	
	public void addInformed(String name, InformedTransition informedTransition){
		informedTransitions.put(name, informedTransition);
	}
	
	public void addFired(String name, FiredTransition firedTransition){
		firedTransitions.put(name, firedTransition);
	}
	
	public FiredTransition getFired(String name) {
		return firedTransitions.get(name);
	}
	
	public boolean containFired(String name) {
		return firedTransitions.containsKey(name);
	}
	
	public InformedTransition getInformed(String name) {
		return informedTransitions.get(name);
	}

	public void setPNData(PNData pd) {
		this.pnData = pd;
	}
	
		
	public Set<String> getInformedNameList(){
		return informedTransitions.keySet();
	}

	public Set<String> getFiredTransitions() {
		return firedTransitions.keySet();
	}

}
