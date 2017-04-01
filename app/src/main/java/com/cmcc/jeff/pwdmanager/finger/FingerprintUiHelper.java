package com.cmcc.jeff.pwdmanager.finger;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.security.keystore.KeyProperties;
import android.support.v4.app.ActivityCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmcc.jeff.pwdmanager.R;

/**
 * Created by jeff on 2017/3/31.
 */
public class FingerprintUiHelper extends FingerprintManager.AuthenticationCallback {

    private static final long ERROR_TIMEOUT_MILLIS = 1600;
    private static final long SUCCESS_DELAY_MILLIS = 1300;

    private CryptoObjectHelper mCryptoObjectHelper;
    private final FingerprintManager mFingerprintManager;

    private final ImageView mIcon;
    private final TextView mErrorTextView;
    private final Callback mCallback;
    private CancellationSignal mCancellationSignal;

    private boolean mSelfCancelled;

    public FingerprintUiHelper(FingerprintManager mFingerprintManager, ImageView mIcon,
                               TextView mErrorTextView, Callback mCallback) {
        this.mFingerprintManager = mFingerprintManager;
        this.mIcon = mIcon;
        this.mErrorTextView = mErrorTextView;
        this.mCallback = mCallback;
    }

    public boolean isFingerprintAnthAvailable() {
        if(ActivityCompat.checkSelfPermission(mErrorTextView.getContext(), Manifest.permission.USE_FINGERPRINT)
                == PackageManager.PERMISSION_GRANTED) {
           return mFingerprintManager.isHardwareDetected() && mFingerprintManager.hasEnrolledFingerprints();
        }
        return false;
    }

    public void startListening() {
        if (!isFingerprintAnthAvailable()) {
            return;
        }
        mCancellationSignal = new CancellationSignal();
        mSelfCancelled = false;
        mFingerprintManager.authenticate(getCryptoObject(), mCancellationSignal, 0, this, null);
        mIcon.setImageResource(R.mipmap.ic_fp_40px);
    }

    /**
     * return FingerprintManager.CryptoObject
     * @return
     */
    public FingerprintManager.CryptoObject getCryptoObject() {
        if(mCryptoObjectHelper == null) {
            mCryptoObjectHelper = new CryptoObjectHelper();
        }
        return mCryptoObjectHelper.buildCryptoObject();
    }

    public void stopListening() {
        if (mCancellationSignal != null) {
            mSelfCancelled = true;
            mCancellationSignal.cancel();
            mCancellationSignal = null;
        }
    }

    @Override
    public void onAuthenticationError(int errMsgId, final CharSequence errString) {
        if (!mSelfCancelled) {
            showError(errString);
            mIcon.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mCallback.onError((String) errString);
                }
            }, ERROR_TIMEOUT_MILLIS);
        }
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        showError(helpString);
    }

    @Override
    public void onAuthenticationFailed() {
        showError(mIcon.getResources().getString(
                R.string.fingerprint_not_recognized));
    }

    @Override
    public void onAuthenticationSucceeded(final FingerprintManager.AuthenticationResult result) {
        mErrorTextView.removeCallbacks(mResetErrorTextRunnable);
        mIcon.setImageResource(R.drawable.ic_fingerprint_success);
        mErrorTextView.setTextColor(
                mErrorTextView.getResources().getColor(R.color.success_color, null));
        mErrorTextView.setText(
                mErrorTextView.getResources().getString(R.string.fingerprint_success));
        mIcon.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCallback.onAuthenticated(result);
            }
        }, SUCCESS_DELAY_MILLIS);
    }

    /**
     * 提示失败信息，1600ms后再试
     * @param error
     */
    private void showError(CharSequence error) {
        mIcon.setImageResource(R.drawable.ic_fingerprint_error);
        mErrorTextView.setText(error);
        mErrorTextView.setTextColor(
                mErrorTextView.getResources().getColor(R.color.warning_color, null));
        mErrorTextView.removeCallbacks(mResetErrorTextRunnable);
        mErrorTextView.postDelayed(mResetErrorTextRunnable, ERROR_TIMEOUT_MILLIS);
    }

    private Runnable mResetErrorTextRunnable = new Runnable() {
        @Override
        public void run() {
            mErrorTextView.setTextColor(
                    mErrorTextView.getResources().getColor(R.color.hint_color, null));
            mErrorTextView.setText(
                    mErrorTextView.getResources().getString(R.string.fingerprint_hint));
            mIcon.setImageResource(R.mipmap.ic_fp_40px);
        }
    };

    public interface  Callback {

        void onAuthenticated(FingerprintManager.AuthenticationResult result);

        void onError(String error);
    }

}
