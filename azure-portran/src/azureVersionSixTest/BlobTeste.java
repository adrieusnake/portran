package azureVersionSixTest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

public class BlobTeste {

	public static final String storageConnectionString =
			"DefaultEndpointsProtocol=https;"
			+ "AccountName=portran;"
			+ "AccountKey=yRPbYRbRIQpjs0YtOC4reEUQOF8VN7EkKJUhsRSlUQZWJT24kgVnPjL8cDPsVW0xk58nuIsTs4FHRTYZMNYsSw==";
	
	public Exception alterarArquivo(String nomeArquivo) {
		return criarArquivo(nomeArquivo);
	}
	public Exception criarArquivo(String nomeArquivo) {
		try {
			CloudStorageAccount account = CloudStorageAccount.parse(storageConnectionString);
            CloudBlobClient serviceClient = account.createCloudBlobClient();
            CloudBlobContainer container = serviceClient.getContainerReference("container-portran");
            nomeArquivo = nomeArquivo.replace('\\', '/');
            String nomeSplit[] = nomeArquivo.split("/");
            int size=nomeSplit.length;
            CloudBlockBlob blob = container.getBlockBlobReference(nomeSplit[size-1]);
            InputStream targetStream = new ByteArrayInputStream(blob);
            File sourceFile = new File(nomeArquivo);
            blob.upload(new FileInputStream(sourceFile), sourceFile.length());
        }
        catch (Exception e) {
            return e;
        }
		return null;	
	}
	
	public String consultarArquivo(String nomeArquivo)  {
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream("src/resources/config.properties"));
			String downloadDir=prop.getProperty("diretorioDownload");
			CloudStorageAccount account = CloudStorageAccount.parse(storageConnectionString);
            CloudBlobClient serviceClient = account.createCloudBlobClient();
            CloudBlobContainer container = serviceClient.getContainerReference("container-portran");
            CloudBlockBlob blob = container.getBlockBlobReference(nomeArquivo);
            blob.downloadToFile(downloadDir+nomeArquivo);
            return downloadDir+nomeArquivo;
        }
        catch (Exception e) {
            return e.getMessage();
        }
	}
	
	public Exception excluirArquivo(String nomeArquivo) {
		try {
			CloudStorageAccount account = CloudStorageAccount.parse(storageConnectionString);
            CloudBlobClient serviceClient = account.createCloudBlobClient();
            CloudBlobContainer container = serviceClient.getContainerReference("container-portran");
            nomeArquivo = nomeArquivo.replace('\\', '/');
            String nomeSplit[] = nomeArquivo.split("/");
            int size=nomeSplit.length;
            CloudBlockBlob blob = container.getBlockBlobReference(nomeSplit[size-1]);
            blob.delete();
        }
        catch (Exception e) {
            return e;
        }
		return null;	
	}
	
	
}
