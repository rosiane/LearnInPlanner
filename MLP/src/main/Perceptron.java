package main;

import static java.lang.System.out;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import auxiliares.Arquivo;

public class Perceptron {
	private double alvoAtivacao;
	private double alvoNaoAtivacao;
	private double somaQuadradoErros;
	private double taxaErro;
	private double taxaErroMaxTreino;
	private double[] deltasOcu;
	private double[] deltasSai;
	private double[] erros;
	private double[] saidasTreino;
	private double[] saidasValidacao;
	private double[] saidasOcuTreino;
	private double[] saidasOcuValidacao;
	private double[][] entradasTreino;
	private double[][] entradasValidacao;
	private double[][] esperadosTreino;
	private double[][] esperadosValidacao;
	private double[][] pesosEntOcu;
	private double[][] pesosOcuSai;
	private int contCiclos;
	private int contRuns;
	private int nEnt;
	private int nMaxCiclos;
	private int nOcu;
	private int nPadroesTreino;
	private int nPadroesValidacao;
	private int nSai;
	private int contClassificacoesCorretasTreino;
	private int contClassificacoesCorretasValidacao;
	private int[][] classificacoesValidacao;
	private List<Double> listSomaQuadradoErros;
	private List<Double> listTaxaErro;
	private Neuronio[] camadaOcu;
	private Neuronio[] camadaSai;
	
	public Perceptron(double taxaAprendizado, double alvoAtivacao,
			double alvoNaoAtivacao, double taxaErroMaxTreino, int nEnt,
			int nOcu, int nSai, int nPadroesTreino, int nPadroesValidacao,
			int nMaxCiclos) {
		this.alvoAtivacao = alvoAtivacao;
		this.alvoNaoAtivacao = alvoNaoAtivacao;
		this.taxaErroMaxTreino = taxaErroMaxTreino;
		this.nEnt = nEnt;
		this.nMaxCiclos = nMaxCiclos;
		this.nOcu = nOcu;
		this.nPadroesTreino = nPadroesTreino;
		this.nPadroesValidacao = nPadroesValidacao;
		this.nSai = nSai;
		deltasOcu = new double[nOcu];
		deltasSai = new double[nSai];
		erros = new double[nSai];
		saidasTreino = new double[nSai];
		saidasValidacao = new double[nSai];
		saidasOcuTreino = new double[nOcu];
		saidasOcuValidacao = new double[nOcu];
		pesosEntOcu = new double[nEnt][nOcu];
		pesosOcuSai = new double[nOcu][nSai];
		entradasTreino = new double[nPadroesTreino][nEnt];
		entradasValidacao = new double[nPadroesValidacao][nEnt];
		esperadosTreino = new double[nPadroesTreino][nSai];
		esperadosValidacao = new double[nPadroesValidacao][nSai];
		classificacoesValidacao = new int[nPadroesValidacao][2];
		listSomaQuadradoErros = new LinkedList<Double>();
		listTaxaErro = new LinkedList<Double>();
		camadaOcu = new Neuronio[nOcu];
		camadaSai = new Neuronio[nSai];

		for (int i = 0; i < nOcu; i++)
			camadaOcu[i] = new Neuronio(taxaAprendizado);

		for (int i = 0; i < nSai; i++)
			camadaSai[i] = new Neuronio(taxaAprendizado);
	}

