package com.lac.petrinet.exceptions;

public class PetriNetException extends Exception {
	public PetriNetException(String message, Throwable t){
		super(message, t);
	}
	
	public PetriNetException(String message){
		super(message);
	}
}
