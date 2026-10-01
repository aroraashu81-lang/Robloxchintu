package com.example.ui.screens.profile

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    userProfile: UserProfile?,
    onBack: () -> Unit,
    onOpenRobux: () -> Unit,
    onOpenPromo: () -> Unit
) {
    var performanceMode by remember { mutableStateOf(true) }
    var soundEffects by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .testTag("profile_screen"),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        // Top Action Bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Text(
                    text = "Player Profile",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                IconButton(onClick = onOpenPromo) {
                    Icon(Icons.Default.CardGiftcard, contentDescription = "Redeem Promo", tint = NeonGreen)
                }
            }
        }

        // Profile Avatar Card (Matching Image 5)
        item {
            Surface(
                color = DarkCard,
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, DarkCardBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(68.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(
                                            listOf(NeonCyan, Color(0xFF131822))
                                        )
                                    )
                                    .border(2.dp, NeonGreen, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(42.dp))
                                // Level badge on bottom
                                Surface(
                                    color = NeonGreen,
                                    shape = CircleShape,
                                    modifier = Modifier.align(Alignment.BottomEnd)
                                ) {
                                    Text(
                                        text = "${userProfile?.level ?: 42}",
                                        color = DarkBackground,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 10.sp,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = userProfile?.username ?: "CyberGamer99",
                                        color = Color.White,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 17.sp
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(Icons.Default.Verified, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp))
                                }
                                Text(text = userProfile?.tag ?: "@cyber_nexus_pro", color = TextSecondary, fontSize = 12.sp)

                                Spacer(modifier = Modifier.height(4.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Surface(color = NeonCyan.copy(alpha = 0.2f), shape = RoundedCornerShape(4.dp)) {
                                        Text(text = "Lv. ${userProfile?.level ?: 42} Pro", color = NeonCyan, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                    Surface(color = RobuxGold.copy(alpha = 0.2f), shape = RoundedCornerShape(4.dp)) {
                                        Text(text = "VIP ELITE", color = RobuxGold, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                }
                            }
                        }

                        IconButton(onClick = onOpenPromo) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings", tint = TextSecondary)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Exploring infinite voxel realities. Building cyberpunk worlds and hunting rare loot across the robloxiachintu Metaverse.",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Robux Balance Card
        item {
            Surface(
                color = DarkCard,
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.dp, RobuxGold.copy(alpha = 0.4f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(RobuxGold.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "R$", color = RobuxGold, fontWeight = FontWeight.Black, fontSize = 18.sp)
                        }
                        Column {
                            Text(text = "Robux Balance", color = TextSecondary, fontSize = 11.sp)
                            Text(
                                text = "${userProfile?.robuxBalance ?: 1450} R$",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp
                            )
                            Text(text = "• Season 5 Pass Active", color = NeonCyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Button(
                        onClick = onOpenRobux,
                        colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = "Get Robux", color = DarkBackground, fontWeight = FontWeight.Black, fontSize = 12.sp)
                    }
                }
            }
        }

        // Stats Grid: Playtime, Badges, Friends, Favorites
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ProfileStatPill("480h", "Playtime", Icons.Default.Schedule, Modifier.weight(1f))
                ProfileStatPill("184", "Badges", Icons.Default.EmojiEvents, Modifier.weight(1f))
                ProfileStatPill("142", "Friends", Icons.Default.People, Modifier.weight(1f))
                ProfileStatPill("38", "Favorites", Icons.Default.Favorite, Modifier.weight(1f))
            }
        }

        // Settings & Preferences
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text(text = "Settings & Preferences", color = Color.White, fontWeight = FontWeight.Black, fontSize = 15.sp)
                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                    color = DarkCard,
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, DarkCardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        SettingsRow(Icons.Default.Security, "Account Info & Security", "Email, Password & 2FA Enabled")
                        HorizontalDivider(color = DarkCardBorder, thickness = 0.5.dp)
                        SettingsRow(Icons.Default.Lock, "Privacy & Chat Permissions", "Who can message or join you")
                        HorizontalDivider(color = DarkCardBorder, thickness = 0.5.dp)

                        // 120 FPS Toggle
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(Icons.Default.GraphicEq, contentDescription = null, tint = NeonCyan)
                                Column {
                                    Text(text = "120 FPS Performance Mode", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text(text = "Maximum refresh rate", color = TextSecondary, fontSize = 11.sp)
                                }
                            }
                            Switch(
                                checked = performanceMode,
                                onCheckedChange = { performanceMode = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = DarkBackground,
                                    checkedTrackColor = NeonGreen
                                )
                            )
                        }

                        HorizontalDivider(color = DarkCardBorder, thickness = 0.5.dp)

                        // Promo Code Redeem shortcut
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onOpenPromo() }
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(Icons.Default.CardGiftcard, contentDescription = null, tint = NeonGreen)
                                Column {
                                    Text(text = "Redeem Promo Code", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text(text = "Unlock bonus Robux & exclusive items", color = TextSecondary, fontSize = 11.sp)
                                }
                            }
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = NeonGreen)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Logout Button
                Surface(
                    color = LaserRed.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, LaserRed.copy(alpha = 0.4f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* switch account */ }
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.ExitToApp, contentDescription = null, tint = LaserRed, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Log Out / Switch Account", color = LaserRed, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileStatPill(value: String, label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier) {
    Surface(
        color = DarkCard,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, DarkCardBorder),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, color = Color.White, fontWeight = FontWeight.Black, fontSize = 13.sp)
            Text(text = label, color = TextSecondary, fontSize = 9.sp)
        }
    }
}

@Composable
private fun SettingsRow(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(icon, contentDescription = null, tint = NeonCyan)
            Column {
                Text(text = title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text(text = subtitle, color = TextSecondary, fontSize = 11.sp)
            }
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted)
    }
}
