package guru.ioio.testtool2

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import guru.ioio.testtool2.utils.ITakeShot
import guru.ioio.testtool2.utils.ScreenshotUtils
import guru.ioio.testtool2.utils.Utils
import kotlinx.android.synthetic.main.activity_browser.*
import java.io.File

class BrowserActivity : Activity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browser)

        webview.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if (newProgress == 100) {
                    progress.visibility = View.INVISIBLE
                } else {
                    progress.visibility = View.VISIBLE
                    progress.progress = newProgress
                }
            }
        }
        webview.webViewClient = object : WebViewClient() {
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val scheme = request?.url?.scheme
                return if (scheme == "http" || scheme == "https") {
                    super.shouldOverrideUrlLoading(view, request)
                } else {
                    true
                }
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                address.setText(url)
            }
        }
        webview.settings.javaScriptEnabled = true
        webview.settings.setSupportZoom(true)
        webview.settings.loadWithOverviewMode = true
        webview.settings.useWideViewPort = true

        go.setOnClickListener {
            if (TextUtils.isEmpty(address.text)) {
                address.setText(address.hint)
            }
            webview.loadUrl(address.text.toString())
            hideInput()
            address.clearFocus()
        }
        address.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                go.performClick()
            }
            false
        }
        screenshot.setOnClickListener {
            ScreenshotUtils.takeShot(root, object : ITakeShot {
                override fun onTaken(bitmap: Bitmap?) {
                    bitmap?.let {
                        val path = Utils.getCachePath() + "/screenshot.png"
                        ScreenshotUtils.save(path, it)
                        ImageViewerActivity.launch(
                            this@BrowserActivity,
                            FileProvider.getUriForFile(
                                this@BrowserActivity,
                                "$packageName.provider",
                                File(path)
                            )
                        )
                    }
                }
            })


        }
    }

    override fun onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack()
        } else {
            super.onBackPressed()
        }
    }

    private fun hideInput() {
        val im = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        im.hideSoftInputFromWindow(address.windowToken, 0)
    }

    override fun onPause() {
        super.onPause()
        webview.onPause()
    }

    override fun onResume() {
        super.onResume()
        webview.onResume()
    }
}