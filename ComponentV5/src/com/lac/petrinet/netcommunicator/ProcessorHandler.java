package com.lac.petrinet.netcommunicator;

import procesadorPNVirtual.ProcesadorPetriVirtual;

public class ProcessorHandler {
	//TODO: when having multiple processors, we must change the parameter to get the transition quantities.
	private static ProcesadorPetriVirtual virtualProcessor;

	// Singleton  // 
	private static ProcessorHandler INSTANCE = null;
	private ProcessorHandler(){}

	private static void createInstance(String configFilePath, Integer transitionQuantity) {
		if (INSTANCE == null) {
			synchronized(ProcessorHandler.class) {
				if (INSTANCE == null) { 
					INSTANCE = new ProcessorHandler();
					INSTANCE.setVirtualProcessor(new ProcesadorPetriVirtual(configFilePath, transitionQuantity.intValue() ));
				}
			}
		}
	}

	private void setVirtualProcessor(ProcesadorPetriVirtual virtualProcessor) {
		ProcessorHandler.virtualProcessor = virtualProcessor;
	}

	public static ProcessorHandler getInstance(String configFilePath,  Integer transitionQuantity) {
		if (INSTANCE == null){
			createInstance(configFilePath, transitionQuantity);
		}else{
			//TODO: handle if the user pass a config file path but the instance already exits
		}
		return INSTANCE;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException(); 
	}

	// end singleton //
	
	public void fire(Transition t){
		virtualProcessor.encolar(t.getNetId(), t.getTransitionId());
	}
	
	public boolean listen(Transition t){
		return virtualProcessor.consultarDisparoTransicion(t.getTransitionId());
	}
	
}
