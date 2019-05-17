package azureVersionSixTest;


import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.Properties;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.file.CloudFile;
import com.microsoft.azure.storage.file.CloudFileClient;
import com.microsoft.azure.storage.file.CloudFileDirectory;
import com.microsoft.azure.storage.file.CloudFileShare;

public class Arquivo implements IAzure{

	private Properties prop;
	private String storageConnectionString;
	private String nomeContainer;
	private String downloadDir;
	private String uploadDir;
	private CloudStorageAccount contaAzure;
	CloudFileClient fileClient;
	CloudFileShare share;
	CloudFileDirectory diretorioRaiz;
	CloudFileDirectory diretorioArquivos;
	CloudFile arquivo;

	public Arquivo() throws FileNotFoundException, IOException {
		prop = new Properties();
		prop.load(new FileInputStream("src/resources/config.properties"));
		storageConnectionString = prop.getProperty("storageConnectionString");
		nomeContainer = prop.getProperty("container");
		uploadDir = prop.getProperty("diretorioUpload");
		downloadDir = prop.getProperty("diretorioDownload");
		try {
			contaAzure = CloudStorageAccount.parse(storageConnectionString);
			fileClient = contaAzure.createCloudFileClient();
			share = fileClient.getShareReference(nomeContainer);
			share.createIfNotExists();
			diretorioRaiz = share.getRootDirectoryReference();
			diretorioArquivos = diretorioRaiz.getDirectoryReference(uploadDir);
			diretorioArquivos.createIfNotExists(); 
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (StorageException e) {
			e.printStackTrace();
		}
	}
	
	public void alterarArquivo(String nomeArquivo) throws URISyntaxException, StorageException, IOException {
		 criarArquivo(nomeArquivo);
	}
	public void criarArquivo(String nomeArquivo) throws URISyntaxException, StorageException, IOException {
            nomeArquivo = nomeArquivo.replace('\\', '/');
            String nomeSplit[] = nomeArquivo.split("/");
            int size=nomeSplit.length;
    		arquivo = diretorioArquivos.getFileReference(nomeSplit[size-1]);
	        arquivo.uploadFromFile(nomeArquivo);
	}
	public String consultarArquivo(String nomeArquivo) throws URISyntaxException, StorageException, IOException  {
		nomeArquivo = nomeArquivo.replace('\\', '/');
        String nomeSplit[] = nomeArquivo.split("/");
        int size=nomeSplit.length;
		arquivo = diretorioArquivos.getFileReference(nomeSplit[size-1]);
		arquivo.downloadToFile(downloadDir+nomeArquivo);
		System.out.println(arquivo.downloadText());
        return downloadDir+nomeArquivo;
	}
	
	@Override
	public void excluirArquivo(String nomeArquivo) throws DadosException {
		nomeArquivo = nomeArquivo.replace('\\', '/');
        String nomeSplit[] = nomeArquivo.split("/");
        int size=nomeSplit.length;
		try {
			arquivo = diretorioArquivos.getFileReference(nomeSplit[size-1]);
			arquivo.deleteIfExists();
		} catch (URISyntaxException e) {
			e.printStackTrace();
			throw new DadosException();
		} catch (StorageException e) {
			e.printStackTrace();
			throw new DadosException();
		}
	}

	@Override
	public void alterarArquivo(byte[] blob, String nomeArquivo) throws DadosException {
		criarArquivo(blob,nomeArquivo);	
	}

	@Override
	public void criarArquivo(byte[] blob, String nomeArquivo) throws DadosException {
		nomeArquivo = nomeArquivo.replace('\\', '/');
        String nomeSplit[] = nomeArquivo.split("/");
        int size=nomeSplit.length;
        try {
        	arquivo = diretorioArquivos.getFileReference(nomeSplit[size-1]);
       		arquivo.uploadFromFile(nomeArquivo);	
        } catch (URISyntaxException e) {
			e.printStackTrace();
			throw new DadosException();
		} catch (StorageException e) {
			e.printStackTrace();
			throw new DadosException();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String consultarArquivo(String baseDir, String nomeArquivo) throws DadosException {
		try {
			nomeArquivo = nomeArquivo.replace('\\', '/');
	        String nomeSplit[] = nomeArquivo.split("/");
	        int size=nomeSplit.length;
			arquivo = diretorioArquivos.getFileReference(nomeSplit[size-1]);
			arquivo.downloadToFile(baseDir+nomeArquivo);
			System.out.println(arquivo.downloadText());
	        return baseDir+nomeArquivo;
		} catch (URISyntaxException e) {
			e.printStackTrace();
			throw new DadosException();
		} catch (StorageException e) {
			e.printStackTrace();
			throw new DadosException();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}
