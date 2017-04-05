package SDES_CASCII;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
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

	public static void bruteForceSDES(String filepath) throws FileNotFoundException {
		byte ciphertext[] = null;
		PrintStream out = new PrintStream(new FileOutputStream("bruteForce_SDES.txt"));

		try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
			String line = br.readLine();
			//System.out.println(line.equals("1011011001111001001011101111110000111110100000000001110111010001111011111101101100010011000000101101011010101000101111100011101011010111100011101001010111101100101110000010010101110001110111011111010101010100001100011000011010101111011111010011110111001001011100101101001000011011111011000010010001011101100011011110000000110010111111010000011100011111111000010111010100001100001010011001010101010000110101101111111010010110001001000001111000000011110000011110110010010101010100001000011010000100011010101100000010111000000010101110100001000111010010010101110111010010111100011111010101111011101111000101001010001101100101100111001110111001100101100011111001100000110100001001100010000100011100000000001001010011101011100101000111011100010001111101011111100000010111110101010000000100110110111111000000111110111010100110000010110000111010001111000101011111101011101101010010100010111100011100000001010101110111111101101100101010011100111011110101011011"));
			
			String strValue[] = line.split("");
			
			String append = "";
			ciphertext = new byte[strValue.length];
			for (int i = 0;i < line.length(); i++){
			    ciphertext[i] = (byte) Integer.parseInt(strValue[i]);
			    append = append + ciphertext[i];
			    //System.out.print(ciphertext[i]);
			}
			//System.out.println();
			//System.out.println(line.equals(append));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String key = "";
		for (int i = 0; i < Math.pow(2, 10); i++) {
			key = intToBinary(i, 10);
			byte[] plaintext = new byte[0];
			byte[] temp = new byte[8];
			byte bitarr[] = new byte[8];
			int bcounter = 0;
			
			for (int j = 0; j < ciphertext.length; j++) {
				if (bcounter <= 7) {
					bitarr[bcounter] = ciphertext[j];
					bcounter++;
				} else {
					temp = SDES.Decrypt(stringToByteArray(key), bitarr);
					plaintext = SDES.appendArray(plaintext, temp);
					bitarr[0] = ciphertext[j];
					bcounter = 1;
				}
				if (j == ciphertext.length - 1) {
					while(bcounter <= 7){
						bitarr[bcounter] = 0;
						bcounter++;
					}
					System.out.println("finalByte:" + bitarr[0]+bitarr[1]+bitarr[2]+bitarr[3]+bitarr[4]+bitarr[5]+bitarr[6]+bitarr[7]);
					temp = SDES.Decrypt(stringToByteArray(key), bitarr);
					plaintext = SDES.appendArray(plaintext, temp);
				}

			}
			
			System.out.println("Key: " + key);
			out.println("Key: " + key);
			System.out.println("plaintext: " + CASCII.toString(plaintext));
			out.println("plaintext: " + CASCII.toString(plaintext));

		}

		out.close();
	}

	public static void bruteForceTSDES(String filepath) throws FileNotFoundException {
		byte ciphertext[] = null;
		PrintStream out = new PrintStream(new FileOutputStream("bruteForce_TSDES.txt"));

		try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
			String line = br.readLine();
			//System.out.println(line.equals("00011111100111111110011111101100111000000011001011110010101010110001011101001101000000110011010111111110000000001010111111000001010010111001111001010101100000110111100011111101011100100100010101000011001100101000000101111011000010011010111100010001001000100001111100100000001000000001101101000000001010111010000001000010011100101111001101111011001001010001100010100000"));
			
			String strValue[] = line.split("");
			
			String append = "";
			ciphertext = new byte[strValue.length];
			for (int i = 0;i < line.length(); i++){
			    ciphertext[i] = (byte) Integer.parseInt(strValue[i]);
			    append = append + ciphertext[i];
			    //System.out.print(ciphertext[i]);
			}
			//System.out.println();
			//System.out.println(line.equals(append));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String key1 = "";
		String key2 = "";
		for (int i = 0; i < Math.pow(2, 10); i++) {
			key1 = intToBinary(i, 10);
			for (int k = 0; k < Math.pow(2, 10); k++) {
				key2 = intToBinary(k, 10);
				byte[] plaintext = new byte[0];
				byte[] temp = new byte[8];
				byte bitarr[] = new byte[8];
				int bcounter = 0;
				for (int j = 0; j < ciphertext.length; j++) {
					if (bcounter < 8) {
						bitarr[bcounter] = ciphertext[j];
						bcounter++;
					} else {
						temp = TripleSDES.Decrypt(stringToByteArray(key1), stringToByteArray(key2), bitarr);
						plaintext = SDES.appendArray(plaintext, temp);

						bcounter = 0;
					}
					if (j == ciphertext.length - 1) {
						while(bcounter < 8){
							bitarr[bcounter] = 0;
							bcounter++;
						}
						
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
	}
}
