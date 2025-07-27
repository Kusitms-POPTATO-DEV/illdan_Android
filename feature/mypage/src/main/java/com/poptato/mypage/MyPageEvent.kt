package com.poptato.mypage

import com.poptato.ui.base.Event

sealed class MyPageEvent: Event {
    data object SendCommentSuccess: MyPageEvent()
}