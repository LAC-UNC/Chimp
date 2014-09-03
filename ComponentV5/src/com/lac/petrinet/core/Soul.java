package com.lac.petrinet.core;

import com.lac.petrinet.components.Dummy;
import com.lac.petrinet.netcommunicator.ProcessorHandler;
import com.lac.petrinet.netcommunicator.Transition;

public class Soul {

	static private Dispatcher dispatcher ;
	static private PetriNetMapper petriNet =  PetriNetMapper.getInstance();
	// the objective of the ProcessorHandler here is just to create the instance with the parameters given.
	static private ProcessorHandler processor ;
	
	
	public synchronized static void associate(String nameTransitionOutput, Class<? extends Dummy> clazz, String nameTransitionInput, 
			String...nameTransitions ) throws Exception{
		Transition transtitionOutput = petriNet.getFiredTransition(nameTransitionOutput);
		Dummy dummy = (Dummy) createInstance(clazz);
		dummy.setTransition(transtitionOutput);
		Transition transitionInput = petriNet.getInformedTransition(nameTransitionInput);
		transitionInput.addDummy(dummy);
		for(String name : nameTransitions){
			transitionInput = petriNet.getInformedTransition(name);
			transitionInput.addDummy(dummy);
		}
		
	}
	
	private static Object createInstance(Class<? extends Dummy> clazz) throws Exception{
		Object instance = null;
		instance = clazz.getConstructor().newInstance();
		return instance;
	}
	
	
	public void starts(String configFilePath, int transitionQuantity) {
		//TODO: create start
		processor = ProcessorHandler.getInstance(configFilePath,transitionQuantity);
		dispatcher = new Dispatcher();
		new Thread(dispatcher).start();
	}
	
	
}
