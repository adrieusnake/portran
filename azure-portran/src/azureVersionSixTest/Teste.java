package azureVersionSixTest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import com.microsoft.azure.storage.StorageException;

public class Teste {
	public static void main(String[] args) {
		try {
			Arquivo a;
			a = new Arquivo();
			a.criarArquivo("D:/portran/imagem1.jpg");
			String retorno = a.consultarArquivo("imagem1.jpg");
			System.out.println(retorno);
			//a.excluirArquivo("D:\\portran\\teste1.txt");
			//System.out.println("Arquivo Deletado!!!");
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (StorageException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
}
