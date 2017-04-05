package SDES_CASCII;

import java.util.Scanner;

import sdes.SDES;

//CASCII.java
//A simplified version of ASCII for encryption and decryption needs

/**
* <p>Title: CASCII</p>
* <p>Description: A Compact ASCII data format</p>
* <p>Copyright: Copyright (c) 2003</p>
* <p>Company: University of Pennsylvania</p>
* @author Michael May
* @version 1.0
*/

/**
 * A class that contains the Compact ASCII representation of text. The total
 * size of the alphabet is 5 bits - 32 total possibilities.
 */

public class CASCII {
	public CASCII() {
	}

	/**
	 * Takes a single ASCII encoded character and returns its CASCII equivalent.
	 * All characters must be in the set { A-Z, '\'', ':', ',', '.', '?', ' ' }
	 *
	 * @param c
	 *            The ASCII encoded character
	 * @return The CASCII byte encoded equivalent.
	 */
	public static byte Convert(char c) {
		// substitute the old char for our CASCII value
		switch (c) {
		case 'A':
			return A;

		case 'B':
			return B;

		case 'C':
			return C;

		case 'D':
			return D;

		case 'E':
			return E;

		case 'F':
			return F;

		case 'G':
			return G;

		case 'H':
			return H;

		case 'I':
			return I;

		case 'J':
			return J;

		case 'K':
			return K;

		case 'L':
			return L;

		case 'M':
			return M;

		case 'N':
			return N;

		case 'O':
			return O;

		case 'P':
			return P;

		case 'Q':
			return Q;

		case 'R':
			return R;

		case 'S':
			return S;

		case 'T':
			return T;

		case 'U':
			return U;

		case 'V':
			return V;

		case 'W':
			return W;

		case 'X':
			return X;

		case 'Y':
			return Y;

		case 'Z':
			return Z;

		case '?':
			return Question;

		case '\'':
			return Apostrophe;

		case ':':
			return Colon;

		case ',':
			return Comma;

		case '.':
			return Period;

		case ' ':
			return Space;

		default:
			throw new java.lang.IllegalArgumentException(
					"Character must be upper case A-Z or { '\'', ':', ',', '.', '?', ' '} -- found: " + c);
		}

	}

	/**
	 * Converts an array of bits from zeros and ones to a single ASCII
	 * character. If the Array isn't exactly 5 slots large, an exception is
	 * raised.
	 * 
	 * @param bits
	 *            A 5 member arrray that contains the bits to be examined
	 * @return ASCII character that is represented by the bits.
	 */
	public static char Convert(byte[] bits) {
		// just a general case of the bounded one
		return Convert(bits, 0, bits.length - 1);
	}

	/**
	 * Converts an array of bits from zeroes and ones to a single ASCII
	 * character. The indexes must reference a five slot series in the array or
	 * else an exception is raised.
	 * 
	 * @param bits
	 *            The array of bits to be converted
	 * @param start
	 *            The first index in the series
	 * @param end
	 *            The last index in the series
	 * @return ASCII character representation of the bits
	 */
	public static char Convert(byte[] bits, int start, int end) {
		// check that it's 5 bits long only
		if (end - start != 4) {
			throw new java.lang.IllegalArgumentException("Argument must only be five bits long");
		}

		// now find out what number this is
		int val = 0;
		for (int i = start; i <= end; i++) {
			// each bit over is a raising of the power of two
			// multiply it by the bit value - one or zero
			val += java.lang.Math.pow(2, i - start) * bits[i];
		}

		// now value is the number for the letter
		switch (val) {
		case 0:
			return ' ';
		case 1:
			return 'A';
		case 2:
			return 'B';
		case 3:
			return 'C';
		case 4:
			return 'D';
		case 5:
			return 'E';
		case 6:
			return 'F';
		case 7:
			return 'G';
		case 8:
			return 'H';
		case 9:
			return 'I';
		case 10:
			return 'J';
		case 11:
			return 'K';
		case 12:
			return 'L';
		case 13:
			return 'M';
		case 14:
			return 'N';
		case 15:
			return 'O';
		case 16:
			return 'P';
		case 17:
			return 'Q';
		case 18:
			return 'R';
		case 19:
			return 'S';
		case 20:
			return 'T';
		case 21:
			return 'U';
		case 22:
			return 'V';
		case 23:
			return 'W';
		case 24:
			return 'X';
		case 25:
			return 'Y';
		case 26:
			return 'Z';
		case 27:
			return ',';
		case 28:
			return '?';
		case 29:
			return ':';
		case 30:
			return '.';
		case 31:
			return '\'';

		default:
			throw new java.lang.IllegalArgumentException("Argument must be be on interval [0, 31]");
		}
	}

