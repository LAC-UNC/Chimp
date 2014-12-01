package com.lac.petrinet.core;

import java.util.List;

import com.lac.petrinet.netcommunicator.Transition;

public class TransitionCycleListener implements Runnable {

	private List<? extends Transition> transitions;
	
	public TransitionCycleListener(List<? extends Transition> transitions) {
		this.transitions = transitions;
	}

	@Override
	public void run() {
		for(Transition transition : transitions ){
			transition.communicate();
		}
	}

}
