package com.lac.petrinet.core;

import java.util.List;

import com.lac.petrinet.netcommunicator.Transition;

public class TransitionCycleListener implements Runnable {

	private List<? extends Transition> transitions;
	private int numberOfCycles = -1;
	
	public TransitionCycleListener(List<? extends Transition> transitions) {
		this.transitions = transitions;
	}
	
	public TransitionCycleListener(List<? extends Transition> transitions, int numberOfCycles) {
		this.transitions = transitions;
		this.numberOfCycles = numberOfCycles;
	}

	@Override
	public void run() {
		int counter = 0;
		while(counter < numberOfCycles || numberOfCycles < 0){
			for(Transition transition : transitions ){
				transition.communicate();
			}
			counter++;
		}
	}

}
