package com.lac.petrinet.configuration;

import com.lac.petrinet.core.PetriNet;
import com.lac.petrinet.exceptions.PetriNetException;


public interface ConfigurationReader {

	public PetriNet loadConfiguration(String path) throws PetriNetException;
}
