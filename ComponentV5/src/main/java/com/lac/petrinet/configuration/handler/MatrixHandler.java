package com.lac.petrinet.configuration.handler;

import com.lac.petrinet.configuration.PNData;

public class MatrixHandler {
	public static MatrixHandler INSTANCE = new MatrixHandler();

	// El constructor privado no permite que se genere un constructor por defecto.
	// (con mismo modificador de acceso que la definición de la clase) 
	private MatrixHandler() {}

	public static MatrixHandler getInstance() {
		return INSTANCE;
	}
	
	
	
	private PNData pnData;
	
	public void setPNData(PNData pnd) {
		this.pnData = pnd;
	}
	
	public PNData getPNData() {
		return this.pnData;
	}

	public boolean sharePlaceVertically(int tAbove, int tBelow) {
		// TODO Auto-generated method stub
		int[][] pos = pnData.getMatrizIncidenciaPositiva();
		int[][] neg = pnData.getMatrizIncidenciaNegativa();
		
		for(int i = 0; i<pos.length; i++){
			if(pos[i][tAbove] > 0 && neg[i][tBelow] > 0) {
				return true;
			}
		}
		
		return false;
	}
}
