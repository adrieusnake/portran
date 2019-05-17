package azureVersionSixTest;

import azureVersionSix.TestVersionSix;


public class TestAzure {
	

	public static void main(String[] args) {
	
		
		String arquivos[] = {"ALM_ERROR.jpg","ErroBanco.jpg","ErroOracle.jpg"};
		
		
		TestVersionSix azure = new TestVersionSix();
		
		
		for(String arquivo : arquivos){
			
			azure.sendFileAzure(0, arquivo);
			
		}
		

}
	
}
