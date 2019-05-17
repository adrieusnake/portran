package ipp.aci.portran.dados.azure;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.Iterator;
import java.util.Properties;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;
import com.microsoft.azure.storage.file.CloudFile;
import com.microsoft.azure.storage.file.CloudFileDirectory;


public class Blob implements IAzure{

	private Properties prop;
	private String storageConnectionString;
	private String nomeContainer;

	private CloudStorageAccount contaAzure;
	CloudBlobClient blobClient;
	CloudBlobContainer container;
	CloudFileDirectory diretorioRaiz;
	CloudFileDirectory diretorioArquivos;
	CloudFile arquivo;
	CloudBlockBlob blockBlob;
	InputStream inputStream;
	
	public Blob() throws FileNotFoundException, IOException {
		try {
			prop = new Properties();
			prop.load(new FileInputStream("src/resources/config.properties"));
			storageConnectionString = prop.getProperty("storageConnectionString");
			nomeContainer = prop.getProperty("container");
			contaAzure = CloudStorageAccount.parse(storageConnectionString);
			blobClient = contaAzure.createCloudBlobClient();
			container = blobClient.getContainerReference(nomeContainer);
			container.createIfNotExists();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (StorageException e) {
			e.printStackTrace();
		}
	}
	
	public Iterable<ListBlobItem> listarArquivos(String nomeArquivo) throws DadosException {
		 Iterable<ListBlobItem> listBlob = container.listBlobs(nomeArquivo);
		 Iterator<ListBlobItem> iterator = listBlob.iterator();
		 ListBlobItem blob;
		 while (iterator.hasNext()) {
			blob=iterator.next();
	        System.out.println(blob.getUri() + " "); 
		 }
         return listBlob;
	}
	
	@Override
	public void excluirArquivo(String nomeArquivo) throws DadosException {
		try {
            nomeArquivo = nomeArquivo.replace('\\', '/');
            String nomeSplit[] = nomeArquivo.split("/");
            int size=nomeSplit.length;
            blockBlob = container.getBlockBlobReference(nomeSplit[size-1]);
            blockBlob.deleteIfExists();
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
        try {
            nomeArquivo = nomeArquivo.replace('\\', '/');
            String nomeSplit[] = nomeArquivo.split("/");
            int size=nomeSplit.length;
            blockBlob = container.getBlockBlobReference(nomeSplit[size-1]);
            inputStream = new ByteArrayInputStream(blob);
            blockBlob.upload(inputStream,blob.length);
        } catch (URISyntaxException e) {
			e.printStackTrace();
			throw new DadosException();
		} catch (StorageException e) {
			e.printStackTrace();
			throw new DadosException();
		} catch (IOException e) {
			e.printStackTrace();
			throw new DadosException();
		}
	}

	@Override
	public String consultarArquivo(String baseDir, String nomeArquivo) throws DadosException {
		try {
			nomeArquivo = nomeArquivo.replace('\\', '/');
	        String nomeSplit[] = nomeArquivo.split("/");
	        int size=nomeSplit.length;
			blockBlob = container.getBlockBlobReference(nomeSplit[size-1]);
	        blockBlob.downloadToFile(baseDir+nomeArquivo);
	        return baseDir+nomeArquivo;
		 } catch (URISyntaxException e) {
			e.printStackTrace();
			throw new DadosException();
		} catch (StorageException e) {
			e.printStackTrace();
			throw new DadosException();
		} catch (IOException e) {
			e.printStackTrace();
			throw new DadosException();
		}
	}
	
	
}
