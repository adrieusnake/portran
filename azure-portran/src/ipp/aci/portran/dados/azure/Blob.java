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

public class Blob implements IAzure {

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

	private void iniciarAzure() throws DadosException {
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
			throw new DadosException();
		} catch (URISyntaxException e) {
			e.printStackTrace();
			throw new DadosException();
		} catch (StorageException e) {
			e.printStackTrace();
			throw new DadosException();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * M�todo para listar os arquivos na nuvem
	 * 
	 * @param expRegular expressao regular para selecao dos arquivos a serem
	 *                   excluidos do diretorio da nuvem
	 * 
	 * @throws DadosException
	 * 
	 */

	public Iterator<ListBlobItem> listarArquivos(String nomeArquivo) throws DadosException {
		iniciarAzure();
		Iterable<ListBlobItem> listBlob = container.listBlobs(nomeArquivo);
		Iterator<ListBlobItem> iterator = listBlob.iterator();
		ListBlobItem blob;
		while (iterator.hasNext()) {
			blob = iterator.next();
			System.out.println(blob.getUri() + " ");
		}
		return iterator;
	}

	/**
	 * 
	 * M�todo para excluir um arquivo na nuvem
	 * 
	 * @param baseDir    diret�rio do arquivo na nuvem, por exemplo "/motoristas/"
	 *                   ou "/veiculos/"
	 * 
	 * @param expRegular expressao regular para selecao dos arquivos a serem
	 *                   excluidos do diretorio da nuvem
	 * 
	 * @throws DadosException
	 * 
	 */
	@Override
	public void excluirArquivo(String baseDir,String nomeArquivo) throws DadosException {
		iniciarAzure();
		try {
			nomeArquivo = nomeArquivo.replace('\\', '/');
			String nomeSplit[] = nomeArquivo.split("/");
			int size = nomeSplit.length;
			blockBlob = container.getBlockBlobReference(nomeSplit[size - 1]);
			blockBlob.deleteIfExists();
		} catch (URISyntaxException e) {
			e.printStackTrace();
			throw new DadosException();
		} catch (StorageException e) {
			e.printStackTrace();
			throw new DadosException();
		}
	}

	/**
	 * 
	 * M�todo para sobrescrever um arquivo na nuvem
	 *
	 * 
	 * 
	 * @param blob        conte�do do arquivo
	 * 
	 * @param baseDir     diret�rio do arquivo na nuvem, por exemplo "/motoristas/"
	 *                    ou "/veiculos/"
	 * 
	 * @param nomeArquivo nome do arquivo de imagem com sua extens�o
	 * 
	 * @throws DadosException
	 * 
	 */
	@Override
	public void alterarArquivo(byte[] blob, String baseDir, String nomeArquivo) throws DadosException {
		criarArquivo(blob, baseDir, nomeArquivo);
	}

	/**
	 * 
	 * M�todo para criar um arquivo na nuvem
	 *
	 * 
	 * 
	 * @param blob        conte�do do arquivo
	 * 
	 * @param baseDir     diret�rio do arquivo na nuvem, por exemplo "/motoristas/"
	 *                    ou "/veiculos/"
	 * 
	 * @param nomeArquivo nome do arquivo de imagem com sua extens�o
	 * 
	 * @throws DadosException
	 * 
	 */
	@Override
	public void criarArquivo(byte[] blob, String baseDir, String nomeArquivo) throws DadosException {
		iniciarAzure();
		try {
			blockBlob = container.getBlockBlobReference(nomeArquivo);
			inputStream = new ByteArrayInputStream(blob);
			blockBlob.upload(inputStream, blob.length);
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

	/**
	 * 
	 * M�todo para buscar um arquivo na nuvem e baixa-lo para um diretorio de
	 * download local
	 *
	 * 
	 * 
	 * @param baseDir     diret�rio do arquivo na nuvem, por exemplo "/motoristas/"
	 *                    ou "/veiculos/"
	 * 
	 * @param nomeArquivo nome do arquivo de imagem com sua extens�o
	 * 
	 * @return nome do arquivo gravado no diret�rio local com caminho completo
	 * 
	 * @throws DadosException
	 * 
	 */
	@Override
	public byte[] consultarArquivo(String baseDir, String nomeArquivo) throws DadosException {
		iniciarAzure();
		try {
			blockBlob = container.getBlockBlobReference(nomeArquivo);
			blockBlob.downloadToFile(baseDir + nomeArquivo);
			
			int size = blockBlob.getStreamMinimumReadSizeInBytes();
			byte[] buffer = new byte[size];
			blockBlob.downloadToByteArray(buffer, 0);
			return buffer;
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
