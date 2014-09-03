package com.lac.petrinet.core;

import java.util.ArrayList;

import com.lac.petrinet.configuration.ConfigurationReader;
import com.lac.petrinet.configuration.TinaConfigurationReader;
import com.lac.petrinet.configuration.TransitionGroup;
import com.lac.petrinet.netcommunicator.Transition;

public class PetriNetMapper {

	private static TransitionGroup transitionsMapper ;
	private static ConfigurationReader configReader ;

	// Singleton  // 
	private static PetriNetMapper INSTANCE = null;
	private PetriNetMapper(){}

	private static void createInstance() {
		if (INSTANCE == null) {
			synchronized(PetriNetMapper.class) {
				if (INSTANCE == null) { 
					INSTANCE = new PetriNetMapper();
					// TODO: handle dynamically the reader.
					configReader = new TinaConfigurationReader();
					transitionsMapper = configReader.getConfiguration();
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
