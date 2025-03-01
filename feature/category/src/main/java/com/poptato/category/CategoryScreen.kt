package com.poptato.category

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.poptato.design_system.CategoryAddTitle
import com.poptato.design_system.CategoryIconDialogTitle
import com.poptato.design_system.CategoryModifyTitle
import com.poptato.design_system.CategoryNameDialogTitle
import com.poptato.design_system.CategoryNameInputTitle
import com.poptato.design_system.Complete
import com.poptato.design_system.Confirm
import com.poptato.design_system.Gray00
import com.poptato.design_system.Gray100
import com.poptato.design_system.Gray70
import com.poptato.design_system.Gray90
import com.poptato.design_system.Gray95
import com.poptato.design_system.PoptatoTypo
import com.poptato.design_system.R
import com.poptato.domain.model.enums.CategoryScreenType
import com.poptato.domain.model.enums.DialogType
import com.poptato.domain.model.response.category.CategoryIconItemModel
import com.poptato.domain.model.response.category.CategoryIconTotalListModel
import com.poptato.domain.model.response.category.CategoryScreenContentModel
import com.poptato.domain.model.response.dialog.DialogContentModel
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun CategoryScreen(
    goBackToBacklog: () -> Unit = {},
    goToBacklog: () -> Unit = {},
    showIconBottomSheet: (CategoryIconTotalListModel) -> Unit = {},
    selectedIconInBottomSheet: SharedFlow<CategoryIconItemModel>,
    showDialog: (DialogContentModel) -> Unit = {},
    screenContent: SharedFlow<CategoryScreenContentModel>
) {

    val viewModel: CategoryViewModel = hiltViewModel()
    val uiState: CategoryPageState by viewModel.uiState.collectAsStateWithLifecycle()
    val interactionSource = remember { MutableInteractionSource() }
    val isCategorySettingValid by remember {
        derivedStateOf {
            uiState.categoryName.isNotBlank() && uiState.selectedIcon != null
        }
    }

    LaunchedEffect(selectedIconInBottomSheet) {
        selectedIconInBottomSheet.collect {
            viewModel.getSelectedIcon(it)
        }
    }

    LaunchedEffect(screenContent) {
        screenContent.collect {
            viewModel.getModifyIconItem(it)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is CategoryEvent.GoToBacklog -> {
                    goToBacklog()
                }
            }
        }
    }

    CategoryContent(
        uiState = uiState,
        interactionSource = interactionSource,
        onClickBackBtn = { goBackToBacklog() },
        onClickFinishBtn = {
            if (isCategorySettingValid) {
                viewModel.finishSettingCategory()
            } else {
                showDialog(
                    DialogContentModel(
                        dialogType = DialogType.OneBtn,
                        titleText = if (uiState.categoryName.isBlank()) CategoryNameDialogTitle else CategoryIconDialogTitle,
                        positiveBtnText = Confirm
                    )
                )
            }
        },
        onValueChange = { newValue -> viewModel.onValueChange(newValue) },
        onClickSelectCategoryIcon = {
            showIconBottomSheet(uiState.categoryIconList)
        }
    )
}

@Composable
fun CategoryContent(
    uiState: CategoryPageState = CategoryPageState(),
    interactionSource: MutableInteractionSource = MutableInteractionSource(),
    onClickBackBtn: () -> Unit = {},
    onClickFinishBtn: () -> Unit = {},
    onValueChange: (String) -> Unit = {},
    onClickSelectCategoryIcon: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray100)
    ) {
        CategoryTitle(
            interactionSource = interactionSource,
            onClickBackBtn = onClickBackBtn,
            onClickFinishBtn = onClickFinishBtn,
            screenType = uiState.screenType
        )

        CategoryAddContent(
            uiState = uiState,
            interactionSource = interactionSource,
            onValueChange = onValueChange,
            onClickSelectIcon = onClickSelectCategoryIcon
        )
    }
}

@Composable
fun CategoryTitle(
    interactionSource: MutableInteractionSource,
    screenType: CategoryScreenType,
    onClickBackBtn: () -> Unit = {},
    onClickFinishBtn: () -> Unit = {}
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 16.dp, end = 16.dp)
    ) {

        Icon(
            painter = painterResource(id = R.drawable.ic_back_arrow),
            contentDescription = "back",
            tint = Color.Unspecified,
            modifier = Modifier
                .padding(top = 28.dp, bottom = 4.dp)
                .size(width = 24.dp, height = 24.dp)
                .clickable(
                    indication = null,
                    interactionSource = interactionSource,
                    onClick = { onClickBackBtn() }
                )
        )

        Text(
            text = if (screenType == CategoryScreenType.Add) CategoryAddTitle else CategoryModifyTitle,
            style = PoptatoTypo.mdSemiBold,
            color = Gray00,
            modifier = Modifier
                .padding(top = 28.dp, bottom = 4.dp)
                .align(Alignment.Center)
        )

        Box(
            modifier = Modifier
                .padding(top = 24.dp)
                .align(Alignment.CenterEnd)
        ) {
            AddFinishBtn(
                interactionSource = interactionSource,
                onClickFinishBtn = onClickFinishBtn
            )
        }
    }
}

@Composable
fun AddFinishBtn(
    interactionSource: MutableInteractionSource,
    onClickFinishBtn: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(8.dp))
            .background(Gray95)
            .clickable(
                indication = null,
                interactionSource = interactionSource,
                onClick = { onClickFinishBtn() }
            )
    ) {
        Text(
            text = Complete,
            style = PoptatoTypo.smSemiBold,
            color = Gray00,
            modifier = Modifier
                .padding(vertical = 6.dp, horizontal = 12.dp)
        )
    }
}

@Composable
fun CategoryAddContent(
    uiState: CategoryPageState = CategoryPageState(),
    interactionSource: MutableInteractionSource,
    onValueChange: (String) -> Unit = {},
    onClickSelectIcon: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {

        Box(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
                .padding(end = 16.dp)
        ) {
            CategoryNameTextField(
                textInput = uiState.categoryName,
                onValueChange = onValueChange
            )
        }

        Box(
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.CenterVertically)
        ) {
            if (uiState.categoryIconImgUrl.isEmpty()) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add_category_icon),
                    contentDescription = "add category icon",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            indication = null,
                            interactionSource = interactionSource,
                            onClick = { onClickSelectIcon() }
                        )
                )
            } else {
                Image(
                    painter = rememberAsyncImagePainter(model = uiState.categoryIconImgUrl),
                    contentDescription = "selected icon",
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            indication = null,
                            interactionSource = interactionSource,
                            onClick = { onClickSelectIcon() }
                        )
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategoryNameTextField(
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
    ) {
        BasicTextField(
            value = textInput,
            onValueChange = { input ->
                if (input.length <= 15) {
                    onValueChange(input)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },
            textStyle = PoptatoTypo.xLMedium.copy(color = Gray00),
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ){
                        if (textInput.isEmpty() && !isFocused) {
                            Text(
                                text = CategoryNameInputTitle,
                                style = PoptatoTypo.xLMedium,
                                color = Gray70
                            )
                        }
                        innerTextField()
                    }
                    Divider(
                        color = if (textInput.isEmpty()) Gray90 else Gray00,
                        thickness = 1.dp,
                        modifier = Modifier.
                        padding(top = 4.dp)
                    )
                }
            }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewCategory() {
    CategoryContent()
}