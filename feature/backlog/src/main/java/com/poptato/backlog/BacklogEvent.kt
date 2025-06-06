package com.poptato.backlog

import com.poptato.ui.base.Event

sealed class BacklogEvent: Event {
    data object OnFailedUpdateBacklogList: BacklogEvent()
    data object OnSuccessDeleteBacklog: BacklogEvent()
}