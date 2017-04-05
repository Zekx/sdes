package SDES_CASCII;

import sdes.SDES;

public class SDES_CASCII {

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

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//STR IS THE STRING THAT YOU WANT TO ENCRYPT
		String str = "CRYPTOGRAPHY";
		//CONVERT STR INTO A BIT ARRAY WITH PADDING
		byte[] bittext = CASCII.Convert(str);
		//SKEY IS THE STRING VALUE OF THE 10-BIT KEY THAT YOU WANT TO USE
		String skey = "0111001101";
		byte[] bkey = stringToByteArray(skey);
		byte ciphertext[] = new byte[0];
		byte temp[] = new byte[8];
		byte bitarr[] = new byte[8];
		int bcounter = 0;
		for (int i = 0; i < bittext.length; i++){
			if (bcounter <= 7){
				bitarr[bcounter] = bittext[i];
				bcounter++;
			}
			else {
				temp = SDES.Encrypt(bkey, bitarr);
				ciphertext = SDES.appendArray(ciphertext, temp);
				bitarr[0] = bittext[i];
				bcounter = 1;
			}
			if (i == bittext.length - 1){
				temp = SDES.Encrypt(bkey, bitarr);
				ciphertext = SDES.appendArray(ciphertext, temp);
			}

		}
		System.out.println("PLAINTEXT: " + str);
		System.out.print("CIPHERTEXT: ");
		
		
		
	}
}
