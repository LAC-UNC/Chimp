package com.lac.petrinet.configuration;

import java.util.HashMap;
import java.util.Map;

import com.lac.petrinet.netcommunicator.Transition;

public class TransitionGroup {

	private Map<String, Transition> informedTransitions = new HashMap<String, Transition>();
	private Map<String, Transition> firedTransitions = new HashMap<String, Transition>();
	
	public Map<String, Transition> getInformedTransitions() {
		return informedTransitions;
	}
	
	public Map<String, Transition> getFiredTransitions() {
		return firedTransitions;
	}

	
}
