package com.aboot.test

import android.content.Context
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.ProgressBar

open class CommonWebView: WebView {
    private var mProgressBar: ProgressBar? = null
    private var mEnableProgressBar = true

    constructor(context: Context): super(context) {
        init()
    }
    constructor(context: Context, attributes: AttributeSet): super(context, attributes) {
        init()
    }
    constructor(context: Context, attributes: AttributeSet, style: Int): super(context, attributes, style) {
        init()
    }

    private fun init() {
        initProgressBar(context)

        defaultSettings()
    }

    private fun initProgressBar(context: Context?) {
        mProgressBar = ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal)
        mProgressBar?.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, 4, 0, 0)
        addView(mProgressBar)
    }

    private fun defaultSettings() {
        defaultNormalSettings()
        enableWebCache()
        supportViewPort(true)
        supportZoom(false)
        setEnableProgressBar(true)
    }

    private fun defaultNormalSettings() {
        isVerticalScrollBarEnabled = false
        isHorizontalScrollBarEnabled = false
        settings.displayZoomControls = false
        when {
            Build.VERSION.SDK_INT >= 21 -> {
                setLayerType(View.LAYER_TYPE_HARDWARE, null as Paint?)
            }
        }
        defaultSecuritySettings()
    }

    private fun enableWebCache() {
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        settings.setAppCachePath(context.cacheDir.absolutePath + "webcache/")
        settings.setAppCacheEnabled(true)
        settings.domStorageEnabled = true
    }

    private fun defaultSecuritySettings() {
        settings.savePassword = false
        settings.javaScriptEnabled = true
        supportFileAccess(false)
    }

    private fun supportFileAccess(support: Boolean) {
        settings.allowFileAccess = support
        settings.allowFileAccessFromFileURLs = support
        settings.allowUniversalAccessFromFileURLs = support
    }

    private fun supportViewPort(support: Boolean) {
        settings.useWideViewPort = support
        settings.loadWithOverviewMode = support
    }

    private fun supportZoom(support: Boolean) {
        settings.setSupportZoom(support)
        settings.builtInZoomControls = support
    }

    override fun canGoBack(): Boolean {
        return if (copyBackForwardList().currentIndex == 1) {
            false
        } else super.canGoBack()
    }

    fun updateProgress(newProgress: Int) {
        if (mProgressBar == null || !mEnableProgressBar || newProgress < 0) {
            return
        }
        if (newProgress == 100) {
            mProgressBar?.visibility = GONE
        } else {
            if (mProgressBar?.visibility == GONE) {
                mProgressBar?.visibility = VISIBLE
            }
            mProgressBar?.progress = newProgress
        }
    }

    private fun setEnableProgressBar(enableProgressBar: Boolean) {
        mEnableProgressBar = enableProgressBar
        if (mProgressBar != null) {
            if (enableProgressBar) {
                mProgressBar?.visibility = VISIBLE
            } else {
                mProgressBar?.visibility = GONE
            }
        }
    }
}
