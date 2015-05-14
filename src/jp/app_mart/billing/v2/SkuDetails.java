package jp.app_mart.billing.v2;

import org.json.JSONException;
import org.json.JSONObject;

public class SkuDetails {

	String mSku;
	String mPrice;
	String mTitle;
	String mDescription;
	String mJson;
	String mPriceCurrencyCode;

	/* constructor 1 */
	public SkuDetails() {}

	public SkuDetails(String sku, String price, String title, String description) {
		this.mSku=sku;
		this.mPrice=price;
		this.mTitle= title;
		this.mDescription= description;
	}
	
	/* constructor 2 */
    public SkuDetails(String jsonSkuDetails) throws JSONException {
        
        mJson = jsonSkuDetails;
        
        JSONObject o = new JSONObject(mJson);
        mSku = o.optString("productId");
        mPrice = o.optString("price");
        mTitle = o.optString("title");
        mDescription = o.optString("description");
        mPriceCurrencyCode = o.optString("price_currency_code");
        
    }
    
    public String getSku(){
    	return mSku;
    }

	public String getPrice() {
		return mPrice;
	}

	public String getTitle() {
		return mTitle;
	}

	public String getDescription() {
		return mDescription;
	}
	
	public String getPriceCurrencyCode() {
		return mPriceCurrencyCode;
	}
	
	public String getOriginalJson(){
		return mJson;
	}

    @Override
    public String toString() {
        return "SkuDetails:" + mJson;
    }

}
