package azureVersionSixTest;

//import ipp.aci.pdsiframework.util.excecao.DadosException;
 
public interface IAzure {
 
       public void alterarArquivo(byte[] blob, String nomeArquivo) throws DadosException;
       public void criarArquivo(byte[] blob, String nomeArquivo) throws DadosException;
       public String consultarArquivo(String baseDir, String nomeArquivo) throws DadosException;
       public void excluirArquivo(String nomeArquivo) throws DadosException;
}