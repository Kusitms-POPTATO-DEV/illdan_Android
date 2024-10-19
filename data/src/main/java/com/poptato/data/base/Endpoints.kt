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

    object Todo {
        private const val TODO = "/todo"
        const val DELETE = "$TODO/{todoId}"
        const val MODIFY = "$DELETE/content"
        const val DRAG_DROP = "/dragAndDrop"
        const val DEADLINE = "$DELETE/deadline"
        const val BOOKMARK = "$DELETE/bookmark"
        const val SWIPE = "swipe"
    }

    object MyPage {
        const val USER_DELETE = "/user"
    }
}