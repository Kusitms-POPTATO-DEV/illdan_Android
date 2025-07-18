package com.poptato.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.poptato.backlog.BacklogScreen
import com.poptato.category.CategoryScreen
import com.poptato.domain.model.response.category.CategoryIconItemModel
import com.poptato.domain.model.response.category.CategoryIconTotalListModel
import com.poptato.domain.model.response.category.CategoryItemModel
import com.poptato.domain.model.response.category.CategoryScreenContentModel
import com.poptato.domain.model.response.dialog.DialogContentModel
import com.poptato.domain.model.response.today.TodoItemModel
import com.poptato.login.KaKaoLoginScreen
import com.poptato.mypage.MyPageScreen
import com.poptato.mypage.MyPageViewModel
import com.poptato.mypage.comment.UserCommentScreen
import com.poptato.mypage.policy.PolicyViewerScreen
import com.poptato.mypage.viewer.FAQViewerScreen
import com.poptato.mypage.viewer.NoticeViewerScreen
import com.poptato.setting.servicedelete.finish.ServiceDeleteFinishScreen
import com.poptato.onboarding.OnboardingScreen
import com.poptato.setting.servicedelete.ServiceDeleteScreen
import com.poptato.setting.userdata.UserDataScreen
import com.poptato.splash.SplashScreen
import com.poptato.today.TodayScreen
import com.poptato.ui.event.TodoExternalEvent
import com.poptato.yesterdaylist.YesterdayListScreen
import com.poptato.yesterdaylist.allcheck.AllCheckScreen
import com.potato.history.HistoryScreen
import kotlinx.coroutines.flow.SharedFlow

