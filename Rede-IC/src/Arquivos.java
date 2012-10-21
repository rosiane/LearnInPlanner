/****************************************************************/
/*                                                              */
/*  Rosiane Correia Santos Nº USP: 5364242                      */
/*  Orientadora: Prof. Dra. Patrícia Rufino Oliveira            */ 
/*                                                              */
/*      REDE NEURAL – APRENDIZADO SUPERVISIONADO                */
/*                                                              */
/*  Algoritmo para Classificação das Flores do Conjunto Iris    */
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