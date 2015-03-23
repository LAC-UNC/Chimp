package com.lac.petrinet.configuration.data;

public class Plaza {
	private int posicionFilaMatriz;
	private int marcadoPlaza;

	public Plaza(int posicionFilaMatriz, int marcadoPlaza) {
		super();
		this.posicionFilaMatriz = posicionFilaMatriz;
		this.marcadoPlaza = marcadoPlaza;
	}

	public int getPosicionFilaMatriz() {
		return posicionFilaMatriz;
	}

	public void setPosicionFilaMatriz(int posicionFilaMatriz) {
		this.posicionFilaMatriz = posicionFilaMatriz;
	}

	public int getMarcadoPlaza() {
		return marcadoPlaza;
	}

	public void setMarcadoPlaza(int marcadoPlaza) {
		this.marcadoPlaza = marcadoPlaza;
	}

}
