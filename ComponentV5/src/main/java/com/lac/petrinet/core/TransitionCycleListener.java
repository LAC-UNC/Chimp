package com.lac.petrinet.core;

import java.util.ArrayList;
import java.util.List;

import com.lac.petrinet.netcommunicator.InformedTransition;

public class TransitionCycleListener implements Runnable {

	private List<InformedTransition> transitions;
	private int numberOfCycles = -1;
	
	public TransitionCycleListener(List<InformedTransition> transitions) {

		this.transitions = transitions;
	}
	
	public TransitionCycleListener(List<InformedTransition> transitions, int numberOfCycles) {
		this.transitions = transitions;
		this.numberOfCycles = numberOfCycles;
	}

	@Override
	public void run() {
		int counter = 0;
		List<InformedTransition> transitionsWithInformed = new ArrayList<InformedTransition>();		
		
		while(counter < numberOfCycles || numberOfCycles < 0){
			try {
				transitionsWithInformed.removeAll(transitionsWithInformed);
				for(InformedTransition transition : transitions ){
					if(transition.communicate()){
						transitionsWithInformed.add(transition);	
					}
				}
				for(InformedTransition t : transitionsWithInformed){
					t.startDummies();
					Thread.yield();
				}
				counter++;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
