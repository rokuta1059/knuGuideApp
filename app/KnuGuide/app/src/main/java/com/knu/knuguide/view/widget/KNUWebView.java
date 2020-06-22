package com.knu.knuguide.view.widget;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class KNUWebView extends WebView {
    public interface KSWebViewListener {
        void onWebViewClick(String url);
    }

    public KNUWebView(Context context) {
        super(context);
        initDefaultSetting();
    }

    public KNUWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDefaultSetting();
    }

    public KNUWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDefaultSetting();
    }

    private void initDefaultSetting() {
        WebSettings settings = this.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setUseWideViewPort(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setLoadWithOverviewMode(true);

        // Zoom 관련 옵션
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true);
        settings.setDisplayZoomControls(false);

        setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        setScrollbarFadingEnabled(true);

        // Download 할성화
        setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                browserIntent.setPackage("com.android.chrome");
                try{
                    getContext().startActivity(browserIntent);
                }catch(Exception e){
                    browserIntent.setPackage(null);
                    getContext().startActivity(browserIntent);
                }
            }
        });

        setWebViewClient( new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                System.out.println(url);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                browserIntent.setPackage("com.android.chrome");
                try{
                    getContext().startActivity(browserIntent);
                }catch(Exception e){
                    browserIntent.setPackage(null);
                    getContext().startActivity(browserIntent);
                }
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                System.out.println(request.getUrl());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, request.getUrl());
                    browserIntent.setPackage("com.android.chrome");
                    try {
                        getContext().startActivity(browserIntent);
                    } catch (Exception e) {
                        browserIntent.setPackage(null);
                        getContext().startActivity(browserIntent);
                    }
                    return true;
                }else{
                    return false;
                }
            }
        });
        setWebChromeClient(new WebChromeClient());
    }
}