	public void iniciarTreino(double[][] entradas, double[][] esperados) {
		entradasTreino = entradas;
		esperadosTreino = esperados;
		contCiclos = 0;
		taxaErro = 100;
		contClassificacoesCorretasTreino = 0;
		inicializaPesos();

		Arquivo.escrever(Arquivo.diretorioRaiz
				+ "logsPerceptron/inicializacaoTreino", ".txt",
				imprimeInicioTreino());
		out.println("Treinando...");

		while (taxaErro > taxaErroMaxTreino && contCiclos < nMaxCiclos) {
			contClassificacoesCorretasTreino = 0;
			somaQuadradoErros = 0;

			out.println("Ciclo: " + (contCiclos + 1));

			for (int j = 0; j < 1/*nPadroesTreino*/; j++) {
				int max = 0;
				System.out.println("Forward");
				System.out.println("Entrada Oculta");
				for (int k = 0; k < nOcu; k++) {
					saidasOcuTreino[k] = camadaOcu[k].forward(
							entradasTreino[j], pesosEntOcu, k);
					System.out.println("[" + k + "]" + saidasOcuTreino[k]);
				}

				System.out.println("Oculta Saída");
				for (int k = 0; k < nSai; k++) {
					saidasTreino[k] = camadaSai[k].forward(saidasOcuTreino,
							pesosOcuSai, k);
					System.out.println("[" + k + "]" + saidasTreino[k]);
				}

				System.out.println("Erro");
				for (int k = 0; k < nSai; k++) {
					erros[k] = esperadosTreino[j][k] - saidasTreino[k];
					System.out.print(erros[k] + " ");
				}

				System.out.println();

				System.out.println("Backpropagation");
				System.out.println("Delta Saída");
				for (int k = 0; k < nSai; k++) {
					deltasSai[k] = camadaSai[k].deltaSaida(saidasOcuTreino,
							pesosOcuSai, k, erros[k]);
					System.out.println("[" + k + "]" + deltasSai[k]);
				}

				System.out.println("Update Oculta Saída");
				for (int k = 0; k < nOcu; k++) {
					for (int l = 0; l < nSai; l++){
						pesosOcuSai[k][l] += camadaSai[l].getTaxaAprendizado()
								* deltasSai[l] * saidasOcuTreino[k];
						System.out.print("[" + k + "][" + l + "]" + pesosOcuSai[k][l]);
					}
					System.out.println();
				}

				System.out.println("Delta Oculta");
				for (int k = 0; k < nOcu; k++){
					deltasOcu[k] = camadaOcu[k].deltaOculto(entradasTreino[j],
							pesosEntOcu, k, deltasSai, pesosOcuSai, k);
					System.out.println("[" + k + "]" +  deltasOcu[k]);
				}

				System.out.println("Update Entrada Oculta");
				for (int k = 0; k < nEnt; k++) {
					for (int l = 0; l < nOcu; l++){
						pesosEntOcu[k][l] += camadaOcu[l].getTaxaAprendizado()
								* deltasOcu[l] * entradasTreino[j][k];
						System.out.print("[" + k + "][" + l + "]" + pesosEntOcu[k][l]);
					}
					System.out.println();
				}

				for (int k = 1; k < nSai; k++) {
					if (saidasTreino[k] > saidasTreino[max])
						max = k;
				}

				if (esperadosTreino[j][max] == alvoAtivacao)
					contClassificacoesCorretasTreino++;

				for (int k = 0; k < nSai; k++)
					somaQuadradoErros += Math.pow(erros[k], 2);

				// Arquivo.escrever(Arquivo.diretorioRaiz +
				// "logsPerceptron/treinoSaidasPorPadrao", ".txt",
				// imprimeTreinoCadaPadrao();
			} // Fim de um Padr�o

			taxaErro = 100 - ((double) contClassificacoesCorretasTreino / nPadroesTreino) * 100;
			listTaxaErro.add(taxaErro);
			listSomaQuadradoErros.add(somaQuadradoErros);
			contCiclos++;
		} // Fim do Treino
		Arquivo.escrever(Arquivo.diretorioRaiz + "logsPerceptron/finalTreino",
				".txt", imprimeFinalTreino());
		Arquivo.escrever(Arquivo.diretorioRaiz
				+ "logsPerceptron/plotContCiclos", ".txt",
				imprimePlotContCiclos());
		Arquivo.escrever(Arquivo.diretorioRaiz
				+ "logsPerceptron/plotSomaQuadradoErros", ".txt",
				imprimePlotErrosQuadraticos());
		Arquivo.escrever(
				Arquivo.diretorioRaiz + "logsPerceptron/plotTaxaErros", ".txt",
				imprimePlotTaxaErro());

		out.println("Treinamento terminado. Num. Ciclos: " + contCiclos);
		out.println();
	}

