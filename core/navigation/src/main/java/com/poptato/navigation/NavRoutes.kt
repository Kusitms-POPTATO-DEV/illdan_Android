package com.poptato.navigation

sealed class NavRoutes(val route: String) {
    // Splash Graph
    data object SplashGraph: NavRoutes("splash_graph")
    data object SplashScreen: NavRoutes("splash")

    // KaKaoLogin Graph
    data object KaKaoLoginGraph: NavRoutes("kakao_login_graph")
    data object KaKaoLoginScreen: NavRoutes("kakao_login")

    // Backlog Graph
    data object BacklogGraph: NavRoutes("backlog_graph")
    data object BacklogScreen: NavRoutes("backlog")

    // YesterdayList
    data object YesterdayListGraph: NavRoutes("yesterday_list_graph")
    data object YesterdayListScreen: NavRoutes("yesterday_list")
    data object YesterdayAllCheckScreen: NavRoutes("yesterday_all_check")

    // MyPage
    data object MyPageGraph: NavRoutes("my_page_graph")
    data object MyPageScreen: NavRoutes("my_page")
    data object SettingScreen: NavRoutes("setting")
    data object ServiceDeleteScreen: NavRoutes("service_delete")
    data object UserDataScreen: NavRoutes("user_data")
    data object NoticeViewScreen: NavRoutes("viewer")

    // Today Graph
    data object TodayGraph: NavRoutes("today_graph")
    data object TodayScreen: NavRoutes("today")

    // HistoryGraph
    data object HistoryGraph: NavRoutes("history_graph")
    data object HistoryScreen: NavRoutes("history")
}
