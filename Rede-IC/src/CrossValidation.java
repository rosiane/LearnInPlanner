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

public class CrossValidation
{
  public Arquivos arquivos = new Arquivos();
  public int quantidadeCiclosTreinamento = 0, quantidadeFloresTeste = 0, 
  quantidadeFloresTreinamento = 0;
  
  public int testaRede(RedeNeural rede, FileReader arquivoTeste)
    throws IOException
  {
    ClassificaFlor classificaFlor = new ClassificaFlor();
    double [] medidas = new double[4];
    int count = 0, countF = 0, countM = 0, quantidadeAcertos = 0;
    String aux = "", resposta = "";
    try
    {
      while(countF < quantidadeFloresTeste)
      {
        resposta = resposta + (char)arquivoTeste.read();
        arquivoTeste.read();
        while(countM < medidas.length)
        {
          while(count < 3)
          {
            aux = aux + (char)arquivoTeste.read();
            count++;
          }
          medidas[countM] = Double.parseDouble(aux);
          arquivoTeste.read();
          aux = "";
          count = 0;
          countM++;
        }
        arquivoTeste.read();
        count = 0;
        countM = 0;
        classificaFlor.classifica(rede, medidas);
        System.out.println("Medidas: "+medidas[0]+"\t"+medidas[1]+"\t"+medidas[2]+"\t"+medidas[3]);
        classificaFlor.imprimeResultado(rede);
        System.out.println("Resposta correta: Flor "+resposta+"\n");
        if((classificaFlor.calculaPosicaoMaior(rede)+1) == Integer.parseInt(resposta))
          quantidadeAcertos++;
        resposta = "";
        countF++;
      }
    }
    catch(IOException error)
    {
      arquivoTeste.close();
    }
    finally
    {
      arquivoTeste.close();
      return quantidadeAcertos;
    }
  }
  
  public void aplicaCrossValidation(RedeNeural rede, String caminhoTreinamento, String caminhoTeste)
  {
    quantidadeCiclosTreinamento = 5000;
    quantidadeFloresTeste = 15;
    quantidadeFloresTreinamento = 135;
    double [] mediaAcertos = new double[10];
    double mediaTotalAcertos = 0;
    for(int i = 0; i < 10; i++)
    {
      rede.inicializaTodosPesos();
      rede.zeraCorrecoesAnteriorPeso();
      try
      {
        System.out.println("\n+++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("Treinamento "+(i+1)+" de 10");
        treinaRede(rede, quantidadeCiclosTreinamento, (i+1), caminhoTreinamento);
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
        FileReader arquivoTeste = arquivos.abrirFluxoArquivoLeitura(caminhoTeste+"Flores_Teste"+(i+1)+".txt");
        System.out.println("\n#######################################################");
        System.out.println("Teste "+(i+1));
        int quantidadeAcertos = testaRede(rede, arquivoTeste);
        mediaAcertos[i] = quantidadeAcertos/15.0;
        mediaTotalAcertos = mediaTotalAcertos + mediaAcertos[i];
        System.out.println("\tQuantidade Acertos: "+quantidadeAcertos);
        System.out.println("\n#######################################################");
      }
      catch(IOException error)
      {
        System.err.println("Caminho não encontrado.\n");
      }
    }
    double soma = 0;
    double media = mediaTotalAcertos/10.0;
    for(int i = 0; i < mediaAcertos.length; i++)
      soma = soma + ((mediaAcertos[i] - media)*(mediaAcertos[i] - media));
    double desviopadrao = Math. sqrt(soma/mediaAcertos.length);
    System.out.println("\n Média de acertos: "+media);
    System.out.println("Desvio Padrão: "+desviopadrao);
  }
  
  public void treinaRede(RedeNeural rede, int quantidadeCiclosTreinamento, int num, String caminhoTreinamento)
    throws IOException
  {
    TreinamentoPorPadrao treinamentoPorPadrao = new TreinamentoPorPadrao();
    boolean stop = false;
    int countCiclos = 0, quantidadeAcertos = 0;
    System.out.println("\tTreinando...");
    for(int y = 0; y < quantidadeCiclosTreinamento; y++)
    {
      countCiclos++;
      quantidadeAcertos = treinamentoPorPadrao.treinaRede(rede, quantidadeFloresTreinamento, num, caminhoTreinamento);
      if((quantidadeAcertos/135.0) >= 0.95)
      {
        System.out.println("Porcentagem Acerto: "+(quantidadeAcertos/135.0));
        System.out.println("Quantidade de Ciclos: "+countCiclos);
        stop = true;
        break;
      }
    }
    if(stop == false)
    {
      System.out.println("Porcentagem Acerto: "+(quantidadeAcertos/135.0));
      System.out.println("Quantidade de Ciclos: "+countCiclos);
    }
  }
}