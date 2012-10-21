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

public class ClassificaFlor
{
  public double [] medidas = new double[4];
  
  public void classifica(RedeNeural rede, double [] m)
  {
    for(int i = 0; (i < medidas.length) && (i < rede.camadaEntrada.length); i++)
    {
      medidas[i] = m[i];
      rede.camadaEntrada[i].insereEntrada(medidas[i]);
      rede.camadaEntrada[i].ativacao = medidas[i];
    }
    rede.gerarSaidas(1);
    rede.aplicarFuncaoAtivacao(2);
    rede.gerarSaidas(2);
    rede.aplicarFuncaoAtivacao(3);
    rede.gerarSaidas(3);
    rede.zeraPosicaoEntradas();
  }
  
  public int calculaPosicaoMaior(RedeNeural rede)
  {
    int maior = 0;
    for(int i = 1; i < rede.camadaSaida.length; i++)
      if(rede.camadaSaida[maior].saidas[0] < rede.camadaSaida[i].saidas[0])
      maior = i;
    return maior;
  }
  
  public void imprimeResultado(RedeNeural rede)
  {
    int maior = calculaPosicaoMaior(rede);
    System.out.println("Resultado da Classificação: Flor "+(maior+1));
  }
}