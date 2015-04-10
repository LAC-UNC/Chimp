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
		boolean needNewRound = false;
		List<InformedTransition> transitionsWithInformed = new ArrayList<InformedTransition>();		
		
		while(counter < numberOfCycles || numberOfCycles < 0){
			try {
				transitionsWithInformed.removeAll(transitionsWithInformed);
				do{
					needNewRound = false;
					for(InformedTransition transition : transitions ){
						if(transition.communicate()){
							transitionsWithInformed.add(transition);	
							needNewRound = true;
						}
					}
				}while(needNewRound);
				
				int i = 0; 
				for(InformedTransition t : transitions){
					if(transitionsWithInformed.contains(t)){
						transitionsWithInformed.remove(transitionsWithInformed.indexOf(t));
						t.startDummies();
						Thread.yield();
						i++;
					}
				}
				
				counter++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
