package petrinet.support.Petri;

import java.util.Random;

import petrinet.support.IOFiles.ManejadorTXT;

public class PetriNet
{
	private int[][] matriz_incidencia;
	private int[][] matrizInhibidores;
	private int[][] marcado_inicial;
	private int[][] matriz_relacion;
	private int[][] vector_cotas;
	private int[][] disparos_automaticos;
	private int[][] matriz_prioridad;
	private int[][] marcado_actual;
	private int[][] matriz_resultado;
	private int[][] sensibilizados_signo;
	private int[][] matriz_cotas;
	private int[][] sensibilizados_cotas;
	private int[][] sensibilizados_locales;
	private int[][] mascara_internos;
	private int[][] mascara_habilitacion;
	private int[][] sensibilizados_internos;
	private int[][] sensibilizados_borde;
	private int[][] sensibilizados_borde_correlacionados;
	private int[][] disparo_distribuido;
	private int[][] disparo_borde;
	private int[][] disparos_red;
	private int[][] disparo_final;
	private ColaEntrada colaEntrada = null;

	public PetriNet(int[][] _matriz_incidencia, int[][] _marcado_inicial, int[][] _matriz_relacion,
			int[][] _vector_cotas, int[][] _disparos_automaticos, int[][] _matriz_prioridad, int [][] matrizInhibidores)
	{
		this.matriz_incidencia = _matriz_incidencia;
		this.matrizInhibidores = matrizInhibidores;
		this.marcado_inicial = _marcado_inicial;
		this.matriz_relacion = _matriz_relacion;
		this.vector_cotas = _vector_cotas;
		this.disparos_automaticos = _disparos_automaticos;
		this.matriz_prioridad = _matriz_prioridad;

		this.marcado_actual = ((int[][])this.marcado_inicial.clone());

		this.matriz_resultado = new int[this.matriz_incidencia.length][this.matriz_incidencia[0].length];
		this.sensibilizados_signo = new int[this.matriz_incidencia[0].length][1];
		this.matriz_cotas = new int[this.matriz_incidencia.length][this.matriz_incidencia[0].length];
		this.sensibilizados_cotas = new int[this.matriz_incidencia[0].length][1];
		this.sensibilizados_locales = new int[this.matriz_incidencia[0].length][1];
		this.mascara_internos = new int[this.matriz_incidencia[0].length][1];
		this.mascara_habilitacion = new int[this.matriz_relacion.length][1];
		this.sensibilizados_internos = new int[this.matriz_incidencia[0].length][1];
		this.sensibilizados_borde = new int[this.matriz_relacion.length][1];
		this.sensibilizados_borde_correlacionados = new int[this.matriz_relacion.length][1];
		this.disparo_distribuido = new int[this.matriz_relacion.length][1];
		this.disparo_borde = new int[this.matriz_incidencia[0].length][1];
		this.disparos_red = new int[this.matriz_incidencia[0].length][1];
		this.disparo_final = new int[this.matriz_incidencia[0].length][1];

		this.colaEntrada = new ColaEntrada(this.matriz_incidencia[0].length);

		actualizar();
	}

	private void calcularMatrizResultado()
	{
		for (int i = 0; i < this.matriz_resultado.length; i++) {
			for (int j = 0; j < this.matriz_resultado[0].length; j++) {
				this.matriz_resultado[i][j] = (this.matriz_incidencia[i][j] + this.marcado_actual[i][0]);
			}
		}
	}

	private int[][] obtenerTransicionesInhibidas(){
		return producto(this.matrizInhibidores, this.marcado_actual);
	}

	private void calcularSensibilizadosSigno()
	{
		int[][] tempTransicionesInhibidas = obtenerTransicionesInhibidas();
		for (int j = 0; j < this.matriz_resultado[0].length; j++)
		{
			int negativo = 0;
			for (int i = 0; i < this.matriz_resultado.length; i++) {
				if (this.matriz_resultado[i][j] < 0) {
					negativo = 1;
				}
			}
			if (negativo == 0 && tempTransicionesInhibidas[j][0] == 0) {
				this.sensibilizados_signo[j][0] = 1;
			} else {
				this.sensibilizados_signo[j][0] = 0;
			}
		}
	}

