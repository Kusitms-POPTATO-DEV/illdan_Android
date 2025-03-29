package com.poptato.design_system

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Gray Scale
val Gray00 = Color(0xFFFFFFFF)
val Gray05 = Color(0xFFFAFAFA)
val Gray10 = Color(0xFFF4F4F5)
val Gray20 = Color(0xFFDDDDDF)
val Gray30 = Color(0xFFC5C5C9)
val Gray40 = Color(0xFFADADB3)
val Gray50 = Color(0xFF95959D)
val Gray60 = Color(0xFF7D7D87)
val Gray70 = Color(0xFF67676F)
val Gray80 = Color(0xFF515157)
val Gray90 = Color(0xFF3B3B40)
val Gray95 = Color(0xFF2C2C30)
val Gray100 = Color(0xFF1E1E20)

// Primary
val Primary10 = Color(0xFFEDFCFA)
val Primary20 = Color(0xFFD7F9F3)
val Primary30 = Color(0xFFABF2E6)
val Primary40 = Color(0xFF7FEBD9)
val Primary50 = Color(0xFF52E5CC)
val Primary60 = Color(0xFF27DEBF)
val Primary70 = Color(0xFF1CB59C)
val Primary80 = Color(0xFF158976)
val Primary90 = Color(0xFF0E5D50)
val Primary100 = Color(0xFF07312A)

// Danger
val Danger10 = Color(0xFFFEECEF)
val Danger20 = Color(0xFFFDDEE3)
val Danger30 = Color(0xFFF9AEBB)
val Danger40 = Color(0xFFF67F92)
val Danger50 = Color(0xFFF24D69)
val Danger60 = Color(0xFFE22C4A)
val Danger70 = Color(0xFFC11A36)

// Warning
val warning10 = Color(0xFFFFFBEB)
val warning20 = Color(0xFFFFF4C7)
val warning30 = Color(0xFFFFEA94)
val warning40 = Color(0xFFFFDF61)
val warning50 = Color(0xFFFFD52F)
val warning60 = Color(0xFFFAC800)
val warning70 = Color(0xFFC79F00)

// KaKao
val KaKaoMain = Color(0xFFFEE500)

// ETC
val Bookmark = Color(0xFF294746)
val KaKaoLogin = Brush.linearGradient(
    colors = listOf(
        Color(0xFF1E1E20).copy(alpha = 0.5f),
        Color(0xFF1E1E20)
    )
)
val Splash = Brush.linearGradient(
    colors = listOf(
        Color(0xFF1E1E20).copy(alpha = 0.1f),
        Color(0xFF1CB59C)
    )
)

// Background
val BgSnackBar = Color(0xFF121214)
val BgProgressBar = Color(0xFF000000)