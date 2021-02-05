package com.zk.library.Base

import android.app.Activity
import androidx.fragment.app.Fragment
import java.util.*

/**
 * Author:drawn
 * Description: Activity/fragment 实例管理类
 * date:2020/12/24
 */
class AppManager {

    companion object {
        val instance: AppManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            AppManager()
        }
    }

    private var activityStack: Stack<Activity>? = null
    private var fragmentStack: Stack<Fragment>? = null

    private var activityShowStack: Stack<Activity>? = null

    fun resumeActivity(activity: Activity) {
        if (activityShowStack == null) {
            activityShowStack = Stack()
        }
        activityShowStack?.add(activity)
    }

    fun pauseActivity(activity: Activity) {
        if (activityShowStack == null) {
            activityShowStack = Stack()
        }
        activityShowStack?.remove(activity)
    }

    fun addActivity(activity: Activity) {
        if (activityStack == null) {
            activityStack = Stack()
        }
        activityStack?.add(activity)
    }

    fun removeActivity(activity: Activity?) {
        if (activity != null) {
            activityStack?.remove(activity)
        }
    }

    fun getActivityNums(): Int {
        return activityStack?.size ?: 0
    }

    /**
     * 是否还有有activity
     */
    fun isActivity(): Boolean {
        return if (activityStack != null) {
            !activityStack!!.isEmpty()
        } else false
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    fun currentActivity(): Activity? {
        if (activityShowStack != null && activityShowStack!!.size >= 1) {
            return activityShowStack?.lastElement()
        }
        return null
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    fun finishCurActivity() {
        finishActivity(currentActivity())
    }

    /**
     * 结束指定的Activity
     */
    fun finishActivity(activity: Activity?) {
        if (activity != null) {
            if (!activity.isFinishing) {
                activity.finish()
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    fun finishActivity(cls: Class<*>) {
        for (activity in activityStack!!) {
            if (activity.javaClass == cls) {
                finishActivity(activity)
                break
            }
        }
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        var i = 0
        val size = activityStack!!.size
        while (i < size) {
            if (null != activityStack!![i]) {
                finishActivity(activityStack!![i])
            }
            i++
        }
        activityStack!!.clear()
    }

    /**
     * 结束除当前页面其他页面
     */
    fun finishOtherActivity() {
        var i = 0
        val size = activityStack!!.size
        while (i < size) {
            if (null != activityStack!![i] && currentActivity() !== activityStack!![i]) {
                finishActivity(activityStack!![i])
            }
            i++
        }
        activityStack!!.clear()
    }

    /**
     * 获取指定的Activity
     *
     * @author kymjs
     */
    fun getActivity(cls: Class<*>): Activity? {
        if (activityStack != null)
            for (activity in activityStack!!) {
                if (activity.javaClass == cls) {
                    return activity
                }
            }
        return null
    }

    /**
     * 添加Fragment到堆栈
     */
    fun addFragment(fragment: Fragment) {
        if (fragmentStack == null) {
            fragmentStack = Stack()
        }
        fragmentStack!!.add(fragment)
    }

    /**
     * 移除指定的Fragment
     */
    fun removeFragment(fragment: Fragment?) {
        if (fragment != null) {
            fragmentStack!!.remove(fragment)
        }
    }

    /**
     * 是否有Fragment
     */
    fun isFragment(): Boolean {
        return if (fragmentStack != null) {
            !fragmentStack!!.isEmpty()
        } else false
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    fun currentFragment(): Fragment? {
        return if (fragmentStack != null) {
            fragmentStack!!.lastElement()
        } else null
    }

    /**
     * 退出应用程序
     */
    fun exit() {
        try {
            finishAllActivity()
        } catch (e: Exception) {
            activityStack!!.clear()
            e.printStackTrace()
        }
    }
}