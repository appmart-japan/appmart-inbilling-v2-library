package jp.app_mart.service;

import android.os.Bundle;

interface InAppBillingV2Interface {

	/**
    * バージョンがサポートされているかチェック
    * @param apiVersion アプリ内課金バージョン
    * @return : 0=成功 、 その他=エラー
    */
    int isBillingSupported(int apiVersion);



    /**
    * リターンされるPendingIntentをstartし、決済画面へ遷移
    * @param apiVersion アプリ内課金バージョン
    * @param appId アプリID
    * @param sku サービスID
    * @param licenceKey ライセンスキー
    * @param developerPayload 決済完了リターンされるパラメータ（任意項目）
    * @return returnされるBundleの情報
    *         "RESPONSE_CODE" 0=成功 、 その他=エラー
    *         "BUY_INTENT" - 決済画面へ遷移されるためのPendingIntent
    *               
    *   注意点：   必ずstartIntentSenderForResultでPendingIntentをstartしてください。
    *           　決済後はonActivityResult()が呼ばれ、[resultCode](OK/CANCEL)と[data]がreturnされる。
    *　　　　　　　　決済が正常に完了した場合は,[data]から決済情報を抽出する:
    *           "RESPONSE_CODE" 0=成功 、 その他=エラー
    *           "INAPP_PURCHASE_DATA" - 決済情報（JSON形式）:
    *              '{"orderId":"9999999999999",
    *                "productId":"exampleSku",
    *				 "purchaseState":"1",
    *                "developerPayload":"example developer payload" }'
    *         "INAPP_DATA_SIGNATURE" - 購入キー
    */
    Bundle getBuyIntent(int apiVersion, String appId, String sku, String licenceKey, String developerPayload);



     /**
    * 購入可能なサービス情報を問い合わせる(JSON形式)
    * @param apiVersion アプリ内課金バージョン
    * @param appId アプリID
    * @param licenceKey ライセンスキー
    * @param skusBundle 取得するサービスのID（"ITEM_ID_LIST"）：StringArrayList
    * @return returnされるBundleの情報
    *         "RESPONSE_CODE" 0=成功 、 その他=エラー
    *         "DETAILS_LIST" 購入可能なサービスの情報（JSON形式）
    *              '{ 	"productId" : "exampleSku", 
    *					"price" : "500",
    *                 	"title : "アプリ名", 
    *					"description" : "アプリ詳細情報" }'
    */
    Bundle getSkuDetails(int apiVersion, String appId, String licenceKey, in Bundle skusBundle);



    /**
    * ユーザーの購入済みサービス情報を問い合わせる
    * @param apiVersion  アプリ内課金バージョン
    * @param appId アプリID
    * @param licenceKey ライセンスキー
    * @return returnされるBundleの情報
    *         "RESPONSE_CODE" 	0=成功 、 その他=エラー
    *         "INAPP_PURCHASE_ITEM_LIST" - 購入済みサービスのID (StringArrayList)
    *         "INAPP_PURCHASE_DATA_LIST" - 購入情報 (StringArrayList)
    *              '{ 	"productId" : "exampleSku", 
    *					"orderId" : "9999999999999",
    *                 	"purchaseTime : "2015/05/01 17:17", 
    *					"developerPayload" : "example developer payload" }'   
    */
    Bundle getPurchases(int apiVersion, String appId, String licenceKey);



    /**
    * 同じサービスを購入する前に必ず購入を消費しなければなりません。
    * @param apiVersion アプリ内課金バージョン
    * @param sku サービスID
    * @param licenceKey ライセンスキー
    * @param purchaseId 決済ID
    * @return 0=成功 、 その他=エラー
    */
    int consumePurchase(int apiVersion, String sku, String licenceKey, String purchaseId);
     
}
