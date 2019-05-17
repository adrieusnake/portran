package azureVersionSix;


import java.io.*;

import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.blob.*;

public class OriginalAzure {
	public static final String storageConnectionString =
		"DefaultEndpointsProtocol=http;"
		+ "AccountName=blobportran;"
		+ "AccountKey=5Q5rH/9bs1JrwEt3EXSofvQtkcxOaIylU4KXOQUN4iAHYE/xUvbi42sa/l7V0codPoP3ljofE2Vhp45MQjBLog==";

	public static void main(String[] args) {
		try {
			CloudStorageAccount account = CloudStorageAccount.parse(storageConnectionString);
            CloudBlobClient serviceClient = account.createCloudBlobClient();

         
            CloudBlobContainer container = serviceClient.getContainerReference("container-portran");
            //container.createIfNotExists();

      
            CloudBlockBlob blob = container.getBlockBlobReference("ErroBanco.jpg");
            File sourceFile = new File("c:\\filesTestPortran\\ErroBanco.jpg");
            blob.upload(new FileInputStream(sourceFile), sourceFile.length());
            
            blob.downloadToFile( new File("/Temp").getCanonicalPath());
            
            // Download the image file.
           // File destinationFile = new File(sourceFile.getParentFile(), "rasc.txt");
            //blob.downloadToFile(destinationFile.getAbsolutePath());
            
            
        }
        catch (FileNotFoundException fileNotFoundException) {
            System.out.print("FileNotFoundException encountered: ");
            System.out.println(fileNotFoundException.getMessage());
            System.exit(-1);
        }
        catch (StorageException storageException) {
            System.out.print("StorageException encountered: ");
            System.out.println(storageException.getMessage());
            System.exit(-1);
        }
        catch (Exception e) {
            System.out.print("Exception encountered: ");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
	}
}
