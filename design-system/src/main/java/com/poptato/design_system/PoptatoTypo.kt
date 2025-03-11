package com.poptato.design_system

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.sp

fun Dp.toSp(): TextUnit = TextUnit(value = this.value, type = TextUnitType.Sp)

val fontFamily = FontFamily(
    Font(R.font.pretendard_black, FontWeight.Black),
    Font(R.font.pretendard_bold, FontWeight.Bold),
    Font(R.font.pretendard_extra_bold, FontWeight.ExtraBold),
    Font(R.font.pretendard_extra_light, FontWeight.ExtraLight),
    Font(R.font.pretendard_light, FontWeight.Light),
    Font(R.font.pretendard_medium, FontWeight.Medium),
    Font(R.font.pretendard_regular, FontWeight.Normal),
    Font(R.font.pretendard_semi_bold, FontWeight.SemiBold),
    Font(R.font.pretendard_thin, FontWeight.Thin)
)

object PoptatoTypo {
    val xxxLSemiBold: TextStyle = TextStyle(
        fontSize = 24.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 36.sp
    )

    val xxxLMedium: TextStyle = TextStyle(
        fontSize = 24.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Medium,
        lineHeight = 36.sp
    )

    val xxxLRegular: TextStyle = TextStyle(
        fontSize = 24.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        lineHeight = 36.sp
    )

    val xxLSemiBold: TextStyle = TextStyle(
        fontSize = 22.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 33.sp
    )

    val xxLMedium: TextStyle = TextStyle(
        fontSize = 22.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Medium,
        lineHeight = 33.sp
    )

    val xxLRegular: TextStyle = TextStyle(
        fontSize = 22.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        lineHeight = 33.sp
    )

    val xLSemiBold: TextStyle = TextStyle(
        fontSize = 20.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 30.sp
    )

    val xLMedium: TextStyle = TextStyle(
        fontSize = 20.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Medium,
        lineHeight = 30.sp
    )

    val xLRegular: TextStyle = TextStyle(
        fontSize = 20.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        lineHeight = 30.sp
    )

    val lgSemiBold: TextStyle = TextStyle(
        fontSize = 18.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 27.sp
    )

    val lgMedium: TextStyle = TextStyle(
        fontSize = 18.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Medium,
        lineHeight = 27.sp
    )

    val lgRegular: TextStyle = TextStyle(
        fontSize = 18.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        lineHeight = 27.sp
    )

    val mdSemiBold: TextStyle = TextStyle(
        fontSize = 16.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 24.sp
    )

    val mdMedium: TextStyle = TextStyle(
        fontSize = 16.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Medium,
        lineHeight = 24.sp
    )

    val mdRegular: TextStyle = TextStyle(
        fontSize = 16.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp,
        color = Gray00
    )

    val smSemiBold: TextStyle = TextStyle(
        fontSize = 14.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 21.sp
    )

    val smMedium: TextStyle = TextStyle(
        fontSize = 14.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Medium,
        lineHeight = 21.sp
    )

    val smRegular: TextStyle = TextStyle(
        fontSize = 14.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        lineHeight = 21.sp
    )

    val xsSemiBold: TextStyle = TextStyle(
        fontSize = 12.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 18.sp
    )

    val xsMedium: TextStyle = TextStyle(
        fontSize = 12.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Medium,
        lineHeight = 18.sp
    )

    val xsRegular: TextStyle = TextStyle(
        fontSize = 12.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        lineHeight = 18.sp
    )

    val calSemiBold: TextStyle = TextStyle(
        fontSize = 10.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 15.sp
    )

    val calMedium: TextStyle = TextStyle(
        fontSize = 10.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Medium,
        lineHeight = 15.sp
    )

    val calRegular: TextStyle = TextStyle(
        fontSize = 10.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        lineHeight = 15.sp
    )
}