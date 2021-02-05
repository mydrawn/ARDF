package com.mydrawn.ARDF

import com.blankj.utilcode.util.TimeUtils
import org.junit.Test

import org.junit.Assert.*
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        var time = TimeUtils.date2String(Date(), "MMddHHmm_")
        System.out.println(time)
    }
}