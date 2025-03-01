package com.poptato.ui.common

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.poptato.design_system.CategoryEmptyBottomSheet
import com.poptato.design_system.Complete
import com.poptato.design_system.DELETE
import com.poptato.design_system.Gray00
import com.poptato.design_system.Gray100
import com.poptato.design_system.Gray80
import com.poptato.design_system.Gray90
import com.poptato.design_system.Gray95
import com.poptato.design_system.PoptatoTypo
import com.poptato.design_system.Primary60
import com.poptato.design_system.R
import com.poptato.domain.model.response.category.CategoryItemModel

@Composable
fun CategoryBottomSheet(
    categoryId: Long = -1,
    categoryList: List<CategoryItemModel> = emptyList(),
    onDismiss: () -> Unit = {},
    onCategorySelected: (Long?) -> Unit = {}
) {
    var selectedCategory by remember { mutableLongStateOf(categoryId) }

    CategoryListBottomSheetContent(
        categoryList = categoryList,
        selectedCategory = selectedCategory,
        onClickCategory = { selectedCategory = it },
        onClickDeleteBtn = { onCategorySelected(null) },
        onClickFinishBtn = {
            if (selectedCategory.toInt() == -1) onCategorySelected(null) else onCategorySelected(selectedCategory)
        },
        onDismiss = onDismiss
    )
}

@Composable
fun CategoryListBottomSheetContent(
    categoryList: List<CategoryItemModel> = emptyList(),
    selectedCategory: Long? = null,
    onClickCategory: (Long) -> Unit = {},
    onClickDeleteBtn: () -> Unit = {},
    onClickFinishBtn: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Gray95)
    ) {

        if (categoryList.size == 2) {
            Text(
                text = CategoryEmptyBottomSheet,
                color = Gray80,
                style = PoptatoTypo.lgMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 129.dp, bottom = 105.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 16.dp)
                    .height(264.dp),
            ) {
                itemsIndexed(categoryList, key = { _, item -> item.categoryId }) { index, item ->
                    if (index != 0 && index != 1) {
                        CategoryBottomSheetItem(
                            iconImg = item.categoryImgUrl,
                            categoryName = item.categoryName,
                            isSelected = item.categoryId == selectedCategory,
                            onClick = { onClickCategory(item.categoryId) }
                        )
                    }
                }
            }
        }

        CategoryModifyBtnContent(
            isCategoryListEmpty = categoryList.size == 2,
            onClickDeleteBtn = onClickDeleteBtn,
            onClickFinishBtn = onClickFinishBtn,
            onDismiss = onDismiss
        )
    }
}

@Composable
fun CategoryBottomSheetItem(
    iconImg: String = "",
    categoryName: String = "",
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(if (isSelected) Gray90 else Gray95)
            .padding(vertical = 12.dp)
            .padding(horizontal = 24.dp)
            .clickable {
                onClick()
            },
    ) {
        AsyncImage(
            model = iconImg,
            contentDescription = "category icon",
            modifier = Modifier
                .size(24.dp),
            contentScale = ContentScale.Crop
        )

        Text(
            text = categoryName,
            style = PoptatoTypo.mdMedium,
            color = Gray00,
            modifier = Modifier
                .padding(start = 8.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        if (isSelected) {
            Icon(
                painter = painterResource(id = R.drawable.ic_check),
                contentDescription = "check icon",
                tint = Color.Unspecified
            )
        }

    }
}

@Composable
fun CategoryModifyBtnContent(
    isCategoryListEmpty: Boolean = false,
    onClickDeleteBtn: () -> Unit = {},
    onClickFinishBtn: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    Spacer(modifier = Modifier.height(16.dp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 16.dp)
    ) {
        if (!isCategoryListEmpty) {
            CategoryBottomSheetBtn(
                btnText = DELETE,
                btnColor = Gray90,
                textColor = Gray00,
                textStyle = PoptatoTypo.mdMedium,
                modifier = Modifier.weight(1f),
                onClickBtn = {
                    onClickDeleteBtn()
                    onDismiss()
                }
            )

            Spacer(modifier = Modifier.width(8.dp))
        }

        CategoryBottomSheetBtn(
            btnText = Complete,
            btnColor = Primary60,
            textColor = Gray100,
            textStyle = PoptatoTypo.mdSemiBold,
            modifier = Modifier.weight(1f),
            onClickBtn = {
                onClickFinishBtn()
                onDismiss()
            }
        )
    }

    Spacer(modifier = Modifier.height(16.dp))
}

@SuppressLint("ModifierParameter")
@Composable
fun CategoryBottomSheetBtn(
    btnText: String = "",
    btnColor: Color = Color.Unspecified,
    textColor: Color = Color.Unspecified,
    textStyle: TextStyle = PoptatoTypo.lgSemiBold,
    onClickBtn: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(btnColor)
            .clickable { onClickBtn() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = btnText,
            style = textStyle,
            color = textColor
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewCategoryListBottomSheet() {
    CategoryListBottomSheetContent()
}