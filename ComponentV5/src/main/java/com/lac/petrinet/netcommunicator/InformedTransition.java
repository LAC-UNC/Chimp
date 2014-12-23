package com.lac.petrinet.netcommunicator;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;

import com.lac.petrinet.components.Dummy;

public class InformedTransition extends Transition {

	private ExecutorService threadPool;
	private ArrayList<Dummy> dummies;
	private Semaphore syncronizer;
	
	public InformedTransition(ProcessorHandler processor, int trasitionId, ExecutorService threadPool) {
		super(processor, trasitionId);
		this.threadPool = threadPool;
		this.dummies = new ArrayList<Dummy>();
		this.syncronizer = new Semaphore(0);
	}
	
	@Override
	public boolean communicate(){
		return this.processor.listen(this.transitionId);
	}
	
	public void startDummies() throws InterruptedException{
		for(Dummy dumb : dummies){
			threadPool.submit(dumb);
		}
		this.syncronizer.acquire(dummies.size());
	}
	
	public void addDummy(Dummy dumb) {
		dumb.setSyncronizer(this.syncronizer);
		this.dummies.add(dumb);
	}
	
	public boolean contains(Dummy dumb) {
		return this.dummies.contains(dumb);
	}
}
