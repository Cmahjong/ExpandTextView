package com.yinjin.expandtextview

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.support.v7.widget.AppCompatTextView
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.util.AttributeSet
import android.view.View


/**
 * desc:展开收起
 * time: 2018/1/9 0009
 * @author yinYin
 */
class ExpandTextView : AppCompatTextView {
    /**展开状态 true：展开，false：收起 */
    var expandState: Boolean = false
    /** 状态接口 */
    var mCallback: Callback? = null
    /** 源文字内容 */
    var mText: String? = ""
    /** 最多展示的行数 */
    var maxLineCount = 3
    /** 省略文字 */
    var ellipsizeText = "..."
    /** 展开文案文字 */
    var expandText = "全文"
    /** 展开文案文字颜色 */
    var expandTextColor: Int = Color.parseColor("#1C7FFD")
    /** 收起文案文字 */
    var collapseText = "收起"
    /** 收起文案文字颜色 */
    var collapseTextColor: Int = Color.parseColor("#1C7FFD")
    /**是否支持收起功能*/
    var collapseEnable = false
    /** 是否添加下划线 */
    var underlineEnable = true

    constructor(context: Context) : super(context) {
        initTextView()
    }

    constructor(context: Context, attributes: AttributeSet) : super(context, attributes) {
        initTextView()
    }

    private fun initTextView() {
        movementMethod = LinkMovementMethod.getInstance()
    }

    constructor(context: Context, attributes: AttributeSet, defStyleAttr: Int) : super(context, attributes, defStyleAttr) {
        initTextView()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 文字计算辅助工具
        if (mText.isNullOrEmpty()) {
            setMeasuredDimension(measuredWidth, measuredHeight)
        }
        //StaticLayout对象
        val sl = StaticLayout(mText, paint, measuredWidth - paddingLeft - paddingRight, Layout.Alignment.ALIGN_CENTER, 1f, 0f, true)
        // 总计行数
        var lineCount = sl.lineCount
        //总行数大于最大行数
        if (lineCount > maxLineCount) {
            if (expandState) {
                text = mText
                //是否支持收起功能
                if (collapseEnable) {
                    // 收起文案和源文字组成的新的文字
                    val newEndLineText = mText + collapseText
                    //收起文案和源文字组成的新的文字
                    val spannableString = SpannableString(newEndLineText)
                    //给收起设成监听
                    spannableString.setSpan(object : ClickableSpan() {
                        override fun onClick(widget: View?) {
                            if (mCallback != null) {
                                mCallback!!.onCollapseClick()
                            }
                        }
                    }, newEndLineText.length - collapseText.length, newEndLineText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    if (underlineEnable) {
                        //给收起添加下划线
                        spannableString.setSpan(UnderlineSpan(), newEndLineText.length - collapseText.length, newEndLineText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }
                    //给收起设成蓝色
                    spannableString.setSpan(ForegroundColorSpan(collapseTextColor), newEndLineText.length - collapseText.length, newEndLineText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    text = spannableString
                }
                if (mCallback != null) {
                    mCallback!!.onExpand()
                }
            } else {
                lineCount = maxLineCount
                // 省略文字和展开文案的宽度
                val dotWidth = paint.measureText(ellipsizeText + expandText)
                // 找出显示最后一行的文字
                val start = sl.getLineStart(lineCount - 1)
                val end = sl.getLineEnd(lineCount - 1)
                val lineText = mText!!.substring(start, end)
                // 将第最后一行最后的文字替换为 ellipsizeText和expandText
                var endIndex = 0
                for (i in lineText.length - 1 downTo 0) {
                    val str = lineText.substring(i, lineText.length)
                    // 找出文字宽度大于 ellipsizeText 的字符
                    if (paint.measureText(str) >= dotWidth) {
                        endIndex = i
                        break
                    }
                }
                // 新的文字
                val newEndLineText = mText!!.substring(0, start) + lineText.substring(0, endIndex) + ellipsizeText + expandText
                //全部文字
                val spannableString = SpannableString(newEndLineText)
                //给查看全部设成监听
                spannableString.setSpan(object : ClickableSpan() {
                    override fun onClick(widget: View?) {
                        if (mCallback != null) {
                            mCallback!!.onExpandClick()
                        }
                    }
                }, newEndLineText.length - expandText.length, newEndLineText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                if (underlineEnable) {
                    spannableString.setSpan(UnderlineSpan(), newEndLineText.length - expandText.length, newEndLineText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                //给查看全部设成颜色
                spannableString.setSpan(expandTextColor, newEndLineText.length - expandText.length, newEndLineText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                // 最终显示的文字
                text = spannableString
                if (mCallback != null) {
                    mCallback!!.onCollapse()
                }
            }
        } else {
            text = mText
            if (mCallback != null) {
                mCallback!!.onLoss()
            }

        }
        // 重新计算高度
        var lineHeight = 0
        for (i in 0 until lineCount) {
            val lineBound = Rect()
            sl.getLineBounds(i, lineBound)
            lineHeight += lineBound.height()
        }
        lineHeight = (paddingTop + paddingBottom + lineHeight * lineSpacingMultiplier).toInt()
        setMeasuredDimension(measuredWidth, lineHeight)
    }

    /**
     * 设置要显示的文字以及状态
     * @param text
     * @param expanded true：展开，false：收起
     * @param callback
     */
    fun setText(text: String, expanded: Boolean, callback: Callback) {
        mText = text
        expandState = expanded
        mCallback = callback

        // 设置要显示的文字，这一行必须要，否则 onMeasure 宽度测量不正确
        setText(text)
    }

    /**
     * 展开收起状态变化
     * @param expanded
     */
    fun setChanged(expanded: Boolean) {
        expandState = expanded
        requestLayout()
    }
}

interface Callback {
    /**
     * 展开状态
     */
    fun onExpand()

    /**
     * 收起状态
     */
    fun onCollapse()

    /**
     * 行数小于最小行数，不满足展开或者收起条件
     */
    fun onLoss()

    /**
     * 点击全文
     */
    fun onExpandClick()

    /**
     * 点击收起
     */
    fun onCollapseClick()
}