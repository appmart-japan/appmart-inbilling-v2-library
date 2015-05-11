package jp.app_mart.billing.v2;

import org.json.JSONException;
import org.json.JSONObject;

public class SkuDetails {

	String mSku;
	String mPrice;
	String mTitle;
	String mDescription;
	String mJson;

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

    @Override
    public String toString() {
        return "SkuDetails:" + mJson;
    }

}
