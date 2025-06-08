package com.poptato.design_system

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Gray Scale
val Gray00 = Color(0xFFFFFFFF)
val Gray05 = Color(0xFFF4F4F6)
val Gray10 = Color(0xFFE6E6EA)
val Gray20 = Color(0xFFDBDBE1)
val Gray30 = Color(0xFFC2C2CC)
val Gray40 = Color(0xFFA9A9B7)
val Gray50 = Color(0xFF9090A2)
val Gray60 = Color(0xFF77778D)
val Gray70 = Color(0xFF616175)
val Gray80 = Color(0xFF4D4D5C)
val Gray90 = Color(0xFF383843)
val Gray95 = Color(0xFF2E2E38)
val Gray100 = Color(0xFF23232A)

// Primary
val Primary00 = Color(0xFFE0FAFA)
val Primary10 = Color(0xFFB4F3F3)
val Primary20 = Color(0xFF88ECEC)
val Primary30 = Color(0xFF5DE5E5)
val Primary40 = Color(0xFF30DEDE)
val Primary50 = Color(0xFF1EBDBD)
val Primary60 = Color(0xFF179191)
val Primary70 = Color(0xFF106565)
val Primary80 = Color(0xFF093939)
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
val Warning10 = Color(0xFFFDDEE9)
val Warning20 = Color(0xFFF9AECA)
val Warning30 = Color(0xFFF67FAA)
val Warning40 = Color(0xFFF24D8A)
val Warning50 = Color(0xFFEF206C)
val Warning60 = Color(0xFFC61556)
val Warning70 = Color(0xFF941443)

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
val BottomSheet = Color(0xFF000000).copy(alpha = 0.5f)