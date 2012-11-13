package deeplearning;

import java.util.Random;

public class CRBM {
	int qtde_ocultas;
	int qtde_visiveis;
	double learning_rate_weights;
	double[][] weights;

	double theta_low;
	double theta_high;
	double sigma;
	double learning_rate_aj;

	double[][] recons_exemplo_por_epocas;
	int exemplo_em_questao;

	int[] epocas_EQM;

	double[][] aj_visiveis;
	double[][] aj_ocultos;

	// Energia energia;

	double[] EQM_por_epoca;

	public CRBM(int qtde_visiveis, int qtde_ocultas,
			double learning_rate_weights, double learning_rate_aj,
			double theta_low, double theta_high, double sigma, int[] epocas_EQM) {
		this.qtde_ocultas = qtde_ocultas;
		this.qtde_visiveis = qtde_visiveis;
		this.learning_rate_weights = learning_rate_weights;
		this.learning_rate_aj = learning_rate_aj;
		this.theta_low = theta_low;
		this.theta_high = theta_high;
		this.sigma = sigma;
		this.epocas_EQM = epocas_EQM;

		this.aj_visiveis = new double[1][qtde_visiveis + 1];
		this.aj_ocultos = new double[1][qtde_ocultas + 1];

		// inicializa a matriz de pesos, de tamanho (qtde_visiveis x
		// qtde_ocultas), usando
		// uma distribuicao Gaussiana com media 0 e desvio padrao 0.1.
		weights = matrizRandom(qtde_visiveis, qtde_ocultas, 0.1);
		// insere pesos para os biases (sempre 0.0) na primeira linha e primeira
		// coluna da matriz de pesos.
		weights = inserePrimeiraLinha(weights, 0.0);
		weights = inserePrimeiraColuna(weights, 0.0);
	}

	private double[][] matrizRandom(int altura, int largura, double fator) {
		double[][] retorno = new double[altura][largura];
		for (int i = 0; i < retorno.length; i++)
			for (int j = 0; j < retorno[0].length; j++)
				retorno[i][j] = /*Math.random() * fator*/(2 * new Random().nextDouble()) - 1;
		return retorno;
	}

	double[][] inserePrimeiraLinha(double[][] matriz, double valor) {
		double[][] retorno = new double[matriz.length + 1][matriz[0].length];
		for (int i = 1; i < retorno.length; i++)
			for (int j = 0; j < matriz[0].length; j++)
				retorno[i][j] = matriz[i - 1][j];
		for (int k = 0; k < retorno[0].length; k++)
			retorno[0][k] = valor;
		return retorno;
	}

	double[][] inserePrimeiraColuna(double[][] matriz, double valor) {
		double[][] retorno = new double[matriz.length][matriz[0].length + 1];
		for (int i = 0; i < matriz.length; i++)
			for (int j = 1; j < retorno[0].length; j++)
				retorno[i][j] = matriz[i][j - 1];
		for (int k = 0; k < retorno.length; k++)
			retorno[k][0] = valor;
		return retorno;
	}

	private double[][] dot(double[][] matrix1, double[][] matrix2) {
		// return Matrix.multiply(matrix1, matrix2); // LANCANDO EXCECAO DE NO
		// CLASS DEF FOUND ERROR
		return multiplyMatrices(matrix1, matrix2);
	}

	private double[][] matrizTransposta(double[][] matriz) {
		// return Matrix.getTranspose(matriz); // LANCANDO EXCECAO DE NO CLASS
		// DEF FOUND ERROR
		return getTranspose(matriz);
	}

	private double[][] ones(double[][] matriz) {
		for (int i = 0; i < matriz.length; i++)
			for (int j = 0; j < matriz[0].length; j++)
				matriz[i][j] = 1.0;
		return matriz;
	}

	private double[][] calculaAtivacoes(double[][] conjunto_treinamento) {
		// double[][] combinacoes_lineares =
		// Matrix.multiply(conjunto_treinamento, weights); // LANCANDO EXCECAO
		// DE NO CLASS DEF FOUND ERROR
		double[][] combinacoes_lineares = this.multiplyMatrices(
				conjunto_treinamento, weights);

		// soma cada combinacao linear ao valor de sigma * N(0,1)
		for (int i = 0; i < combinacoes_lineares.length; i++) {
			for (int j = 0; j < combinacoes_lineares[0].length; j++) {
				// //TESTE
				// System.out.println("combinacoes_lineares[" + i + "][" + j +
				// "] = " + combinacoes_lineares[i][j]);
				// //fim TESTE
				combinacoes_lineares[i][j] += sigma * Math.random();
			}
		}

		return combinacoes_lineares;
	}

