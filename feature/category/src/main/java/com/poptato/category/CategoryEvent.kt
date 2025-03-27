package com.poptato.category

import com.poptato.ui.base.Event

sealed class CategoryEvent: Event {
    data object CreateCategoryCompleted: CategoryEvent()
    data object EditCategoryCompleted :CategoryEvent()
}