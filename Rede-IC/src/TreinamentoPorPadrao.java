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

public class TreinamentoPorPadrao
{
  public int treinaRede(RedeNeural rede, int quantidadeFlores, int num, String caminhoTreinamento)
    throws IOException
  {
    Backpropagation backpropagation = new Backpropagation();
    ClassificaFlor classificaFlor = new ClassificaFlor();
    double d1 = 0, d2 = 0, d3 = 0;
    double [] medidas = new double[4];
    int count = 0, countF = 0, countM = 0, quantidadeAcertos = 0;
    String aux = "", aux2 = "";
    Arquivos arquivos = new Arquivos();
    FileReader arquivoTreinamento = arquivos.abrirFluxoArquivoLeitura(caminhoTreinamento+"Flores_TR"+num+".txt");
    try
    {
     while(countF < quantidadeFlores)
     {
      aux = aux + (char)arquivoTreinamento.read();
      if(aux.compareTo("1") == 0)
        {
          
          d1 = 1;
          d2 = 0;
          d3 = 0;
        }
        else if(aux.compareTo("2") == 0)
        {
          d1 = 0;
          d2 = 1;
          d3 = 0;
        }
        else if(aux.compareTo("3") == 0)
        {
          d1 = 0;
          d2 = 0;
          d3 = 1;
        }
        arquivoTreinamento.read();
        aux2 = aux;
        aux = "";
        while(countM < medidas.length)
        {
          while(count < 3)
          {
            aux = aux + (char)arquivoTreinamento.read();
            count++;
          }
          medidas[countM] = Double.parseDouble(aux);
          arquivoTreinamento.read();
          aux = "";
          count = 0;
          countM++;
        }
        arquivoTreinamento.read();
        count = 0;
        countM = 0;
        classificaFlor.classifica(rede, medidas);
        if((classificaFlor.calculaPosicaoMaior(rede)+1) == (Integer.parseInt(aux2)))
          quantidadeAcertos++;
        backpropagation.aplicaBackpropagation(rede, d1, d2, d3);
        countF++;
      }
    }
    catch(IOException error)
    {
      arquivoTreinamento.close();
    }
    arquivoTreinamento.close();
    return quantidadeAcertos;
  }
}