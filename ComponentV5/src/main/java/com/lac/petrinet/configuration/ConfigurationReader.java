package com.lac.petrinet.configuration;

import com.lac.petrinet.exceptions.PetriNetException;


public interface ConfigurationReader {

	public TransitionGroup getConfiguration(String path) throws PetriNetException;
}
