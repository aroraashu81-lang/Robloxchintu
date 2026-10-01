package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.DailyRewardItem
import com.example.data.model.UserProfile
import com.example.ui.theme.*
import com.example.ui.viewmodel.ScreenDestination

@Composable
fun RobloxiachintuLogo(modifier: Modifier = Modifier, size: Int = 32) {
    // Custom tilted rounded square emblem with hollow center cutout matching the app icon
    Box(
        modifier = modifier
            .size(size.dp)
            .shadow(8.dp, RoundedCornerShape(size.dp / 4), spotColor = NeonCyan)
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF00E5FF), Color(0xFF2979FF), Color(0xFFA855F7))
                ),
                shape = RoundedCornerShape(size.dp / 4)
            )
            .padding((size / 10).coerceAtLeast(2).dp)
            .background(Color(0xFF131822), shape = RoundedCornerShape((size / 5).coerceAtLeast(3).dp)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size((size * 0.55f).dp)
                .rotate(-15f)
                .background(
                    Brush.linearGradient(listOf(NeonCyan, Color(0xFF00B0FF))),
                    shape = RoundedCornerShape((size * 0.16f).dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size((size * 0.24f).dp)
                    .background(Color(0xFF131822), shape = RoundedCornerShape((size * 0.08f).dp))
            )
        }
    }
}

@Composable
fun TopNavBar(
    userProfile: UserProfile?,
    onNavigate: (ScreenDestination) -> Unit,
    onOpenDailyReward: () -> Unit,
    onOpenRobux: () -> Unit,
    onOpenPromo: () -> Unit
) {
    Surface(
        color = DarkBackground,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // App Branding
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { onNavigate(ScreenDestination.HOME) }
                    .testTag("app_logo_button")
            ) {
                RobloxiachintuLogo(size = 36)
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "ROBLOXIA",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 17.sp,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "CHINTU METAVERSE",
                        color = NeonCyan,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp,
                        letterSpacing = 1.5.sp
                    )
                }
            }

            // Right actions
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Robux Balance Pill
                Surface(
                    color = DarkCard,
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, RobuxGold.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .clickable { onOpenRobux() }
                        .testTag("robux_balance_pill")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .background(RobuxGold, shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "R$",
                                color = DarkBackground,
                                fontWeight = FontWeight.Black,
                                fontSize = 9.sp
                            )
                        }
                        Text(
                            text = "${userProfile?.robuxBalance ?: 1450}",
                            color = RobuxGold,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 13.sp
                        )
                    }
                }

                // Daily Reward Gift Button
                IconButton(
                    onClick = onOpenDailyReward,
                    modifier = Modifier
                        .size(36.dp)
                        .background(NeonPurple.copy(alpha = 0.2f), CircleShape)
                        .testTag("daily_reward_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.CardGiftcard,
                        contentDescription = "Daily Rewards",
                        tint = NeonPurple,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Creator Studio icon button
                IconButton(
                    onClick = { onNavigate(ScreenDestination.CREATOR_STUDIO) },
                    modifier = Modifier
                        .size(36.dp)
                        .background(NeonGreen.copy(alpha = 0.15f), CircleShape)
                        .testTag("creator_studio_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Construction,
                        contentDescription = "Creator Studio",
                        tint = NeonGreen,
                        modifier = Modifier.size(19.dp)
                    )
                }

                // Profile Avatar Thumbnail
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, NeonCyan, CircleShape)
                        .clickable { onNavigate(ScreenDestination.PROFILE) }
                        .testTag("profile_avatar_thumb"),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Brush.radialGradient(listOf(Color(0xFF00E5FF), Color(0xFF131822))))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = Color.White,
                            modifier = Modifier.align(Alignment.Center)
                        )
                        // Online status indicator dot
                        Box(
                            modifier = Modifier
                                .size(9.dp)
                                .background(NeonGreen, CircleShape)
                                .border(1.dp, DarkBackground, CircleShape)
                                .align(Alignment.BottomEnd)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavBar(
    currentScreen: ScreenDestination,
    onNavigate: (ScreenDestination) -> Unit
) {
    val items = listOf(
        NavigationItem(ScreenDestination.HOME, "Home", Icons.Default.Home, Icons.Outlined.Home),
        NavigationItem(ScreenDestination.DISCOVER, "Discover", Icons.Default.Explore, Icons.Outlined.Explore),
        NavigationItem(ScreenDestination.AVATAR, "Avatar", Icons.Default.Person, Icons.Outlined.Person),
        NavigationItem(ScreenDestination.LEADERBOARD, "Ranking", Icons.Default.EmojiEvents, Icons.Outlined.EmojiEvents),
        NavigationItem(ScreenDestination.CHAT, "Social", Icons.AutoMirrored.Filled.Chat, Icons.Outlined.ChatBubbleOutline)
    )

    Surface(
        color = DarkBackground,
        tonalElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .border(
                width = 0.75.dp,
                brush = Brush.horizontalGradient(
                    listOf(DarkCardBorder, NeonCyan.copy(alpha = 0.3f), DarkCardBorder)
                ),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val selected = currentScreen == item.destination
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { onNavigate(item.destination) }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .testTag("nav_item_${item.label.lowercase()}")
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (selected) NeonCyan.copy(alpha = 0.16f) else Color.Transparent
                            )
                            .padding(horizontal = 14.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (selected) item.activeIcon else item.inactiveIcon,
                            contentDescription = item.label,
                            tint = if (selected) NeonCyan else TextSecondary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Text(
                        text = item.label,
                        fontSize = 11.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                        color = if (selected) NeonCyan else TextSecondary
                    )
                }
            }
        }
    }
}

