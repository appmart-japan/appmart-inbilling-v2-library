package jp.app_mart.billing.v2;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import android.util.Log;

/**
 * 
 * @author canujohann
 * appmartアプリ内課金のsignatureをチェックするクラス
 */
public class Security {

	private static final String TAG = "billing/v2/Security";
		
	/*
	* signatureをチェック(verify)
	* @param signature : signature
	* @param origina: original 元の文字列
	* @param publicKey: 公開鍵 
	*/
	public static boolean verifyPurchase(String publicKey,String signature, String original){

		try{
	
			// Get private key from String
			PublicKey pk = loadPublicKey(publicKey);
		
			// text to bytes
			byte[] originalBytes = original.getBytes("UTF8");
		
			//signature to bytes
			byte[] signatureBytes =Base64.decode(signature);
		
			Signature sig = Signature.getInstance("SHA1withRSA");
			sig.initVerify(pk);
			sig.update(originalBytes);
		
			return sig.verify(signatureBytes);
		
		}catch(Exception e){
			e.printStackTrace();
			Log.e("appmartアプリ内課金","error for signature:" + e.getMessage());
			return false;
		}
	}
	
	/*
	* Generate a PublicKey object from a string
	* @ key64 : public key in string format (BASE 64)
	*/
	private static PublicKey loadPublicKey(String key64) throws GeneralSecurityException {
		try{
			//byte[] data = javax.xml.bind.DatatypeConverter.parseBase64Binary(key64);
			byte[] data = Base64.decode(key64);
			X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
			KeyFactory fact = KeyFactory.getInstance("RSA");
			return fact.generatePublic(spec);
		} catch (NoSuchAlgorithmException e) {
	        throw new RuntimeException(e);
	    } catch (InvalidKeySpecException e) {
	        Log.e(TAG, "Invalid key specification.");
	        throw new IllegalArgumentException(e);
	    } catch (Base64DecoderException e) {
	        Log.e(TAG, "Base64 decoding failed.");
	        throw new IllegalArgumentException(e);
	    }
	}
	
	
    
}
