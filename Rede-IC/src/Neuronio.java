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

public class Neuronio 
{
  public double ativacao;
  public double [] entradas;
  public double [] pesos;
  public double [] saidas;
  public double [] correcaoAnteriorPeso;
  public int localizacao, posicaoEntrada = 0;
  public Neuronio [] ligacoes;
  
  public double[] retornaSaidas()
  {
    return saidas;
  }
  
  public void aplicaFuncaoAtivacao()
  {
    double v = 0;
    for(int i = 0; i < entradas.length; i++)
      v = v + entradas[i];
    ativacao = 1/(1 + Math.exp((-1) * v));
  }
  
  public void geraSaidas()
  {
    if(localizacao != 3)
      for(int i = 0; i < ligacoes.length; i++)
    {
      saidas[i] = ativacao * pesos[i];
      ligacoes[i].insereEntrada(saidas[i]);
    }
    else
      saidas[0] = ativacao;
  }
  
  public void inicializa(int quantEntradas, int quantPesos, int quantSaidas, Neuronio [] lig, int localizacao)
  {
    this.localizacao = localizacao;
    entradas = new double[quantEntradas];
    saidas = new double[quantSaidas];
    if(localizacao != 3)
    {
      ligacoes = new Neuronio[lig.length];
      pesos = new double[quantPesos];
      correcaoAnteriorPeso = new double[quantPesos];
      for(int i = 0; i < ligacoes.length; i++)
      {
        ligacoes[i] = lig[i];
        correcaoAnteriorPeso[i] = 0;
      }
    }
  }
  
  public void inicializaPesos()
  {
    for(int i = 0; i < pesos.length; i++)
      pesos[i] = (Math.random() * 2) - 1;
  }
  
  public void insereEntrada(double ent)
  {
    entradas[posicaoEntrada] = ent;
    posicaoEntrada++;
  }
  
  public void zeraCorrecaoAnteriorPeso()
  {
    for(int i = 0; i < correcaoAnteriorPeso.length; i++)
      correcaoAnteriorPeso[i] = 0;
  }
}
