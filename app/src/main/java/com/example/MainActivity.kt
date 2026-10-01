package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.*
import com.example.ui.screens.avatar.AvatarScreen
import com.example.ui.screens.details.ExperienceDetailsScreen
import com.example.ui.screens.discover.DiscoverScreen
import com.example.ui.screens.game.CyberObbyGameScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.leaderboard.LeaderboardScreen
import com.example.ui.screens.profile.ProfileScreen
import com.example.ui.screens.social.SocialScreen
import com.example.ui.screens.studio.CreatorStudioScreen
import com.example.ui.theme.*
import com.example.ui.viewmodel.RobloxiachintuViewModel
import com.example.ui.viewmodel.ScreenDestination

class MainActivity : ComponentActivity() {

    private val viewModel: RobloxiachintuViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            RobloxiachintuTheme {
                MainContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainContent(viewModel: RobloxiachintuViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val dailyRewards by viewModel.dailyRewards.collectAsStateWithLifecycle()
    val avatarItems by viewModel.avatarItems.collectAsStateWithLifecycle()
    val leaderboard by viewModel.currentLeaderboard.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedLeaderboardCategory.collectAsStateWithLifecycle()
    val selectedTimeframe by viewModel.selectedLeaderboardTimeframe.collectAsStateWithLifecycle()
    val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()

    val showDailyRewards by viewModel.showDailyRewardsDialog.collectAsStateWithLifecycle()
    val showPromo by viewModel.showPromoDialog.collectAsStateWithLifecycle()
    val showRobux by viewModel.showRobuxDialog.collectAsStateWithLifecycle()
    val bannerNotification by viewModel.bannerNotification.collectAsStateWithLifecycle()

    // Handle back button for sub-screens
    BackHandler(enabled = currentScreen != ScreenDestination.HOME) {
        when (currentScreen) {
            ScreenDestination.PLAYABLE_GAME,
            ScreenDestination.EXPERIENCE_DETAILS,
            ScreenDestination.CREATOR_STUDIO,
            ScreenDestination.PROFILE -> {
                viewModel.navigateTo(ScreenDestination.HOME)
            }
            else -> {
                viewModel.navigateTo(ScreenDestination.HOME)
            }
        }
    }

    val isFullScreenMode = currentScreen == ScreenDestination.PLAYABLE_GAME

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
        topBar = {
            if (!isFullScreenMode && currentScreen != ScreenDestination.EXPERIENCE_DETAILS && currentScreen != ScreenDestination.CREATOR_STUDIO && currentScreen != ScreenDestination.PROFILE) {
                TopNavBar(
                    userProfile = userProfile,
                    onNavigate = { viewModel.navigateTo(it) },
                    onOpenDailyReward = { viewModel.setDailyRewardsDialog(true) },
                    onOpenRobux = { viewModel.setRobuxDialog(true) },
                    onOpenPromo = { viewModel.setPromoDialog(true) }
                )
            }
        },
        bottomBar = {
            if (!isFullScreenMode && currentScreen != ScreenDestination.EXPERIENCE_DETAILS && currentScreen != ScreenDestination.CREATOR_STUDIO && currentScreen != ScreenDestination.PROFILE) {
                BottomNavBar(
                    currentScreen = currentScreen,
                    onNavigate = { viewModel.navigateTo(it) }
                )
            }
        },
        containerColor = DarkBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(if (isFullScreenMode) PaddingValues(0.dp) else innerPadding)
        ) {
            when (currentScreen) {
                ScreenDestination.HOME -> {
                    HomeScreen(
                        userProfile = userProfile,
                        onNavigate = { viewModel.navigateTo(it) },
                        onOpenExperience = { viewModel.openExperienceDetails(it) },
                        onLaunchGame = { viewModel.launchPlayableGame() },
                        onOpenDailyReward = { viewModel.setDailyRewardsDialog(true) },
                        onOpenPromo = { viewModel.setPromoDialog(true) }
                    )
                }
                ScreenDestination.DISCOVER -> {
                    DiscoverScreen(
                        onOpenExperience = { viewModel.openExperienceDetails(it) },
                        onLaunchGame = { viewModel.launchPlayableGame() }
                    )
                }
                ScreenDestination.AVATAR -> {
                    AvatarScreen(
                        userProfile = userProfile,
                        avatarItems = avatarItems,
                        onPurchaseItem = { viewModel.purchaseAvatarItem(it) },
                        onEquipItem = { viewModel.equipAvatarItem(it) }
                    )
                }
                ScreenDestination.LEADERBOARD -> {
                    LeaderboardScreen(
                        userProfile = userProfile,
                        currentLeaderboard = leaderboard,
                        selectedCategory = selectedCategory,
                        selectedTimeframe = selectedTimeframe,
                        onCategoryChange = { viewModel.setLeaderboardCategory(it) },
                        onTimeframeChange = { viewModel.setLeaderboardTimeframe(it) },
                        onClaimRankReward = { viewModel.claimRankReward(it) }
                    )
                }
                ScreenDestination.CHAT -> {
                    SocialScreen(
                        chatMessages = chatMessages,
                        onSendMessage = { viewModel.sendChatMessage(it) },
                        onLaunchGame = { viewModel.launchPlayableGame() }
                    )
                }
                ScreenDestination.EXPERIENCE_DETAILS -> {
                    ExperienceDetailsScreen(
                        experienceId = viewModel.selectedExperience.value,
                        userProfile = userProfile,
                        onBack = { viewModel.navigateTo(ScreenDestination.HOME) },
                        onLaunchGame = { viewModel.launchPlayableGame() },
                        onBuyPass = { type, cost, name -> viewModel.buyGamePass(type, cost, name) },
                        onRedeemCode = { viewModel.redeemPromo(it) }
                    )
                }
                ScreenDestination.PLAYABLE_GAME -> {
                    CyberObbyGameScreen(
                        userProfile = userProfile,
                        onExitGame = { viewModel.navigateTo(ScreenDestination.HOME) },
                        onStageCompleted = { stage, timeMs, coins ->
                            viewModel.onObbyCompleted(stage, timeMs, coins)
                        }
                    )
                }
                ScreenDestination.CREATOR_STUDIO -> {
                    CreatorStudioScreen(
                        onBack = { viewModel.navigateTo(ScreenDestination.HOME) },
                        onTestPlay = { viewModel.launchPlayableGame() }
                    )
                }
                ScreenDestination.PROFILE -> {
                    ProfileScreen(
                        userProfile = userProfile,
                        onBack = { viewModel.navigateTo(ScreenDestination.HOME) },
                        onOpenRobux = { viewModel.setRobuxDialog(true) },
                        onOpenPromo = { viewModel.setPromoDialog(true) }
                    )
                }
            }

            // Notification Banner / Toast
            AnimatedVisibility(
                visible = bannerNotification != null,
                enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            ) {
                Surface(
                    color = DarkCard,
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, NeonGreen),
                    shadowElevation = 10.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = bannerNotification ?: "",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = { viewModel.clearNotification() },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Text(text = "✕", color = TextSecondary, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }

    // Daily Rewards Modal
    if (showDailyRewards) {
        DailyRewardModal(
            rewards = dailyRewards,
            currentStreak = userProfile?.currentStreak ?: 3,
            onClaimDay = { day -> viewModel.claimDailyReward(day) },
            onDismiss = { viewModel.setDailyRewardsDialog(false) }
        )
    }

    // Promo Code Modal
    if (showPromo) {
        PromoCodeModal(
            onRedeem = { code -> viewModel.redeemPromo(code) },
            onDismiss = { viewModel.setPromoDialog(false) }
        )
    }

    // Robux Top-up Modal
    if (showRobux) {
        RobuxTopUpModal(
            onAddRobux = { amount -> viewModel.addRobux(amount) },
            onDismiss = { viewModel.setRobuxDialog(false) }
        )
    }
}
