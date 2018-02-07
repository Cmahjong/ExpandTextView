# SuperCalendar

展开收起控件
[![](https://jitpack.io/v/yinjinyj/ExpandTextView.svg)](https://jitpack.io/#yinjinyj/ExpandTextView)

其他先不说先看效果图

![sample](/gif/1.gif)

使用方法

Step 1 Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
Step 2. Add the dependency

	dependencies {
    	        compile 'com.github.yinjinyj:ExpandTextView:1.0.0'
    	}
  
Step 3该控件可以单独使用，也可以配合RecycleView一起使用，只是展开收起的状态需要维护demo里面的状态是直接放在实体类里面的，大家可以参考，也可以用其他方法去维护

常见方法说明：
 1:使用ExpandTextView必须使用里面的

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
 
 2，你也可以设置：

        最多展示的行数               max上班
        LineCount
        省略文字的文案               ellipsizeText
        展开文案文字                 expandText
        展开文案文字颜色             expandTextColor
        收起文案文字                 collapseText
        收起文案文字颜色             collapseTextColor
        是否支持收起功能             collapseEnable
        是否添加展开收起的下划线     underlineEnable

3，你也可以参考demo,demo就是一个RecycleView


