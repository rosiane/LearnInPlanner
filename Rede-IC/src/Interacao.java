/****************************************************************/
/*                                                              */
/*  Rosiane Correia Santos N� USP: 5364242                      */
/*  Orientadora: Prof. Dra. Patr�cia Rufino Oliveira            */ 
/*                                                              */
/*      REDE NEURAL � APRENDIZADO SUPERVISIONADO                */
/*                                                              */
/*  Algoritmo para Classifica��o das Flores do Conjunto Iris    */
/*                                                              */
/****************************************************************/

import java.util.*;

public class Interacao
{
  public static void main(String args[])
  {
    int numeroNeuronioCamadaOculta = 0;
    Scanner sc = new Scanner(System.in);
    System.out.print("\nDigite o numero de neuronios da camada oculta: ");
    numeroNeuronioCamadaOculta = Integer.parseInt(sc.nextLine());
    System.out.println(numeroNeuronioCamadaOculta+"\n");
    String caminhoTreinamento = "/home/rosy/Documentos/USP/Mestrado/Meu Trabalho/Outros/Arquivos_Treinamentos/";
    String caminhoTeste = "/home/rosy/Documentos/USP/Mestrado/Meu Trabalho/Outros/Arquivos_Testes/";
    RedeNeural rede = new RedeNeural(numeroNeuronioCamadaOculta);
    CrossValidation crossValidation = new CrossValidation();
    crossValidation.aplicaCrossValidation(rede, caminhoTreinamento, caminhoTeste);
  }
}