package com.poptato.setting.servicedelete

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poptato.design_system.Cancel
import com.poptato.design_system.Danger50
import com.poptato.design_system.DeleteDialogContent
import com.poptato.design_system.DeleteDialogTitle
import com.poptato.design_system.DeleteReasonInputHint
import com.poptato.design_system.Gray00
import com.poptato.design_system.Gray100
import com.poptato.design_system.Gray40
import com.poptato.design_system.Gray60
import com.poptato.design_system.Gray80
import com.poptato.design_system.Gray95
import com.poptato.design_system.MissingFeature
import com.poptato.design_system.NotUsed
import com.poptato.design_system.PoptatoTypo
import com.poptato.design_system.R
import com.poptato.design_system.TooComplex
import com.poptato.design_system.UserDeleteBtn
import com.poptato.design_system.UserDeleteContent
import com.poptato.design_system.UserDeleteTitle
import com.poptato.domain.model.enums.DialogType
import com.poptato.domain.model.enums.UserDeleteType
import com.poptato.domain.model.response.dialog.DialogContentModel
import com.poptato.ui.common.PoptatoCheckBox
import com.poptato.ui.util.AnalyticsManager
import kotlinx.coroutines.flow.SharedFlow
import timber.log.Timber

@Composable
fun ServiceDeleteScreen(
    deleteUserName: SharedFlow<String>,
    goBackToSetting: () -> Unit = {},
    showDialog: (DialogContentModel) -> Unit = {},
    showDeleteFinishScreen: (String) -> Unit = {}
) {

    val viewModel: ServiceDeleteViewModel = hiltViewModel()
    val uiState: ServiceDeletePageState by viewModel.uiState.collectAsStateWithLifecycle()
    val isDeleteValid by remember {
        derivedStateOf {
            uiState.selectedReasonList.isNotEmpty() || uiState.deleteInputReason.isNotEmpty()
        }
    }

    LaunchedEffect(deleteUserName) {
        deleteUserName.collect {
            viewModel.getDeleteUserName(it)
        }
    }

    ServiceDeleteContent(
        onClickCloseBtn = { goBackToSetting() },
        onClickDeleteBtn = {
            showDialog(
                DialogContentModel(
                    dialogType = DialogType.TwoBtn,
                    titleText = DeleteDialogTitle,
                    dialogContentText = DeleteDialogContent,
                    positiveBtnText = UserDeleteBtn,
                    cancelBtnText = Cancel,
                    positiveBtnAction = {
                        viewModel.userDelete()
                        AnalyticsManager.logEvent(
                            eventName = "delete_account",
                            params = mapOf("reason" to uiState.selectedReasonList.map { it.reasons }.toString(), "user_input_reason" to uiState.deleteInputReason)
                        )
                        showDeleteFinishScreen(uiState.userName)
                    }
                )
            )
        },
        isSelectedReasonsList = uiState.selectedReasonList,
        onSelectedReason = { viewModel.setSelectedReason(it) },
        deleteInputReason = uiState.deleteInputReason,
        onValueChange = { newValue -> viewModel.onValueChange(newValue) },
        isDeleteBtnValid = isDeleteValid
    )
}

@Composable
fun ServiceDeleteContent(
    onClickCloseBtn: () -> Unit = {},
    onClickDeleteBtn: () -> Unit = {},
    onSelectedReason: (UserDeleteType) -> Unit = {},
    isSelectedReasonsList: List<UserDeleteType> = emptyList(),
    deleteInputReason: String = "",
    onValueChange: (String) -> Unit = {},
    isDeleteBtnValid: Boolean = false,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray100)
    ) {

        CloseBtn(
            onClickCloseBtn = onClickCloseBtn
        )

        Text(
            text = UserDeleteTitle,
            color = Gray00,
            style = PoptatoTypo.xxLSemiBold,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .padding(start = 25.dp)
        )

        Text(
            text = UserDeleteContent,
            color = Gray40,
            style = PoptatoTypo.mdRegular,
            modifier = Modifier
                .padding(top = 8.dp)
                .padding(horizontal = 25.dp)
        )

        DeleteReasonsContent(
            isSelectedReasonsList = isSelectedReasonsList,
            onSelectedReason = onSelectedReason,
            deleteInputReason = deleteInputReason,
            onValueChange = onValueChange
        )

        UserDeleteBtn(
            onClickDeleteBtn = onClickDeleteBtn,
            isDeleteBtnValid = isDeleteBtnValid
        )
    }
}

@Composable
fun CloseBtn(
    onClickCloseBtn: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .padding(end = 16.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_close_no_bg),
            contentDescription = "",
            tint = Color.Unspecified,
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.TopEnd)
                .clickable { onClickCloseBtn() }
        )
    }
}

@Composable
fun DeleteReasonsContent(
    onSelectedReason: (UserDeleteType) -> Unit = {},
    isSelectedReasonsList: List<UserDeleteType> = emptyList(),
    deleteInputReason: String = "",
    onValueChange: (String) -> Unit = {}
) {

    val reasonsList: List<UserDeleteType> = listOf(UserDeleteType.NOT_USED_OFTEN, UserDeleteType.MISSING_FEATURES, UserDeleteType.TOO_COMPLEX)
    val reasonsContextList: List<String> = listOf(NotUsed, MissingFeature, TooComplex)

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 17.dp)
            .padding(top = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(reasonsList, key = { _, item -> item }) { index, item ->
            DeleteReasonsItem(
                reason = reasonsContextList[index],
                isSelected = isSelectedReasonsList.contains(item),
                onClick = { onSelectedReason(item) }
            )
        }

        item {
            DeleteReasonTextField(
                textInput = deleteInputReason,
                onValueChange = onValueChange
            )
        }
    }
}

@Composable
fun DeleteReasonsItem(
    reason: String,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Gray95)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
                .padding(vertical = 18.dp),
        ) {
            PoptatoCheckBox(
                checkBoxDrawable = R.drawable.ic_checked_danger,
                isChecked = isSelected,
                onCheckedChange = { onClick() }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = reason,
                style = PoptatoTypo.smMedium,
                color = Gray00
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DeleteReasonTextField(
    textInput: String = "",
    onValueChange: (String) -> Unit = {}
) {
    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val imeVisible = WindowInsets.isImeVisible

    LaunchedEffect(imeVisible) {
        if (!imeVisible) {
            focusManager.clearFocus()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Gray95)
    ) {
        BasicTextField(
            value = textInput,
            onValueChange = { input ->
                onValueChange(input)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
                .padding(vertical = 16.dp)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },
            textStyle = PoptatoTypo.smMedium.copy(color = Gray00),
            cursorBrush = SolidColor(Gray00),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    if (textInput.isEmpty() && !isFocused) {
                        Text(
                            text = DeleteReasonInputHint,
                            style = PoptatoTypo.smMedium,
                            color = Gray60
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}

@Composable
fun UserDeleteBtn(
    isDeleteBtnValid: Boolean = false,
    onClickDeleteBtn: () -> Unit = {}
) {

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 16.dp)
                .padding(bottom = 8.dp)
                .background(
                    if (isDeleteBtnValid) Danger50 else Gray95,
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable(
                    enabled = isDeleteBtnValid,
                    onClick = {
                        onClickDeleteBtn()
                    }
                )
        ) {
            Text(
                text = UserDeleteBtn,
                style = PoptatoTypo.mdSemiBold,
                color = if (isDeleteBtnValid) Gray100 else Gray80,
                modifier = Modifier
                    .padding(vertical = 15.dp)
                    .align(Alignment.Center)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewSetting() {
    ServiceDeleteContent()
}