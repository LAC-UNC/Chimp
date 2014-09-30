package com.lac.petrinet.netcommunicator;

public interface ProcessorHandler {

	public void fire(int transitionId);
	public boolean listen(int transitionId);
	public void setConfiguration(String filesPath);
}
