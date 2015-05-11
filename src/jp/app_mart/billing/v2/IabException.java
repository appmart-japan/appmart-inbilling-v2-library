package jp.app_mart.billing.v2;

public class IabException extends Exception {
	
	private static final long serialVersionUID = 1L;
	IabResult mResult;

	public IabException(IabResult r) {
		this(r, null);
	}

	public IabException(int response, String message) {
		this(new IabResult(response, message));
	}

	public IabException(IabResult r, Exception cause) {
		super(r.getMessage(), cause);
		mResult = r;
	}

	public IabException(int response, String message, Exception cause) {
		this(new IabResult(response, message), cause);
	}

	/** Returns the IAB result (error) that this exception signals. */
	public IabResult getResult() {
		return mResult;
	}
}
