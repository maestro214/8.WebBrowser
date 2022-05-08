package com.example.webbrowser

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.webkit.URLUtil
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.behavior.SwipeDismissBehavior
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    private val refreshLayout : SwipeRefreshLayout by lazy {
        findViewById(R.id.refreshLayout)
    }

    private val progressBar : ContentLoadingProgressBar by lazy{
        findViewById(R.id.progressBar)
    }

    private val goHomeButton : ImageButton by lazy{
        findViewById(R.id.goHomeButton)
    }

    private val goBackButton : ImageButton by lazy {
        findViewById(R.id.goBackButton)
    }

    private val goForwardButton : ImageButton by lazy{
        findViewById(R.id.goForwordButton)
    }

    private val addressBar : EditText by lazy{
        findViewById(R.id.addressBar)
    }

    private val webView : WebView by lazy{
        findViewById(R.id.webView)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        bindViews()

    }

    override fun onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack()
        }else{
            super.onBackPressed()
        }
        super.onBackPressed()
    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun initViews(){
//        webView.webViewClient = WebViewClient()
//        //보안상이슈  메뉴접근
//        webView.settings.javaScriptEnabled = true
//        webView.loadUrl("http://www.google.com/")

        webView.apply {
            webChromeClient = WebChromeClient()
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            loadUrl(DEFALT_URL)
        }
    }

    private fun bindViews() {
        addressBar.setOnEditorActionListener { v, acitonId, event ->
            if (acitonId == EditorInfo.IME_ACTION_DONE) {
                val loadingUrl = v.text.toString()
                if(URLUtil.isNetworkUrl(loadingUrl)) {
                    webView.loadUrl(loadingUrl)
                }else{
                    webView.loadUrl("http://$loadingUrl")
                }

            }
            return@setOnEditorActionListener false
        }

        goHomeButton.setOnClickListener {
            webView.loadUrl(DEFALT_URL)
        }

        goBackButton.setOnClickListener {
            webView.goBack()
        }
        goForwardButton.setOnClickListener {
            webView.goForward()
        }

        refreshLayout.setOnClickListener {
            webView.reload()
        }

    }

    inner class WebViewClient : android.webkit.WebViewClient(){

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)

            progressBar.show()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)

            refreshLayout.isRefreshing = false
            progressBar.hide()
            goBackButton.isEnabled = webView.canGoBack()
            goForwardButton.isEnabled = webView.canGoForward()
            addressBar.setText(url)
        }
    }


    inner class WebChromeClient : android.webkit.WebChromeClient(){

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)

            progressBar.progress = newProgress
        }
    }
    companion object{
        private const val DEFALT_URL = "http://www.google.com/"
    }
}