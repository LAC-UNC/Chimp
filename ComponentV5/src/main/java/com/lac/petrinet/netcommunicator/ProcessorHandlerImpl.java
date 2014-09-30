package com.lac.petrinet.netcommunicator;


public class ProcessorHandlerImpl implements ProcessorHandler{

	@Override
	public void fire(int transitionId) {
			throw new UnsupportedOperationException();
		
	}

	@Override
	public boolean listen(int transitionId) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setConfiguration(String filesPath) {
		throw new UnsupportedOperationException();		
	}
	
	
//	private static ProcesadorPetriVirtual virtualProcessor;
//
//	// Singleton  // 
//	private static ProcessorHandlerImpl INSTANCE = null;
//	private ProcessorHandlerImpl(){}
//
//	private static void createInstance(String configFilePath, Integer transitionQuantity) {
//		if (INSTANCE == null) {
//			synchronized(ProcessorHandlerImpl.class) {
//				if (INSTANCE == null) { 
//					INSTANCE = new ProcessorHandlerImpl();
//					INSTANCE.setVirtualProcessor(new ProcesadorPetriVirtual(configFilePath, transitionQuantity.intValue() ));
//				}
//			}
//		}
//	}
//
//	private void setVirtualProcessor(ProcesadorPetriVirtual virtualProcessor) {
//		ProcessorHandlerImpl.virtualProcessor = virtualProcessor;
//	}
//
//	public static ProcessorHandlerImpl getInstance(String configFilePath,  Integer transitionQuantity) {
//		if (INSTANCE == null){
//			createInstance(configFilePath, transitionQuantity);
//		}else{
//			//TODO: handle if the user pass a config file path but the instance already exists
//		}
//		return INSTANCE;
//	}
//
//	@Override
//	public Object clone() throws CloneNotSupportedException {
//		throw new CloneNotSupportedException(); 
//	}
//
//	// end singleton //
//	
//	public void fire(Transition t){
//		virtualProcessor.encolar(t.getNetId(), t.getTransitionId());
//	}
//	
//	public boolean listen(Transition t){
//		return virtualProcessor.consultarDisparoTransicion(t.getTransitionId());
//	}
//	
}