	double funcaoLogistica(double x) {
		return funcaoLogistica(x, false);
	}

	double funcaoLogistica(double x, boolean print) {
		double exp = Math.exp(x);
		// //TESTE
		// String _exp = exp+"";
		// int pos_E = _exp.indexOf("E");
		// if(pos_E > -1) {
		// System.out.println("\t\texp = " + exp);
		// //fim TESTE
		// _exp = _exp.substring(0, pos_E);
		// exp = Double.parseDouble(_exp);
		// }
		// TESTE
		// if(print) {
		// System.out.println("\t\texp = " + exp);
		// System.out.println("\t\t(1.0 / exp) = " + (1.0 / exp));
		// System.out.println("\t\t(1.0 + (1.0 / exp)) = " + (1.0 + (1.0 /
		// exp)));
		// }
		// fim TESTE
		return (1.0 / (1.0 + (1.0 / exp)));
	}

	private double[][] calculaSigmoide(double[][] ativacoes,
			double[][] matriz_aj, int epoca) {
		double[][] retorno = new double[ativacoes.length][ativacoes[0].length];

		// coloca, na funcao sigmoide, cada valor de ativacao calculado
		// anteriorimente
		for (int i = 0; i < ativacoes.length; i++) {
			for (int j = 0; j < ativacoes[0].length; j++) {
				retorno[i][j] = theta_low + (theta_high - theta_low)
						* funcaoLogistica(matriz_aj[i][j] * ativacoes[i][j]);
				// TESTE
				// if(i < 2 && epoca >= 3995) {
				// System.out.println("Epoca: " + epoca);
				// System.out.println("\tmatriz_aj[" + i + "][" + j + "]=" +
				// matriz_aj[i][j]);
				// System.out.println("\tativacoes[" + i + "][" + j + "]=" +
				// ativacoes[i][j]);
				// System.out.println("\tmatriz_aj[" + i + "][" + j +
				// "] * ativacoes[" + i + "][" + j + "] = " + (matriz_aj[i][j] *
				// ativacoes[i][j]));
				// System.out.println("\tlogistica[" + i + "][" + j + "]=" +
				// funcaoLogistica(matriz_aj[i][j] * ativacoes[i][j], true));
				// System.out.println("\tsigmoide[" + i + "][" + j + "]=" +
				// retorno[i][j]);
				// }
				// fim TESTE
			}
		}

		return retorno;
	}

	private void normalizarPesos() {
		// Cada coluna da matriz de pesos esta relacionada a uma unidade oculta,
		// portanto cada coluna da matriz deve ter sua norma.
		for (int coluna = 0; coluna < weights[0].length; coluna++) {
			// Calculo da norma da coluna.
			double norma = 0.0;
			for (int linha = 0; linha < weights.length; linha++) {
				norma += Math.pow(weights[linha][coluna], 2);
			}
			norma = Math.sqrt(norma);

			// Atualiza cada peso da coluna dividindo pela norma encontrada
			// dessa coluna
			for (int linha = 0; linha < weights.length; linha++) {
				weights[linha][coluna] = weights[linha][coluna] / norma;
			}
		}
	}

	private double[][] hidden_layer;