	public void iniciarValidacao(double[][] entradas, double[][] esperados) {
		entradasValidacao = entradas;
		esperadosValidacao = esperados;
		contClassificacoesCorretasValidacao = 0;

		Arquivo.escrever(Arquivo.diretorioRaiz
				+ "logsPerceptron/inicializacaoValidacao", ".txt",
				imprimeInicioValidacao());
		out.println("Validando...");

		for (int j = 0; j < nPadroesValidacao; j++) {
			int max = 0;

			for (int k = 0; k < nOcu; k++)
				saidasOcuValidacao[k] = camadaOcu[k].forward(
						entradasValidacao[j], pesosEntOcu, k);

			for (int k = 0; k < nSai; k++)
				saidasValidacao[k] = camadaSai[k].forward(saidasOcuValidacao,
						pesosOcuSai, k);

			for (int k = 1; k < nSai; k++) {
				if (saidasValidacao[k] > saidasValidacao[max])
					max = k;
			}
			if (esperadosValidacao[j][max] == alvoAtivacao) {
				contClassificacoesCorretasValidacao++;
			}

			classificacoesValidacao[j][0] = max;
			classificacoesValidacao[j][1] = esperadoToInt(esperadosValidacao, j);
			// Arquivo.escrever(Arquivo.diretorioRaiz +
			// "logsPerceptron/validacaoSaidasPorPadrao", ".txt",
			// imprimeValidacao());
		} // Fim da Validacao

		Arquivo.escrever(Arquivo.diretorioRaiz
				+ "logsPerceptron/classificacoesValidacao", ".txt",
				imprimeClassificacoesValidacao());

		out.println("Valida��o Terminada. Acertos: "
				+ contClassificacoesCorretasValidacao);
		out.println();

		contRuns++;
	}

	public void inicializaPesos() {
		for (int i = 0; i < nEnt; i++) {
			for (int j = 0; j < nOcu; j++)
				pesosEntOcu[i][j] = Math.random() * 2 - 1;
		}

		for (int i = 0; i < nOcu; i++) {
			for (int j = 0; j < nSai; j++)
				pesosOcuSai[i][j] = Math.random() * 2 - 1;
		}
	}

	public int esperadoToInt(double[][] esperados, int i) {
		int classe = 0;

		for (int j = 0; j < nSai; j++) {
			if (esperados[i][j] == alvoAtivacao) {
				classe = j;
				break;
			}
		}

		return classe;
	}

	public String imprimeInicioTreino() {
		String saida = "";

		saida += "******************** RUN " + (contRuns + 1)
				+ " ********************\n";
		saida += "alvoAtivacao: " + alvoAtivacao + "\n";
		saida += "alvoNaoAtivacao: " + alvoNaoAtivacao + "\n";
		saida += "taxaErroMaxTreino: " + taxaErroMaxTreino + "\n";
		saida += "nEnt: " + nEnt + "\n";
		saida += "nMaxCiclos: " + nMaxCiclos + "\n";
		saida += "nOcu: " + nOcu + "\n";
		saida += "nPadroesTreino: " + nPadroesTreino + "\n";
		saida += "nSai: " + nSai + "\n";

		saida += "entradasTreino\n";
		for (int i = 0; i < nPadroesTreino; i++) {
			for (int j = 0; j < nEnt; j++)
				saida += entradasTreino[i][j] + "   ";
			saida += "\n";
		}
		saida += "\n";
		saida += "esperadosTreino\n";
		for (int i = 0; i < nPadroesTreino; i++) {
			for (int j = 0; j < nSai; j++)
				saida += esperadosTreino[i][j] + "   ";
			saida += "\n";
		}
		saida += "\n";

		return saida;
	}

