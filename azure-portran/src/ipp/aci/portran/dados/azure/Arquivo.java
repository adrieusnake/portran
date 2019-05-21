package ipp.aci.portran.dados.azure;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.file.CloudFile;
import com.microsoft.azure.storage.file.CloudFileClient;
import com.microsoft.azure.storage.file.CloudFileDirectory;
import com.microsoft.azure.storage.file.CloudFileShare;
import com.microsoft.azure.storage.file.ListFileItem;

public class Arquivo implements IAzure {

	private Properties prop;
	private String storageConnectionString;
	private String nomeContainer;
	private String downloadDir;
	private CloudStorageAccount contaAzure;
	CloudFileClient fileClient;
	CloudFileShare share;
	CloudFileDirectory diretorioRaiz;
	CloudFileDirectory diretorioArquivos;
	CloudFile arquivo;

	private void IniciarAzure() throws DadosException {
		try {
			prop = new Properties();
			prop.load(new FileInputStream("src/resources/config.properties"));
			storageConnectionString = prop.getProperty("storageConnectionString");
			nomeContainer = prop.getProperty("container");
			downloadDir = prop.getProperty("diretorioDownload");
			contaAzure = CloudStorageAccount.parse(storageConnectionString);
			fileClient = contaAzure.createCloudFileClient();
			share = fileClient.getShareReference(nomeContainer);
			share.createIfNotExists();
			diretorioRaiz = share.getRootDirectoryReference();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			throw new DadosException();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new DadosException();
		} catch (IOException e) {
			e.printStackTrace();
			throw new DadosException();
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
	 * Método para listar os arquivos na nuvem
	 * 
	 * @param expRegular expressao regular para selecao dos arquivos a serem
	 *                   excluidos do diretorio da nuvem
	 * 
	 * @throws DadosException
	 * 
	 */

	public List<String> listarArquivos(String baseDir, String nomeArquivo) throws DadosException {
		IniciarAzure();
		Iterable<ListFileItem> listFile;
		boolean inicia = false;
		boolean termina = false;
		String path;
		String[] arquivo;
		List<String> listaArquivos = new ArrayList<String>();
		if (baseDir.length() == 0) {
			listFile = diretorioRaiz.listFilesAndDirectories("", null, null);
		} else {
			try {
				diretorioArquivos = diretorioRaiz.getDirectoryReference(baseDir);
				listFile = diretorioArquivos.listFilesAndDirectories("", null, null);
			} catch (URISyntaxException e) {
				e.printStackTrace();
				throw new DadosException();

			} catch (StorageException e) {
				e.printStackTrace();
				throw new DadosException();
			}
		}
		Iterator<ListFileItem> iterator = listFile.iterator();
		ListFileItem file;
		if (nomeArquivo.startsWith("*")) {
			termina = true;
		} else if (nomeArquivo.endsWith("*")) {
			inicia = true;
		}
		nomeArquivo = nomeArquivo.replace("*", "");
		while (iterator.hasNext()) {
			file = iterator.next();
			path = file.getUri().getRawPath();
			arquivo = path.split("/");
			path = arquivo[arquivo.length - 1];
			if ((termina && path.endsWith(nomeArquivo)) || (inicia && path.startsWith(nomeArquivo))
					|| (path.equals(nomeArquivo)) || (nomeArquivo.length() == 0)) {
				listaArquivos.add(path);
				;
			}
		}
		return listaArquivos;
	}

	/**
	 * 
	 * Método para excluir um arquivo na nuvem
	 *
	 * @param baseDir    diretório do arquivo na nuvem, por exemplo "motoristas" ou
	 *                   "veiculos"
	 * 
	 * @param expRegular expressao regular para selecao dos arquivos a serem
	 *                   excluidos do diretorio da nuvem caracter coringa no inicio ou no fim de uma string * exemplo *.txt ou motorista* ou o nome completo do arquivo.
	 * 
	 * @throws DadosException
	 * 
	 */
	@Override
	public void excluirArquivo(String baseDir, String expRegular) throws DadosException {
		IniciarAzure();
		try {
			List<String> lista = listarArquivos(baseDir, expRegular);
			if (baseDir.length() == 0) {
				diretorioArquivos = diretorioRaiz;
			} else {
				diretorioArquivos = diretorioRaiz.getDirectoryReference(baseDir);
			}
			for (int i = 0; i < lista.size(); i++) {
				arquivo = diretorioArquivos.getFileReference(lista.get(i));
				boolean s = arquivo.deleteIfExists();
				if (s) {
					System.out.println("deletando: " + lista.get(i) + " " + s);
				} else {
					excluirArquivo(lista.get(i), "");
					s = diretorioArquivos.deleteIfExists();
					System.out.println("deletando diretorio: " + lista.get(i) + " " + s);
				}
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
			throw new DadosException();
		} catch (StorageException e) {
			e.printStackTrace();
			throw new DadosException();
		}
	}

	@Override
	public void alterarArquivo(byte[] blob, String baseDir, String nomeArquivo) throws DadosException {
		criarArquivo(blob, baseDir, nomeArquivo);
	}

	/**
	 * 
	 * Método para criar um arquivo na nuvem
	 *
	 * 
	 * 
	 * @param blob        conteúdo do arquivo
	 * 
	 * @param baseDir     diretório do arquivo na nuvem, por exemplo "motoristas" ou
	 *                    "veiculos"
	 * 
	 * @param nomeArquivo nome do arquivo de imagem com sua extensão
	 * 
	 * @throws DadosException
	 * 
	 */
	@Override
	public void criarArquivo(byte[] blob, String baseDir, String nomeArquivo) throws DadosException {
		IniciarAzure();
		try {
			diretorioArquivos = diretorioRaiz.getDirectoryReference(baseDir);
			diretorioArquivos.createIfNotExists();
			arquivo = diretorioArquivos.getFileReference(nomeArquivo);
			arquivo.uploadFromByteArray(blob, 0, blob.length);
			// arquivo.uploadFromFile(nomeArquivo);
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
	 * Método para buscar um arquivo na nuvem e baixa-lo para um diretorio de
	 * download local
	 *
	 * 
	 * 
	 * @param baseDir     diretório do arquivo na nuvem, por exemplo "motoristas"
	 *                    ou "veiculos"
	 * 
	 * @param nomeArquivo nome do arquivo de imagem com sua extensão
	 * 
	 * @return nome do arquivo gravado no diretório local com caminho completo
	 * 
	 * @throws DadosException
	 * 
	 */
	@Override
	public byte[] consultarArquivo(String baseDir, String nomeArquivo) throws DadosException {
		IniciarAzure();
		try {
			diretorioArquivos = diretorioRaiz.getDirectoryReference(baseDir);
			arquivo = diretorioArquivos.getFileReference(nomeArquivo);
			int size = arquivo.getStreamMinimumReadSizeInBytes();
			byte[] buffer = new byte[size]; 
			arquivo.downloadToByteArray(buffer,0);
			return buffer;
		} catch (URISyntaxException e) {
			e.printStackTrace();
			throw new DadosException();
		} catch (StorageException e) {
			e.printStackTrace();
			throw new DadosException();
		}
	}

}
