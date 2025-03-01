package com.poptato.data.base

object Endpoints {

    object Auth {
        private const val AUTH = "/auth"
        const val LOGIN = "$AUTH/login"
        const val REFRESH = "$AUTH/refresh"
        const val LOGOUT = "$AUTH/logout"
    }

    object Backlog {
        const val BACKLOG = "/backlog"
        const val BACKLOGS = "/backlogs"
    }

    object Yesterday {
        const val YESTERDAY = "/yesterdays"
    }

    object History {
        const val HISTORIES = "/histories"
        const val CALENDAR = "/calendar"
    }

    object Todo {
        private const val TODO = "/todo"
        const val DELETE = "$TODO/{todoId}"
        const val MODIFY = "$DELETE/content"
        const val DRAG_DROP = "$TODO/dragAndDrop"
        const val DEADLINE = "$DELETE/deadline"
        const val BOOKMARK = "$DELETE/bookmark"
        const val SWIPE = "swipe"
        const val COMPLETION = "$DELETE/achieve"
        const val REPEAT = "$TODO/{todoId}/repeat"
        const val UPDATECATEGIRY = "$DELETE/category"
    }

    object MyPage {
        private const val USER = "/user"
        const val USER_DELETE = "$USER/delete"
        const val USER_DATA = "$USER/mypage"
        const val POLICY = "/policy"
    }

    object Today {
        const val TODAYS = "/todays"
    }

    object Category {
        const val ICONS = "/emojis"
        const val CATEGORY = "/category"
        const val LIST = "$CATEGORY/list"
        const val MODIFY = "$CATEGORY/{categoryId}"
        const val DRAG_DROP = "$CATEGORY/dragAndDrop"
    }
}
