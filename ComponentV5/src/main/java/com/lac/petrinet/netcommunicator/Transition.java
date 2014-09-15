package com.lac.petrinet.netcommunicator;

import java.util.ArrayList;

import com.lac.petrinet.components.Dummy;

public class Transition {

	private String name;
	private int transitionId;
	private int netId;
	private ArrayList<Dummy> dummyCollection = new ArrayList<Dummy>();
	private ProcessorHandler processor  ;
	
	
	public Transition(int transitionId, int netId) {
		this.transitionId = transitionId;
		this.netId = netId;
		// parameters null because I don't care to create a new instance of the processor, because it should have been
		// created before. In the Soul() should be the 1st call to the singleton and the real construction of the Processorhander
		 processor = ProcessorHandler.getInstance(null,null) ;
	}
	
	public Transition(String name){
		this.name =name;
	}
	
	public int getTransitionId() {
		return transitionId;
	}
	public void setTransitionId(int transitionId) {
		this.transitionId = transitionId;
	}
	public int getNetId() {
		return netId;
	}
	public void setNetId(int netId) {
		this.netId = netId;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Dummy> getDummyCollection() {
		return dummyCollection;
	}

	public void setDummyCollection(ArrayList<Dummy> dummyCollection) {
		this.dummyCollection = dummyCollection;
	}

	public void addDummy(Dummy d){
		dummyCollection.add(d);
	}
	
	public void fire(){
		processor.fire(this);
	}
	
	public ArrayList<Dummy> listen(){
		ArrayList<Dummy> dummies = new ArrayList<Dummy>();
		if(processor.listen(this)){
			dummies =  dummyCollection;
		}
		return dummies;
	}
	
}