	public String imprimeInicioValidacao() {
		String saida = "";

		saida += "******************** RUN " + (contRuns + 1)
				+ " ********************\n";
		saida += "alvoAtivacao: " + alvoAtivacao + "\n";
		saida += "alvoNaoAtivacao: " + alvoNaoAtivacao + "\n";
		saida += "nEnt: " + nEnt + "\n";
		saida += "nOcu: " + nOcu + "\n";
		saida += "nPadroesValidacao: " + nPadroesValidacao + "\n";
		saida += "nSai: " + nSai + "\n";

		saida += "entradasValidacao\n";
		for (int i = 0; i < nPadroesValidacao; i++) {
			for (int j = 0; j < nEnt; j++)
				saida += entradasValidacao[i][j] + "   ";
			saida += "\n";
		}
		saida += "\n";
		saida += "esperadosValidacao\n";
		for (int i = 0; i < nPadroesValidacao; i++) {
			for (int j = 0; j < nSai; j++)
				saida += esperadosValidacao[i][j] + "   ";
			saida += "\n";
		}
		saida += "\n";

		return saida;
	}

	public String imprimeTreinoCadaPadrao() {
		String saida = "";

		saida += "saidasOcuTreino\n";
		for (int i = 0; i < nOcu; i++)
			saida += saidasOcuTreino[i] + "   ";
		saida += "\n";
		saida += "saidasTreino\n";
		for (int i = 0; i < nPadroesTreino; i++)
			saida += saidasTreino[i] + "   ";
		saida += "\n";
		saida += "erros\n";
		for (int i = 0; i < nPadroesTreino; i++)
			saida += erros[i] + "   ";
		saida += "\n";
		saida += "deltasSai\n";
		for (int i = 0; i < nPadroesTreino; i++)
			saida += deltasSai[i] + "   ";
		saida += "\n";
		saida += "deltasOcu\n";
		for (int i = 0; i < nPadroesTreino; i++)
			saida += deltasOcu[i] + "   ";
		saida += "\n";

		return saida;
	}

	public String imprimeTreinoCiclo() {
		String saida = "";

		saida += "somaQuadradoErros: " + somaQuadradoErros + "\n";
		saida += "taxaErro: " + taxaErro + "\n";
		saida += "contCiclos: " + contCiclos + "\n";

		return saida;
	}

	public String imprimePesos() {
		String saida = "";

		saida += "pesosEntOcu\n";
		for (int i = 0; i < nEnt; i++) {
			for (int j = 0; j < nOcu; j++)
				saida += pesosEntOcu[i][j] + "   ";
			saida += "\n";
		}
		saida += "\n";
		saida += "pesosOcuSai\n";
		for (int i = 0; i < nOcu; i++) {
			for (int j = 0; j < nSai; j++)
				saida += pesosOcuSai[i][j] + "   ";
			saida += "\n";
		}
		saida += "\n";

		return saida;
	}

	public String imprimeFinalTreino() {
		String saida = "";

		saida += "******************** RUN " + (contRuns + 1)
				+ " ********************\n";
		saida += "contClassificacoesCorretasTreino: "
				+ contClassificacoesCorretasTreino + "\n";
		saida += "contCiclos: " + contCiclos + "\n\n";

		return saida;
	}

	public String imprimeValidacaoSaidasPadrao() {
		String saida = "";

		saida += "saidasOcuValidacao\n";
		for (int i = 0; i < nPadroesValidacao; i++)
			saida += saidasOcuValidacao[i] + "   ";
		saida += "\n";
		saida += "saidasValidacao\n";
		for (int i = 0; i < nPadroesValidacao; i++)
			saida += saidasValidacao[i] + "   ";
		saida += "\n";

		return saida;
	}

	public String imprimeClassificacoesValidacao() {
		String saida = "";

		saida += "******************** RUN " + (contRuns + 1)
				+ " ********************\n";
		saida += "Modelo:\n";
		saida += "[classificacao da rede] - [esperado] -> [CORRETO/ERRADO]\n\n";

		for (int i = 0; i < nPadroesValidacao; i++) {
			saida += (classificacoesValidacao[i][0] + 1) + " - "
					+ (classificacoesValidacao[i][1] + 1) + " -> ";

			if (classificacoesValidacao[i][0] == classificacoesValidacao[i][1])
				saida += "CORRETO\n";
			else
				saida += "ERRADO\n";
		}
		saida += "\n";
		saida += "Acertos: " + contClassificacoesCorretasValidacao + " de "
				+ nPadroesValidacao + "\n";
		saida += "Porcentagem: "
				+ ((double) contClassificacoesCorretasValidacao
						/ nPadroesValidacao * 100) + "\n";
		saida += "\n";

		return saida;
	}