		private void calcularMatrizCotas()
		{
			for (int i = 0; i < this.matriz_resultado.length; i++) {
				for (int j = 0; j < this.matriz_resultado[0].length; j++) {
					if ((this.vector_cotas[i][0] == 0) || (this.matriz_resultado[i][j] <= this.vector_cotas[i][0])) {
						this.matriz_cotas[i][j] = 1;
					} else {
						this.matriz_cotas[i][j] = 0;
					}
				}
			}
		}

		private void calcularSensibilizadosCotas()
		{
			for (int j = 0; j < this.matriz_cotas[0].length; j++)
			{
				int cumple = 1;
				for (int i = 0; i < this.matriz_cotas.length; i++) {
					if (this.matriz_cotas[i][j] == 0) {
						cumple = 0;
					}
				}
				if (cumple == 1) {
					this.sensibilizados_cotas[j][0] = 1;
				} else {
					this.sensibilizados_cotas[j][0] = 0;
				}
			}
		}

		private void calcularSensibilizadosLocales()
		{
			int[][] temp = new int[this.matriz_incidencia[0].length][1];
			for (int i = 0; i < temp.length; i++) {
				temp[i][0] = (this.colaEntrada.getEstadoCola()[i][0] | this.disparos_automaticos[i][0]);
			}
			for (int i = 0; i < this.sensibilizados_locales.length; i++) {
				// aca se define si : tiene evento de entrada o es automatico & sensibilizado por la matriz de resultado & sensibilizado 
				this.sensibilizados_locales[i][0] = (temp[i][0] & this.sensibilizados_signo[i][0] & this.sensibilizados_cotas[i][0]);
			}
		}

		private void calcularMascaraInternos()
		{
			for (int j = 0; j < this.matriz_relacion[0].length; j++)
			{
				int relacion = 0;
				for (int i = 0; i < this.matriz_relacion.length; i++) {
					if (this.matriz_relacion[i][j] != 0) {
						relacion = 1;
					}
				}
				if (relacion == 1) {
					this.mascara_internos[j][0] = 0;
				} else {
					this.mascara_internos[j][0] = 1;
				}
			}
		}

		private void calcularMascaraHabilitacion()
		{
			for (int i = 0; i < this.matriz_relacion.length; i++)
			{
				int relacion = 0;
				for (int j = 0; j < this.matriz_relacion[0].length; j++) {
					if (this.matriz_relacion[i][j] != 0) {
						relacion = 1;
					}
				}
				if (relacion == 1) {
					this.mascara_habilitacion[i][0] = 0;
				} else {
					this.mascara_habilitacion[i][0] = 1;
				}
			}
		}

		private void calcularSensibilizadosInternos()
		{
			for (int i = 0; i < this.sensibilizados_internos.length; i++) {
				this.sensibilizados_internos[i][0] = (this.sensibilizados_locales[i][0] & this.mascara_internos[i][0]);
			}
		}

		private void calcularSensibilizadosBorde()
		{
			this.sensibilizados_borde = producto(this.matriz_relacion, this.sensibilizados_locales);
		}

		private void calcularSensibilizadosBordeCorrelacionados()
		{
			for (int i = 0; i < this.sensibilizados_borde_correlacionados.length; i++) {
				this.sensibilizados_borde_correlacionados[i][0] = (this.sensibilizados_borde[i][0] | this.mascara_habilitacion[i][0]);
			}
		}

		private void calcularDisparoBorde()
		{
			this.disparo_borde = producto(transpuesta(this.matriz_relacion), this.disparo_distribuido);
		}

		private void calcularDisparosRed()
		{
			for (int i = 0; i < this.disparos_red.length; i++) {
				this.disparos_red[i][0] = (this.sensibilizados_internos[i][0] | this.disparo_borde[i][0]);
			}
		}