	/**
	 * Takes an ASCII encoded character string and returns a CASCII encoded byte
	 * array. All characters must be in the set { A-Z, '\'', ':', ',', '.', '?',
	 * ' ' }
	 *
	 * @param text
	 *            The ASCII text to convert
	 * @return The CASCII encoded byte array
	 */
	public static byte[] Convert(char[] text) {
		// make an output array of bytes that is the converted text
		byte out[] = new byte[text.length];

		// go through each character in the text and convert it
		for (int i = 0; i < text.length; i++) {
			out[i] = Convert(text[i]);
		}

		// send back the new byte array
		return out;
	}

	/**
	 * Takes an ASCII encoded String and returns a CASCII encoded byte array.
	 * All characters must be in the set { A-Z, '\'', ':', ',', '.', '?', ' ' }
	 * The resulting byte array is padded to a length that is a multiple of 8.
	 *
	 * @param s
	 *            The ASCII String to convert
	 * @return The CASCII encoded byte array
	 */
	public static byte[] Convert(String s) {
		// make this String into a byte array in CASCII form
		byte cas[] = new byte[s.length()];

		// convert it to CASCII byte encoding
		for (int i = 0; i < s.length(); i++) {
			cas[i] = CASCII.Convert(s.charAt(i));
		}

		// now expand the bytes to be just bits
		int size = 5 * s.length();
		// pad it to be a multiple of 8
		size = size + (8 - (size % 8));
		byte bits[] = new byte[size];
		// set them to be zero to begin
		for (int i = 0; i < bits.length; i++) {
			bits[i] = 0;
		}

		// expand the character out into bits
		byte b;
		for (int j = 0; j < cas.length; j++) {
			b = cas[j];
			// make this 5 bit number into real bits
			for (int i = 0; b != 0; i++) {
				if (b % 2 == 0) {
					bits[j * 5 + i] = 0;
				} else {
					bits[j * 5 + i] = 1;
				}

				b /= 2;
			}
		}

		// send back the new byte array
		return bits;
	}

	/**
	 * Takes an array of CASCII bits and returns an ASCII encoded String. If
	 * cas.length is not a multiple of 5, the remaining bits are discarded.
	 *
	 * @param cas
	 *            The CASCII text to convert
	 * @return The ASCII encoded string
	 */
	public static String toString(byte[] cas) {
		// make an output array of bytes that is the converted text
		char output[] = new char[cas.length / 5];

		// go through each character in the text and convert it
		for (int block = 0; block < cas.length; block += 5) {
			// if it's reached the end of the 5-bit strings, quit
			if (block + 5 > cas.length)
				break;

			output[block / 5] = CASCII.Convert(cas, block, block + 4);
		}

		// send back the new byte array
		return new String(output);
	}

	// Make up the letters that will be in the enumeration
	public static byte Space = 0;
	public static byte A = 1;
	public static byte B = 2;
	public static byte C = 3;
	public static byte D = 4;
	public static byte E = 5;
	public static byte F = 6;
	public static byte G = 7;
	public static byte H = 8;
	public static byte I = 9;
	public static byte J = 10;
	public static byte K = 11;
	public static byte L = 12;
	public static byte M = 13;
	public static byte N = 14;
	public static byte O = 15;
	public static byte P = 16;
	public static byte Q = 17;
	public static byte R = 18;
	public static byte S = 19;
	public static byte T = 20;
	public static byte U = 21;
	public static byte V = 22;
	public static byte W = 23;
	public static byte X = 24;
	public static byte Y = 25;
	public static byte Z = 26;
	public static byte Comma = 27;
	public static byte Question = 28;
	public static byte Colon = 29;
	public static byte Period = 30;
	public static byte Apostrophe = 31;

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

