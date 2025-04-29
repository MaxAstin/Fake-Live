package com.bunbeauty.tiptoplive.features.main.view

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.serialization.generateHashCode
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.navigation.BottomNavigationRoute
import com.bunbeauty.tiptoplive.common.navigation.NavigationRoute
import com.bunbeauty.tiptoplive.common.ui.components.GradientIcon
import com.bunbeauty.tiptoplive.common.ui.components.PulsingBadge
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer

private val bottomRoutes: List<BottomNavigationRoute> = listOf(
    NavigationRoute.Awards,
    NavigationRoute.Preparation,
    NavigationRoute.More
)

data class BottomNavigationItemList(
    val items: List<BottomNavigationItem>
) {
    val current: BottomNavigationItem = items.find { route ->
        route.isSelected
    } ?: error("Current route not found")
}

data class BottomNavigationItem(
    val route: BottomNavigationRoute,
    val isSelected: Boolean,
    val hasBadge: Boolean
)

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    hasNewAwards: Boolean
) {
    val backStack by navController.currentBackStackEntryAsState()
    val currentDestination = backStack?.destination

    if (!currentDestination.isBottomNavigation()) {
        return
    }

    val itemList = remember(currentDestination) {
        BottomNavigationItemList(
            items = bottomRoutes.map { route ->
                BottomNavigationItem(
                    route = route,
                    isSelected = currentDestination.same(route),
                    hasBadge = route == NavigationRoute.Awards && hasNewAwards
                )
            }
        )
    }
    BottomNavigationBarContent(
        navController = navController,
        itemList = itemList
    )
}

@Composable
private fun BottomNavigationBarContent(
    navController: NavHostController,
    itemList: BottomNavigationItemList
) {
    Column {
        HorizontalDivider(
            thickness = 1.dp,
            color = FakeLiveTheme.colors.inactive
        )
        NavigationBar(
            containerColor = FakeLiveTheme.colors.background
        ) {
            itemList.items.forEach { item ->
                val route = item.route
                val brush = if (item.isSelected) {
                    Brush.horizontalGradient(
                        colors = listOf(
                            FakeLiveTheme.colors.instagram.logo1,
                            FakeLiveTheme.colors.instagram.logo2,
                            FakeLiveTheme.colors.instagram.logo3
                        )
                    )
                } else {
                    SolidColor(value = FakeLiveTheme.colors.onBackgroundVariant)
                }

                NavigationBarItem(
                    selected = item.isSelected,
                    icon = {
                        Box {
                            GradientIcon(
                                modifier = Modifier.size(24.dp),
                                imageVector = ImageVector.vectorResource(route.icon()),
                                brush = brush,
                                contentDescription = route.javaClass.simpleName
                            )
                            if (item.hasBadge) {
                                PulsingBadge(modifier = Modifier.align(Alignment.TopStart))
                            }
                        }
                    },
                    label = {
                        Text(
                            text = stringResource(route.text()),
                            style = FakeLiveTheme.typography.bodySmall.copy(
                                brush = brush
                            )
                        )
                    },
                    onClick = {
                        if (!item.isSelected) {
                            navController.navigate(route) {
                                popUpTo(itemList.current.route) {
                                    inclusive = true
                                }
                            }
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = FakeLiveTheme.colors.interactive,
                        selectedTextColor = FakeLiveTheme.colors.interactive,
                        indicatorColor = Color.Transparent,
                        unselectedIconColor = FakeLiveTheme.colors.onBackgroundVariant,
                        unselectedTextColor = FakeLiveTheme.colors.onBackgroundVariant
                    )
                )
            }
        }
    }
}

private fun BottomNavigationRoute.icon(): Int {
    return when (this) {
        NavigationRoute.Preparation -> R.drawable.ic_stream
        NavigationRoute.Awards -> R.drawable.ic_cup
        NavigationRoute.More -> R.drawable.ic_more
    }
}

private fun BottomNavigationRoute.text(): Int {
    return when (this) {
        NavigationRoute.Preparation -> R.string.navigation_live
        NavigationRoute.Awards -> R.string.navigation_awards
        NavigationRoute.More -> R.string.navigation_more
    }
}

@SuppressLint("RestrictedApi")
@OptIn(InternalSerializationApi::class)
private fun NavDestination?.same(route: NavigationRoute): Boolean {
    if (this == null) return false
    Log.d("testTag", "route ${route::class.serializer().generateHashCode()}")
    Log.d("testTag", "destination $navigatorName")
    return hasRoute(route::class)
}

private fun NavDestination?.isBottomNavigation(): Boolean {
    return bottomRoutes.any { route ->
        same(route)
    }
}

@Preview
@Composable
private fun BottomNavigationBarPreview() {
    FakeLiveTheme {
        BottomNavigationBarContent(
            navController = rememberNavController(),
            itemList = BottomNavigationItemList(
                items = listOf(
                    BottomNavigationItem(
                        route = NavigationRoute.Awards,
                        isSelected = false,
                        hasBadge = true
                    ),
                    BottomNavigationItem(
                        route = NavigationRoute.Preparation,
                        isSelected = true,
                        hasBadge = false
                    ),
                    BottomNavigationItem(
                        route = NavigationRoute.More,
                        isSelected = false,
                        hasBadge = false
                    )
                )
            )
        )
    }
}