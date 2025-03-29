package com.poptato.ui.common

import com.poptato.design_system.DEADLINE
import com.poptato.design_system.DEADLINE_DDAY
import com.poptato.design_system.DEADLINE_PASSED

fun formatDeadline(dDay: Int?): String {
    return when {
        dDay == null -> ""
        dDay > 0 -> String.format(DEADLINE, dDay)
        dDay < 0 -> String.format(DEADLINE_PASSED, -dDay)
        else -> DEADLINE_DDAY
    }
}