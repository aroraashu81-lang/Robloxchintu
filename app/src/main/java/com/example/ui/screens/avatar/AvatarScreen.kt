package com.example.ui.screens.avatar

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AvatarItem
import com.example.data.model.UserProfile
import com.example.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AvatarScreen(
    userProfile: UserProfile?,
    avatarItems: List<AvatarItem>,
    onPurchaseItem: (AvatarItem) -> Unit,
    onEquipItem: (AvatarItem) -> Unit
) {
    var rotationAngle by remember { mutableFloatStateOf(0f) }
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Outfits", "Hats", "Hair", "Faces", "Weapons", "Wings")

    val filteredItems = remember(selectedCategory, avatarItems) {
        if (selectedCategory == "All") avatarItems else avatarItems.filter { it.category == selectedCategory }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .testTag("avatar_screen"),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        // 3D Viewport Card
        item {
            Surface(
                color = DarkCard,
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.5.dp, DarkCardBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Header badges
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = NeonCyan.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(20.dp),
                            border = BorderStroke(1.dp, NeonCyan.copy(alpha = 0.4f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(NeonCyan, CircleShape)
                                )
                                Text(
                                    text = "LIVE 3D VIEW",
                                    color = NeonCyan,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            IconButton(
                                onClick = { rotationAngle = (rotationAngle + 45f) % 360f },
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(DarkSurfaceElevated, CircleShape)
                            ) {
                                Icon(Icons.Default.RotateRight, contentDescription = "Rotate", tint = Color.White, modifier = Modifier.size(18.dp))
                            }
                            IconButton(
                                onClick = { rotationAngle = 0f },
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(DarkSurfaceElevated, CircleShape)
                            ) {
                                Icon(Icons.Default.CenterFocusStrong, contentDescription = "Reset", tint = Color.White, modifier = Modifier.size(18.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 3D Character Interactive Viewport
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(230.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xFF00E5FF).copy(alpha = 0.2f),
                                        Color(0xFF131822),
                                        Color(0xFF0B0E14)
                                    )
                                )
                            )
                            .pointerInput(Unit) {
                                detectDragGestures { change, dragAmount ->
                                    change.consume()
                                    rotationAngle = (rotationAngle + dragAmount.x * 0.8f) % 360f
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val centerX = size.width / 2f
                            val centerY = size.height / 2f

                            // Draw glowing pedestal
                            drawOval(
                                color = NeonCyan.copy(alpha = 0.3f),
                                topLeft = Offset(centerX - 120f, centerY + 70f),
                                size = Size(240f, 40f)
                            )
                            drawOval(
                                color = NeonCyan,
                                topLeft = Offset(centerX - 100f, centerY + 75f),
                                size = Size(200f, 30f),
                                style = Stroke(width = 3f)
                            )

                            // Render Blocky Voxel Avatar with rotation projection
                            val rad = Math.toRadians(rotationAngle.toDouble())
                            val rotCos = cos(rad).toFloat()
                            val rotSin = sin(rad).toFloat()

                            // Wings (if equipped)
                            if (userProfile?.equippedWings?.contains("Wings") == true || userProfile?.equippedWings?.contains("Quantum") == true) {
                                drawArc(
                                    brush = Brush.radialGradient(listOf(NeonPink, NeonPurple)),
                                    startAngle = 180f,
                                    sweepAngle = 180f,
                                    useCenter = true,
                                    topLeft = Offset(centerX - 140f, centerY - 80f),
                                    size = Size(280f, 100f)
                                )
                            }

                            // Legs
                            drawRect(
                                color = Color(0xFF1E293B),
                                topLeft = Offset(centerX - 35f, centerY + 25f),
                                size = Size(28f, 55f)
                            )
                            drawRect(
                                color = Color(0xFF1E293B),
                                topLeft = Offset(centerX + 7f, centerY + 25f),
                                size = Size(28f, 55f)
                            )
                            // Boots (glow cyan)
                            drawRect(
                                color = NeonCyan,
                                topLeft = Offset(centerX - 35f, centerY + 65f),
                                size = Size(28f, 15f)
                            )
                            drawRect(
                                color = NeonCyan,
                                topLeft = Offset(centerX + 7f, centerY + 65f),
                                size = Size(28f, 15f)
                            )

                            // Torso (Blocky Cyber Suit)
                            drawRect(
                                color = Color(0xFF0F172A),
                                topLeft = Offset(centerX - 42f, centerY - 45f),
                                size = Size(84f, 70f)
                            )
                            // Torso Neon Accent Lines
                            drawLine(
                                color = NeonGreen,
                                start = Offset(centerX - 25f, centerY - 35f),
                                end = Offset(centerX + 25f, centerY + 15f),
                                strokeWidth = 5f
                            )
                            drawLine(
                                color = NeonCyan,
                                start = Offset(centerX + 25f, centerY - 35f),
                                end = Offset(centerX - 25f, centerY + 15f),
                                strokeWidth = 5f
                            )

                            // Arms
                            val armOffset = rotSin * 10f
                            drawRect(
                                color = Color(0xFF1E293B),
                                topLeft = Offset(centerX - 68f, centerY - 40f + armOffset),
                                size = Size(22f, 65f)
                            )
                            drawRect(
                                color = Color(0xFF1E293B),
                                topLeft = Offset(centerX + 46f, centerY - 40f - armOffset),
                                size = Size(22f, 65f)
                            )

                            // Katana on back / hand
                            if (userProfile?.equippedWeapon?.contains("Katana") == true) {
                                drawLine(
                                    color = NeonCyan,
                                    start = Offset(centerX + 55f, centerY - 55f),
                                    end = Offset(centerX + 70f, centerY + 45f),
                                    strokeWidth = 6f
                                )
                                drawLine(
                                    color = Color.White,
                                    start = Offset(centerX + 55f, centerY - 55f),
                                    end = Offset(centerX + 70f, centerY + 45f),
                                    strokeWidth = 2f
                                )
                            }

                            // Head (Cube)
                            drawRect(
                                color = Color(0xFFF1C40F), // Voxel skin
                                topLeft = Offset(centerX - 30f, centerY - 105f),
                                size = Size(60f, 60f)
                            )

                            // Cyber Hair / Helmet
                            drawRect(
                                color = Color(0xFF0F172A),
                                topLeft = Offset(centerX - 32f, centerY - 110f),
                                size = Size(64f, 25f)
                            )

                            // Holographic Visor (Green/Cyan)
                            val visorShift = rotCos * 8f
                            drawRect(
                                color = NeonGreen,
                                topLeft = Offset(centerX - 28f + visorShift, centerY - 88f),
                                size = Size(56f, 18f)
                            )
                            // Glowing eye dots
                            drawCircle(
                                color = Color.White,
                                radius = 3.5f,
                                center = Offset(centerX - 12f + visorShift, centerY - 80f)
                            )
                            drawCircle(
                                color = Color.White,
                                radius = 3.5f,
                                center = Offset(centerX + 12f + visorShift, centerY - 80f)
                            )

                            // Cyber Headphones
                            drawCircle(
                                color = NeonCyan,
                                radius = 12f,
                                center = Offset(centerX - 35f, centerY - 75f)
                            )
                            drawCircle(
                                color = NeonCyan,
                                radius = 12f,
                                center = Offset(centerX + 35f, centerY - 75f)
                            )
                        }

                        Text(
                            text = "Drag to rotate avatar 360°",
                            color = TextMuted,
                            fontSize = 10.sp,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 6.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Outfit Status & Save
                    Row(
                        modifier = Modifier.fillMaxWidth(),
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
                                    .background(NeonGreen.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Shield, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(20.dp))
                            }
                            Column {
                                Text(
                                    text = userProfile?.equippedOutfit ?: "Cyber-Ninja v2",
                                    color = Color.White,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "Power: 9,850 R$ Outfit",
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Button(
                            onClick = { /* save loadout */ },
                            colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(text = "Save Outfit", color = DarkBackground, fontWeight = FontWeight.Black, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Saved Loadouts Carousel
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Saved Loadouts",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "Manage All",
                        color = NeonCyan,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    val loadouts = listOf("Cyber Ninja", "Neon Street", "Golden Mech", "Skater Boi")
                    items(loadouts) { loadout ->
                        val isEquipped = loadout == "Cyber Ninja"
                        Surface(
                            color = DarkCard,
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(
                                if (isEquipped) 2.dp else 1.dp,
                                if (isEquipped) NeonGreen else DarkCardBorder
                            ),
                            modifier = Modifier
                                .width(90.dp)
                                .clickable { /* switch loadout */ }
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(54.dp)
                                        .background(DarkSurfaceElevated, RoundedCornerShape(12.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.AccessibilityNew, contentDescription = null, tint = if (isEquipped) NeonGreen else Color.White)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = loadout,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    // Add new slot
                    item {
                        Surface(
                            color = DarkSurfaceElevated,
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, DarkCardBorder),
                            modifier = Modifier.width(90.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(54.dp)
                                        .background(DarkCard, RoundedCornerShape(12.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = null, tint = TextSecondary)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = "New Slot", color = TextSecondary, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }

        // Category Filter Chips
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { cat ->
                    val isSelected = selectedCategory == cat
                    Surface(
                        color = if (isSelected) NeonGreen else DarkCard,
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, if (isSelected) NeonGreen else DarkCardBorder),
                        modifier = Modifier.clickable { selectedCategory = cat }
                    ) {
                        Text(
                            text = cat,
                            color = if (isSelected) DarkBackground else Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                        )
                    }
                }
            }
        }

        // Marketplace Items Section
        item {
            Text(
                text = "Trending Marketplace",
                color = Color.White,
                fontWeight = FontWeight.Black,
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }

        items(filteredItems.chunked(2)) { pair ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                pair.forEach { item ->
                    MarketplaceItemCard(
                        item = item,
                        onBuy = { onPurchaseItem(item) },
                        onEquip = { onEquipItem(item) },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (pair.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun MarketplaceItemCard(
    item: AvatarItem,
    onBuy: () -> Unit,
    onEquip: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = DarkCard,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(
            if (item.isEquipped) 1.5.dp else 1.dp,
            if (item.isEquipped) NeonGreen else DarkCardBorder
        ),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Preview Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(item.accentColor).copy(alpha = 0.25f),
                                DarkBackground
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Rating pill
                Surface(
                    color = Color.Black.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = RobuxGold, modifier = Modifier.size(12.dp))
                        Text(text = "${item.rating}", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Item icon
                Icon(
                    imageVector = when (item.category) {
                        "Weapons" -> Icons.Default.FlashOn
                        "Hats" -> Icons.Default.SportsEsports
                        "Wings" -> Icons.Default.AllInclusive
                        else -> Icons.Default.Checkroom
                    },
                    contentDescription = null,
                    tint = Color(item.accentColor),
                    modifier = Modifier.size(42.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.name,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Action Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (item.isPurchased) {
                    Text(text = "Owned", color = TextSecondary, fontSize = 11.sp)
                    Button(
                        onClick = onEquip,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (item.isEquipped) NeonGreen else DarkSurfaceElevated
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.height(30.dp)
                    ) {
                        Text(
                            text = if (item.isEquipped) "Equipped" else "Equip",
                            color = if (item.isEquipped) DarkBackground else Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "${item.priceRobux} R$", color = RobuxGold, fontWeight = FontWeight.Black, fontSize = 12.sp)
                    }
                    Button(
                        onClick = onBuy,
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.height(30.dp)
                    ) {
                        Text(text = "Buy", color = DarkBackground, fontWeight = FontWeight.Black, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}
