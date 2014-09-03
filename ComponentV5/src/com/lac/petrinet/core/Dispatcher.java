package com.lac.petrinet.core;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
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
		ArrayList<Dummy> dummiesToExecute ;
		while(Boolean.TRUE){
			for(Transition transition : petriNet.getAllInformedTransitions()){
				dummiesToExecute = transition.listen();
				for()
			}
		}
	}
	/**
	 * All the time listening to all the transitions, when a transitions has an "inform", it assigns the corresponding task to a thread. 
	 * TODO: fix exceptions
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	private void listen(){
		// when it found an "Inform" from a transition, it has to assign the corresponding dummy to a thread.
	}
	
	/**
	 * Create the instance and assign it to the corresponding thread. 
	 * 
	 * TODO: store the instance somewhere
	 * TODO: Is right to create an instance where we don't know if there is a constructor that don't receives parameters? 
	 * TODO: fix exceptions
	 * @param transition
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@SuppressWarnings("unchecked")
	private void assignDummy(Transition transition) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		pool.submit();
	}
	
}
