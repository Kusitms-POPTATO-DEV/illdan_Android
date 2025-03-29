package com.poptato.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.Coil
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import com.poptato.design_system.Gray60
import com.poptato.design_system.Gray80
import com.poptato.design_system.Gray90
import com.poptato.design_system.PoptatoTypo
import com.poptato.domain.model.response.category.CategoryIconItemModel
import com.poptato.domain.model.response.category.CategoryIconTotalListModel
import com.poptato.domain.model.response.category.CategoryIconTypeListModel

@Composable
fun CategoryIconBottomSheet(
    categoryIconList: CategoryIconTotalListModel = CategoryIconTotalListModel(),
    onSelectCategoryIcon: (CategoryIconItemModel) -> Unit = {}
) {

    CategoryBottomSheetContent(
        categoryIconList = categoryIconList,
        onSelectCategoryIcon = onSelectCategoryIcon
    )
}

@Composable
fun CategoryBottomSheetContent(
    categoryIconList: CategoryIconTotalListModel = CategoryIconTotalListModel(),
    onSelectCategoryIcon: (CategoryIconItemModel) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(610.dp)
            .background(Gray90)
    ) {
        Box(
            modifier = Modifier
                .padding(top = 12.dp, bottom = 16.dp)
                .height(4.dp)
                .width(64.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Gray80)
                .align(Alignment.CenterHorizontally)
        )

        CategoryIconListTotal(
            categoryIconList = categoryIconList,
            onSelectCategoryIcon = onSelectCategoryIcon
        )
    }
}

@Composable
fun CategoryIconListTotal(
    categoryIconList: CategoryIconTotalListModel = CategoryIconTotalListModel(),
    onSelectCategoryIcon: (CategoryIconItemModel) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(categoryIconList.icons, key = { it.iconType }) { iconTypes ->
            CategoryIconListForType(
                categoryIcons = iconTypes,
                onSelectCategoryIcon = onSelectCategoryIcon
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategoryIconListForType(
    categoryIcons: CategoryIconTypeListModel = CategoryIconTypeListModel(),
    onSelectCategoryIcon: (CategoryIconItemModel) -> Unit = {}
) {

    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()
    Coil.setImageLoader(imageLoader)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 24.dp)
    ) {
        Text(
            text = categoryIcons.iconType,
            style = PoptatoTypo.smMedium,
            color = Gray60
        )

        FlowRow(
            maxItemsInEachRow = 7,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {

            categoryIcons.icons.forEach { item ->
                AsyncImage(
                    model = item.iconImgUrl,
                    contentDescription = "icon ${item.iconId}",
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            onSelectCategoryIcon(item)
                        },
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewCategoryBottomSheet() {
    CategoryBottomSheetContent()
}