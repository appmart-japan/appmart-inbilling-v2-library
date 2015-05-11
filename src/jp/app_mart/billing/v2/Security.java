package jp.app_mart.billing.v2;

import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import android.util.Base64;
import android.util.Log;

public class Security {

	private static final String TAG = "IABUtil/Security";
	
    public static boolean verifyPurchase(String strPublicKey, String stringEncrypted, String orderId) {
    	if (orderId == null) {
            Log.e(TAG, "data is null");
            return false;
        }    
        if(!encryptRSAToString(orderId,strPublicKey).equals(stringEncrypted)){
            Log.w(TAG, "signature does not match data.");
            return false;
        }
        return true;
    }
    
    /*
     * Encrypt data with the public key
     */
	public static String encryptRSAToString(String text, String strPublicKey) {
	    byte[] cipherText = null;
	    String strEncryInfoData="";
	    try {
	    	
	    	KeyFactory keyFac = KeyFactory.getInstance("RSA");
			KeySpec keySpec = new X509EncodedKeySpec(Base64.decode(strPublicKey.trim().getBytes(), Base64.DEFAULT));
			Key publicKey = keyFac.generatePublic(keySpec);
	    	
	    	
			// get an RSA cipher object and print the provider
			final Cipher cipher = Cipher.getInstance("RSA");
			// encrypt the plain text using the public key
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			cipherText = cipher.doFinal(text.getBytes());
			strEncryInfoData = new String(Base64.encode(cipherText,Base64.DEFAULT));
			
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return strEncryInfoData.replaceAll("(\\r|\\n)", "");
	}
    
}
