package triplesdes;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import sdes.SDES;

public class TripleSDES {
	/*
	 * This class uses the Simplified DES algorithm to encrypt the plaintext three times
	 * using two keys. The encryption works the following way: 
	 * Encrypt(key1, Decrypt(key2, Encrypt(key1, plaintext)))
	 * 
	 * And to do the decryption, we simply do the opposite:
	 * Decryption(key1, Encrypt(key2, Decrypt(key1, ciphertext)))
	 * 
	 * Note that the encrypt/decrypt in the middle of both formula does not
	 * actually revert to the original text since they are using key2 to
	 * do the operation rather than the original key.
	 * 
	 */
	public static byte[] Encrypt( byte[] rawkey1, byte[] rawkey2, byte[] plaintext )
	{
		/*
		 * Triple Simplified DES encryption on the plaintext using two keys:
		 * Encrypt(key1, Decrypt(key2, Encrypt(key1, plaintext)))
		 * 
		 */
		
		byte[] first = SDES.Encrypt(rawkey1, plaintext);
		byte[] second = SDES.Decrypt(rawkey2, first);
		byte[] third = SDES.Encrypt(rawkey1, second);
		
		
		return  third;
	}
	public static byte[] Decrypt( byte[] rawkey1, byte[] rawkey2, byte[] ciphertext )
	{
		/*
		 * Decrypt the Triple Simplified DES ciphertext encryption using two keys:
		 * Decryption(key1, Encrypt(key2, Decrypt(key1, ciphertext)))
		 * 
		 */
		
		byte[] first = SDES.Decrypt(rawkey1, ciphertext);
		byte[] second = SDES.Encrypt(rawkey2, first);
		byte[] third = SDES.Decrypt(rawkey1, second);
		
		return third;
	}
	
	public static String byteToString(byte[] ar){
		String byteStr = "";
		for( byte a : ar){
			byteStr = byteStr + String.valueOf(a);
		}
		return byteStr;
	}
	
	public static byte[] stringToByteArray(String str){
		String[] arList = str.split("");
		byte[] byteAr = new byte[str.length()];

		for( int i = 0; i < arList.length - 1; i ++){
			byteAr[i] = Byte.valueOf(arList[i]);
		}
		return byteAr;
	}
	
	public static void main(String args[]){
		// For testing the keys
		// ========== Parsing the text file =======================================
		String filepath = "src\\resources\\TripleSDES.txt";
		File file = new File(filepath);
		
		List<String> original = new ArrayList<String>();
		List<ArrayList<String>> applyAnswer = new ArrayList<ArrayList<String>>();
		try(BufferedReader br = new BufferedReader(new FileReader(file))){
			int currRow = 0;
			String str = br.readLine(); // We skip the first line
			original.add(str);
			while((str = br.readLine()) != null){
				original.add(str);
				applyAnswer.add(new ArrayList<String>());
				for( String s : str.split(" ")){
					if(!s.isEmpty()){
						applyAnswer.get(currRow).add(s);
					}
				}
				
				currRow ++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// ========== Printing the text file =======================================
		System.out.println("========== Original TripleSDES ============");
		for( String o : original){
			System.out.println(o);
		}
		System.out.println("=========== Solved TripleSDES =============");
		System.out.printf("%-15s%-15s%-15s%-15s", "Raw Key 1", "Raw Key 2", "Plaintext", "CipherText");
		System.out.println();
		for ( ArrayList<String> arList : applyAnswer){
			boolean solve = false; // This variable is to check whether if a row is "filled" or not ( requires answer )
			
			for(int i = 0; i < arList.size(); i ++){
				if(arList.get(i).equals("?")){
					
					if(i == 2){ // If plaintext requires answer
						String plaintext = byteToString(Decrypt(stringToByteArray(arList.get(0)), stringToByteArray(arList.get(1)),stringToByteArray(arList.get(3))));
						System.out.printf("%-15s%-15s%-15s%-15s", arList.get(0), arList.get(1), plaintext, arList.get(3));
						solve = true;
						break;
					}
					if(i == 3){ // If Ciphertext requires answer
						String ciphertext = byteToString(Encrypt(stringToByteArray(arList.get(0)), stringToByteArray(arList.get(1)), stringToByteArray(arList.get(2))));
						System.out.printf("%-15s%-15s%-15s%-15s", arList.get(0), arList.get(1), arList.get(2), ciphertext);
						solve = true;
						break;
					}
				}
			}
			if(!solve){
				System.out.printf("%-15s%-15s%-15s%-15s", arList.get(0), arList.get(1), arList.get(2), arList.get(3));
			}
			System.out.println();
		}

	}
}
