package com.yinjin.expandtextview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.Layout
import android.text.Spannable
import android.text.SpannableString
import android.text.StaticLayout
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView


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
    var marginStartPX = 0//marginStart
    var marginEndPX = 0//marginStart

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

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (measuredWidth == 0) {
            return
        }
        if ((mText?.length?:0) == 0) {
            return
        }
        val canUseWidth =
            resources.displayMetrics.widthPixels - paddingLeft - paddingRight - marginStartPX - marginEndPX
        //StaticLayout对象
        val sl = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StaticLayout.Builder.obtain(
                mText ?: "",
                0,
                mText?.length ?: 0,
                paint,
                canUseWidth
            ).apply {
                setAlignment(Layout.Alignment.ALIGN_CENTER)
            }.build()
        } else {
            StaticLayout(
                mText,
                paint,
                canUseWidth,
                Layout.Alignment.ALIGN_CENTER,
                1f,
                0f,
                true
            )
        }
        var result:SpannableString?=null
        // 总计行数
        var lineCount = sl.lineCount
        //总行数大于最大行数
        if (lineCount > maxLineCount) {
            if (expandState) {
                //是否支持收起功能
                if (collapseEnable) {
                    // 收起文案和源文字组成的新的文字
                    val newEndLineText = mText + collapseText
                    //收起文案和源文字组成的新的文字
                    result = SpannableString(newEndLineText)
                        .apply {
                            //给收起设成监听
                            setSpan(
                                object : ClickableSpan() {
                                    override fun onClick(widget: View) {
                                        mCallback?.onCollapseClick()
                                    }

                                },
                                newEndLineText.length - collapseText.length,
                                newEndLineText.length,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                            if (underlineEnable) {
                                //给收起添加下划线
                                setSpan(
                                    UnderlineSpan(),
                                    newEndLineText.length - collapseText.length,
                                    newEndLineText.length,
                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                                )
                            }
                            //给收起设成蓝色
                            setSpan(
                                ForegroundColorSpan(collapseTextColor),
                                newEndLineText.length - collapseText.length,
                                newEndLineText.length,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                        }
                }
                mCallback?.onExpand()
            } else {

                lineCount = maxLineCount
                // 省略文字和展开文案的宽度
                val dotWidth = paint.measureText(ellipsizeText + expandText)
                // 找出显示最后一行的文字
                val start = sl.getLineStart(lineCount - 1)
                val end = sl.getLineEnd(lineCount - 1)
                var lineText = mText?.substring(start, end) ?: ""
                // 将第最后一行最后的文字替换为 ellipsizeText和expandText
                //如果有换行符的话，会出现收齐状态显示未占满全文状态，那么先判断收齐状态的情况下是否有换行符，然后文字内容加上省略符号是否超过可用宽度
                lineText = lineText.replace("\r\n", "",true)
                lineText = lineText.replace("\n", "",true)
                var newEndLineText: String
                if (paint.measureText(lineText + ellipsizeText + expandText) > canUseWidth) {
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
                    newEndLineText = (mText?.substring(0, start) ?: "") + lineText.substring(
                        0,
                        endIndex
                    ) + ellipsizeText + expandText
                } else {
                    newEndLineText = (mText?.substring(0, start) ?: "") +lineText + ellipsizeText + expandText
                }

                //全部文字
                result = SpannableString(newEndLineText).apply {
                    //给查看全部设成监听
                    setSpan(
                        object : ClickableSpan() {
                            override fun onClick(widget: View) {
                                mCallback?.onExpandClick()
                            }
                        },
                        newEndLineText.length - expandText.length,
                        newEndLineText.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    if (underlineEnable) {
                        setSpan(
                            UnderlineSpan(),
                            newEndLineText.length - expandText.length,
                            newEndLineText.length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                    //给查看全部设成颜色
                    setSpan(
                        ForegroundColorSpan(expandTextColor),
                        newEndLineText.length - expandText.length,
                        newEndLineText.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }

                // 最终显示的文字

                mCallback?.onCollapse()
            }
        } else {
            result = SpannableString(mText)
            mCallback?.onLoss()

        }
        // 重新计算高度
        var lineHeight = 0f
        for (i in 0 until lineCount) {
            lineHeight +=(paint.fontMetrics.bottom - paint.fontMetrics.top)* lineSpacingMultiplier
        }
        lineHeight += paddingTop + paddingBottom
        setMeasuredDimension(measuredWidth, lineHeight.toInt()+1)
        text = result
    }

    /**
     * 设置要显示的文字以及状态
     * @param text
     * @param expanded true：展开，false：收起
     * @param callback
     */
    fun setText(text: String, expanded: Boolean, callback: Callback?) {
        mText = text
        expandState = expanded
        mCallback = callback
        invalidate()
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