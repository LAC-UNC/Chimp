package com.lac.petrinet;

import java.util.Map;

import com.lac.petrinet.configuration.PNData;
import com.lac.petrinet.configuration.TransitionGroup;
import com.lac.petrinet.configuration.providers.PNMLConfigurationReader;
import com.lac.petrinet.exceptions.PetriNetException;
import com.lac.petrinet.netcommunicator.Transition;

public class main {

	public static void main(String[] args) throws PetriNetException {
		String configurationFilePath = "D:/Estudio/facu/tesis/estudio preliminar/ejemplo barcos/archivos config nvos";
		String pnmlFilePath = "D:/Estudio/facu/tesis/estudio preliminar/ejemplo barcos/barcos-conContrainst.pnml";
		PNData pnData = new PNData();
		pnData.cargarRed(pnmlFilePath);
		PNMLConfigurationReader pnmlConfigurator = new PNMLConfigurationReader();
		pnmlConfigurator.generarArchivosConfiguracion(pnData, configurationFilePath);
		
		TransitionGroup tg = pnmlConfigurator.getConfiguration(pnmlFilePath);
		Map<String, Transition>  tFired = tg.getFiredTransitions();
		Map<String, Transition>  tInformed = tg.getInformedTransitions();
		
		
		
	}

}