	public double[][] treinar(double[][] conjunto_treinamento, int qtde_epocas) {
		int qtde_exemplos_treinamento = conjunto_treinamento.length;

		// insere unidades de biases valendo 1 na primeira coluna.
		conjunto_treinamento = inserePrimeiraColuna(conjunto_treinamento, 1);

		// Acompanhamento do exemplo em questao ao longo das epocas
		recons_exemplo_por_epocas = new double[qtde_epocas][conjunto_treinamento[0].length - 1];
		// Inicializa o vetor de Erros Quadraticos Medios por epoca
		EQM_por_epoca = new double[qtde_epocas];

		// energia = new Energia(this, qtde_epocas,
		// conjunto_treinamento.length);

		// Matrizes dos 'controles de ruido' de cada unidade da CRBM. Todos
		// iniciam com 1.
		// Valores aj das unidades visiveis: tamanho (tamanho do conjunto de
		// treinamento x qtde. de unidades visiveis + 1 [bias])
		double[][] _aj_visiveis = ones(new double[conjunto_treinamento.length][qtde_visiveis + 1]);
		// Valores aj das unidades ocultas: tamanho (tamanho do conjunto de
		// treinamento x qtde. de unidades ocultas + 1 [bias])
		double[][] _aj_ocultos = ones(new double[conjunto_treinamento.length][qtde_ocultas + 1]);

		// Retorno: a reconstrucao da camada visivel
		double[][] neg_visible_sj = null;

		// Camada oculta
		double[][] neg_hidden_sj = new double[_aj_ocultos.length][_aj_ocultos[0].length];

		for (int epoca = 1; epoca <= qtde_epocas; epoca++) {
			// //TESTE
			// System.out.println("- Epoca " + epoca + " -");
			// //fim TESTE

			// Clamp to the data and sample from the hidden units.
			// (This is the "positive CD phase", aka the reality phase.)
			double[][] pos_hidden_activations = calculaAtivacoes(conjunto_treinamento);
			// //TESTE
			// if(epoca == 1 || epoca==2|| epoca == 13 || epoca== 14 ||
			// epoca==250 || epoca==500 || epoca==1000 || epoca == 4000) {
			// System.out.println("Epoca " + epoca + "; ativacoes ocultas:");
			// Main.printArray(pos_hidden_activations);
			// }
			// //fim TESTE
			double[][] pos_hidden_sj = calculaSigmoide(pos_hidden_activations,
					_aj_ocultos, epoca);
			// //TESTE
			// if(epoca == 1 || epoca==2|| epoca==250 || epoca==500 ||
			// epoca==1000) {
			// System.out.println("Epoca " + epoca + "; sj ocultos:");
			// Main.printArray(pos_hidden_sj);
			// }
			// //fim TESTE
			// <si*sj>
			double[][] pos_associations = dot(
					matrizTransposta(conjunto_treinamento), pos_hidden_sj);

			// Reconstruct the visible units and sample again from the hidden
			// units.
			// (This is the "negative CD phase", aka the daydreaming phase.)
			double[][] neg_visible_activations = dot(pos_hidden_sj,
					matrizTransposta(weights));
			// //TESTE
			// if(epoca == 1 || epoca==2|| epoca==250 || epoca==500 ||
			// epoca==1000) {
			// System.out.println("Epoca " + epoca + "; ativacoes visiveis:");
			// Main.printArray(neg_visible_activations);
			// }
			// //fim TESTE
			neg_visible_sj = calculaSigmoide(neg_visible_activations,
					_aj_visiveis, epoca);
			// Arrumar os estados dos biases: sao sempre 1.0
			for (int i = 0; i < neg_visible_sj.length; i++) {
				neg_visible_sj[i][0] = 1.0;
			}
			double[][] neg_hidden_activations = dot(neg_visible_sj, weights);
			neg_hidden_sj = calculaSigmoide(neg_hidden_activations,
					_aj_ocultos, epoca);
			// <s^i*s^j>
			double[][] neg_associations = dot(matrizTransposta(neg_visible_sj),
					neg_hidden_sj);

			// Ajuste dos pesos da CRBM.
			for (int i = 0; i < weights.length; i++) {
				for (int j = 0; j < weights[0].length; j++) {
					weights[i][j] += learning_rate_weights
							* ((pos_associations[i][j] - neg_associations[i][j]) / (double) qtde_exemplos_treinamento);
					// //TESTE
					// System.out.println("\t\tpos_associations[" + i + "][" + j
					// + "]=" + pos_associations[i][j]);
					// System.out.println("\t\t\tneg_associations[" + i + "][" +
					// j + "]=" + neg_associations[i][j]);
					// System.out.println("\t\t\t\tpos - neg=" +
					// (pos_associations[i][j] - neg_associations[i][j]));
					// //fim TESTE
				}
			}
			// Normalizacao dos pesos ajustados.
//			normalizarPesos();

			// TESTE
			// if(epoca == 1 || epoca==2|| epoca==250 || epoca==500 ||
			// epoca==1000 || epoca == 4000) {
			// System.out.println("Epoca " + epoca + "; pesos:");
			// Main.printArray(weights);
			// }
			// fim TESTE

			// Ajuste dos 'controles de ruido' aj de cada unidade da CRBM.
			for (int exemplo_atual = 0; exemplo_atual < conjunto_treinamento.length; exemplo_atual++) {
				// Para cada exemplo do conjunto de treinamento,
				// Ajusta o aj de cada unidade visivel.
				for (int unidade_visivel_j = 0; unidade_visivel_j < _aj_visiveis[0].length; unidade_visivel_j++) {
					_aj_visiveis[exemplo_atual][unidade_visivel_j] += (learning_rate_aj / Math
							.pow(_aj_visiveis[exemplo_atual][unidade_visivel_j],
									2))
							* (((Math
									.pow(conjunto_treinamento[exemplo_atual][unidade_visivel_j],
											2)) - (Math
									.pow(neg_visible_sj[exemplo_atual][unidade_visivel_j],
											2))) / (double) qtde_exemplos_treinamento);
				}
				// Ajusta o aj de cada unidade oculta.
				for (int unidade_oculta_j = 0; unidade_oculta_j < _aj_ocultos[0].length; unidade_oculta_j++) {
					_aj_ocultos[exemplo_atual][unidade_oculta_j] += (learning_rate_aj / Math
							.pow(_aj_ocultos[exemplo_atual][unidade_oculta_j],
									2))
							* (((Math
									.pow(pos_hidden_sj[exemplo_atual][unidade_oculta_j],
											2)) - (Math
									.pow(neg_hidden_sj[exemplo_atual][unidade_oculta_j],
											2))) / (double) qtde_exemplos_treinamento);
				}
			}

			// if(mostrarEQM(epoca)) {
			double error = CRBM.calculaErroQuadraticoMedio(
					conjunto_treinamento, neg_visible_sj, true);
			System.out
					.println("\t\tEpoch: " + epoca + "\n\t\t\tEQM = " + error);
			// }

			// Atualiza a matriz global dos 'controles de ruido'
			this.aj_visiveis = _aj_visiveis;
			this.aj_ocultos = _aj_ocultos;

			// Calculo da Energia da rede baseado nos estados escontrados nas
			// reconstrucoes de cada exemplo
			// energia.atualizaEnergiasEpoca_CRBM(neg_visible_sj, pos_hidden_sj,
			// (epoca-1));

			// double error = calculaErroQuadraticoMedio(conjunto_treinamento,
			// neg_visible_sj, true);
			// EQM_por_epoca[epoca-1] = error;
			//
			// // Atualiza o vetor de acompanhamento do exemplo em questao
			// // Pular o bias:
			// for (int unidade = 0; unidade <
			// recons_exemplo_por_epocas[0].length; unidade++) {
			// recons_exemplo_por_epocas[epoca-1][unidade] =
			// neg_visible_sj[exemplo_em_questao][unidade+1];
			// }

			// //TESTE
			// if(epoca == 1 || epoca==2|| epoca==250 || epoca==500 ||
			// epoca==1000 || epoca==4000) {
			// System.out.println("Epoca " + epoca + "; aj:");
			// for(int i = 0; i < conjunto_treinamento.length; i++) {
			// System.out.print("aj_visiveis: {");
			// for(int j = 0; j < aj_visiveis[i].length; j++) {
			// System.out.print(aj_visiveis[i][j] + ",");
			// }
			// System.out.print("}\naj_ocultos: {");
			// for(int j = 0; j < aj_ocultos[i].length; j++) {
			// System.out.print(aj_ocultos[i][j] + ",");
			// }
			// System.out.println("}\n");
			// }
			// }
			// //fim TESTE

			// //TESTE
			// if(epoca == 1 || epoca==2|| epoca==250 || epoca==500 ||
			// epoca==1000) {
			// System.out.println("Epoca " + epoca + "; erro = " +
			// calculaErroQuadraticoMedio(conjunto_treinamento, neg_visible_sj,
			// false));
			// Main.printArray(neg_visible_sj);
			// }
			// //fim TESTE

			// //TESTE
			// System.out.print("\trecons: {");
			// for(int i = 0; i < neg_visible_sj[6].length; i++)
			// System.out.print(neg_visible_sj[6][i] + ", ");
			// System.out.println("}");
			// System.out.print("\thidden: {");
			// for(int i = 0; i < pos_hidden_sj[6].length; i++)
			// System.out.print(pos_hidden_sj[6][i] + ", ");
			// System.out.println("}");
			// System.out.println("\tpesos:");
			// Main.printArray(weights);
			// //fim TESTE
		}

		// Camada oculta
		this.hidden_layer = neg_hidden_sj;

		return neg_visible_sj;
	}