	public String imprimePlotContCiclos() {
		String saida = "";

		saida += contCiclos + "\n";

		return saida;
	}

	public String imprimePlotErrosQuadraticos() {
		Iterator<Double> iterator = listSomaQuadradoErros.iterator();
		String saida = "";

		saida += "******************** RUN " + (contRuns + 1)
				+ " ********************\n";

		while (iterator.hasNext())
			saida += iterator.next().toString().replace('.', ',') + "\n";

		saida += "\n\n";

		return saida;
	}

	public String imprimePlotTaxaErro() {
		Iterator<Double> iterator = listTaxaErro.iterator();
		String saida = "";

		saida += "******************** RUN " + (contRuns + 1)
				+ " ********************\n";

		while (iterator.hasNext())
			saida += iterator.next().toString().replace('.', ',') + "\n";

		saida += "\n\n";

		return saida;
	}

	public double getAlvoAtivacao() {
		return alvoAtivacao;
	}

	public void setAlvoAtivacao(double alvoAtivacao) {
		this.alvoAtivacao = alvoAtivacao;
	}

	public double getAlvoNaoAtivacao() {
		return alvoNaoAtivacao;
	}

	public void setAlvoNaoAtivacao(double alvoNaoAtivacao) {
		this.alvoNaoAtivacao = alvoNaoAtivacao;
	}

	public Neuronio[] getCamadaOcu() {
		return camadaOcu;
	}

	public void setCamadaOcu(Neuronio[] camadaOcu) {
		this.camadaOcu = camadaOcu;
	}

	public Neuronio[] getCamadaSai() {
		return camadaSai;
	}

	public void setCamadaSai(Neuronio[] camadaSai) {
		this.camadaSai = camadaSai;
	}

	public int[][] getClassificacoesValidacao() {
		return classificacoesValidacao;
	}

	public void setClassificacoesValidacao(int[][] classificacoesValidacao) {
		this.classificacoesValidacao = classificacoesValidacao;
	}

	public int getContCiclos() {
		return contCiclos;
	}

	public void setContCiclos(int contCiclos) {
		this.contCiclos = contCiclos;
	}

	public int getContClassificacoesCorretasTreino() {
		return contClassificacoesCorretasTreino;
	}

	public void setContClassificacoesCorretasTreino(
			int contClassificacoesCorretasTreino) {
		this.contClassificacoesCorretasTreino = contClassificacoesCorretasTreino;
	}

	public int getContClassificacoesCorretasValidacao() {
		return contClassificacoesCorretasValidacao;
	}

	public void setContClassificacoesCorretasValidacao(
			int contClassificacoesCorretasValidacao) {
		this.contClassificacoesCorretasValidacao = contClassificacoesCorretasValidacao;
	}

	public int getContRuns() {
		return contRuns;
	}

	public void setContRuns(int contRuns) {
		this.contRuns = contRuns;
	}

	public double[] getDeltasOcu() {
		return deltasOcu;
	}

	public void setDeltasOcu(double[] deltasOcu) {
		this.deltasOcu = deltasOcu;
	}

	public double[] getDeltasSai() {
		return deltasSai;
	}

	public void setDeltasSai(double[] deltasSai) {
		this.deltasSai = deltasSai;
	}

	public double[][] getEntradasTreino() {
		return entradasTreino;
	}

	public void setEntradasTreino(double[][] entradasTreino) {
		this.entradasTreino = entradasTreino;
	}

	public double[][] getEntradasValidacao() {
		return entradasValidacao;
	}

