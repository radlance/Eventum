package com.radlance.eventum.navigation.base

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.radlance.eventum.presentation.authorization.common.AuthViewModel
import com.radlance.eventum.presentation.authorization.signin.ForgotPasswordScreen
import com.radlance.eventum.presentation.authorization.signin.SignInScreen
import com.radlance.eventum.presentation.authorization.signin.VerificationScreen
import com.radlance.eventum.presentation.authorization.signup.SignUpScreen
import com.radlance.eventum.presentation.cart.CartScreen
import com.radlance.eventum.presentation.common.EventViewModel
import com.radlance.eventum.presentation.history.HistoryScreen
import com.radlance.eventum.presentation.home.catalog.CatalogScreen
import com.radlance.eventum.presentation.home.details.EventDetailsScreen
import com.radlance.eventum.presentation.home.popular.PopularScreen
import com.radlance.eventum.presentation.home.search.SearchScreen
import com.radlance.eventum.presentation.notification.NotificationScreen
import com.radlance.eventum.presentation.onboarding.OnboardingFirst
import com.radlance.eventum.presentation.onboarding.OnboardingSecond
import com.radlance.eventum.presentation.onboarding.OnboardingThird
import com.radlance.eventum.presentation.onboarding.SplashScreen
import com.radlance.eventum.presentation.map.MapScreen
import com.radlance.eventum.presentation.order.OrderScreen
import com.radlance.eventum.presentation.profile.FullScreenBarcode
import com.radlance.eventum.presentation.profile.ProfileScreen
import com.radlance.eventum.presentation.profile.edit.EditProfileScreen
import com.radlance.eventum.ui.theme.backgroundGradient

