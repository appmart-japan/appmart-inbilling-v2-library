# appmart アプリ内課金 library (V2)

![last-version](http://img.shields.io/badge/last%20version-1.1-green.svg "last version:1.1") 
![license apache 2.0](http://img.shields.io/badge/license-apache%202.0-brightgreen.svg "licence apache 2.0")

## V1対V2

appmartアプリ内課金V2はGoogle play IAB V3に合わせて作られたものとなります。*Google play  IAB*同様に全てのサービスが管理されており、同じサービスを２回購入する場合は**購入を「消費」しなければなりません**。

## 実装方法について

デベロッパーがServiceに接続し**AIDLの各メソッドを実装する(AIDL方式)**か**ライブラリーを使う(Plugin方式)**という二つの実装方法があります。androidのアプリ内課金に慣れていない方またはGoogle V3の経験を持っている方はHelper方式をおすすめします。完全にカスタマイズしたい方はAIDL方式をおすすめします。

> Phonegap/Cordovaユーザーの方は[こちら](https://gist.github.com/info-appmart/6617ce23dcda7fe73f1b)


- - -

# Plugin方式

## 設定

![appmartアプリ内課金V2 :1.1](http://img.shields.io/badge/last%20version-1.1-green.svg "appmartアプリ内課金V2 :1.1") 
![license apache 2.0](http://img.shields.io/badge/license-apache%202.0-brightgreen.svg "licence apache 2.0")

デベロッパーのローカル環境にプロジェクトをダウンロードしていただき、libraryとして参照します。

> プロジェクト URL : [https://github.com/appmart-japan/appmart-inbilling-v2-library](https://github.com/appmart-japan/appmart-inbilling-v2-library)

> サンプル URL : [https://github.com/appmart-japan/appmart-inbilling-v2-sample](https://github.com/appmart-japan/appmart-inbilling-v2-sample)

### プロジェクトclone

先ずはgithubプロジェクトをローカルでcloneしてください。

```shell
cd /home/user/your_directory
git clone https://github.com/appmart-japan/appmart-inbilling-v2-library.git
```

#### プロジェクトに追加 (eclipse)

androidプロジェクトとしてworkspaceに導入します：

+  File
+  Import
+  Existing Android Code Into Workspace
+  先ほどcloneしたプロジェクトを選択

> **注意点**　Eclipseにうまく読み込まれないために、workspace以外のフォルダーにcloneしてください。

![Eclipse:appmart アプリ内課金V2](http://data.app-mart.jp/docs/dev/images/import-library-v2.gif "Eclipse:appmart アプリ内課金 V2")

#### プロジェクトに導入

libraryとしてプロジェクトを導入します：

+  プロジェクトに右クリック　
+  Properties 
+  Android
+  Libraries  :  Add ([appmart-inbilling-as-project]を選択)

![appmart V2:プロジェクトとしてインポート](http://data.app-mart.jp/docs/dev/images/import-as-project-v2.gif "appmart V2:プロジェクトとしてインポート")

#### permission追加

appmartアプリ内課金V2を利用するには下記permissionsを追加してください。

```xml
<!-- 課金API用 -->
<uses-permission android:name="jp.app_mart.permissions.APPMART_BILLING" />
```

## helper使用

[Google play IAB V3のサンプル](http://developer.android.com/training/in-app-billing/preparing-iab-app.html)同様にhelperが用意されており、簡単に**サービス情報・購入情報の取得**と**購入処理**と**消費処理**を実装することができます。

> 各メソッドの引数情報は[メソッド参照](https://gist.github.com/info-appmart/1204bf0595e5fe7b6667#%E3%83%A1%E3%82%BD%E3%83%83%E3%83%89%E5%8F%82%E7%85%A7)をご確認ください。

#### helperインスタンス化

Helperクラスを**インスタンス化**します。

```java
AppmartIabHelper mHelper= new AppmartIabHelper(this, "your_application_id", "your_license_key");
```

#### helper初期設定

**startSetup**メソッドでhelperの初期設定を行います。

```java
mHelper.startSetup(new AppmartIabHelper.OnIabSetupFinishedListener() {
	   //設定完了後呼ばれるcallback
	   public void onIabSetupFinished(IabResult result) {
	      if (!result.isSuccess()){
	    	  if(result.getResponse() == AppmartIabHelper.BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE)
	    		  debugMess("appmartを更新してください。");			    	  
	    	  Log.d("appmart", "appmartアプリ内課金：問題が発生しました。" + result);
	      }else{
	    	  Log.d("appmart", "appmartアプリ内課金：設定完了");
	      }
	   }
});
```

上記のコードで設定が完了になります。設定が完了した後に**onIabSetupFinished**(callback)が呼ばれます。

**onDestroy**メッソドでappmartのアプリ内課金serviceと切断します。

```java
@Override
public void onDestroy() {
   super.onDestroy();
   if (mHelper != null) mHelper.dispose();
   mHelper = null;
}
```

#### Inventory取得

**inventory**を取得することによってエンドユーザーの**購入履歴**と**サービス情報**を取得することができます。
Googleのアプリ内課金V3同様にqueryInventoryAsyncで情報を取得できます。（UIThreadでも呼び出し可能です）

> 消費されたサービスは購入履歴に入りませんのでご注意ください !

```java
//取得したいサービスのID
List<String> additionalSkuList = new ArrayList<String>();
additionalSkuList.add("my_service_id_1");
additionalSkuList.add("my_service_id_2");

// Inventoryを取得
mHelper.queryInventoryAsync(additionalSkuList, mQueryFinishedListener);

// inventory取得後のcallback
AppmartIabHelper.QueryInventoryFinishedListener 
   mQueryFinishedListener = new AppmartIabHelper.QueryInventoryFinishedListener() {
   
   //　Inventory取得後のcallback
   public void onQueryInventoryFinished(IabResult result, Inventory inventory){
      	if (result.isFailure()) {
          if(result.getResponse() == AppmartIabHelper.BILLING_RESPONSE_USER_NOT_LOG){
	  	debugMess("appmartでログインしてください。");
	  }else{
		debugMess(result.getMessage());
	  }
        return;
	}

	// TODO UIを更新
       	String applePrice =  inventory.getSkuDetails("my_service_id_1").getPrice();

	// 購入サービス情報取得
	//Purchase purchases = inventory.getAllOwnedSkus();
	
	// サービス情報の取得
	//SkuDetails skus = inventory.getAllSkuDetails()
	       
   }
}
```

#### サービス購入

サービスを購入する際に、launchPurchaseFlowを使ってください。

```java
mHelper.launchPurchaseFlow(this,"service_id", 10001,mPurchaseFinishedListener,"test string");
   
// 決済完了後 callback
AppmartIabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener 
= new AppmartIabHelper.OnIabPurchaseFinishedListener() {
	
	// 決済完了後 callback
	public void onIabPurchaseFinished(IabResult result, Purchase purchase){
   		if (result.isFailure()) {
			 Log.d(TAG, "購入エラー: " + result);
			 return;
		}
		
		debugMess("サービスが購入されました (" + purchase.getSku() + ")");
		// TODO UI更新
	}
};	
```

:warning: セキュリティー向上

デベロッパーの秘密鍵で署名された**決済ID**が**シグネチャ**に入っております。シグネチャを使うことによって購入の有効性チェックが可能になります。javaでの実装の場合は[library](https://github.com/appmart-japan/appmart-inbilling-v2-library/blob/master/src/jp/app_mart/billing/v2/Security.java)をご確認ください。

| n  |情報  |取得  |
|---|-----------|---|
| 1  |  決済ID   | purchase.getOrderId()  | 
| 2  |  シグネチャ   | purchase.getSignature()  | 

> 有効性チェックは外部サーバーでの実装をおすすめします。


決済情報を正常に受け取るために下記のように**onActivityResult**を変更してください。

```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
	    super.onActivityResult(requestCode, resultCode, data);
	}else {
    		Log.d(TAG, "onActivityResult handled by AppmartIabHelper.");
    	}	
}
```

:warning: セキュリティー向上
 
公開鍵を追加することによって、開発者の秘密鍵で署名された決済IDが確認されます。外部サーバーでの有効性チェックできない場合はおすすめします。

```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	if (!mHelper.handleActivityResult(requestCode, resultCode, data, "YOUR_PUBLIC_KEY")) {
	    super.onActivityResult(requestCode, resultCode, data);
	}else {
    		Log.d(TAG, "onActivityResult handled by AppmartIabHelper.");
    	}	
}
```




#### サービスを消費

Googleアプリ内課金V3同様に全てのサービスが管理されており、同じサービスを購入する前に必ず購入を消費しなければなりません。
過去購入されたサービスを消費するにはConsumeメソッドを使ってください。

> 継続決済の場合は「消費」は不可能になります！

```java
// inventoryで購入履歴をチェック
if(mInventory.hasPurchase(entry.getSku())){
	Purchase p = mInventory.getPurchase("my_service_id_1");
	mHelper.consumeAsync(p, mConsumeFinishedListener);
}else{
	Log.d(mActivity.getClass().getSimpleName(), "未消費の情報がございません。");
}

// サービス消費後 callback
AppmartIabHelper.OnConsumeFinishedListener onConsumeFinishedListener = new AppmartIabHelper.OnConsumeFinishedListener(){
	@Override
	public void onConsumeFinished(Purchase purchase,IabResult result) {		
		if(result.isFailure()){
			debugMess("サービスが消費されませんでした。");
		}else{
			debugMess("サービスが消費されました。");
			//TODO UIを更新
		}
	}
};
```

:warning: 複数のPurchasesを消費する場合は：

```java

if(inventory.getAllPurchases().size()>0) {
	mHelper.consumeAsync(inventory.getAllPurchases(), onConsumeMultiFinishedListener);
}

// サービス消費後 callback
AppmartIabHelper.OnConsumeMultiFinishedListener onConsumeMultiFinishedListener = new AppmartIabHelper.OnConsumeMultiFinishedListener(){
        @Override
        public void onConsumeMultiFinished(List<Purchase> purchase, List<IabResult> result) {
            debugMess("全てのアイテムが消費されました。");
        }
};
```

## メソッド参照

#### mHelper.queryInventoryAsync()

```java
mHelper.queryInventoryAsync(additionalSkuList, mQueryFinishedListener);
```

> Inventory情報を取得

| n  |型                               |参考               |
|---|---------------------------------|-------------------|
| 1  |  List                           | 取得希望ItemのID  |
| 2  |  QueryInventoryFinishedListener|   取得後のcallback| 


callbackの二つ目の引数は[inventory]オブジェクトです。inventoryで簡単に**過去購入**されたサービス（未消費）と指定された**サービス情報**を取得できます。詳細は[Inventoryクラス](https://github.com/appmart-japan/appmart-inbilling-v2-library/blob/master/src/jp/app_mart/billing/v2/Inventory.java)をご確認ください。


#### mHelper.launchPurchaseFlow()

```java
mHelper.launchPurchaseFlow(this,"service_id", 10001,mPurchaseFinishedListener,"test string");
```

> 決済を実行

| n  |型                               |参考  |
|---|-----------|---|
| 1  |  Activity   | Activity  | 
| 2  | String     | サービスID |
| 3  |  String   |   onActivityResult用のresponse_code | 
| 4  |  OnIabPurchaseFinishedListener   |   決済完了後のcallback| 
| 5  |  String   |   決済完了後にreturnされる値(任意)| 

#### consumeAsync

```java 
mHelper.consumeAsync(p, mConsumeFinishedListener);
```

> 過去購入されたサービスを消費

| n  |型                               |参考  |
|---|-----------|---|
| 1  |  Purchase   |  queryInventoryAsyncで取得したpurchase | 
| 2  |   OnConsumeFinishedListener   | 消費完了後のcallback  |


- - -
- - -

# AIDL方式

![appmartアプリ内課金V2 :1.1](http://img.shields.io/badge/last%20version-1.1-green.svg "appmartアプリ内課金V2 :1.1") 
![license apache 2.0](http://img.shields.io/badge/license-apache%202.0-brightgreen.svg "licence apache 2.0")

## 設定+サービス接続


####  AIDLファイルの生成

Appmartの課金システムサービスとやりとりするために、AIDLファイルを作成する必要があります。
 
| package名              | class名                        |
| ---------------------- |------------------------------- |
|  jp.app_mart.service   |　InAppBillingV2Interface.aidl |


```java
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
```

> 必ず上記5つのメソッドを用意してください 。メソッドの引数・戻り値は【リファレンス】を参照してください。

#### パーミッション追加

appmartを利用するには下記permissionsを追加してください。

```xml
<!-- 課金API用 -->
<uses-permission android:name="jp.app_mart.permissions.APPMART_BILLING" />
```

#### ServiceConnectionを作成

少なくとも以下の処理がアプリ内で必要です。

+ IInAppBillingServiceにバインドします。 
+ appmart アプリに IPCでbillingリクエストを送ります。 
+ 各billingリクエストごとに返ってくる同期レスポンスメッセージを処理します。 

appmartのアプリ内課金サービスへの接続を確立するには、Activityを**InAppBillingV2Interface**にバインドするための ServiceConnection を実装します。**onServiceDisconnected** と **onServiceConnected** メソッドをOverrideし、接続が確立された後に IInAppBillingServiceインスタンスへの参照を取得します。 

```java

InAppBillingV2Interface mService;
ServiceConnection mServiceConn = new ServiceConnection() {
	@Override
	public void onServiceDisconnected(ComponentName name) {
		mService = null;
	}
	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		mService = InAppBillingV2Interface.Stub.asInterface((IBinder) service);
	}
};
```

#### Appmartとの連動
 
ActivityのonCreateメソッド内で、**bindService**メソッドを呼んでバインドします。メソッドには アプリ内課金サービスを参照するIntentおよびServiceConnectionのインスタンスを渡します。
 
```java
Intent serviceIntent = new Intent();
serviceIntent.setClassName("jp.app_mart", "jp.app_mart.service.AppmartInBillingV2Service");

if (!mContext.getPackageManager().queryIntentServices(serviceIntent, 0).isEmpty()) {
	mContext.bindService(serviceIntent, mServiceConn,Context.BIND_AUTO_CREATE);
} else {
	if (listener != null) {
		listener.onIabSetupFinished(new IabResult(BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE,
				"Billing service unavailable on device."));
	}
}
```

> **注意点**：Activityが終了する場合、サービスからのアンバインドを忘れずに行ってください。

```java
@Override
public void onDestroy() {
    super.onDestroy();
    if (mService != null){
        unbindService(mServiceConn);
    }
}
```

サービスが購入されると、ユーザーがそのサービスの**所有権**を取得したと認識し、そのサービスが**消費されるまで**同じプロダクトIDのサービスが購入されるのを防止します。アプリではサービスがどのように消費されるかをコントロールすることができ、appmartにそのサービスが**再度購入**できるようになったことを通知できます。また、ユーザーによって作成された購入リストをappmartから素早く取得することができます。例えば、ユーザーがアプリを起動したときにユーザーの購入リストをリストアしたときに便利です。

#### 購入可能なサービス情報を問い合わせ

ユーザーの購入状況と関係なくappmartからサービスの詳細を問い合わせることができます。アプリ内課金V2サービスにリクエストを送るには、サービスIDの String ArrayList を作成し、それを "ITEM_ID_LIST" というキーで Bundle に保持します。 

```java
ArrayList<String> skuList = new ArrayList<String> ();
skuList.add("my_first_service_id");
skuList.add("my_second_service_id");
Bundle querySkus = new Bundle();
querySkus.putStringArrayList(“ITEM_ID_LIST”, skuList);
```

appmart から情報を取得するには、アプリ内課金V2 の**getSkuDetails** メソッドを呼び出します。第1引数には アプリ内課金 version の "2"、第2引数には呼び出しアプリのID、
第3引数には作成した Bundle を渡します。

```java
// appmartにサービス情報を問い合わせる
Bundle skuDetails = mService.getSkuDetails(2, "application_id", "license_key", querySkus);
```

リクエストが成功した場合、返ってきた Bundle には RESPONSE_CODE というキーで 0 が含まれます。 

##### Response Code 一覧:

| 値         | 備考    |
|---|----------------|
|0|成功 |
|1|ユーザーが[back]キーを押しました|
|4|指定されたサービスイDが購入不可能|
|5|渡された引数が正しくありません|
|6|致命的なエラーが発生|
|7|所有権あり|
|8|所有権がないため、消費不可能|


> getSkuDetails メソッドをメインスレッドから呼ばないでください。このメソッドはネットワークリクエストのトリガーになり、メインスレッドをブロックします。 

問い合わせた結果は、"DETAIL_LIST" というキーで String ArrayList に格納され、各購入情報は JSON 形式の文字列で格納されます。

```java
int response = skuDetails.getInt("RESPONSE_CODE");
if (response == 0) {
   ArrayList<String> responseList
      = skuDetails.getStringArrayList("DETAILS_LIST");

   for (String thisResponse : responseList) {
      JSONObject object = new JSONObject(thisResponse);
      String sku = object.getString("productId");
      String price = object.getString("price");
      // TODO UIを更新
   }
}
```

##### JSON項目一覧：

| json項目 | 備考    |
|---|----------------|
|productId|サービスID|
|price|サービスの価格 (例：¥120)|
|title|サービス名|
|description|サービス説明|
|price_currency_code|通貨|

#### サービス購入

アプリから購入リクエストを開始するには、getBuyIntentメソッドを呼び出します。 第1引数にはアプリ内課金**version**の"2"、第2引数には呼び出し**アプリID**、第3引数には**サービス ID**、第4引数にはアプリのライセンスキー、第4引数には **developerPayload** 文字列を渡します。 developerPayload 文字列は、購入情報としてappmartから返してほしい付加的な引数を指定するのに使います。 

```java
Bundle buyIntentBundle = mService.getBuyIntent(2, "application_id", "my_service_id", "license_key", "developer_pay_load");
```

リクエストが成功した場合、返ってきた Bundle には RESPONSE_CODE というキーで **0** が含まれます。また、"BUY_INTENT" というキーで取得できる PendingIntent で購入フローを開始出来ます。

```java
PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
```

購入処理を完了するには、取得したpendingIntentで**startIntentSenderForResult**メソッドを呼び出します。

```java
startIntentSenderForResult(pendingIntent.getIntentSender(),
   	1001, new Intent(), Integer.valueOf(0), Integer.valueOf(0),
   	Integer.valueOf(0));
```

PendingIntentのレスポンスをアプリのonActivityResultに返します。onActivityResultメソッドはActivity.RESULT_OK (1) もしくは Activity.RESULT_CANCELED (0) を resultCode として持ちます。

| 値 | 備考    |
|---|----------------|
|RESPONSE_CODE|　0=成功|
|INAPP_PURCHASE_DATA|購入情報をもつJSON文字列|
|INAPP_DATA_SIGNATURE|デベロッパーの公開鍵で暗号化された決済ID|

購入データは JSON 形式の文字列として Intent に格納されており、"INAPP_PURCHASE_DATA" というキーで取得できます。 

```js
{   
   "orderId":"1299976316905",   
   "packageName":"com.example.app",  
   "productId":"exampleSku",  
   "developerPayload":"developer_payload",   
 }
 ```
 
 > onActivityResultの実装
 
 ```java
 @Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
   if (requestCode == 1001) {
      int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
      String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
      String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");
      
      if (resultCode == RESULT_OK) {
         try {
            JSONObject jo = new JSONObject(purchaseData);
            String sku = jo.getString("productId");
            alert("You have bought the " + sku );
          }
          catch (JSONException e) {
             alert("Failed to parse purchase data.");
             e.printStackTrace();
          }
      }
   }
}
 ```
 
 > セキュリティー向上のためdeveloperPayloadの文字列が以前に送った購入リクエストのものと一致するかチェックします。 
 
 #### 購入したサービスを問い合わせる
 
 ユーザーによる購入情報を取得するには、アプリ内課金V2のgetPurchasesメソッドを呼び出します。第1引数には **アプリ内課金version**の "2"、第2引数には呼び出し**アプリのID**、第3引数には**ライセンスキー**を渡します。 
 
 ```java
 Bundle ownedServices = mService.getPurchases(2, "application_id", "license_key");
 ```
 
 appmartサービスは、デバイスに現在ログインしているユーザーアカウントの購入についてのみ返します。リクエストが成功した場合、返ってきた Bundle には RESPONSE_CODE というキーで **0** が含まれます。また、"INAPP_PURCHASE_ITEM_LIST" というキーで product ID のリストが、"INAPP_PURCHASE_DATA_LIST" というキーでオーダー詳細のリストが含まれます。
 
 ```java
 int response = ownedItems.getInt("RESPONSE_CODE");
if (response == 0) {
   ArrayList<String> ownedSkus =  ownedItems.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
   ArrayList<String>  purchaseDataList =  ownedItems.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
   
   for (int i = 0; i < purchaseDataList.size(); ++i) {
      String purchaseData = purchaseDataList.get(i);
      String sku = ownedSkus.get(i);

      // do something with this purchase information
      // e.g. display the updated list of products owned by user
   }

   // if continuationToken != null, call getPurchases again
   // and pass in the token to retrieve more items
}
 ```
 
 #### 購入サービスの消費
 
 アプリ内課金V2を使って、appmartの購入されたサービスの所有権をトラックできます。一度サービスが購入されると、その所有権があると考えられappmartから購入できなくなります。appmart が再びサービスを購入可能にする前に、サービスの消費リクエストを送らなければなりません。 

消費メカニズムをどう利用するかは開発者次第になります。 
典型的には、ユーザーが複数回購入したいような一時的な利益があるプロダクト（例えばゲーム内通貨や装備）は消費可能な実装にします。一度だけ購入したり、永続的な効果を提供するプロダクト（例えばプレミアムアップグレード）は消費しないように実装します。 

購入サービスの消費を記録するには、consumePurchaseメソッドを呼び出します。第1引数にはアプリ内課金versionの"2"、第2引数には呼び出しアプリのサービスID、第3引数にはライセンスキー、第4引数には決済IDを渡します。

```java
int response = mService.consumePurchase(2, "my_first_service", "license_key", "9999999999999");
```

consumePurchase はメインスレッドから呼び出さないでください。このメソッドはネットワークリクエストのトリガーになり、メインスレッドをブロックします。 

購入したサービスがユーザーにどのように提供されるかをコントロールしトラックするかは開発者の責任です。例えば、ゲーム内通貨をユーザーが購入した場合、購入した通貨の量に応じてプレイヤーの状態を変えなければなりません。 

Security Recommendation: ユーザーにアプリ内課金を消費する利益をプロビジョニングする前に消費リクエストを送らなければなりません。サービスをプロビジョンする前にappmartから成功した消費レスポンスを受けとっていることを確認します。


 