	public void setEntradasValidacao(double[][] entradasValidacao) {
		this.entradasValidacao = entradasValidacao;
	}

	public double[] getErros() {
		return erros;
	}

	public void setErros(double[] erros) {
		this.erros = erros;
	}

	public double[][] getEsperadosTreino() {
		return esperadosTreino;
	}

	public void setEsperadosTreino(double[][] esperadosTreino) {
		this.esperadosTreino = esperadosTreino;
	}

	public double[][] getEsperadosValidacao() {
		return esperadosValidacao;
	}

	public void setEsperadosValidacao(double[][] esperadosValidacao) {
		this.esperadosValidacao = esperadosValidacao;
	}

	public List<Double> getListSomaQuadradoErros() {
		return listSomaQuadradoErros;
	}

	public void setListSomaQuadradoErros(List<Double> listSomaQuadradoErros) {
		this.listSomaQuadradoErros = listSomaQuadradoErros;
	}

	public List<Double> getListTaxaErro() {
		return listTaxaErro;
	}

	public void setListTaxaErro(List<Double> listTaxaErro) {
		this.listTaxaErro = listTaxaErro;
	}

	public int getNEnt() {
		return nEnt;
	}

	public void setNEnt(int ent) {
		nEnt = ent;
	}

	public int getNMaxCiclos() {
		return nMaxCiclos;
	}

	public void setNMaxCiclos(int maxCiclos) {
		nMaxCiclos = maxCiclos;
	}

	public int getNOcu() {
		return nOcu;
	}

	public void setNOcu(int ocu) {
		nOcu = ocu;
	}

	public int getNPadroesTreino() {
		return nPadroesTreino;
	}

	public void setNPadroesTreino(int padroesTreino) {
		nPadroesTreino = padroesTreino;
	}

	public int getNPadroesValidacao() {
		return nPadroesValidacao;
	}

	public void setNPadroesValidacao(int padroesValidacao) {
		nPadroesValidacao = padroesValidacao;
	}

	public int getNSai() {
		return nSai;
	}

	public void setNSai(int sai) {
		nSai = sai;
	}

	public double[][] getPesosEntOcu() {
		return pesosEntOcu;
	}

	public void setPesosEntOcu(double[][] pesosEntOcu) {
		this.pesosEntOcu = pesosEntOcu;
	}

	public double[][] getPesosOcuSai() {
		return pesosOcuSai;
	}

	public void setPesosOcuSai(double[][] pesosOcuSai) {
		this.pesosOcuSai = pesosOcuSai;
	}

	public double[] getSaidasOcuTreino() {
		return saidasOcuTreino;
	}

	public void setSaidasOcuTreino(double[] saidasOcuTreino) {
		this.saidasOcuTreino = saidasOcuTreino;
	}

	public double[] getSaidasOcuValidacao() {
		return saidasOcuValidacao;
	}

	public void setSaidasOcuValidacao(double[] saidasOcuValidacao) {
		this.saidasOcuValidacao = saidasOcuValidacao;
	}

	public double[] getSaidasTreino() {
		return saidasTreino;
	}

	public void setSaidasTreino(double[] saidasTreino) {
		this.saidasTreino = saidasTreino;
	}

	public double[] getSaidasValidacao() {
		return saidasValidacao;
	}

	public void setSaidasValidacao(double[] saidasValidacao) {
		this.saidasValidacao = saidasValidacao;
	}

	public double getSomaQuadradoErros() {
		return somaQuadradoErros;
	}

	public void setSomaQuadradoErros(double somaQuadradoErros) {
		this.somaQuadradoErros = somaQuadradoErros;
	}

	public double getTaxaErro() {
		return taxaErro;
	}

	public void setTaxaErro(double taxaErro) {
		this.taxaErro = taxaErro;
	}

	public double getTaxaErroMaxTreino() {
		return taxaErroMaxTreino;
	}

	public void setTaxaErroMaxTreino(double taxaErroMaxTreino) {
		this.taxaErroMaxTreino = taxaErroMaxTreino;
	}

}