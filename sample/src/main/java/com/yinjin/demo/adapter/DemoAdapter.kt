package com.yinjin.demo.adapter

import android.graphics.Color
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yinjin.demo.R
import com.yinjin.demo.bean.DemoBean
import com.yinjin.expandtextview.Callback
import com.yinjin.expandtextview.ExpandTextView

/**
 * desc: Demo适配器
 * time: 2018/2/7
 * @author yinYin
 */
class DemoAdapter : BaseQuickAdapter<DemoBean, BaseViewHolder>(R.layout.item) {
    override fun convert(helper: BaseViewHolder, item: DemoBean) {
        helper.apply {
            setText(R.id.tv_title, item.title)
            setText(R.id.tv_time, item.time)
            val expandTextView = getView<ExpandTextView>(R.id.tv_content)
            //设置最大显示行数
            expandTextView.maxLineCount=3
            //收起文案
            expandTextView.collapseText = "收起"
            //展开文案
            expandTextView.expandText = "查看全文"
            //是否支持收起功能
            expandTextView.collapseEnable = true
            //是否给展开收起添加下划线
            expandTextView.underlineEnable = true
            expandTextView.marginStartPX = 100
            expandTextView.marginEndPX = 100
            //收起文案颜色
            expandTextView.collapseTextColor = Color.parseColor("#9c9c9d")
            //展开文案颜色
            expandTextView.expandTextColor = Color.parseColor("#1C7FFD")
            expandTextView.setText(item.content, item.state, object : Callback {
                override fun onExpand() {

                }

                override fun onCollapse() {
                }

                override fun onLoss() {
                }

                override fun onExpandClick() {
                    item.state = !item.state
                    expandTextView.setChanged(item.state)
                }

                override fun onCollapseClick() {
                    item.state = !item.state
                    expandTextView.setChanged(item.state)
                }
            })
        }
    }
}