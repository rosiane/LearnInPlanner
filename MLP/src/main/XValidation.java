package main;

import static java.lang.System.out;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import auxiliares.Arquivo;

public class XValidation {
	public XValidation() {
		Arquivo.criarDiretorio(Arquivo.diretorioRaiz + "logsPerceptron");
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		double alvoAtivacao = 1;
		double alvoNaoAtivacao = 0;
		double taxaAprendizado = 0.2;
		double taxaErroMaxTreino = 2;
		int nEnt = 4;
		int nOcu = 9;
		int nSai = 3;
		int nPadroesTreino = 135;
		int nPadroesValidacao = 15;
		int nMaxCiclos = 1;
		int nRuns = 1;
		XValidation xVal = new XValidation();
		
		out.println("Rede iniciada");
		
		Perceptron perceptron = new Perceptron(taxaAprendizado, alvoAtivacao, alvoNaoAtivacao,
				taxaErroMaxTreino, nEnt, nOcu, nSai, nPadroesTreino, nPadroesValidacao,
				nMaxCiclos);
		
		xVal.crossValidation(perceptron, nRuns);
		
		out.println("Rede terminada");
	}
	
	public void crossValidation(Perceptron p, int nRuns) {
		double[] acertosPorcentagem = new double[nRuns];
		int[] acertos = new int[nRuns];
		
		for (int i = 0; i < nRuns; i++) {
			String strPadroesTreino = Arquivo.ler(Arquivo.diretorioRaiz + "CrossValidationPerceptron" + i + ".txt");
			String strPadroesValidacao = Arquivo.ler(Arquivo.diretorioRaiz + "ConjuntoTestePerceptron" + i + ".txt");
			List<String> listPadroesTreino = padroesStrToList(strPadroesTreino, p.getNEnt(), p.getNSai());
			List<String> listPadroesValidacao = padroesStrToList(strPadroesValidacao, p.getNEnt(), p.getNSai());
			double[][] entradasTreino = getEntradasPadroes(listPadroesTreino, p.getNPadroesTreino(), p.getNEnt());
			double[][] entradasValidacao = getEntradasPadroes(listPadroesValidacao, p.getNPadroesValidacao(), p.getNEnt());
			double[][] esperadosTreino = getEsperadosPadroes(listPadroesTreino, p.getNPadroesTreino(), p.getNSai());
			double[][] esperadosValidacao = getEsperadosPadroes(listPadroesValidacao, p.getNPadroesValidacao(), p.getNSai());
			
			out.println(listPadroesTreino.get(0));
			out.println(listPadroesTreino.get(1));
			
			p.iniciarTreino(entradasTreino, esperadosTreino);
			p.iniciarValidacao(entradasValidacao, esperadosValidacao);
			
			acertos[i] = p.getContClassificacoesCorretasValidacao();
			acertosPorcentagem[i] = (double)p.getContClassificacoesCorretasValidacao() / p.getNPadroesValidacao() * 100;
		}
		
		Arquivo.escrever(Arquivo.diretorioRaiz + "logsPerceptron/resultadosValidacao", ".txt", imprimeValidacao(nRuns, acertos, acertosPorcentagem));
		
		out.println("Fim");
	}
	
	public List<String> padroesStrToList(String padroes, int nEnt, int nSai) {
		int i = 0;
		List<String> listPadroes = new LinkedList<String>();
		String entrada = "";
		String esperado = "";
		StringTokenizer tokensPadroes = new StringTokenizer(padroes);
		
		while (tokensPadroes.hasMoreTokens()) {
			if (i < nEnt) {
				entrada += tokensPadroes.nextToken() + " ";
				i++;
			} else if (i >= nEnt && i < (nEnt + nSai)) {
				esperado += tokensPadroes.nextToken() + " ";
				i++;
			}
			if (i == (nEnt + nSai)) {
				listPadroes.add(entrada);
				listPadroes.add(esperado);
				i = 0;
				entrada = "";
				esperado = "";
			}
		}
		
		return listPadroes;
	}
	
	public double[][] getEntradasPadroes(List<String> listPadroes, int nPadroes, int nEnt) {
		double[][] entradas = new double[nPadroes][nEnt];
		Iterator<String> iterator = listPadroes.iterator();
		
		for (int i = 0; i < nPadroes && iterator.hasNext(); i++) {
			StringTokenizer tokensPadrao = new StringTokenizer(iterator.next());
			for (int j = 0; j < nEnt && tokensPadrao.hasMoreTokens(); j++) {
				entradas[i][j] = Double.parseDouble(tokensPadrao.nextToken());
			}
			iterator.next();
		}
		
		return entradas;
	}
	
	public double[][] getEsperadosPadroes(List<String> listPadroes, int nPadroes, int nSai) {
		double[][] esperados = new double[nPadroes][nSai];
		Iterator<String> iterator = listPadroes.iterator();
		
		for (int i = 0; i < nPadroes && iterator.hasNext(); i++) {
			iterator.next();
			StringTokenizer tokensPadrao = new StringTokenizer(iterator.next());
			for (int j = 0; j < nSai && tokensPadrao.hasMoreTokens(); j++) {
				esperados[i][j] = Double.parseDouble(tokensPadrao.nextToken());
			}
		}
		
		return esperados;
	}
	
	public String imprimeValidacao(int nRuns, int[] acertos, double[] acertosPorcentagem) {
		double mediaAcertos = 0;
		double mediaAcertosPorcentagem = 0;
		double variancia = 0;
		double varianciaPorcentagem = 0;
		String saida = "";
		
		for (int i = 0; i < nRuns; i++) {
			mediaAcertos += acertos[i];
			mediaAcertosPorcentagem += acertosPorcentagem[i];
		}
	
		mediaAcertos /= nRuns;
		mediaAcertosPorcentagem /= nRuns;
	
		for (int i = 0; i < nRuns; i++) {
			variancia += Math.pow(acertos[i] - mediaAcertos, 2);
			varianciaPorcentagem += Math.pow(acertosPorcentagem[i] - mediaAcertosPorcentagem, 2);
		}
	
		variancia /= nRuns;
		varianciaPorcentagem /= nRuns;
		
		saida += "Resultado da Validacao\n";
		saida += "Acertos\tPorcentagem\n";
		
		for (int i = 0; i < nRuns; i++)
			saida += acertos[i] + "\t\t" + acertosPorcentagem[i] + "\n";
		
		saida += "\n";
		saida += "M�dia de acertos: " + mediaAcertos + "\n";
		saida += "Desvio padr�o: " + Math.sqrt(variancia) + "\n";
		saida += "\n";
		saida += "M�dia de acertos (%): " + mediaAcertosPorcentagem + "\n";
		saida += "Desvio Padr�o: " + Math.sqrt(varianciaPorcentagem) + "\n";
		saida += "\n";
		
		return saida;
	}
}
