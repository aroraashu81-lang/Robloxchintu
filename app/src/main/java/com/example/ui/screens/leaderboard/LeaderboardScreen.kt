package com.example.ui.screens.leaderboard

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LeaderboardEntry
import com.example.data.model.UserProfile
import com.example.ui.theme.*

@Composable
fun LeaderboardScreen(
    userProfile: UserProfile?,
    currentLeaderboard: List<LeaderboardEntry>,
    selectedCategory: String,
    selectedTimeframe: String,
    onCategoryChange: (String) -> Unit,
    onTimeframeChange: (String) -> Unit,
    onClaimRankReward: (Int) -> Unit
) {
    val categories = listOf(
        Pair("OBBY_SPEEDRUN", "🚀 Speedrun"),
        Pair("OBBY_STAGES", "🏆 Obby Stages"),
        Pair("RIVALS_PVP", "⚔️ Rivals PvP"),
        Pair("ROBUX_RICH", "💎 Rich Tycoons")
    )
    val timeframes = listOf("All Time", "This Week", "Today")

    val top1 = currentLeaderboard.find { it.rank == 1 }
    val top2 = currentLeaderboard.find { it.rank == 2 }
    val top3 = currentLeaderboard.find { it.rank == 3 }
    val rest = currentLeaderboard.filter { it.rank > 3 }
    val currentUserEntry = currentLeaderboard.find { it.isCurrentUser }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .testTag("leaderboard_screen")
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            // Header
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = RobuxGold,
                            modifier = Modifier.size(28.dp)
                        )
                        Text(
                            text = "GLOBAL LEADERBOARDS",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp,
                            letterSpacing = 1.2.sp
                        )
                    }
                    Text(
                        text = "Official robloxiachintu World Rankings & Records",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Categories Selector
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        categories.forEach { (catId, label) ->
                            val isSelected = selectedCategory == catId
                            Surface(
                                color = if (isSelected) NeonCyan else DarkCard,
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(1.dp, if (isSelected) NeonCyan else DarkCardBorder),
                                modifier = Modifier
                                    .clickable { onCategoryChange(catId) }
                                    .testTag("lb_cat_$catId")
                            ) {
                                Text(
                                    text = label,
                                    color = if (isSelected) DarkBackground else Color.White,
                                    fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Timeframe filter tabs
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(DarkCard, RoundedCornerShape(12.dp))
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        timeframes.forEach { tf ->
                            val isSelected = selectedTimeframe == tf
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) NeonPurple.copy(alpha = 0.3f) else Color.Transparent)
                                    .clickable { onTimeframeChange(tf) }
                                    .padding(vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = tf,
                                    color = if (isSelected) NeonPurple else TextSecondary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }

            // Top 3 Podium
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "TOP 3 PODIUM",
                        color = RobuxGold,
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        // #2 Rank
                        PodiumColumn(
                            entry = top2,
                            place = 2,
                            pillarHeight = 110.dp,
                            pillarColor = Color(0xFFC0C0C0),
                            modifier = Modifier.weight(1f)
                        )

                        // #1 Rank (Center, Highest)
                        PodiumColumn(
                            entry = top1,
                            place = 1,
                            pillarHeight = 145.dp,
                            pillarColor = RobuxGold,
                            isFirst = true,
                            modifier = Modifier.weight(1.1f)
                        )

                        // #3 Rank
                        PodiumColumn(
                            entry = top3,
                            place = 3,
                            pillarHeight = 90.dp,
                            pillarColor = Color(0xFFCD7F32),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Other Ranks Header
            item {
                PaddingValues(horizontal = 16.dp, vertical = 8.dp).let {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(it),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "CONTENDERS",
                            color = TextSecondary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "RECORD / SCORE",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            // List of Ranks 4+
            items(rest) { entry ->
                val isMe = entry.isCurrentUser
                Surface(
                    color = if (isMe) NeonCyan.copy(alpha = 0.12f) else DarkCard,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(
                        width = if (isMe) 1.5.dp else 1.dp,
                        color = if (isMe) NeonCyan else DarkCardBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Rank pill
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .background(DarkSurfaceElevated, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "#${entry.rank}",
                                    color = if (isMe) NeonCyan else Color.White,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 11.sp
                                )
                            }

                            // Avatar icon
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(
                                            listOf(
                                                if (isMe) NeonCyan else Color(0xFF334155),
                                                Color(0xFF0F172A)
                                            )
                                        )
                                    )
                                    .border(1.dp, if (isMe) NeonCyan else DarkCardBorder, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = if (isMe) NeonCyan else Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = entry.username,
                                        color = if (isMe) NeonCyan else Color.White,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 13.sp
                                    )
                                    if (isMe) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Surface(
                                            color = NeonCyan,
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                text = "YOU",
                                                color = DarkBackground,
                                                fontWeight = FontWeight.Black,
                                                fontSize = 9.sp,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = entry.badge,
                                    color = TextSecondary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Score Pill
                        Surface(
                            color = DarkBackground,
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, if (isMe) NeonCyan.copy(alpha = 0.5f) else DarkCardBorder)
                        ) {
                            Text(
                                text = entry.scoreText,
                                color = if (isMe) NeonCyan else RobuxGold,
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        // Pinned Bottom User Standing Bar
        Surface(
            color = DarkCard,
            tonalElevation = 12.dp,
            border = BorderStroke(1.5.dp, NeonCyan.copy(alpha = 0.6f)),
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 60.dp) // above bottom nav
                .shadow(16.dp, spotColor = NeonCyan)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(NeonCyan, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "#${currentUserEntry?.rank ?: 4}",
                            color = DarkBackground,
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp
                        )
                    }
                    Column {
                        Text(
                            text = "Your Global Position",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Text(
                            text = currentUserEntry?.scoreText ?: "01:34.50",
                            color = NeonCyan,
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp
                        )
                    }
                }

                Button(
                    onClick = { onClaimRankReward(100) },
                    colors = ButtonDefaults.buttonColors(containerColor = RobuxGold),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                    modifier = Modifier.testTag("claim_rank_reward_btn")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.CardGiftcard, contentDescription = null, tint = DarkBackground, modifier = Modifier.size(16.dp))
                        Text(text = "Claim +100 R$", color = DarkBackground, fontWeight = FontWeight.Black, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun PodiumColumn(
    entry: LeaderboardEntry?,
    place: Int,
    pillarHeight: androidx.compose.ui.unit.Dp,
    pillarColor: Color,
    isFirst: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        if (isFirst) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = RobuxGold,
                modifier = Modifier.size(20.dp)
            )
        }

        // Avatar
        Box(
            modifier = Modifier
                .size(if (isFirst) 46.dp else 38.dp)
                .clip(CircleShape)
                .background(pillarColor.copy(alpha = 0.25f))
                .border(2.dp, pillarColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(if (isFirst) 26.dp else 20.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = entry?.username ?: "Player",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = if (isFirst) 12.sp else 10.sp,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
        Text(
            text = entry?.scoreText ?: "--",
            color = pillarColor,
            fontWeight = FontWeight.Black,
            fontSize = if (isFirst) 11.sp else 9.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Pillar Block
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(pillarHeight)
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(pillarColor.copy(alpha = 0.5f), pillarColor.copy(alpha = 0.15f))
                    )
                )
                .border(1.dp, pillarColor.copy(alpha = 0.6f), RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$place",
                color = pillarColor,
                fontWeight = FontWeight.Black,
                fontSize = if (isFirst) 32.sp else 24.sp
            )
        }
    }
}
