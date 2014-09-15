package com.lac.petrinet.core;

import java.util.ArrayList;

import com.lac.petrinet.configuration.ConfigurationReader;
import com.lac.petrinet.configuration.TransitionGroup;
import com.lac.petrinet.configuration.providers.FakeConfigurationReader;
import com.lac.petrinet.configuration.providers.PNMLConfigurationReader;
import com.lac.petrinet.exceptions.PetriNetException;
import com.lac.petrinet.netcommunicator.Transition;

public class PetriNetMapper {

	private TransitionGroup transitionsMapper ;
	private TransitionGroup transitionsMapperIncomplete ;
	private static ConfigurationReader configReader ;
	

	// Singleton  // 
	private static PetriNetMapper INSTANCE = null;
	private PetriNetMapper(){}

	private static void createInstance(){
		if (INSTANCE == null) {
			synchronized(PetriNetMapper.class) {
				if (INSTANCE == null) { 
					INSTANCE = new PetriNetMapper();
					INSTANCE.transitionsMapper = new TransitionGroup();
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
		Transition transit = transitionsMapper.getInformedTransitions().get(transitionName);
		if( transit == null){
			transit = new Transition(transitionName);
			transitionsMapperIncomplete.getInformedTransitions().put(transitionName, transit);
		}
		return transit;
	}

	public Transition getFiredTransition(String transitionName){
		Transition transit =  transitionsMapper.getFiredTransitions().get(transitionName);
		if( transit == null){
			transit = new Transition(transitionName);
			transitionsMapperIncomplete.getFiredTransitions().put(transitionName, transit);
		}
		return transit;
	}
	
	public void setConfigurationFake(FakeConfigurationReader reader) throws PetriNetException{
		this.configReader = reader;
		this.transitionsMapper = this.configReader.getConfiguration("");
	}
	
	public void setConfigurationPNML(String path) throws PetriNetException{
		this.configReader = new PNMLConfigurationReader();
		this.transitionsMapper = this.configReader.getConfiguration(path);
	}
}
