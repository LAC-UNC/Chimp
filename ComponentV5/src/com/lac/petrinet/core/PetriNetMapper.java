package com.lac.petrinet.core;

import java.util.ArrayList;

import com.lac.petrinet.configuration.ConfigurationReader;
import com.lac.petrinet.configuration.TransitionGroup;
import com.lac.petrinet.configuration.providers.PNMLConfigurationReader;
import com.lac.petrinet.exceptions.PetriNetException;
import com.lac.petrinet.netcommunicator.Transition;

public class PetriNetMapper {

	private static TransitionGroup transitionsMapper ;
	private static ConfigurationReader configReader ;

	// Singleton  // 
	private static PetriNetMapper INSTANCE = null;
	private PetriNetMapper(){}

	private static void createInstance(String path) throws PetriNetException {
		if (INSTANCE == null) {
			synchronized(PetriNetMapper.class) {
				if (INSTANCE == null) { 
					INSTANCE = new PetriNetMapper();
					// TODO: handle dynamically the reader.
					configReader = new PNMLConfigurationReader();
					transitionsMapper = configReader.getConfiguration(path);
				}
			}
		}
	}

	private static void createInstance(){
		if (INSTANCE == null) {
			synchronized(PetriNetMapper.class) {
				if (INSTANCE == null) { 
					INSTANCE = new PetriNetMapper();
					transitionsMapper = new TransitionGroup();
				}
			}
		}
	}
	
	public static PetriNetMapper getInstance() {
		if (INSTANCE == null){
			createInstance();
		}
		return INSTANCE;
	}
	
	public static PetriNetMapper getInstance(String path) throws PetriNetException {
		if (INSTANCE == null){
			createInstance(path);
		}
		return INSTANCE;
	}
	

	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException(); 
	}

	// end singleton //

	public ArrayList<Transition> getAllInformedTransitions(){
		return new ArrayList<Transition>(transitionsMapper.getInformedTransitions().values());
	}

	public Transition getInformedTransition(String transitionName){
		return transitionsMapper.getInformedTransitions().get(transitionName);
	}

	public Transition getFiredTransition(String transitionName){
		return transitionsMapper.getFiredTransitions().get(transitionName);
	}


}
