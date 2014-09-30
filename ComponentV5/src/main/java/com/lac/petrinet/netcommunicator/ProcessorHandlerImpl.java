package com.lac.petrinet.netcommunicator;

import procesadorPNVirtual.ProcesadorPetriVirtual;


public class ProcessorHandlerImpl implements ProcessorHandler{

	private static ProcesadorPetriVirtual virtualProcessor;
	
	public ProcessorHandlerImpl(String configFilePath, int TransitionQuantities){
		virtualProcessor = new ProcesadorPetriVirtual(configFilePath, TransitionQuantities);
	}
	
	@Override
	public void fire(int transitionId) {
		virtualProcessor.encolar(0, transitionId);
		
	}

	@Override
	public boolean listen(int transitionId) {
		return virtualProcessor.consultarDisparoTransicion(transitionId);
	}

}
