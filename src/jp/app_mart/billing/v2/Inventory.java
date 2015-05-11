package jp.app_mart.billing.v2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inventory {
	
	Map<String, SkuDetails> mSkuMap = new HashMap<String, SkuDetails>();
	Map<String, Purchase> mPurchaseMap = new HashMap<String, Purchase>();

	public Inventory() {
	}

	/** Returns the listing details for an in-app service. */
	public SkuDetails getSkuDetails(String sku) {
		return mSkuMap.get(sku);
	}

	/**
	 * Returns purchase information for a given service, or null if there is no
	 * purchase.
	 */
	public Purchase getPurchase(String sku) {
		return mPurchaseMap.get(sku);
	}

	/** Returns whether or not there exists a purchase of the given service. */
	public boolean hasPurchase(String sku) {
		return mPurchaseMap.containsKey(sku);
	}

	/** Return whether or not details about the given service are available. */
	public boolean hasDetails(String sku) {
		return mSkuMap.containsKey(sku);
	}

	/**
	 * Erase a purchase (locally) from the inventory, given its product ID. This
	 * just modifies the Inventory object locally and has no effect on the
	 * server! This is useful when you have an existing Inventory object which
	 * you know to be up to date, and you have just consumed an item
	 * successfully, which means that erasing its purchase data from the
	 * Inventory you already have is quicker than querying for a new Inventory.
	 */
	public void erasePurchase(String sku) {
		if (mPurchaseMap.containsKey(sku))
			mPurchaseMap.remove(sku);
	}

	/** Returns a list of all owned services IDs. */
	public List<String> getAllOwnedSkus() {
		return new ArrayList<String>(mPurchaseMap.keySet());
	}

	/** Returns a list of all purchases. */
	public List<Purchase> getAllPurchases() {
		return new ArrayList<Purchase>(mPurchaseMap.values());
	}
	
	/** Returns a list of all purchases. */
	public List<SkuDetails> getAllSkuDetails() {
		return new ArrayList<SkuDetails>(mSkuMap.values());
	}

	/** Add a skuDetails to the list of Skus */
	void addSkuDetails(SkuDetails d) {
		mSkuMap.put(d.getSku(), d);
	}

	/** Add a purchase to the Purchases list */
	void addPurchase(Purchase p) {
		mPurchaseMap.put(p.getSku(), p);
	}
	
}