	public double[][] reconstruir(double[][] dados) {
		double[][] estados_ocultos = run_visible(dados);
		double[][] reconstrucao = run_hidden(estados_ocultos);

		return reconstrucao;
	}

	private double[][] run_visible(double[][] dados) {
		double[][] ativacoes = calculaAtivacoes(dados);
		double[][] sj_ocultos = this.calculaSigmoide(ativacoes,
				this.aj_visiveis, 0);

		return sj_ocultos;
	}

	private double[][] run_hidden(double[][] dados) {
		double[][] ativacoes = calculaAtivacoes(dados);
		double[][] sj_reconstrucao = this.calculaSigmoide(ativacoes,
				this.aj_ocultos, 0);

		return sj_reconstrucao;
	}

	private boolean mostrarEQM(int epoca) {
		if (epocas_EQM == null)
			return false;

		return contains(epocas_EQM, epoca);
	}

	private boolean contains(int[] array, int valor) {
		for (int i = 0; i < array.length; i++)
			if (array[i] == valor)
				return true;

		return false;
	}

	public static double calculaErroQuadraticoMedio(
			double[][] conjunto_treinamento, double[][] recons,
			boolean pular_bias) {
		double erro = 0;

		// j pode ser iniciado no for() com 1 porque os conjuntos reconstruidos
		// tem as unidades de bias na primeira coluna.
		int valor_inicio_j = (pular_bias) ? 1 : 0;

		for (int i = 0; i < conjunto_treinamento.length; i++) {
			for (int j = valor_inicio_j; j < recons[0].length; j++) {
				int posicao_j = (pular_bias) ? j - 1 : j;
				erro += Math.pow(
						(conjunto_treinamento[i][posicao_j] - recons[i][j]), 2);
			}
		}

		// Media.
		double denominador = conjunto_treinamento.length;
		if (pular_bias)
			denominador--;
		denominador *= conjunto_treinamento[0].length;
		erro /= denominador;

		return erro;
	}

