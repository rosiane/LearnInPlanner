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

import java.io.*;

public class Arquivos
{
  public FileReader leitura;
  
  public FileReader abrirFluxoArquivoLeitura(String arquivo)
    throws IOException
  {
    return new FileReader(arquivo);
  }
}