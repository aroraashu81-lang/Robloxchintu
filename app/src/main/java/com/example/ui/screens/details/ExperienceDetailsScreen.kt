package com.example.ui.screens.details

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.UserProfile
import com.example.ui.theme.*

@Composable
fun ExperienceDetailsScreen(
    experienceId: String,
    userProfile: UserProfile?,
    onBack: () -> Unit,
    onLaunchGame: () -> Unit,
    onBuyPass: (type: String, cost: Int, name: String) -> Unit,
    onRedeemCode: (String) -> Unit
) {
    var selectedTab by remember { mutableStateOf("About") }
    val tabs = listOf("About", "Store & Passes", "Servers")
    var isFavorited by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .testTag("experience_details_screen"),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        // Top Action Bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("details_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Text(
                    text = "Experience Details",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(onClick = { /* share */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.White)
                    }
                }
            }
        }

        // Hero Image Banner with trailer preview icon
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(20.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.banner_cyber_obby),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Dark vignette gradient
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Black.copy(alpha = 0.4f), Color.Transparent, Color.Black.copy(alpha = 0.6f))
                            )
                        )
                )

                // Top badges
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Surface(
                            color = Color.Black.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Verified, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "VERIFIED", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Surface(
                            color = Color.Black.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "AGE 9+",
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Surface(
                        color = NeonGreen.copy(alpha = 0.85f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "SERVERS: GLOBAL",
                            color = DarkBackground,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }

                // Center Play trailer button
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(Color.Black.copy(alpha = 0.65f), CircleShape)
                        .border(2.dp, NeonCyan, CircleShape)
                        .align(Alignment.Center)
                        .clickable { onLaunchGame() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Play", tint = NeonCyan, modifier = Modifier.size(36.dp))
                }
            }
        }

        // Title and Creator
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Surface(color = NeonCyan.copy(alpha = 0.15f), shape = RoundedCornerShape(4.dp)) {
                        Text(text = "NEW UPDATE", color = NeonCyan, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                    }
                    Surface(color = NeonPurple.copy(alpha = 0.15f), shape = RoundedCornerShape(4.dp)) {
                        Text(text = "SEASON 4", color = NeonPurple, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Cyber Obby Escape [UPDATE 4.0]",
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "By ", color = TextSecondary, fontSize = 12.sp)
                    Text(text = "NeoBlox Studios", color = NeonCyan, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.Verified, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(14.dp))
                }
            }
        }

        // Main Play Button & Favorite
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onLaunchGame,
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .shadow(8.dp, RoundedCornerShape(14.dp), spotColor = NeonGreen)
                        .testTag("details_play_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = DarkBackground, modifier = Modifier.size(26.dp))
                        Text(
                            text = "PLAY (142.5K ACTIVE)",
                            color = DarkBackground,
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp
                        )
                    }
                }

                Surface(
                    color = DarkCard,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, if (isFavorited) LaserRed else DarkCardBorder),
                    modifier = Modifier
                        .size(52.dp)
                        .clickable { isFavorited = !isFavorited }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (isFavorited) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorited) LaserRed else Color.White
                        )
                    }
                }
            }
        }

        // Stats Grid Cards
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        icon = Icons.Default.People,
                        label = "Active Players",
                        value = "142,520",
                        tint = NeonGreen,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        icon = Icons.Default.Favorite,
                        label = "Favorites",
                        value = "2.4M",
                        tint = NeonPink,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        icon = Icons.Default.ThumbUp,
                        label = "Upvote Ratio",
                        value = "94% (240K 👍)",
                        tint = NeonCyan,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        icon = Icons.Default.RocketLaunch,
                        label = "Total Visits",
                        value = "480M+",
                        tint = RobuxGold,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Friend in Server card
        item {
            Surface(
                color = DarkCard,
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, DarkCardBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(NeonCyan.copy(alpha = 0.2f))
                                .border(1.dp, NeonCyan, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "SX", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "xX_Shadow_Xx", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(modifier = Modifier.size(6.dp).background(NeonGreen, CircleShape))
                            }
                            Text(text = "Playing in Server #12 (14ms ping)", color = TextSecondary, fontSize = 11.sp)
                        }
                    }

                    Button(
                        onClick = onLaunchGame,
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text(text = "Join Server", color = DarkBackground, fontWeight = FontWeight.Black, fontSize = 11.sp)
                    }
                }
            }
        }

        // Tabs Selector: About, Store & Passes, Servers
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .background(DarkCard, RoundedCornerShape(14.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                tabs.forEach { tab ->
                    val isSelected = selectedTab == tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) NeonGreen else Color.Transparent)
                            .clickable { selectedTab = tab }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tab,
                            color = if (isSelected) DarkBackground else Color.White,
                            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Tab Content
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                when (selectedTab) {
                    "About" -> {
                        Surface(
                            color = DarkCard,
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, DarkCardBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Description",
                                    color = Color.White,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 15.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "🚀 Welcome to Cyber Obby Escape! Leap across neon cyber beams, dodge laser traps, and escape the dystopian megacity across 30 thrilling parkour stages!",
                                    color = TextSecondary,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp
                                )

                                Spacer(modifier = Modifier.height(14.dp))
                                Text(
                                    text = "⚡ NEW IN UPDATE 4.0:",
                                    color = NeonCyan,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("• 15 New high-speed cyber stages\n• Neon Gliders equipped in stage 20+\n• Hardcore Boss Rush challenge mode", color = TextSecondary, fontSize = 12.sp)

                                Spacer(modifier = Modifier.height(14.dp))

                                // Promo code redemption card
                                Surface(
                                    color = DarkSurfaceElevated,
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(1.dp, NeonGreen.copy(alpha = 0.4f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(text = "Use Code: CYBERBOOST", color = NeonGreen, fontWeight = FontWeight.Black, fontSize = 13.sp)
                                            Text(text = "+300 R$ & Double Jump Boots", color = TextSecondary, fontSize = 11.sp)
                                        }
                                        Button(
                                            onClick = { onRedeemCode("CYBERBOOST") },
                                            colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                                            shape = RoundedCornerShape(8.dp),
                                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                            modifier = Modifier.height(30.dp)
                                        ) {
                                            Text(text = "Redeem", color = DarkBackground, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))
                                Text(text = "Game Attributes", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Column {
                                        Text("Genre", color = TextSecondary, fontSize = 11.sp)
                                        Text("Obby / Parkour", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    }
                                    Column {
                                        Text("Max Players", color = TextSecondary, fontSize = 11.sp)
                                        Text("16 / Server", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    }
                                    Column {
                                        Text("Updated", color = TextSecondary, fontSize = 11.sp)
                                        Text("Today (v4.0.2)", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }

                    "Store & Passes" -> {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            // VIP Pass
                            GamePassCard(
                                title = "VIP Cyber Pass",
                                desc = "Exclusive neon trail, 2x coin multiplier & VIP tag in chat",
                                price = 450,
                                isOwned = userProfile?.hasVipPass == true,
                                onBuy = { onBuyPass("VIP", 450, "VIP Cyber Pass") }
                            )

                            // Double Jump Boots
                            GamePassCard(
                                title = "Double Jump Boots",
                                desc = "Leap twice in mid-air to skip tricky neon obstacles easily",
                                price = 150,
                                isOwned = userProfile?.hasDoubleJump == true,
                                onBuy = { onBuyPass("DOUBLE_JUMP", 150, "Double Jump Boots") }
                            )
                        }
                    }

                    "Servers" -> {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            ServerCard("US-East #12 (Main)", "15/16 Players", "14ms", onLaunchGame)
                            ServerCard("EU-West #04", "12/16 Players", "24ms", onLaunchGame)
                            ServerCard("Asia-Pacific #01", "8/16 Players", "42ms", onLaunchGame)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        color = DarkCard,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, DarkCardBorder),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(tint.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(20.dp))
            }
            Column {
                Text(text = label, color = TextSecondary, fontSize = 10.sp)
                Text(text = value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun GamePassCard(
    title: String,
    desc: String,
    price: Int,
    isOwned: Boolean,
    onBuy: () -> Unit
) {
    Surface(
        color = DarkCard,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, if (isOwned) NeonGreen else DarkCardBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = desc, color = TextSecondary, fontSize = 11.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            if (isOwned) {
                Surface(
                    color = NeonGreen.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "OWNED",
                        color = NeonGreen,
                        fontWeight = FontWeight.Black,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            } else {
                Button(
                    onClick = onBuy,
                    colors = ButtonDefaults.buttonColors(containerColor = RobuxGold),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(text = "$price R$", color = DarkBackground, fontWeight = FontWeight.Black, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun ServerCard(name: String, players: String, ping: String, onJoin: () -> Unit) {
    Surface(
        color = DarkCard,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, DarkCardBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text(text = "$players • Ping: $ping", color = NeonGreen, fontSize = 11.sp)
            }
            Button(
                onClick = onJoin,
                colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceElevated),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                modifier = Modifier.height(30.dp)
            ) {
                Text(text = "Join", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }
        }
    }
}
