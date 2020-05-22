package com.yinjin.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.yinjin.demo.adapter.DemoAdapter
import com.yinjin.demo.bean.DemoBean
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val demoAdapter: DemoAdapter by lazy {
        DemoAdapter()
    }
    private val data = arrayListOf<DemoBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        assignView()
        initData()
    }

    /**
     * 初始化View
     */
    private fun assignView() {
        recycle_view.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = demoAdapter
        }
    }

    /**
     * 初始化数据
     */
    private fun initData() {
        for (i in 0..20) {
            data.add(DemoBean("2018-2-7 11:11:11",
                    false,
                    "这是一个支持展开收起功能的自定义控件，这是一个支持展开收起功能的自定义控件，这是一个支持展开收起功能的自定义控件，这是一个支持展开收起功能的自定义控件，这是一个支持展开收起功能的自定义控件，这是一个支持展开收起功能的自定义控件，这是一个支持展开收起功能的自定义控件，这是一个支持展开收起功能的自定义控件，这是一个支持展开收起功能的自定义控件，这是一个支持展开收起功能的自定义控件，这是一个支持展开收起功能的自定义控件，这是一个支持展开收起功能的自定义控件",
                    "展开收起"))
        }
        demoAdapter.setNewInstance(data)
    }
}
