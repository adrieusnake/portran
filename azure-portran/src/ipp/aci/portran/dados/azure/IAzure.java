package ipp.aci.portran.dados.azure;

import java.util.Iterator;

//import ipp.aci.pdsiframework.util.excecao.DadosException;

public interface IAzure {
	

	public void alterarArquivo(byte[] blob, String baseDir, String nomeArquivo) throws DadosException;

	public void criarArquivo(byte[] blob, String baseDir, String nomeArquivo) throws DadosException;

	public String consultarArquivo(String baseDir, String nomeArquivo) throws DadosException;

	public void excluirArquivo(String baseDir, String nomeArquivo) throws DadosException;
}