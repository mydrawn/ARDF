package com.mydrawn.moudel_guide

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.mydrawn.lib_base.base.BaseActivity
import com.mydrawn.module_guide.R

@Route(path = "/test/GuideActivity")
class GuideActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.guide_activity_guide)
    }

    override fun releaseResources() {
    }
}