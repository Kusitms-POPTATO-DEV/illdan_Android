package com.poptato.setting.logout

data class LogOutDialogState (
    var isShowDialog: Boolean = false,
    val onDismissRequest: () -> Unit = {},
    val onClickLogOutBtn: () -> Unit = {},
    val onClickBackBtn: () -> Unit = {}
)