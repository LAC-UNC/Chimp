package com.lac.petrinet.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lac.petrinet.components.Dummy;
import com.lac.petrinet.netcommunicator.Transition;

public class Dispatcher implements Runnable {

	private ExecutorService pool ;
	private PetriNetMapper petriNet ;
	
	public Dispatcher(){
		pool = Executors.newCachedThreadPool();
		petriNet = PetriNetMapper.getInstance();
	}
	
	public void run() {
		while(Boolean.TRUE){
			for(Transition transition : petriNet.getAllInformedTransitions()){
				// for each over the dummies to execute. If the transition was NOT informed, then 
				// the collection returned is empty.
				for(Dummy dummy : transition.listen()){
					pool.submit(dummy);
				}
			}
		}
	}
}
