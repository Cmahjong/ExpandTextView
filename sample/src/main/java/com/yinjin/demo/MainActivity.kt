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
        data.add(  DemoBean("2018-2-7 11:11:11",
            false,"蒹葭苍苍，白露为霜。\n" +
                    "所谓伊人，在水一方。\n" +
                    "溯洄从之，道阻且长。\n" +
                    "溯游从之，宛在水中。\n" +
                    "蒹葭凄凄，白露未晞。\n" +
                    "所谓伊人，在水之湄。\n" +
                    "溯洄从之，道阻且跻。\n" +
                    "溯游从之，宛在水中。\n" +
                    "蒹葭采采，白露未已。\n" +
                    "所谓伊人，在水之涘。\n" +
                    "溯洄从之，道阻且右。\n" +
                    "溯游从之，宛在水中。","展开收起"))
        for (i in 0..20) {
            data.add(
                DemoBean(
                    "2018-2-7 11:11:11",
                    false,
                    "这是一个支持展开收起功能的自定义控件，这是一个支持展开收起功能的自定义控件，这是一个支持展开收起功能的自定义控件，这是一个支持展开收起功能的自定义控件，这是一个支持展开收起功能的自定义控件，这是一个支持展开收起功能的自定义控件，这是一个支持展开收起功能的自定义控件，这是一个支持展开收起功能的自定义控件，这是一个支持展开收起功能的自定义控件，这是一个支持展开收起功能的自定义控件，这是一个支持展开收起功能的自定义控件，这是一个支持展开收起功能的自定义控件",
                    "展开收起"
                )
            )
        }
        demoAdapter.setNewInstance(data)
    }
}
