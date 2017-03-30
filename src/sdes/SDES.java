package sdes;

import java.util.ArrayList;
import java.util.List;
import java.io.*;

/* From http://mercury.webster.edu/aleshunas/COSC%205130/G-SDES.pdf
 * 
 * The S-DES encryption algorithm takes an 8-bit block of plaintext (example: 10111101)
and a 10-bit key as input and produces an 8-bit block of ciphertext as output. The S-DES
decryption algorithm takes an 8-bit block of ciphertext and the same 10-bit key used to produce
that ciphertext as input and produces the original 8-bit block of plaintext.


The encryption algorithm involves five functions: an initial permutation (IP); a complex
function labeled fK, which involves both permutation and substitution operations and depends on
a key input; a simple permutation function that switches (SW) the two halves of the data; the
function fK again; and finally a permutation function that is the inverse of the initial permutation
(IP�1). As was mentioned in Chapter 2, the use of multiple stages of permutation and substitution
results in a more complex algorithm, which increases the difficulty of cryptanalysis.

IP -> fk -> SW -> fk -> IP-1
 */

public class SDES {
	
	//substitution box 0
	static byte[][] s0 = { { 1, 0, 3, 2 }, { 3, 2, 1, 0 }, { 0, 2, 1, 3 }, { 3, 1, 3, 2 } };
	
	//substitution box 1
	static byte[][] s1 = { { 0, 1, 2, 3 }, { 2, 0, 1, 3 }, { 3, 0, 1, 0 }, { 2, 1, 0, 3 } };

