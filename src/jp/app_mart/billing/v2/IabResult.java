package jp.app_mart.billing.v2;

public class IabResult {
    int mResponse;
    String mMessage;

    public IabResult(int response, String message) {
        mResponse = response;
        if (message == null || message.trim().length() == 0) {
            mMessage = AppmartIabHelper.getResponseDesc(response);
        }else {
            mMessage = message + " (response: " + AppmartIabHelper.getResponseDesc(response) + ")";
        }
    }
    
    public int getResponse() { return mResponse; }
    public String getMessage() { return mMessage; }
    public boolean isSuccess() { return mResponse == AppmartIabHelper.BILLING_RESPONSE_RESULT_OK; }
    public boolean isFailure() { return !isSuccess(); }
    public String toString() { return "IabResult: " + getMessage(); }
}