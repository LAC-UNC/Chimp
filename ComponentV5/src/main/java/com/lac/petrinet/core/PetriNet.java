package com.lac.petrinet.core;

import java.util.ArrayList;
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
	PNData pnData;
	private List<List<InformedTransition>>  transitionGroupList = new ArrayList<List<InformedTransition>>();
	List<Thread> listenerThreads = new ArrayList<Thread>();
	private HashMap<String, String> inputEventsMap = null;
	private HashMap<String, String> outputEventsMap;

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
			listenNonGroupedTransitions();
		}
	}
	
	public void startListening(int numberOfCicles){
		if(transitionGroupList == null || transitionGroupList.isEmpty()){
			listenAll(numberOfCicles);
		}
		else{
			transitionGroupListening(numberOfCicles);
			listenNonGroupedTransitions();
		}
	}
	
	private void transitionGroupListening(){
		for(List<InformedTransition> transitionGroup : transitionGroupList){
			Thread t = new Thread(new TransitionCycleListener(transitionGroup));
			listenerThreads.add(t);
			t.start();
		}
	}
	
	private void transitionGroupListening(int numberOfCycles){
		for(List<InformedTransition> transitionGroup : transitionGroupList){
			Thread t = new Thread(new TransitionCycleListener(transitionGroup,numberOfCycles));
			listenerThreads.add(t);
			t.start();
		}
	}
	
	
	private void listenNonGroupedTransitions(){
		List<InformedTransition>	transitionsNonGrouped = new ArrayList<InformedTransition>();
		transitionsNonGrouped.addAll(informedTransitions.values());
		List<InformedTransition> transitionsGrouped = new ArrayList<InformedTransition>();
		for(List<InformedTransition> transitions: transitionGroupList){
			transitionsGrouped.addAll(transitions);
		}
		transitionsNonGrouped.removeAll(transitionsGrouped);
		Thread t = new Thread(new TransitionCycleListener(transitionsNonGrouped));
		listenerThreads.add(t);
		t.start();
		
	}
	
	private void listenAll(){
		List<InformedTransition> list = new ArrayList<InformedTransition>(informedTransitions.values());
		Thread t = new Thread(new TransitionCycleListener(list));
		listenerThreads.add(t);
		t.start();
	}
	
	private void listenAll(int numberOfCicles){
		List<InformedTransition> list = new ArrayList<InformedTransition>(informedTransitions.values());
		Thread t = new Thread(new TransitionCycleListener(list, numberOfCicles));
		listenerThreads.add(t);
		t.start();
	}
	
	public void addTransitionGroup(List<InformedTransition> group){
		this.transitionGroupList.add(group);
	}
	
	public void addTransitionNameGroup(List<String> group){
		List<InformedTransition> tempList = new ArrayList<InformedTransition>();
		for(String name : group){
			tempList.add(getInformed(this.convertOutputEventNameToTransitionName(name)));
		}
		addTransitionGroup(tempList);
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
	
	public void assignDummy(String event, Dummy dumb) throws PetriNetException{
		String tName = this.convertOutputEventNameToTransitionName(event);
		
		InformedTransition it = informedTransitions.get(tName);
		dumb.setPetriNet(this);
		
		if(it == null)
			throw new PetriNetException("There is no output event named: " + tName);
		
		if(!this.sharePlaceVertically(it.getTransitionId(), this.getFired(dumb.getTransitionName()).getTransitionId()))
			throw new PetriNetException("The informed transition " + tName + " is not separed by only a place with the fired transition " + dumb.getTransitionName());
		
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
	
	public void fire(String event) throws PetriNetException{
		String tName = this.convertInputEventNameToTransitionName(event);
		
		FiredTransition ft = firedTransitions.get(tName);
		if(ft == null)
			throw new PetriNetException("There is no input event named: " + tName);
		
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
	
	public boolean containInformed(String name) {
		return informedTransitions.containsKey(name);
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

	/*public void setInputEventsMap(HashMap<String, String> InputEventsMap) {
		this.inputEventsMap = InputEventsMap;
	}
	
	public void setOutputEventsMap(HashMap<String, String> OutputEventsMap) {
		this.outputEventsMap = OutputEventsMap;
	}*/
	
	public void addInputEventAlias(String eventAlias, String transitionName) throws PetriNetException {
		if(this.inputEventsMap == null) {
			this.inputEventsMap = new HashMap<String, String>();
		}
		
		if(this.containFired(transitionName)) {
			this.inputEventsMap.put(eventAlias, transitionName);
		} else {
			throw new PetriNetException("There is no fired transition named: " + transitionName);
		}
	}
	
	public void addOutputEventAlias(String eventAlias, String transitionName) throws PetriNetException {
		if(this.outputEventsMap == null) {
			this.outputEventsMap = new HashMap<String, String>();
		}
		
		if(this.containInformed(transitionName)) {
			this.outputEventsMap.put(eventAlias, transitionName);
		} else {
			throw new PetriNetException("There is no informed transition named: " + transitionName);
		}
	}

	public boolean containsInputEvent(String eventName) {
		return this.containsEvent(eventName, this.inputEventsMap);
	}

	public String convertInputEventNameToTransitionName(String eventName) {
		return convertEventNameToTransitionName(eventName, this.inputEventsMap);
	}
	
	public boolean containsOutputEvent(String eventName) {
		return this.containsEvent(eventName, this.outputEventsMap);
	}

	public String convertOutputEventNameToTransitionName(String eventName) {
		return convertEventNameToTransitionName(eventName, this.outputEventsMap);
	}
	
	private boolean containsEvent(String eventName, HashMap<String, String> eventsMap) {
		return eventsMap != null && eventsMap.containsKey(eventName);
	}
	
	private String convertEventNameToTransitionName(String eventName, HashMap<String, String> eventsMap) {
		String toRet = eventName;
		if(containsEvent(eventName, eventsMap)) {
			toRet = eventsMap.get(eventName);
		}
		
		return toRet;
	}
}
