package SDES_CASCII;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import sdes.SDES;
import triplesdes.TripleSDES;

public class BruteForce {

	public static String byteToString(byte[] ar) {
		String byteStr = "";
		for (byte a : ar) {
			byteStr = byteStr + String.valueOf(a);
		}
		return byteStr;
	}

	public static byte[] stringToByteArray(String str) {
		String[] arList = str.split("");
		byte[] byteAr = new byte[str.length()];

		for (int i = 0; i < arList.length - 1; i++) {
			byteAr[i] = Byte.valueOf(arList[i]);
		}
		return byteAr;
	}

	public static String intToBinary(int n, int numOfBits) {
		String binary = "";
		for (int i = 0; i < numOfBits; i++) {
			if (n % 2 == 0) {
				binary = "0" + binary;
			} else {
				binary = "1" + binary;
			}
			n = n / 2;
		}

		return binary;
	}

	public static boolean checkPossibleString(String text){
		boolean possible = true;
		char[] split = text.toCharArray();
		if(((Character)split[0]).equals('?') || ((Character)split[0]).equals(':') || ((Character)split[0]).equals(',')){
			return false;
		}
		else{
			Map<String, Integer> alphaStats = new HashMap<>();
			
			
			for( Character ch = 'A'; ch <= 'Z'; ++ch){
				alphaStats.put(String.valueOf(ch), 0);
			}
			
			int special = 0;
			for(int i = 0; i < split.length; i++){
				if(((Character)split[i]).equals('?') || ((Character)split[i]).equals(':') || ((Character)split[i]).equals(',') 
						|| ((Character)split[i]).equals('\'') || ((Character)split[i]).equals('.')){
					special ++;
				}
				else if(((Character)split[i]).equals(' ')){
					special = 0;
				}
				else{
					alphaStats.put(String.valueOf((Character)split[i]), alphaStats.get(String.valueOf((Character)split[i])) + 1);
				}
				
				if(special >= 2){
					return false;
				}
			}
			
			int max = 0;
			String highestTerm = "E";
			for(Entry<String, Integer> entry: alphaStats.entrySet()){
				if(entry.getValue() > max){
					max = entry.getValue();
					highestTerm = entry.getKey();
				}
			}
			
			if(!highestTerm.equals("E")){
				possible = false;
			}
			
			return possible;
		}
	}
	
	public static void bruteForceSDES(String filepath) throws IOException {
		File file = new File(filepath);
		String cipher = "";
		String line;
		try (BufferedReader br = new BufferedReader(new FileReader(file));
			//PrintStream out = new PrintStream(new FileOutputStream("bruteForce_SDES.txt"));
				) {
			while((line = br.readLine()) != null){
				cipher = cipher + line;
			}
			
			System.out.println("Cipher: " + cipher);
			String[] cipherSplit = cipher.split("(?<=\\G........)");
			String key = "";
			byte[] decryptText = new byte[0];
			for(int i = 0; i < Math.pow(2, 10); i++){
				decryptText = new byte[0];
				key = intToBinary(i, 10);
				
				for(String set: cipherSplit){
					decryptText = SDES.appendArray(decryptText, SDES.Decrypt(SDES.stringToByteArray(key), SDES.stringToByteArray(set)));
				}
				
				if(checkPossibleString(CASCII.toString(decryptText))){
					System.out.println("<" + key + ">: " + CASCII.toString(decryptText));
				}
				//out.println("<" + key + ">: " + CASCII.toString(decryptText));
			}
			
		}
	}

	public static void bruteForceTSDES(String filepath) throws IOException {
		File file2 = new File(filepath);
		String cipherTSDES = "";
		String lineTSDES;
		try (BufferedReader br = new BufferedReader(new FileReader(file2));
			//PrintStream out2 = new PrintStream(new FileOutputStream("bruteForce_TSDES.txt"));
				) {
			while((lineTSDES = br.readLine()) != null){
				cipherTSDES = cipherTSDES + lineTSDES;
			}
			
			System.out.println(cipherTSDES.length());
			
			String[] cipherTDESSplit = cipherTSDES.split("(?<=\\G........)"); // Split the string every 8 characters

			for(int i = 0; i < Math.pow(2, 10); i++){
				String key1 = intToBinary(i, 10);
				for(int j = 0; j < Math.pow(2, 10); j ++){
					String key2 = intToBinary(j, 10);
					byte[] decryptTSDES = new byte[0];
					for(String set: cipherTDESSplit){
						byte[] temp =  TripleSDES.Decrypt(SDES.stringToByteArray(key1), SDES.stringToByteArray(key2), SDES.stringToByteArray(set));
						decryptTSDES = SDES.appendArray(decryptTSDES, temp);
					}
					if(checkPossibleString(CASCII.toString(decryptTSDES))){
						System.out.println("<" + key1+", "+key2 + ">: " + CASCII.toString(decryptTSDES));
						//out2.println(CASCII.toString(decryptTSDES));
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		int input = 0;
		String filepath = "";
		Scanner in = new Scanner(System.in);

		do {
			System.out.println("Select the following options...");
			System.out.println("1.	Decrypt a cipher (SDES).");
			System.out.println("2.	Decrypt a cipher (TSDES).");
			System.out.println("3.	Exit.");

			try {
				input = in.nextInt();

				while (input != 1 && input != 2 && input != 3) {
					System.out.println("Not an appropriate option!\n");
					System.out.println("1.	Decrypt a cipher (SDES).");
					System.out.println("2.	Decrypt a cipher (TSDES).");
					System.out.println("3.	Exit.");

					input = in.nextInt();
				}
			} catch (Exception e) {
				System.out.println("Invalid input!");
				System.exit(0);
			}

			if (input == 1) {
				System.out.println("Please enter the file you wish to decrypt: ");
				filepath = in.next();
				try{
					bruteForceSDES(filepath);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			if (input == 2) {
				System.out.println("Please enter the file you wish to decrypt: ");
				filepath = in.next();
				try{
					bruteForceTSDES(filepath);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		} while (input != 3);
		in.close();
	}
}
