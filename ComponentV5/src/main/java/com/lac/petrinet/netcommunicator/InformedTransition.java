package com.lac.petrinet.netcommunicator;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lac.petrinet.components.Dummy;

public class InformedTransition extends Transition {

	private ExecutorService threadPool;
	private ArrayList<Dummy> dummies;
	
	public InformedTransition(ProcessorHandler processor, int trasitionId, ExecutorService threadPool) {
		super(processor, trasitionId);
		this.threadPool = threadPool;
		this.dummies = new ArrayList<Dummy>();
	}
	
	@Override
	public void communicate() {
		if(this.processor.listen(this.transitionId)){
			try {
				threadPool.invokeAll(this.dummies);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
	}
	
	public void addDummy(Dummy dumb) {
		this.dummies.add(dumb);
	}
	
	public boolean contains(Dummy dumb) {
		return this.dummies.contains(dumb);
	}
}
