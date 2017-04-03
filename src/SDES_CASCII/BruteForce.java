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
		byte ciphertext[] = new byte[0];
		PrintStream out = new PrintStream(new FileOutputStream("bruteForce_SDES.txt"));

		try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
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
		// TODO Auto-generated method stub

		PrintStream out = null;
		byte ciphertext[] = new byte[0];

		try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
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
		for (int i = 0; i < Math.pow(2, 10); i++) {
			key1 = intToBinary(i, 10);
			for (int k = 0; k < Math.pow(2, 10); k++) {
				key2 = intToBinary(k, 10);
				byte[] plaintext = new byte[0];
				byte[] temp = new byte[8];
				byte bitarr[] = new byte[8];
				int bcounter = 0;
				for (int j = 0; j < ciphertext.length; j++) {
					if (bcounter <= 7) {
						bitarr[bcounter] = ciphertext[j];
						bcounter++;
					} else {
						temp = TripleSDES.Decrypt(stringToByteArray(key1), stringToByteArray(key2), bitarr);
						plaintext = SDES.appendArray(plaintext, temp);
						bitarr[0] = ciphertext[j];
						bcounter = 1;
					}
					if (j == ciphertext.length - 1) {
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
