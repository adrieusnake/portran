package azureVersionSix;

import java.io.*;

import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.blob.*;

public class TestVersionSix {
	
	
	String valor = null;
	String storageConnectionString = null;
	CloudStorageAccount account = null;
	CloudBlobClient serviceClient = null;
	CloudBlobContainer container = null;
	CloudBlockBlob blob = null;
	File sourceFile = null;

	public void init(String arquivo) {

		storageConnectionString = "DefaultEndpointsProtocol=http;"
				+ "AccountName=blobportran;"
				+ "AccountKey=5Q5rH/9bs1JrwEt3EXSofvQtkcxOaIylU4KXOQUN4iAHYE/xUvbi42sa/l7V0codPoP3ljofE2Vhp45MQjBLog==";
							  
		try {

			account = CloudStorageAccount.parse(storageConnectionString);
			serviceClient = account.createCloudBlobClient();

			container = serviceClient.getContainerReference("container-portran");
			container.createIfNotExists();

			container.getBlockBlobReference(arquivo);

		} catch (Exception e) {
			
			System.out.println("ERROR"+e.getMessage());
		}

	  sourceFile = new File("c:\\filesTestPortran\\"+arquivo+"");

	}
	
	
	public String sendFileAzure(int enter, String file) {

		init(file);

		try {

			blob.upload(new FileInputStream(sourceFile), sourceFile.length());

		} catch (FileNotFoundException fileNotFoundException) {
			System.out.print("FileNotFoundException encountered: ");
			System.out.println(fileNotFoundException.getMessage());
			System.exit(-1);
		} catch (StorageException storageException) {
			System.out.print("StorageException encountered: ");
			System.out.println(storageException.getMessage());
			System.exit(-1);
		} catch (Exception e) {

			valor = "you cannot create this file !";
			System.out.println("ERROR" + e.getMessage());
		}

		return valor;
	}
	
	
	public String getFileAzure(int enter, String file) {

		init(file);

		try {

			blob.downloadToFile(new File("/Temp").getCanonicalPath());

		} catch (FileNotFoundException fileNotFoundException) {
			System.out.print("FileNotFoundException encountered: ");
			System.out.println(fileNotFoundException.getMessage());
			System.exit(-1);
		} catch (StorageException storageException) {
			System.out.print("StorageException encountered: ");
			System.out.println(storageException.getMessage());
			System.exit(-1);
		} catch (Exception e) {

			valor = "you cannot recovery this file !";
			System.out.println("ERROR" + e.getMessage());
		}

		return valor;
	}
	
	public String deleteFileAzure(int enter, String file) {

		init(file);

		try {

			blob.downloadToFile(new File("/Temp").getCanonicalPath());
			

		} catch (FileNotFoundException fileNotFoundException) {
			System.out.print("FileNotFoundException encountered: ");
			System.out.println(fileNotFoundException.getMessage());
			System.exit(-1);
		} catch (StorageException storageException) {
			System.out.print("StorageException encountered: ");
			System.out.println(storageException.getMessage());
			System.exit(-1);
		} catch (Exception e) {

			valor = "you cannot delete this file !";
			System.out.println("ERROR" + e.getMessage());
			System.exit(-1);
		}

		return valor;
	}

}
