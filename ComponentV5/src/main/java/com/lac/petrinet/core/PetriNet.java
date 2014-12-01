package com.lac.petrinet.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
	// Para largar varios hilos q escuchen el procesador...hacer una lista de listas, y crear una clase runnable que el constructor
	// reciba una lista. recorrer esta lista de lista (a partir de ahora 'transitionGroupList' ) y crear los 
	// runnables pasandole cada una de estas listas, si esta 'transitionGroupList' esta vacia, seguir haciendo 
	// como estaba hasta ahora. 
	// este modo o lista sera seteado como un metodo por separado ( no en el constructor, para mantener compatibilidad hacia atras.)
	// TODO: agergar manejo para cuando no estan todas las informadas en las listas de escucha. 
	PNData pnData;
	private List<List<InformedTransition>>  transitionGroupList;

	public PetriNet(){
		
	}
	
	public PetriNet(List<List<InformedTransition>> TransitionGroupList){
		this.transitionGroupList = TransitionGroupList;
	}
	
	public void startListening(){
		if(transitionGroupList == null || transitionGroupList.isEmpty()){
			listenAll();
		}
		else{
			transitionGroupListening();
		}
	}
	
	public void startListening(int numberOfCicles){
		if(transitionGroupList == null || transitionGroupList.isEmpty()){
			listenAll(numberOfCicles);
		}
		else{
			transitionGroupListening();
		}
	}
	
	private void transitionGroupListening(){
		for(List<InformedTransition> transitionGroup : transitionGroupList){
			Thread t = new Thread(new TransitionCycleListener(transitionGroup));
			t.run();
		}
	}
	
	private void listenAll(){
		while(true) { // As far as I know, this method can't be tested because of this infinite cycle.
			this.nextCicle();
		}
	}
	
	private void listenAll(int numberOfCicles){
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
	
	public void setTransitionGroupList(List<List<InformedTransition>> TransitionGroupList){
		this.transitionGroupList = TransitionGroupList;
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
