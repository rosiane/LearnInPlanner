package main;

public class Neuronio {
	private double taxaAprendizado;
	
	public Neuronio(double taxaAprendizado) {
		this.taxaAprendizado = taxaAprendizado;
	}
	
	public double forward(double[] entradas, double[][] pesos, int j) {
		return funcaoAtivacao(calculaV(entradas, pesos, j));
	}
	
	public double deltaSaida(double[] entradas, double[][] pesos, int j, double erro) {
		return erro * derivadaFuncao(calculaV(entradas, pesos, j));
	}
	
	public double deltaOculto(double[] entradas, double[][] pesosEntradas, int j,
			double[] deltas, double[][] pesosDeltas, int i) {
		double somaDelta = 0;
		
		for (int k = 0; k < deltas.length; k++)
			somaDelta += deltas[k] * pesosDeltas[i][k];
		
		return derivadaFuncao(calculaV(entradas, pesosEntradas, j)) * somaDelta;
	}
	
	public double calculaV(double[] entradas, double[][] pesos, int j) {
		double v = 0;
		
		for (int i = 0; i < entradas.length; i++)
			v += entradas[i] * pesos[i][j];
		
		return v;
	}
	
	public double funcaoAtivacao(double v) {
		return 1 / (1 + Math.exp(-v));
		//return (Math.exp(v) - Math.exp(-v)) / (Math.exp(v) + Math.exp(-v));
	}
	
	public double derivadaFuncao(double v) {
		return funcaoAtivacao(v) * (1 - funcaoAtivacao(v));
	}

	
	public double getTaxaAprendizado() {
		return taxaAprendizado;
	}

	
	public void setTaxaAprendizado(double taxaAprendizado) {
		this.taxaAprendizado = taxaAprendizado;
	}
}