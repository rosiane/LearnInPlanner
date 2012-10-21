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

public class Backpropagation 
{
  public double [] gradientesSaida = new double[3];
  
  public double calculaGradiente(double e, double y)
  {
    return e*(y*(1 - y));
  }
  
  public double[] calculaErroCamadaSaida(RedeNeural rede, double d1, double d2, double d3)
  {
    double [] erros = new double[3];
    erros[0] = d1 - rede.camadaSaida[0].saidas[0];
    erros[1] = d2 - rede.camadaSaida[1].saidas[0];
    erros[2] = d3 - rede.camadaSaida[2].saidas[0];
    return erros;
  }
  
  public double [] calculaSomatorio(RedeNeural rede)
  {
    double [] somatorio = new double[rede.camadaOculta.length];
    for(int i = 0; i < rede.camadaOculta.length; i++)
    {
      for(int y = 0; y < rede.camadaOculta[i].pesos.length; y++)
      {
        somatorio[i] = somatorio[i] + (rede.camadaOculta[i].pesos[y]*gradientesSaida[y]);
      }
    }
    return somatorio;
  }
  
  public void aplicaBackpropagation(RedeNeural rede, double d1, double d2, double d3)
  {
   
    corrigePesoCamadaOcultaSaida(rede, d1, d2, d3);
    corrigePesoCamadaEntradaOculta(rede);
  }
  
  public void corrigePesoCamadaEntradaOculta(RedeNeural rede)
  {
    double [] somatorio = calculaSomatorio(rede);
    for(int i = 0; i < rede.camadaEntrada.length; i++)
    {
      for(int y = 0; y < rede.camadaOculta.length; y++)
      {
        rede.camadaEntrada[i].pesos[y] = rede.camadaEntrada[i].pesos[y] + ((0.1*rede.camadaEntrada[i].correcaoAnteriorPeso[y]) + (0.2*(rede.camadaOculta[y].ativacao*somatorio[y])*rede.camadaEntrada[i].ativacao));
        rede.camadaEntrada[i].correcaoAnteriorPeso[y] = (0.1*rede.camadaEntrada[i].correcaoAnteriorPeso[y]) + (0.2*(rede.camadaOculta[y].ativacao*somatorio[y])*rede.camadaEntrada[i].ativacao);
      }
    }
  }

  public void corrigePesoCamadaOcultaSaida(RedeNeural rede, double d1, double d2, double d3)
  {
    double [] erros = calculaErroCamadaSaida(rede, d1, d2, d3);
    for(int i = 0; i < rede.camadaOculta.length; i++)
    {
      for(int y = 0; y < rede.camadaSaida.length; y++)
      {
        gradientesSaida[y] = calculaGradiente(erros[y], rede.camadaSaida[y].saidas[0]);
        rede.camadaOculta[i].pesos[y] = rede.camadaOculta[i].pesos[y] + ((0.1*rede.camadaOculta[i].correcaoAnteriorPeso[y]) + (0.2*gradientesSaida[y]*rede.camadaOculta[i].ativacao));
        rede.camadaOculta[i].correcaoAnteriorPeso[y] = (0.1*rede.camadaOculta[i].correcaoAnteriorPeso[y]) + (0.2*gradientesSaida[y]*rede.camadaOculta[i].ativacao);
      }
    }
  }
}
