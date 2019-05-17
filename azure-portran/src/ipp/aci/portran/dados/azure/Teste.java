package ipp.aci.portran.dados.azure;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import com.microsoft.azure.storage.StorageException;

public class Teste {
	public static void main(String[] args) throws DadosException {
		try {
			byte[] blob = { 0, 1, 2 };
			Blob a = new Blob();
			a.criarArquivo(blob, "D:/portran/blob1.txt");
			a.criarArquivo(blob, "D:/portran/blob2.txt");
			a.listarArquivos("");
			Arquivo b = new Arquivo();
			b.listarArquivos("");
			
			//String retorno = a.consultarArquivo("imagem1.jpg");
			//System.out.println(retorno);
			//a.excluirArquivo("D:\\portran\\teste1.txt");
			//System.out.println("Arquivo Deletado!!!");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
}
