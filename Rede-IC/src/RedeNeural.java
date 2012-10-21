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

public class RedeNeural 
{
  public int numeroNeuronioCamadaOculta, maiorLength;
  public Neuronio camadaEntrada[] = new Neuronio[4];
  public Neuronio camadaOculta[];
  public Neuronio camadaSaida[] = new Neuronio[3];
  
  public RedeNeural(int numeroNeuronioCamadaOculta)
  {
    this.numeroNeuronioCamadaOculta = numeroNeuronioCamadaOculta;
    camadaOculta = new Neuronio[numeroNeuronioCamadaOculta];
    maiorLength = calculaMaior(4, numeroNeuronioCamadaOculta);
    for(int i = 0; i < maiorLength; i++)
    {
      if(i < 4)
        camadaEntrada[i] = new Neuronio();
      if(i < numeroNeuronioCamadaOculta)
        camadaOculta[i] = new Neuronio();
      if(i < 3)
        camadaSaida[i] = new Neuronio();
    }
    fazLigacoes();
    inicializaTodosPesos();
  }
  
  public double[] retornarResultadosCamadaSaida()
  {
    double [] resultados = new double[camadaSaida.length];
    for(int i = 0; i < camadaSaida.length; i++)
    {
      resultados[i] = camadaSaida[i].retornaSaidas()[0];
    }
    return resultados;    
  }
  
  public void aplicarFuncaoAtivacao(int loc)
  {
    for(int i = 0; i < maiorLength; i++)
    {
      if(loc == 2)
      {
        if(i < numeroNeuronioCamadaOculta)
          camadaOculta[i].aplicaFuncaoAtivacao();
        else
          break;
      }
      if(loc == 3)
      {
        if(i < 3)
          camadaSaida[i].aplicaFuncaoAtivacao();  
        else
          break;
      }
    }
  }  
  
  public int calculaMaior(int n1, int n2)
  {
    if(n1 > n2)
      return n1;
    else
      return n2;
  }
  
  public void fazLigacoes()
  {
    for(int i = 0; i < camadaEntrada.length; i++)
    {
      camadaEntrada[i].inicializa(1, numeroNeuronioCamadaOculta, numeroNeuronioCamadaOculta, camadaOculta, 1);
    }
    for(int i = 0; i < camadaOculta.length; i++)
    {
      camadaOculta[i].inicializa(4, 3, 3, camadaSaida, 2);
    }   
    for(int i = 0; i < camadaSaida.length; i++)
    {
      camadaSaida[i].inicializa(numeroNeuronioCamadaOculta, 0, 1, null, 3);
    }
  }
  
  public void gerarSaidas(int loc)
  {
    for(int i = 0; i < maiorLength; i++)
    {
      if(loc == 1)
      {
        if(i < 4)
          camadaEntrada[i].geraSaidas();
        else
          break;
      }
      if(loc == 2)
      {
        if(i < numeroNeuronioCamadaOculta)
          camadaOculta[i].geraSaidas();
        else
          break;
      }
      if(loc == 3)
      {
        if(i < 3)
          camadaSaida[i].geraSaidas();
        else
          break;
      }
    }
  }
  
  public void inicializaTodosPesos()
  {
    for(int i = 0; i < maiorLength; i++)
    {
      if(i < 4)
        camadaEntrada[i].inicializaPesos();
      if(i < numeroNeuronioCamadaOculta)
        camadaOculta[i].inicializaPesos();     
    }
  }
   
  public void zeraCorrecoesAnteriorPeso()
  {
    for(int i = 0; i < maiorLength; i++)
    {
      if(i < 4)
        camadaEntrada[i].zeraCorrecaoAnteriorPeso();
      if(i < numeroNeuronioCamadaOculta)
        camadaOculta[i].zeraCorrecaoAnteriorPeso();
    }
  }
  
  public void zeraPosicaoEntradas()
  {
    for(int i = 0; i < maiorLength; i++)
    {
      if(i < 4)
        camadaEntrada[i].posicaoEntrada = 0;
      if(i < numeroNeuronioCamadaOculta)
        camadaOculta[i].posicaoEntrada = 0;
      if(i < 3)
        camadaSaida[i].posicaoEntrada = 0;
    }
  }
}