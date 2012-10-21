package auxiliares;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Arquivo {
	public static String diretorioRaiz = "/home/rosy/Development/workspaces/LearnInPlanner/MLP/";
	
	public static String ler( String nomeArquivo ) {
		String texto = "";
		
		try {
			File f = new File(nomeArquivo);
			FileReader fr = new FileReader( f );
			
        
			while ( true ) {
				int x = fr.read();
            
				if( x == -1 )
					break;
            
				texto += (char)x;
			}
        
			fr.close();
		} catch(IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return texto;
    }
	
	public static void escrever(String nomeArquivo, String extensao, String conteudoTexto) {
		File f = new File(nomeArquivo + extensao);
		
		try {
        	FileWriter fw = null;;
    		fw = new FileWriter( f, true );
			fw.write(conteudoTexto);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void escrever(String nomeArquivo, String extensao,  String conteudoTexto, String num) {
		File f = new File(nomeArquivo + num + extensao);
		
		try {
        	FileWriter fw = null;;
    		fw = new FileWriter( f, true );
			fw.write(conteudoTexto);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void criarDiretorio(String nomeDir) {
		File f = new File(nomeDir);
		
		f.mkdir();
	}
}