@Composable
fun NavGraph(
    navController: NavHostController,
    navigationViewModel: NavigationViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel(),
    sharedViewModel: EventViewModel = viewModel()
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val navigationState by navigationViewModel.navigationState.collectAsState()

    val context = LocalContext.current

    val modifier = if (currentRoute in listOf(
            OnboardingFirst,
            OnboardingSecond,
            OnboardingThird
        ).map { it::class.qualifiedName } || currentRoute == null
    ) {
        Modifier.background(brush = backgroundGradient)
    } else {
        Modifier.background(color = MaterialTheme.colorScheme.background)
    }

    val navigateToSignIn: () -> Unit = {
        navController.navigate(SignIn) {
            popUpTo<Home> { inclusive = true }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Splash,
        modifier = modifier.fillMaxSize()
    ) {
        composable<Splash> {
            SplashScreen(
                onDelayFinished = {
                    navigationState.navigate(navController)
                }
            )
        }

        composable<OnboardingFirst> {
            OnboardingFirst(
                onStartClicked = {
                    navController.navigate(OnboardingSecond) {
                        popUpTo<OnboardingFirst> { inclusive = true }
                    }
                },
                onBackPressed = {
                    (context as Activity).finish()
                }
            )
        }

        composable<OnboardingSecond> {
            OnboardingSecond(
                onNextClicked = {
                    navController.navigate(OnboardingThird)
                },
                onBackPressed = {
                    navController.navigate(OnboardingFirst) {
                        popUpTo<OnboardingSecond> { inclusive = true }
                    }
                }
            )
        }

        composable<OnboardingThird> {
            OnboardingThird(
                onNextClicked = {
                    navController.navigate(SignIn) {
                        popUpTo<OnboardingThird> { inclusive = false }
                    }
                    navigationViewModel.setOnBoardingViewed(viewed = true)
                },
                onBackPressed = {
                    navController.navigate(OnboardingSecond) {
                        popUpTo<OnboardingThird> { inclusive = true }
                    }
                }
            )
        }

        composable<SignIn> {
            SignInScreen(
                onIconBackPressed = {
                    navController.navigate(OnboardingThird) {
                        popUpTo<SignIn> { inclusive = true }
                    }
                },

                onSignUpTextClicked = {
                    navController.navigate(SignUp) {
                        popUpTo<SignIn> { inclusive = true }
                    }
                },
                onRecoverPasswordTextClicked = {
                    navController.navigate(ForgotPassword) {
                        popUpTo<SignIn> { inclusive = true }
                    }
                },

                onSuccessSignIn = {
                    navController.navigate(Home) {
                        popUpTo<SignIn> { inclusive = true }
                        navigationViewModel.setUserLoggedIn()
                    }
                },
                viewModel = authViewModel.copy()
            )
        }

        composable<SignUp> {
            SignUpScreen(
                onBackPressed = {
                    navController.navigate(SignIn) {
                        popUpTo<SignUp> { inclusive = true }
                    }
                },
                onSignInTextClicked = {
                    navController.navigate(SignIn) {
                        popUpTo<SignUp> { inclusive = true }
                    }
                },
                onSuccessSignUp = {
                    navController.navigate(Home) {
                        popUpTo<SignUp> { inclusive = true }
                        navigationViewModel.
                        setUserLoggedIn()
                    }
                },
                viewModel = authViewModel.copy()
            )
        }

        composable<ForgotPassword> {
            ForgotPasswordScreen(
                onBackPressed = {
                    navController.navigate(SignIn) {
                        popUpTo<ForgotPassword> { inclusive = true }
                    }
                },
                onSuccessSending = {
                    navController.navigate(Verification(email = it)) {
                        popUpTo<ForgotPassword> { inclusive = true }
                    }
                }
            )
        }

        composable<Verification> {
            val args = it.toRoute<Verification>()
            VerificationScreen(
                onBackPressed = {
                    navController.navigate(ForgotPassword) {
                        popUpTo<Verification> { inclusive = true }
                    }
                },
                email = args.email,
                onSuccessPasswordUpdating = {
                    navController.navigate(SignIn) {
                        popUpTo<Verification> { inclusive = true }
                    }
                }
            )
        }

        composable<Home> {
            MainScreen(
                onSignOut = navigateToSignIn,
                navigateToCart = { navController.navigate(Cart) },
                navigateToProfile = { navController.navigate(Profile) },
                navigateToNotification = { navController.navigate(Notification) },
                navigateToOrderHistory = { navController.navigate(History) },
                navigateToDetails = { navController.navigate(Details(it)) },
                navigateToSearch = { navController.navigate(Search) },
                navigateToCatalog = { navController.navigate(Catalog(it)) },
                navigateToPopular = { navController.navigate(Popular) },
                sharedEventViewModel = sharedViewModel
            )
        }

        composable<Popular> {
            PopularScreen(
                onBackPressed = { navController.navigate(Home) },
                navigateToFavorite = { navController.navigate(Home) },
                navigateToDetails = { navController.navigate(Details(it)) },
                eventViewModel = sharedViewModel
            )
        }

        composable<Catalog> { backStackEntry ->
            val args = backStackEntry.toRoute<Catalog>()
            CatalogScreen(
                selectedCategoryId = args.categoryId,
                onBackPressed = { navController.navigate(Home) },
                navigateToDetails = { navController.navigate(Details(it)) },
                eventViewModel = sharedViewModel
            )
        }

        composable<Search> {
            SearchScreen(
                onBackPressed = navController::navigateUp,
                navigateToDetails = { navController.navigate(Details(it)) },
                eventViewModel = sharedViewModel
            )
        }

        composable<Details> {
            val args = it.toRoute<Details>()
            EventDetailsScreen(
                selectedEventId = args.eventId,
                onBackPressed = navController::navigateUp,
                onNavigateToCart = { navController.navigate(Cart) },
                viewModel = sharedViewModel
            )
        }

        navigation<Payment>(startDestination = Cart) {
            composable<Cart> {
                CartScreen(
                    onPlaceOrderClick = { navController.navigate(Order) },
                    eventViewModel = sharedViewModel,
                    onSignInClick = navigateToSignIn,
                    onBackPressed = { navController.navigate(Home) }
                )
            }

            composable<Order> {
                OrderScreen(
                    onBackPressed = {
                        navController.navigate(Cart) {
                            popUpTo<Order> { inclusive = true }
                        }
                    },
                    onMapClick = { lat, long -> navController.navigate(Map(lat, long)) },
                    navigateToCatalog = { navController.navigate(Home) },
                    eventViewModel = sharedViewModel
                )
            }

            composable<Map> {
                val args = it.toRoute<Map>()
                MapScreen(latitude = args.latitude, longitude = args.longitude)
            }
        }

        composable<Notification> {
            NotificationScreen(onBackPressed = { navController.navigate(Home) })
        }

        navigation<Profile>(startDestination = UserData) {
            composable<UserData> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    ProfileScreen(
                        onBarcodeClick = { navController.navigate(Barcode) },
                        onEditProfileClick = { navController.navigate(EditProfile) },
                        onSignInClick = navigateToSignIn,
                        onBackPressed = { navController.navigate(Home) }
                    )
                }
            }

            composable<Barcode> {
                FullScreenBarcode(
                    onBackPressed = {
                        navController.navigate(UserData) {
                            popUpTo<Barcode> { inclusive = true }
                        }
                    }
                )
            }

            composable<EditProfile> {
                EditProfileScreen(
                    onBackPressed = { navController.navigate(UserData) },
                    onNavigateToProfile = { navController.navigate(UserData) }
                )
            }
        }

        composable<History> {
            HistoryScreen(
                viewModel = sharedViewModel,
                onBackPressed = { navController.navigate(Home) },
                onSignInClick = navigateToSignIn
            )
        }
    }
}