	public static byte[] appendArray(byte[] a, byte[] b) {
		byte[] c = new byte[a.length + b.length];
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);
		return c;
	}

	public static byte[] shiftLeft(byte[] arr) {
		byte[] arrTemp = new byte[arr.length];
		for (int i = 0; i < arr.length; i++) {
			if (i == arr.length - 1) {
				arrTemp[i] = arr[0];
			} else {
				arrTemp[i] = arr[i + 1];
			}
		}

		return arrTemp;
	}

	// P10(k1, k2, k3, k4, k5, k6, k7, k8, k9, k10) = (k3, k5, k2, k7, k4, k10,
	// k1, k9, k8, k6)
	// P8 = (k6, k3, k7, k4, k8, k5, k10, k9)
	public static List<byte[]> keyGeneration(byte[] rawkey) {
		List<byte[]> keys = new ArrayList<byte[]>();

		// Initial Permutation Key. Apply P10.
		byte[] P10 = new byte[10];
		P10[0] = rawkey[2];
		P10[1] = rawkey[4];
		P10[2] = rawkey[1];
		P10[3] = rawkey[6];
		P10[4] = rawkey[3];
		P10[5] = rawkey[9];
		P10[6] = rawkey[0];
		P10[7] = rawkey[8];
		P10[8] = rawkey[7];
		P10[9] = rawkey[5];

		// Next, perform a circular left shift (LS-1), or rotation, separately
		// on the first five bits and the second five bits.
		byte[] leftSide = new byte[5];
		int leftCounter = 0;
		byte[] rightSide = new byte[5];
		int rightCounter = 0;

		// define the left and right sides of the key. Then shift them left.
		for (int i = 0; i < P10.length; i++) {
			if (i < 5) {
				leftSide[leftCounter++] = P10[i];
			} else {
				rightSide[rightCounter++] = P10[i];
			}
		}
		// Shift-Left once for the first key. Apply P8.
		leftSide = shiftLeft(leftSide);
		rightSide = shiftLeft(rightSide);
		byte[] LS_1 = appendArray(leftSide, rightSide);

		byte[] key1 = new byte[8];
		key1[0] = LS_1[5];
		key1[1] = LS_1[2];
		key1[2] = LS_1[6];
		key1[3] = LS_1[3];
		key1[4] = LS_1[7];
		key1[5] = LS_1[4];
		key1[6] = LS_1[9];
		key1[7] = LS_1[8];
		keys.add(key1);

		// Shift left 2 more times for both left and right to get the second
		// key. Apply P8.
		leftSide = shiftLeft(leftSide);
		leftSide = shiftLeft(leftSide);
		rightSide = shiftLeft(rightSide);
		rightSide = shiftLeft(rightSide);
		byte[] LS_2 = appendArray(leftSide, rightSide);

		byte[] key2 = new byte[8];
		key2[0] = LS_2[5];
		key2[1] = LS_2[2];
		key2[2] = LS_2[6];
		key2[3] = LS_2[3];
		key2[4] = LS_2[7];
		key2[5] = LS_2[4];
		key2[6] = LS_2[9];
		key2[7] = LS_2[8];
		keys.add(key2);

		return keys;
	}

	public static byte concatByte(byte one, byte two) {
		byte concat = 0;
		concat = (byte) ((one << 1) | concat);
		concat = (byte) (two | concat);

		return concat;
	}

	public static byte[] extractBit(byte row, byte column, byte[][] sbox) {
		byte[] extracted = new byte[2];

		byte valueRetrieved = sbox[row][column];
		extracted[0] = (byte) (1 & (valueRetrieved >> 1));
		extracted[1] = (byte) (1 & valueRetrieved);

		return extracted;
	}

	public static byte[] F(byte[] rightSide, byte[] key) {
		byte[] result = new byte[4];

		byte[] EP = new byte[8];
		EP[0] = rightSide[3];
		EP[1] = rightSide[0];
		EP[2] = rightSide[1];
		EP[3] = rightSide[2];
		EP[4] = rightSide[1];
		EP[5] = rightSide[2];
		EP[6] = rightSide[3];
		EP[7] = rightSide[0];

		// Apply XOR functionality to EP with key1.
		byte[] XORBit = new byte[8];
		XORBit[0] = (byte) (EP[0] ^ key[0]);
		XORBit[1] = (byte) (EP[1] ^ key[1]);
		XORBit[2] = (byte) (EP[2] ^ key[2]);
		XORBit[3] = (byte) (EP[3] ^ key[3]);
		XORBit[4] = (byte) (EP[4] ^ key[4]);
		XORBit[5] = (byte) (EP[5] ^ key[5]);
		XORBit[6] = (byte) (EP[6] ^ key[6]);
		XORBit[7] = (byte) (EP[7] ^ key[7]);

		// Apply final fK permutation. Look at s0 and s1 for the appropriate
		// location.
		byte[] producedfK = appendArray(
				extractBit(concatByte(XORBit[0], XORBit[3]), concatByte(XORBit[1], XORBit[2]), s0),
				extractBit(concatByte(XORBit[4], XORBit[7]), concatByte(XORBit[5], XORBit[6]), s1));

		result[0] = producedfK[1];
		result[1] = producedfK[3];
		result[2] = producedfK[2];
		result[3] = producedfK[0];

		return result;
	}

	// IP = (k2, k6, k3, k1, k4, k8, k5, k7)
	// IP-1 = (k4, k1, k3, k5, k7, k2, k8, k6)
	public static byte[] Encrypt(byte[] rawkey, byte[] plaintext) {
		// This holds key1 and key2.
		List<byte[]> generatedKey = keyGeneration(rawkey);

		// For testing purposes.
		/*
		 * for(byte[] key: generatedKey){ for(int i = 0; i < key.length; i++){
		 * System.out.print(key[i] + " "); } System.out.println(); }
		 */

		// Apply Initial Permutation => IP(Plain Text)
		byte[] IP = new byte[8];
		IP[0] = plaintext[1];
		IP[1] = plaintext[5];
		IP[2] = plaintext[2];
		IP[3] = plaintext[0];
		IP[4] = plaintext[3];
		IP[5] = plaintext[7];
		IP[6] = plaintext[4];
		IP[7] = plaintext[6];

		// After initial Permutation, we apply fK(L, R) = (L XOR F(R, SK), R)
		// Where L = 4 left bits, R = 4 right bits, and SK = subkey
		// We need to expand the left 4 bits to 8 so it matches the key.
		// E/P = (4, 1, 2, 3, 2, 3, 4, 1)
		byte[] leftSide = new byte[4];
		int leftCounter = 0;
		byte[] rightSide = new byte[4];
		int rightCounter = 0;

		for (int i = 0; i < IP.length; i++) {
			if (i < 4) {
				leftSide[leftCounter++] = IP[i];
			} else {
				rightSide[rightCounter++] = IP[i];
			}
		}

		// Produce the byte using function F. Use this to produce fK
		byte[] F1 = F(rightSide, generatedKey.get(0));
		byte[] leftXORF1 = new byte[4];
		for (int i = 0; i < leftSide.length; i++) {
			leftXORF1[i] = (byte) (leftSide[i] ^ F1[i]);
		}

		// Now switch out the left and right side and perform F again.
		byte[] F2 = F(leftXORF1, generatedKey.get(1));
		byte[] rightXORF2 = new byte[4];
		for (int i = 0; i < rightSide.length; i++) {
			rightXORF2[i] = (byte) (rightSide[i] ^ F2[i]);
		}

		byte[] result = appendArray(rightXORF2, leftXORF1);
		byte[] inverseIP = new byte[8];

		inverseIP[0] = result[3];
		inverseIP[1] = result[0];
		inverseIP[2] = result[2];
		inverseIP[3] = result[4];
		inverseIP[4] = result[6];
		inverseIP[5] = result[1];
		inverseIP[6] = result[7];
		inverseIP[7] = result[5];

		return inverseIP;
	}

	public static byte[] Decrypt(byte[] rawkey, byte[] ciphertext) {
		// This holds key1 and key2.
		List<byte[]> generatedKey = keyGeneration(rawkey);

		// For testing purposes.
		/*
		 * for(byte[] key: generatedKey){ for(int i = 0; i < key.length; i++){
		 * System.out.print(key[i] + " "); } System.out.println(); }
		 */

		// Apply Inverse Initial Permutation => IP-1(Plain Text)
		byte[] IP = new byte[8];
		IP[0] = ciphertext[1];
		IP[1] = ciphertext[5];
		IP[2] = ciphertext[2];
		IP[3] = ciphertext[0];
		IP[4] = ciphertext[3];
		IP[5] = ciphertext[7];
		IP[6] = ciphertext[4];
		IP[7] = ciphertext[6];

		// After initial Permutation, we apply fK(L, R) = (L XOR F(R, SK), R)
		// Where L = 4 left bits, R = 4 right bits, and SK = subkey
		// We need to expand the left 4 bits to 8 so it matches the key.
		// E/P = (4, 1, 2, 3, 2, 3, 4, 1)
		byte[] leftSide = new byte[4];
		int leftCounter = 0;
		byte[] rightSide = new byte[4];
		int rightCounter = 0;

		for (int i = 0; i < IP.length; i++) {
			if (i < 4) {
				leftSide[leftCounter++] = IP[i];
			} else {
				rightSide[rightCounter++] = IP[i];
			}
		}

		// Produce the byte using function F. Use this to produce fK
		byte[] F1 = F(rightSide, generatedKey.get(1));
		byte[] leftXORF1 = new byte[4];
		for (int i = 0; i < leftSide.length; i++) {
			leftXORF1[i] = (byte) (leftSide[i] ^ F1[i]);
		}

		// Now switch out the left and right side and perform F again.
		byte[] F2 = F(leftXORF1, generatedKey.get(0));
		byte[] rightXORF2 = new byte[4];
		for (int i = 0; i < rightSide.length; i++) {
			rightXORF2[i] = (byte) (rightSide[i] ^ F2[i]);
		}

		byte[] result = appendArray(rightXORF2, leftXORF1);
		byte[] inverseIP = new byte[8];

		inverseIP[0] = result[3];
		inverseIP[1] = result[0];
		inverseIP[2] = result[2];
		inverseIP[3] = result[4];
		inverseIP[4] = result[6];
		inverseIP[5] = result[1];
		inverseIP[6] = result[7];
		inverseIP[7] = result[5];

		return inverseIP;
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

		for( int i = 0; i < arList.length; i ++){
			byteAr[i] = Byte.valueOf(arList[i]);
		}
		return byteAr;
	}
	
	public static void main(String[] args) {
		
		// ========== Parsing the text file =======================================
		String filepath = "C:\\Users\\Rose\\Desktop\\SDES.txt";
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
		System.out.println("========== Original SDES ============");
		for( String o : original){
			System.out.println(o);
		}
		System.out.println("=========== Solved SDES =============");
		System.out.printf("%-15s%-15s%-15s", "Raw Key", "Plaintext", "CipherText");
		System.out.println();
		for ( ArrayList<String> arList : applyAnswer){
			boolean solve = false; // This variable is to check whether if a row is "filled" or not ( requires answer )
			
			for(int i = 0; i < arList.size(); i ++){
				if(arList.get(i).equals("?")){
					
					if(i == 1){ // If plaintext requires answer
						String plaintext = byteToString(Decrypt(stringToByteArray(arList.get(0)), stringToByteArray(arList.get(2))));
						System.out.printf("%-15s%-15s%-15s", arList.get(0), plaintext, arList.get(2));
						solve = true;
						break;
					}
					if(i == 2){ // If Ciphertext requires answer
						String ciphertext = byteToString(Encrypt(stringToByteArray(arList.get(0)), stringToByteArray(arList.get(1))));
						System.out.printf("%-15s%-15s%-15s", arList.get(0), arList.get(1), ciphertext);
						solve = true;
						break;
					}
				}
			}
			if(!solve){
				System.out.printf("%-15s%-15s%-15s", arList.get(0), arList.get(1), arList.get(2));
			}
			System.out.println();
		}
	}
}