	public double[][] getWeights() {
		return this.weights;
	}

	public static void main(String[] args) {
		// Parametros com valores usados no treinamento com dados artificiais do
		// artigo
		// "Continuous restricted Boltzmann machine with an implementable training algorithm",
		// de H. Chen e A.F. Murray.
		// CRBM crbm = new CRBM(10, 4, 1.5, 1.0, -1.0, 1.0, 0.2);
		//
		// double[][] conjunto_treinamento = crbm.matrizRandom(400, 10, 1);
		// double[][] conjunto_treinamento = new Iris(false).getDados();

		// Main.printArray(conjunto_treinamento);

		// CRBM crbm = new CRBM(conjunto_treinamento[0].length, 4, 1.5, 1.0,
		// -1.0, 1.0, 0.2, null);

		// double[][] recons = crbm.treinar(conjunto_treinamento, 4000);

		// Main.printArray(recons);

		// double erro = crbm.calculaErroQuadraticoMedio(conjunto_treinamento,
		// recons, true);
		// System.out.println("Erro quadratico medio: " + erro);
	}

	private double[][] multiplyMatrices(double a[][], double b[][]) {

		int aRows = a.length, aColumns = a[0].length, bRows = b.length, bColumns = b[0].length;

		if (aColumns != bRows) {
			throw new IllegalArgumentException("A:Rows: " + aColumns
					+ " did not match B:Columns " + bRows + ".");
		}

		double[][] resultant = new double[aRows][bColumns];

		for (int i = 0; i < aRows; i++) { // aRow
			for (int j = 0; j < bColumns; j++) { // bColumn
				for (int k = 0; k < aColumns; k++) { // aColumn
					resultant[i][j] += a[i][k] * b[k][j];
				}
			}
		}

		return resultant;
	}

	public static double[][] getTranspose(double[][] someMatrix) {
		double[][] resultMatrix = new double[someMatrix[0].length][];
		for (int i = 0; i < someMatrix[0].length; i++) {
			resultMatrix[i] = getCol(someMatrix, i);
		}
		return resultMatrix;
	}

	public static double[] getCol(double[][] someMatrix, int i) {
		double[] col_i = new double[someMatrix.length];
		for (int j = 0; j < someMatrix.length; j++) {
			col_i[j] = someMatrix[j][i];
		}
		return col_i;
	}

	public double[][] getHiddenLayer() {
		// Remove bias unit: first column of every row
		return removeBias(this.hidden_layer);
	}

	private double[][] removeBias(double[][] matrix) {
		double[][] new_matrix = new double[matrix.length][matrix[0].length - 1];

		for (int i = 0; i < matrix.length; i++)
			for (int j = 1; j < matrix[0].length; j++)
				new_matrix[i][j - 1] = matrix[i][j];

		return new_matrix;
	}
}
