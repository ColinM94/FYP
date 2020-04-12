package com.colinmaher.homecareireland

import android.annotation.SuppressLint
import android.content.Context
import android.nfc.NfcAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView

    private var nfcAdapter: NfcAdapter? = null

    // Checks if device has an NFC adapter.
    private val isNfcSupported: Boolean = this.nfcAdapter != null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.supportActionBar!!.hide()

        // WebView Setup
        webView = findViewById(R.id.webview_main)
        webView.settings.javaScriptEnabled = true
        webView.settings.setAppCacheEnabled(false)
        webView.loadUrl("https://carers-app.web.app/")
        webView.addJavascriptInterface(WebAppInterface(this), "Android")

        // NFC Setup
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)?.let { it }

        if(!isNfcSupported) {
            Log.d("TAG", "Yes");
            notification("NFC is supported")
        }else{
            notification("NFC not supported")
        }
    }

    private fun notification(msg:String){
        webView.loadUrl("javascript:And.display('$msg')")
    }
}

/** Instantiate the interface and set the context  */
class WebAppInterface(private val mContext: Context) {
    /** Show a toast from the web page  */
    @JavascriptInterface
    fun showToast(toast: String) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show()
    }
}
