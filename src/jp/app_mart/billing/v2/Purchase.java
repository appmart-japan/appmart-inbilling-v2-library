package jp.app_mart.billing.v2;

import org.json.JSONException;
import org.json.JSONObject;

public class Purchase {
	String mOrderId;
	String mSku;
	String mDeveloperPayload;
	String mOriginalJson;
	String mSignature;
	
	/** Constructor 1 */
	public Purchase(){};
	
	/** Constructor 2 */
	public Purchase(String jsonPurchaseInfo)
			throws JSONException {
		mOriginalJson = jsonPurchaseInfo;
		JSONObject o = new JSONObject(mOriginalJson);
		mOrderId = o.optString("orderId");
		mSku = o.optString("productId");
		mDeveloperPayload = o.optString("developerPayload");
	}
	
	/** Constructor 3 */
	public Purchase(String jsonPurchaseInfo, String signature)
			throws JSONException {
		mOriginalJson = jsonPurchaseInfo;
		JSONObject o = new JSONObject(mOriginalJson);
		mOrderId = o.optString("orderId");
		mSku = o.optString("productId");
		mDeveloperPayload = o.optString("developerPayload");
		mSignature = signature;
	}
	
	public String getOrderId() {
		return mOrderId;
	}

	public String getSku() {
		return mSku;
	}

	public String getDeveloperPayload() {
		return mDeveloperPayload;
	}

	public String getOriginalJson() {
		return mOriginalJson;
	}

	public String getSignature() {
		return mSignature;
	}

	@Override
	public String toString() {
		return "PurchaseInfo :" + mOriginalJson;
	}
}
