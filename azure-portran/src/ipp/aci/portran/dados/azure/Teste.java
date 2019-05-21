package ipp.aci.portran.dados.azure;

import java.util.List;

public class Teste {
	public static void main(String[] args) throws DadosException {
		byte[] blob = { 0, 1, 2 };
		Arquivo a = new Arquivo();
		a.criarArquivo(blob, "projetos1","blob1.txt");
		a.criarArquivo(blob, "projetos1","blob2.txt");
		System.out.println(a.consultarArquivo("projetos1", "blob2.txt"));
		
		//a.listarArquivos("","");
		Arquivo b = new Arquivo();
		System.out.println("--------------------------------------------------------------------");
		List<String> lista=b.listarArquivos("projetos1","*1.txt");
		for (int i =0; i <lista.size(); i++) {
			System.out.println(lista.get(i));
		}
		b.excluirArquivo("projetos1", "*1.txt");
		lista=b.listarArquivos("projetos1","*1.txt");
		for (int i =0; i <lista.size(); i++) {
			System.out.println(lista.get(i));
		}
	}
}