fun NavGraphBuilder.splashNavGraph(navController: NavHostController) {
    navigation(
        startDestination = NavRoutes.SplashScreen.route,
        route = NavRoutes.SplashGraph.route
    ) {
        composable(NavRoutes.SplashScreen.route) {
            SplashScreen(
                goToKaKaoLogin = {
                    navController.navigate(NavRoutes.KaKaoLoginGraph.route) {
                        popUpTo(NavRoutes.SplashScreen.route) {
                            inclusive = true
                        }
                    }
                },
                goToToday = {
                    navController.navigate(NavRoutes.TodayScreen.route) {
                        popUpTo(NavRoutes.SplashScreen.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}

fun NavGraphBuilder.loginNavGraph(
    navController: NavHostController,
    showSnackBar: (String) -> Unit
) {
    navigation(
        startDestination = NavRoutes.KaKaoLoginScreen.route,
        route = NavRoutes.KaKaoLoginGraph.route
    ) {
        composable(NavRoutes.KaKaoLoginScreen.route) {
            KaKaoLoginScreen(
                goToToday = {
                    navController.navigate(NavRoutes.TodayScreen.route) {
                        popUpTo(NavRoutes.KaKaoLoginScreen.route) {
                            inclusive = true
                        }
                    }
                },
                showSnackBar = showSnackBar,
                goToBacklog = {
                    navController.navigate(NavRoutes.BacklogGraph.route) {
                        popUpTo(NavRoutes.KaKaoLoginScreen.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(NavRoutes.OnboardingScreen.route) {
            OnboardingScreen(goToBacklog = { navController.navigate(NavRoutes.BacklogGraph.route) })
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun NavGraphBuilder.backlogNavGraph(
    navController: NavHostController,
    showBottomSheet: (TodoItemModel, List<CategoryItemModel>) -> Unit,
    todoExternalEvent: SharedFlow<TodoExternalEvent>,
    showSnackBar: (String) -> Unit,
    showDialog: (DialogContentModel) -> Unit,
    categoryScreenContent: (CategoryScreenContentModel) -> Unit
) {
    navigation(
        startDestination = NavRoutes.BacklogScreen.createRoute(0),
        route = NavRoutes.BacklogGraph.route
    ) {
        composable(
            route = NavRoutes.BacklogScreen.route,
            arguments = listOf(
                navArgument("index") { type = NavType.IntType }
            )
        ) {
            val index = it.arguments?.getInt("index") ?: 0

            BacklogScreen(
                goToCategorySelect = {
                    categoryScreenContent(it)
                    navController.navigate(NavRoutes.CategoryScreen.route) },
                showBottomSheet = showBottomSheet,
                todoExternalEvent = todoExternalEvent,
                showSnackBar = showSnackBar,
                showDialog = showDialog,
                initialCategoryIndex = index
            )
        }
    }
}

fun NavGraphBuilder.categoryNavGraph(
    navController: NavHostController,
    showCategoryIconBottomSheet: (CategoryIconTotalListModel) -> Unit,
    selectedIconInBottomSheet: SharedFlow<CategoryIconItemModel>,
    showDialog: (DialogContentModel) -> Unit,
    categoryScreenFromBacklog: SharedFlow<CategoryScreenContentModel>
) {
    navigation(
        startDestination = NavRoutes.CategoryScreen.route,
        route = NavRoutes.CategoryGraph.route
    ) {
        composable(NavRoutes.CategoryScreen.route) {
            CategoryScreen(
                popScreen = { index -> navController.navigate(NavRoutes.BacklogScreen.createRoute(index)) },
                goToBacklog = { index ->
                    navController.navigate(NavRoutes.BacklogScreen.createRoute(index))
                },
                showIconBottomSheet = showCategoryIconBottomSheet,
                selectedIconInBottomSheet = selectedIconInBottomSheet,
                showDialog = showDialog,
                screenContent = categoryScreenFromBacklog
            )
        }
    }
}

fun NavGraphBuilder.yesterdayListNavGraph(navController: NavHostController) {
    navigation(
        startDestination = NavRoutes.YesterdayListScreen.route,
        route = NavRoutes.YesterdayListGraph.route
    ) {
        composable(NavRoutes.YesterdayListScreen.route) {
            YesterdayListScreen(
                goBackToBacklog = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.YesterdayAllCheckScreen.route) {
            AllCheckScreen(
                goBackToBacklog = {
                    navController.navigate(NavRoutes.BacklogScreen.createRoute(0)) {
                        popUpTo(NavRoutes.BacklogScreen.route) { inclusive = true }
                    }
                }
            )
        }
    }
}

fun NavGraphBuilder.myPageNavGraph(
    navController: NavHostController,
    showDialog: (DialogContentModel) -> Unit,
    showSnackBar: (String) -> Unit,
    deleteUserName: (String) -> Unit,
    deleteUserNameFromUserData: SharedFlow<String>
    ) {
    navigation(
        startDestination = NavRoutes.MyPageScreen.route,
        route = NavRoutes.MyPageGraph.route,
    ) {
        composable(NavRoutes.MyPageScreen.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(NavRoutes.MyPageGraph.route)
            }
            val viewModel: MyPageViewModel = hiltViewModel(parentEntry)

            MyPageScreen(
                viewModel = viewModel,
                goToUserDataPage = {
                    navController.navigate(NavRoutes.UserDataScreen.route) {
                        launchSingleTop = true
                    }
                },
                goToPolicyViewerPage = {
                    navController.navigate(NavRoutes.PolicyViewScreen.route) {
                        launchSingleTop = true
                    }
                },
                goToUserCommentPage = {
                    navController.navigate(NavRoutes.UserCommentScreen.route) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(NavRoutes.NoticeViewScreen.route) {
            NoticeViewerScreen(
                goBackToMyPage = {
                    navController.navigate(NavRoutes.MyPageScreen.route) {
                        popUpTo(NavRoutes.MyPageScreen.route) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.FAQViewScreen.route) {
            FAQViewerScreen(
                goBackToMyPage = {
                    navController.navigate(NavRoutes.MyPageScreen.route) {
                        popUpTo(NavRoutes.MyPageScreen.route) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.PolicyViewScreen.route) {
            PolicyViewerScreen(
                goBackToMyPage = {
                    navController.navigate(NavRoutes.MyPageScreen.route) {
                        popUpTo(NavRoutes.MyPageScreen.route) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.ServiceDeleteScreen.route) {
            ServiceDeleteScreen(
                goBackToSetting = { navController.popBackStack() },
                showDialog = showDialog,
                deleteUserName = deleteUserNameFromUserData,
                showDeleteFinishScreen = { navController.navigate(NavRoutes.ServiceDeleteFinishScreen.route) }
            )
        }

        composable(NavRoutes.UserDataScreen.route) {
            UserDataScreen(
                goBackToMyPage = { navController.popBackStack() },
                goBackToLogIn = { navController.navigate(NavRoutes.KaKaoLoginScreen.route) },
                goToServiceDelete = {
                    deleteUserName(it)
                    navController.navigate(NavRoutes.ServiceDeleteScreen.route)
                                    },
                showDialog = showDialog,
            )
        }

        composable(NavRoutes.ServiceDeleteFinishScreen.route) {
            ServiceDeleteFinishScreen(
                deleteUserName = deleteUserNameFromUserData,
                goBackToLogIn = { navController.navigate(NavRoutes.KaKaoLoginScreen.route) },
            )
        }

        composable(NavRoutes.UserCommentScreen.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(NavRoutes.MyPageGraph.route)
            }
            val viewModel: MyPageViewModel = hiltViewModel(parentEntry)

            UserCommentScreen(
                viewModel = viewModel,
                showSnackBar = showSnackBar,
                popScreen = { navController.popBackStack() }
            )
        }
    }
}

fun NavGraphBuilder.todayNavGraph(
    navController: NavHostController,
    showSnackBar: (String) -> Unit,
    showBottomSheet: (TodoItemModel, List<CategoryItemModel>) -> Unit,
    todoExternalEvent: SharedFlow<TodoExternalEvent>
) {
    navigation(startDestination = NavRoutes.TodayScreen.route, route = NavRoutes.TodayGraph.route) {
        composable(NavRoutes.TodayScreen.route) {
            TodayScreen(
                goToBacklog = { navController.navigate(NavRoutes.BacklogScreen.createRoute(0)) },
                showSnackBar = showSnackBar,
                showBottomSheet = showBottomSheet,
                todoExternalEvent = todoExternalEvent
            )
        }
    }
}

fun NavGraphBuilder.historyNavGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = NavRoutes.HistoryScreen.route,
        route = NavRoutes.HistoryGraph.route
    ) {
        composable(NavRoutes.HistoryScreen.route) {
            HistoryScreen()
        }
    }
}