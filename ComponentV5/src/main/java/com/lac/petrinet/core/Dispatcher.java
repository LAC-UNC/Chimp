package com.lac.petrinet.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lac.petrinet.components.Dummy;
import com.lac.petrinet.netcommunicator.Transition;

public class Dispatcher implements Runnable {

	private ExecutorService threadPool ;
	private PetriNet petriNet ;
	
	public Dispatcher(PetriNet petriNet){
		this.threadPool = Executors.newCachedThreadPool();
		this.petriNet = petriNet;
	}
	
	public void run() {
		throw new UnsupportedOperationException();
	}
	
	public void start(){
		throw new UnsupportedOperationException();
	}
	
}
