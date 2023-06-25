package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import edu.washington.cs.touchfreelibrary.sensors.CameraGestureSensor;
import edu.washington.cs.touchfreelibrary.sensors.ClickSensor;
import edu.washington.cs.touchfreelibrary.sensors.PermissionCodes;
import edu.washington.cs.touchfreelibrary.utilities.LocalOpenCV;
import edu.washington.cs.touchfreelibrary.utilities.PermissionUtility;

public class WebViewScrollActivity extends AppCompatActivity implements CameraGestureSensor.Listener, ClickSensor.Listener {

    WebView webView;
    LocalOpenCV loader;

    int maxVerticalScroll = 0;

    private final String TAG = "WebViewScrollActivity";

    private final int SCROLL_RANG = 1500;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_webview);

        webView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                int contentHeight = webView.getContentHeight();
                int webViewHeight = webView.getHeight();
                Log.i(TAG,"contentHeight:" + contentHeight + ", webViewHeight:" + webViewHeight);
                maxVerticalScroll = contentHeight - webViewHeight;
            }
        });
        webView.loadUrl("https://www.douguo.com/cookbook/2519829.html");

        if (PermissionUtility.checkCameraPermission(this)) {
            //The third passing in represents a separate click sensor which is not required if you just want the hand motions
            loader = new LocalOpenCV(WebViewScrollActivity.this, WebViewScrollActivity.this, WebViewScrollActivity.this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PermissionUtility.checkCameraPermission(this)) {
            loader = new LocalOpenCV(WebViewScrollActivity.this, WebViewScrollActivity.this, WebViewScrollActivity.this);
        }
    }

    @Override
    public void onGestureUp(CameraGestureSensor caller, long gestureLength) {
        int scrollY = webView.getScrollY();
        Log.i(TAG, "scrollUpY:" + scrollY + ",maxVerticalScroll:" + maxVerticalScroll);
        webView.scrollBy(0, SCROLL_RANG);
    }

    @Override
    public void onGestureDown(CameraGestureSensor caller, long gestureLength) {
        int scrollY = webView.getScrollY();
        Log.i(TAG, "scrollDownY:" + scrollY);
        if (scrollY - SCROLL_RANG < 0) {
            scrollY = -scrollY;
        } else {
            scrollY = -SCROLL_RANG;
        }
        webView.scrollBy(0, scrollY);
    }

    @Override
    public void onGestureLeft(CameraGestureSensor caller, long gestureLength) {

    }

    @Override
    public void onGestureRight(CameraGestureSensor caller, long gestureLength) {

    }

    @Override
    public void onSensorClick(ClickSensor caller) {

    }
}
