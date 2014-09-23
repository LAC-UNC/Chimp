package com.lac.petrinet;

import com.lac.petrinet.configuration.providers.FakeConfigurationReader;
import com.lac.petrinet.core.Soul;
import com.lac.petrinet.exceptions.PetriNetException;
import com.lac.petrinet.netcommunicator.Transition;

import manualTests.ResourceOne;

/**
 * I think for the core, there shouldn't be any main....
 * @author icaio
 *
 */
public final class main {
//
//	public static void main(String[] args) {
//		// Mocking configuration
//		FakeConfigurationReader conf = new FakeConfigurationReader();
//		
//		conf.addInformedTransition("t0", new Transition(0,0));
//		conf.addFiredTransition("t1", new Transition(1,0));
//		try {
//			Soul.associate("t0", ResourceOne.class, "t1");
//			Soul.starts(conf, 2);
//		} catch (PetriNetException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//	}
}