private data class NavigationItem(
    val destination: ScreenDestination,
    val label: String,
    val activeIcon: ImageVector,
    val inactiveIcon: ImageVector
)

@Composable
fun DailyRewardModal(
    rewards: List<DailyRewardItem>,
    currentStreak: Int,
    onClaimDay: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            color = DarkCard,
            shape = RoundedCornerShape(24.dp),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, NeonPurple.copy(alpha = 0.6f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(NeonPurple.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CardGiftcard,
                        contentDescription = null,
                        tint = NeonPurple,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "DAILY LOGIN REWARDS",
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 19.sp,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Current Streak: $currentStreak Days 🔥",
                    color = RobuxGold,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                // 7 Days Grid
                rewards.forEach { reward ->
                    val isCurrentEligible = reward.day == (currentStreak % 7) + 1 && !reward.isClaimed
                    val isPastClaimed = reward.isClaimed

                    Surface(
                        color = when {
                            isPastClaimed -> DarkSurfaceElevated.copy(alpha = 0.6f)
                            isCurrentEligible -> NeonPurple.copy(alpha = 0.2f)
                            else -> DarkSurfaceElevated
                        },
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            width = if (isCurrentEligible) 1.5.dp else 1.dp,
                            color = when {
                                isCurrentEligible -> NeonPurple
                                isPastClaimed -> SuccessGreen.copy(alpha = 0.5f)
                                else -> DarkCardBorder
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(
                                            if (isPastClaimed) SuccessGreen else if (isCurrentEligible) NeonPurple else DarkBackground,
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isPastClaimed) {
                                        Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                    } else {
                                        Text(
                                            text = "D${reward.day}",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                                Column {
                                    Text(
                                        text = reward.rewardItemName,
                                        color = if (isPastClaimed) TextSecondary else Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = "+${reward.rewardRobux} R$ Coins",
                                        color = RobuxGold,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            if (isPastClaimed) {
                                Text(
                                    text = "CLAIMED",
                                    color = SuccessGreen,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            } else if (isCurrentEligible) {
                                Button(
                                    onClick = { onClaimDay(reward.day) },
                                    colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                                ) {
                                    Text(text = "CLAIM", color = Color.White, fontWeight = FontWeight.Black, fontSize = 11.sp)
                                }
                            } else {
                                Text(
                                    text = "LOCKED",
                                    color = TextMuted,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "CLOSE")
                }
            }
        }
    }
}

@Composable
fun PromoCodeModal(
    onRedeem: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var codeInput by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            color = DarkCard,
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, NeonCyan.copy(alpha = 0.6f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "REDEEM PROMO CODE",
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Enter codes like CYBERBOOST, ROBLOXIA2026 or CHINTU to get exclusive Robux & gear!",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = codeInput,
                    onValueChange = { codeInput = it.uppercase() },
                    placeholder = { Text("e.g. CYBERBOOST", color = TextMuted) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = DarkCardBorder,
                        focusedContainerColor = DarkSurface,
                        unfocusedContainerColor = DarkSurface
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("promo_code_input")
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "CANCEL", color = TextSecondary)
                    }

                    Button(
                        onClick = { onRedeem(codeInput) },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("redeem_promo_button")
                    ) {
                        Text(text = "REDEEM", color = DarkBackground, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun RobuxTopUpModal(
    onAddRobux: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val packages = listOf(
        Pair(400, "Free Daily Bonus"),
        Pair(800, "Weekend Mega Pack"),
        Pair(1700, "VIP Creator Grant"),
        Pair(4500, "Ultra Tycoon Stash")
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            color = DarkCard,
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, RobuxGold.copy(alpha = 0.6f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(RobuxGold.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "R$", color = RobuxGold, fontWeight = FontWeight.Black, fontSize = 24.sp)
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "ROBUX VAULT",
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp
                )
                Text(
                    text = "Select an instant Robux grant package for your avatar & passes",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))

                packages.forEach { (amount, label) ->
                    Surface(
                        color = DarkSurfaceElevated,
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, RobuxGold.copy(alpha = 0.3f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp)
                            .clickable { onAddRobux(amount) }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = label, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text(text = "Instant credit to wallet", color = TextSecondary, fontSize = 11.sp)
                            }
                            Button(
                                onClick = { onAddRobux(amount) },
                                colors = ButtonDefaults.buttonColors(containerColor = RobuxGold),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "+$amount R$",
                                    color = DarkBackground,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "CLOSE", color = TextSecondary)
                }
            }
        }
    }
}
