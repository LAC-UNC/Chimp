package com.lac.petrinet.configuration.providers;
import com.lac.petrinet.configuration.ConfigurationReader;
import com.lac.petrinet.configuration.TransitionGroup;
import com.lac.petrinet.exceptions.PetriNetException;
import com.lac.petrinet.netcommunicator.Transition;

public class FakeConfigurationReader implements ConfigurationReader {
//	TODO: after all the code is done, re-code this MOCK. 
//	TransitionGroup tg = new TransitionGroup();
	
	@Override
	public TransitionGroup getConfiguration(String path)
			throws PetriNetException {
		throw new UnsupportedOperationException();
	}

//	public void addFiredTransition(String tid, Transition t) {
//		tg.getFiredTransitions().put(tid, t);
//	}
//	
//	public void addInformedTransition(String tid, Transition t) {
//		tg.getInformedTransitions().put(tid, t);
//	}
}