		private void calcularDisparoFinal()
		{
			int[][] temp = producto(this.matriz_prioridad, this.disparos_red);
			int cantidadPosibles=0;
			int indexPosibles[] = new int[temp.length];

			int[][] seleccion = new int[this.matriz_incidencia[0].length][1];
			for (int i = 0; i < temp.length; i++) {
				if (temp[i][0] == 1)
				{
					indexPosibles[cantidadPosibles] = i;
					cantidadPosibles++;
				}
			}
			if(cantidadPosibles > 0 ){
				Random r = new Random();
				int index = indexPosibles[r.nextInt(cantidadPosibles)];
				seleccion[index][0] = 1;
			}
			this.disparo_final = producto(transpuesta(this.matriz_prioridad), seleccion);
		}

		public void actualizar()
		{
			calcularMatrizResultado();
			calcularSensibilizadosSigno();
			calcularMatrizCotas();
			calcularSensibilizadosCotas();
			calcularSensibilizadosLocales();
			calcularMascaraInternos();
			calcularMascaraHabilitacion();
			calcularSensibilizadosInternos();
			calcularSensibilizadosBorde();
			calcularSensibilizadosBordeCorrelacionados();

			calcularDisparoBorde();
			calcularDisparosRed();
			calcularDisparoFinal();
		}

		public int[][] getSensibilizadosBordeCorrelacionados()
		{
			return this.sensibilizados_borde_correlacionados;
		}

		public void setDisparoDistribuido(int[][] _disparo_distribuido)
		{
			this.disparo_distribuido = _disparo_distribuido;
			calcularDisparoBorde();
			calcularDisparosRed();
			calcularDisparoFinal();
		}

		public int[][] getPrioridades()
		{
			return this.matriz_prioridad;
		}

		public void setPrioridades(int[][] _prioridades)
		{
			this.matriz_prioridad = _prioridades;
		}

		public ColaEntrada getColaEntrada()
		{
			return this.colaEntrada;
		}

		public String getMarcado()
		{
			String marcado = "(";
			for (int i = 0; i < this.marcado_actual.length - 1; i++) {
				marcado = marcado + this.marcado_actual[i][0] + " ";
			}
			marcado = marcado + this.marcado_actual[(this.marcado_actual.length - 1)][0];
			marcado = marcado + ")";

			return marcado;
		}

		public int disparar()
		{
			for (int i = 0; i < this.disparo_final.length; i++) {
				if (this.disparo_final[i][0] == 1)
				{
					this.marcado_actual = producto(this.matriz_resultado, this.disparo_final);
					this.colaEntrada.restarDisparo(this.disparo_final);
					actualizar();

					return i;
				}
			}
			actualizar();
			return -1;
		}

		public static PetriNet createPetriNet(String _incidenciaPath, String _marcadoPath, 
				String _relacionPath, String _cotasPath, String _automaticosPath, String _prioridadPath, String _inhibidoresPath)
		{
			return new PetriNet(ManejadorTXT.getMatrix(_incidenciaPath), ManejadorTXT.getMatrix(_marcadoPath), 
					ManejadorTXT.getMatrix(_relacionPath), ManejadorTXT.getMatrix(_cotasPath), 
					ManejadorTXT.getMatrix(_automaticosPath), ManejadorTXT.getMatrix(_prioridadPath), ManejadorTXT.getMatrix(_inhibidoresPath));
		}

		public static int[][] producto(int[][] A, int[][] B)
		{
			int suma = 0;
			int[][] result = new int[A.length][B[0].length];
			for (int i = 0; i < A.length; i++) {
				for (int j = 0; j < B[0].length; j++)
				{
					suma = 0;
					for (int k = 0; k < B.length; k++) {
						suma += A[i][k] * B[k][j];
					}
					result[i][j] = suma;
				}
			}
			return result;
		}

		public static int[][] transpuesta(int[][] A)
		{
			int[][] result = new int[A[0].length][A.length];
			for (int i = 0; i < result.length; i++) {
				for (int j = 0; j < result[0].length; j++) {
					result[i][j] = A[j][i];
				}
			}
			return result;
		}

		public int[][] getMatrizInhibidores() {
			return matrizInhibidores;
		}
	}