	public static void main(String[] args) {
		int input = 0;
		Scanner in = new Scanner(System.in);

		do {
			System.out.println("Select the following options...");
			System.out.println("1.	Encrypt a message.");
			System.out.println("2.	Exit.");

			try {
				input = in.nextInt();

				while (input != 1 && input != 2 && input != 3) {
					System.out.println("Not an appropriate option!\n");
					System.out.println("Select the following options...");
					System.out.println("1.	Encrypt a message.");
					System.out.println("2.	Exit.");

					input = in.nextInt();
				}
			} catch (Exception e) {
				System.out.println("Invalid input!");
				System.exit(0);
			}

			if (input == 1) {
				/*System.out.println("Please enter the message you want to encode (type: '000' to end): ");

				// STR IS THE STRING THAT YOU WANT TO ENCRYPT
				String str = "";
				while(in.hasNext()){
					String val = in.next();
					if(val.equals("000")){
						break;
					}
					
					str = str + val + " ";
					str = str.toUpperCase();
				}*/
				String str = "WHOEVER THINKS HIS PROBLEM CAN BE SOLVED USING CRYPTOGRAPHY, DOESN'T UNDERSTAND HIS PROBLEM AND DOESN'T UNDERSTAND CRYPTOGRAPHY.  ATTRIBUTED BY ROGER NEEDHAM AND BUTLER LAMPSON TO EACH OTHER";

				// CONVERT STR INTO A BIT ARRAY WITH PADDING
				byte[] bittext = CASCII.Convert(str);
				// SKEY IS THE STRING VALUE OF THE 10-BIT KEY THAT YOU WANT TO
				// USE
				String skey = "1011110100";
				byte[] bkey = stringToByteArray(skey);
				byte ciphertext[] = new byte[0];
				byte temp[] = new byte[8];
				byte bitarr[] = new byte[8];
				int bcounter = 0;
				for (int i = 0; i < bittext.length; i++) {
					if (bcounter <= 7) {
						bitarr[bcounter] = bittext[i];
						bcounter++;
					} else {
						temp = SDES.Encrypt(bkey, bitarr);
						ciphertext = SDES.appendArray(ciphertext, temp);
						bitarr[0] = bittext[i];
						bcounter = 1;
					}
					if (i == bittext.length - 1) {
						temp = SDES.Encrypt(bkey, bitarr);
						ciphertext = SDES.appendArray(ciphertext, temp);
					}

				}
				System.out.println("PLAINTEXT: " + str);
				System.out.print("CIPHERTEXT: ");
				String append = "";
				for (int i = 0; i < ciphertext.length; i++) {
					System.out.print(ciphertext[i]);
					append = append + ciphertext[i];
				}
				
				System.out.println();
				System.out.println(append.equals("1011011001111001001011101111110000111110100000000001110111010001111011111101101100010011000000101101011010101000101111100011101011010111100011101001010111101100101110000010010101110001110111011111010101010100001100011000011010101111011111010011110111001001011100101101001000011011111011000010010001011101100011011110000000110010111111010000011100011111111000010111010100001100001010011001010101010000110101101111111010010110001001000001111000000011110000011110110010010101010100001000011010000100011010101100000010111000000010101110100001000111010010010101110111010010111100011111010101111011101111000101001010001101100101100111001110111001100101100011111001100000110100001001100010000100011100000000001001010011101011100101000111011100010001111101011111100000010111110101010000000100110110111111000000111110111010100110000010110000111010001111000101011111101011101101010010100010111100011100000001010101110111111101101100101010011100111011110101011011"));
				System.out.println("\n");
			}
		} while (input != 2);
	}
}