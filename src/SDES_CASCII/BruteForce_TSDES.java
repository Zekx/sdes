package SDES_CASCII;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import sdes.SDES;
import triplesdes.TripleSDES;

public class BruteForce_TSDES {

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

	public static String intToBinary (int n, int numOfBits) {
		String binary = "";
		for(int i = 0; i < numOfBits; i++) {
			if(n%2 == 0){
				binary = "0" + binary;
			}
			else{
				binary = "1" + binary;
			}
			n=n/2;
		}

		return binary;
	}

	public static void main(String[] args){
		// TODO Auto-generated method stub

		PrintStream out = null;
		byte ciphertext[] = new byte[0];

		try(BufferedReader br = new BufferedReader(new FileReader("src\\resources\\msg2.txt"))) {
			out = new PrintStream(new FileOutputStream("bruteForce_TSDES.txt"));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				line = br.readLine();
			}
			String everything = sb.toString();
			ciphertext = stringToByteArray(everything);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String key1 = "";
		String key2 = "";
		for(int i = 0; i < Math.pow(2, 10); i++){
			key1 = intToBinary(i,10);
			for(int k = 0; k < Math.pow(2, 10); k++){
				key2 = intToBinary(k,10);
				byte[] plaintext = new byte[0];
				byte[] temp = new byte[8];
				byte bitarr[] = new byte[8];
				int bcounter = 0;
				for (int j = 0; j < ciphertext.length; j++){
					if (bcounter <= 7){
						bitarr[bcounter] = ciphertext[j];
						bcounter++;
					}
					else {
						temp = TripleSDES.Decrypt(stringToByteArray(key1), stringToByteArray(key2), bitarr);
						plaintext = SDES.appendArray(plaintext, temp);
						bitarr[0] = ciphertext[j];
						bcounter = 1;
					}
					if (j == ciphertext.length - 1){
						temp = TripleSDES.Decrypt(stringToByteArray(key1), stringToByteArray(key2), bitarr);
						plaintext = SDES.appendArray(plaintext, temp);
					}

				}
				System.out.println("Key1: " + key1);
				out.println("Key1: " + key1);
				System.out.println("Key2: " + key2);
				out.println("Key2: " + key2);
				System.out.println("plaintext: " + CASCII.toString(plaintext));
				out.println("plaintext: " + CASCII.toString(plaintext));
			}
		}
		out.close();
	}
}


