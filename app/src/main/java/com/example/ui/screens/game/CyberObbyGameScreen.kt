package com.example.ui.screens.game

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.math.sin

data class ObbyPlatform(
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float = 24f,
    val isMoving: Boolean = false,
    val moveSpeed: Float = 0f,
    val moveRange: Float = 0f,
    val color: Color = NeonCyan,
    val isSpike: Boolean = false,
    val isJumpPad: Boolean = false
)

data class ObbyCoin(
    val x: Float,
    val y: Float,
    var isCollected: Boolean = false
)

data class ObbyLaser(
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float = 10f,
    val speed: Float = 2f,
    val minX: Float = 0f,
    val maxX: Float = 800f
)

@Composable
fun CyberObbyGameScreen(
    userProfile: UserProfile?,
    onExitGame: () -> Unit,
    onStageCompleted: (stage: Int, timeMs: Long, coinsCollected: Int) -> Unit
) {
    var stage by remember { mutableIntStateOf(1) }
    var lives by remember { mutableIntStateOf(3) }
    var coinsCollected by remember { mutableIntStateOf(0) }
    var gameTimeMs by remember { mutableLongStateOf(0L) }
    var isRunning by remember { mutableStateOf(true) }
    var isStageCleared by remember { mutableStateOf(false) }
    var isGameOver by remember { mutableStateOf(false) }

    // Player Physics State
    var playerX by remember { mutableFloatStateOf(60f) }
    var playerY by remember { mutableFloatStateOf(200f) }
    var velocityX by remember { mutableFloatStateOf(0f) }
    var velocityY by remember { mutableFloatStateOf(0f) }
    var isGrounded by remember { mutableStateOf(false) }
    var jumpCount by remember { mutableIntStateOf(0) }
    val maxJumps = if (userProfile?.hasDoubleJump == true) 2 else 1

    // Control inputs
    var moveLeft by remember { mutableStateOf(false) }
    var moveRight by remember { mutableStateOf(false) }

    // Stage configuration generator
    fun getPlatformsForStage(s: Int): List<ObbyPlatform> {
        val list = mutableListOf<ObbyPlatform>()
        // Spawn ground
        list.add(ObbyPlatform(x = 20f, y = 380f, width = 140f, color = NeonGreen))

        when (s) {
            1 -> {
                list.add(ObbyPlatform(x = 200f, y = 340f, width = 100f, color = NeonCyan))
                list.add(ObbyPlatform(x = 340f, y = 290f, width = 90f, color = NeonPurple))
                list.add(ObbyPlatform(x = 470f, y = 240f, width = 90f, color = NeonCyan))
                list.add(ObbyPlatform(x = 600f, y = 200f, width = 80f, color = RobuxGold, isJumpPad = true))
                list.add(ObbyPlatform(x = 720f, y = 140f, width = 160f, color = NeonGreen)) // Goal
            }
            2 -> {
                list.add(ObbyPlatform(x = 190f, y = 350f, width = 80f, color = NeonCyan))
                list.add(ObbyPlatform(x = 300f, y = 350f, width = 50f, color = LaserRed, isSpike = true))
                list.add(ObbyPlatform(x = 380f, y = 300f, width = 90f, color = NeonPurple))
                list.add(ObbyPlatform(x = 510f, y = 250f, width = 80f, color = NeonCyan, isMoving = true, moveSpeed = 2f, moveRange = 60f))
                list.add(ObbyPlatform(x = 640f, y = 210f, width = 70f, color = RobuxGold))
                list.add(ObbyPlatform(x = 750f, y = 160f, width = 160f, color = NeonGreen)) // Goal
            }
            else -> {
                list.add(ObbyPlatform(x = 180f, y = 340f, width = 70f, color = NeonCyan))
                list.add(ObbyPlatform(x = 280f, y = 300f, width = 70f, color = NeonPurple, isMoving = true, moveSpeed = 3f, moveRange = 80f))
                list.add(ObbyPlatform(x = 420f, y = 250f, width = 60f, color = LaserRed, isSpike = true))
                list.add(ObbyPlatform(x = 510f, y = 220f, width = 80f, color = NeonCyan))
                list.add(ObbyPlatform(x = 630f, y = 180f, width = 70f, color = RobuxGold, isJumpPad = true))
                list.add(ObbyPlatform(x = 740f, y = 130f, width = 160f, color = NeonGreen)) // Goal
            }
        }
        return list
    }

    fun getCoinsForStage(s: Int): List<ObbyCoin> {
        return listOf(
            ObbyCoin(x = 240f, y = 290f),
            ObbyCoin(x = 380f, y = 240f),
            ObbyCoin(x = 510f, y = 190f),
            ObbyCoin(x = 640f, y = 150f)
        )
    }

    var platforms by remember(stage) { mutableStateOf(getPlatformsForStage(stage)) }
    var coins by remember(stage) { mutableStateOf(getCoinsForStage(stage)) }
    var laserX by remember { mutableFloatStateOf(350f) }
    var laserDir by remember { mutableFloatStateOf(3f) }

    fun respawnPlayer() {
        playerX = 60f
        playerY = 320f
        velocityX = 0f
        velocityY = 0f
        jumpCount = 0
        isGrounded = true
    }

    fun jump() {
        if (jumpCount < maxJumps) {
            velocityY = -13.5f
            jumpCount++
            isGrounded = false
        }
    }

    fun dash() {
        velocityX = if (moveLeft) -12f else 12f
    }

    // Main 60 FPS Physics Engine Coroutine
    LaunchedEffect(stage, isRunning, isGameOver, isStageCleared) {
        respawnPlayer()
        while (isRunning && !isGameOver && !isStageCleared) {
            delay(16L) // ~60fps
            gameTimeMs += 16L

            // Horizontal input
            val targetVx = when {
                moveLeft -> -5.5f
                moveRight -> 5.5f
                else -> 0f
            }
            velocityX = velocityX * 0.7f + targetVx * 0.3f
            playerX += velocityX

            // Gravity
            velocityY += 0.75f
            playerY += velocityY

            // Moving laser update
            laserX += laserDir
            if (laserX > 550f || laserX < 260f) {
                laserDir = -laserDir
            }

            // Platform collision checking
            var onGroundThisFrame = false
            val playerBox = RectF(playerX, playerY, playerX + 32f, playerY + 44f)

            for (plat in platforms) {
                val platBox = RectF(plat.x, plat.y, plat.x + plat.width, plat.y + plat.height)

                // Hazard Spike collision
                if (plat.isSpike && playerBox.intersects(platBox)) {
                    lives--
                    if (lives <= 0) {
                        isGameOver = true
                    } else {
                        respawnPlayer()
                    }
                    break
                }

                // Landing on top of platform
                if (playerBox.right > plat.x && playerBox.left < plat.x + plat.width) {
                    val prevPlayerBottom = (playerY + 44f) - velocityY
                    if (prevPlayerBottom <= plat.y + 8f && (playerY + 44f) >= plat.y) {
                        playerY = plat.y - 44f
                        velocityY = if (plat.isJumpPad) -17f else 0f // Bounce pad launch!
                        onGroundThisFrame = true
                        jumpCount = 0
                    }
                }
            }
            isGrounded = onGroundThisFrame

            // Laser Beam collision (Stage 2+)
            if (stage >= 2) {
                val laserBox = RectF(laserX, 270f, laserX + 80f, 276f)
                if (playerBox.intersects(laserBox)) {
                    lives--
                    if (lives <= 0) {
                        isGameOver = true
                    } else {
                        respawnPlayer()
                    }
                }
            }

            // Coin collection
            coins.forEach { coin ->
                if (!coin.isCollected) {
                    val coinBox = RectF(coin.x, coin.y, coin.x + 22f, coin.y + 22f)
                    if (playerBox.intersects(coinBox)) {
                        coin.isCollected = true
                        coinsCollected += 10
                    }
                }
            }

            // Pit fall check
            if (playerY > 500f) {
                lives--
                if (lives <= 0) {
                    isGameOver = true
                } else {
                    respawnPlayer()
                }
            }

            // Check if reached Goal platform (x > 730f)
            if (playerX >= 730f && playerY <= 180f) {
                isStageCleared = true
                onStageCompleted(stage, gameTimeMs, coinsCollected)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .testTag("cyber_obby_game_screen")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // In-Game Top HUD
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = onExitGame,
                        modifier = Modifier
                            .size(36.dp)
                            .background(DarkCard, CircleShape)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Exit", tint = Color.White)
                    }

                    Surface(
                        color = NeonCyan.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, NeonCyan)
                    ) {
                        Text(
                            text = "STAGE $stage/5",
                            color = NeonCyan,
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                // Hearts / Lives
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    repeat(3) { i ->
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = if (i < lives) LaserRed else TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Timer & Coins
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val sec = (gameTimeMs / 1000) % 60
                    val min = (gameTimeMs / 1000) / 60
                    Text(
                        text = "%02d:%02d".format(min, sec),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )

                    Surface(
                        color = DarkCard,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, RobuxGold.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(text = "R$", color = RobuxGold, fontWeight = FontWeight.Black, fontSize = 10.sp)
                            Text(text = "+$coinsCollected", color = RobuxGold, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }

            // Main Canvas Obby Viewport
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .padding(horizontal = 12.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF090D16), Color(0xFF131B2C), Color(0xFF070A0F))
                        )
                    )
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val canvasWidth = size.width
                    val canvasHeight = size.height

                    // Dynamic camera scroll (follow player horizontally)
                    val cameraOffsetX = (canvasWidth / 2f - playerX).coerceAtMost(0f).coerceAtLeast(-450f)

                    // Draw Futuristic Cyber City Grid Background
                    val gridStep = 40f
                    var gx = 0f
                    while (gx < canvasWidth) {
                        drawLine(
                            color = Color(0xFF1E293B).copy(alpha = 0.35f),
                            start = Offset(gx, 0f),
                            end = Offset(gx, canvasHeight),
                            strokeWidth = 1f
                        )
                        gx += gridStep
                    }

                    // Draw Platforms
                    platforms.forEach { plat ->
                        val px = plat.x + cameraOffsetX
                        val py = plat.y

                        // Platform body
                        drawRoundRect(
                            color = plat.color.copy(alpha = 0.85f),
                            topLeft = Offset(px, py),
                            size = Size(plat.width, plat.height),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f)
                        )
                        // Platform top glow line
                        drawLine(
                            color = Color.White,
                            start = Offset(px + 4f, py + 2f),
                            end = Offset(px + plat.width - 4f, py + 2f),
                            strokeWidth = 3f
                        )

                        // Spike visual indicators
                        if (plat.isSpike) {
                            var sx = px + 6f
                            while (sx < px + plat.width - 6f) {
                                drawLine(
                                    color = Color.White,
                                    start = Offset(sx, py),
                                    end = Offset(sx + 5f, py - 12f),
                                    strokeWidth = 3f
                                )
                                drawLine(
                                    color = Color.White,
                                    start = Offset(sx + 5f, py - 12f),
                                    end = Offset(sx + 10f, py),
                                    strokeWidth = 3f
                                )
                                sx += 12f
                            }
                        }

                        // Jump pad spring glow
                        if (plat.isJumpPad) {
                            drawCircle(
                                color = RobuxGold,
                                radius = 8f,
                                center = Offset(px + plat.width / 2f, py + plat.height / 2f)
                            )
                        }
                    }

                    // Draw Laser Beam (Stage 2+)
                    if (stage >= 2) {
                        val lx = laserX + cameraOffsetX
                        drawLine(
                            color = LaserRed,
                            start = Offset(lx, 273f),
                            end = Offset(lx + 80f, 273f),
                            strokeWidth = 6f
                        )
                        drawLine(
                            color = Color.White,
                            start = Offset(lx, 273f),
                            end = Offset(lx + 80f, 273f),
                            strokeWidth = 2f
                        )
                    }

                    // Draw Collectible Coins
                    coins.forEach { coin ->
                        if (!coin.isCollected) {
                            val cx = coin.x + cameraOffsetX + 11f
                            val cy = coin.y + 11f
                            drawCircle(
                                color = RobuxGold,
                                radius = 10f,
                                center = Offset(cx, cy)
                            )
                            drawCircle(
                                color = Color.White,
                                radius = 4f,
                                center = Offset(cx, cy)
                            )
                        }
                    }

                    // Draw Goal Checkpoint Flag
                    val goalX = 800f + cameraOffsetX
                    drawLine(
                        color = Color.White,
                        start = Offset(goalX, 140f),
                        end = Offset(goalX, 80f),
                        strokeWidth = 4f
                    )
                    drawRect(
                        color = NeonGreen,
                        topLeft = Offset(goalX, 80f),
                        size = Size(35f, 24f)
                    )

                    // Draw Player Character (Voxel blocky Cyber Hero)
                    val drawPx = playerX + cameraOffsetX
                    val drawPy = playerY

                    // VIP Trail particles
                    if (userProfile?.hasVipPass == true) {
                        drawCircle(
                            color = NeonCyan.copy(alpha = 0.5f),
                            radius = 6f,
                            center = Offset(drawPx + 8f, drawPy + 38f)
                        )
                        drawCircle(
                            color = NeonPurple.copy(alpha = 0.5f),
                            radius = 4f,
                            center = Offset(drawPx - 4f, drawPy + 40f)
                        )
                    }

                    // Legs
                    drawRect(
                        color = Color(0xFF1E293B),
                        topLeft = Offset(drawPx + 4f, drawPy + 26f),
                        size = Size(10f, 18f)
                    )
                    drawRect(
                        color = Color(0xFF1E293B),
                        topLeft = Offset(drawPx + 18f, drawPy + 26f),
                        size = Size(10f, 18f)
                    )
                    // Boots glow
                    drawRect(
                        color = if (userProfile?.hasDoubleJump == true) NeonGreen else NeonCyan,
                        topLeft = Offset(drawPx + 4f, drawPy + 38f),
                        size = Size(10f, 6f)
                    )
                    drawRect(
                        color = if (userProfile?.hasDoubleJump == true) NeonGreen else NeonCyan,
                        topLeft = Offset(drawPx + 18f, drawPy + 38f),
                        size = Size(10f, 6f)
                    )

                    // Torso
                    drawRect(
                        color = Color(0xFF0F172A),
                        topLeft = Offset(drawPx + 2f, drawPy + 12f),
                        size = Size(28f, 16f)
                    )
                    // Torso Neon Line
                    drawLine(
                        color = NeonGreen,
                        start = Offset(drawPx + 6f, drawPy + 16f),
                        end = Offset(drawPx + 26f, drawPy + 22f),
                        strokeWidth = 2.5f
                    )

                    // Head Cube
                    drawRect(
                        color = Color(0xFFF1C40F),
                        topLeft = Offset(drawPx + 6f, drawPy),
                        size = Size(20f, 14f)
                    )
                    // Visor
                    drawRect(
                        color = NeonGreen,
                        topLeft = Offset(drawPx + 8f, drawPy + 4f),
                        size = Size(16f, 5f)
                    )
                }

                // Double Jump indicator overlay
                if (userProfile?.hasDoubleJump == true) {
                    Surface(
                        color = NeonGreen.copy(alpha = 0.85f),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "2X JUMP BOOTS ACTIVE",
                            color = DarkBackground,
                            fontWeight = FontWeight.Black,
                            fontSize = 9.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // Touch Controls Deck
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .navigationBarsPadding(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left / Right Directional Controls
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    // LEFT BUTTON
                    Surface(
                        color = if (moveLeft) NeonCyan else DarkCard,
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.5.dp, NeonCyan),
                        modifier = Modifier
                            .size(60.dp)
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onPress = {
                                        moveLeft = true
                                        tryAwaitRelease()
                                        moveLeft = false
                                    }
                                )
                            }
                            .testTag("game_btn_left")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Move Left",
                                tint = if (moveLeft) DarkBackground else Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }

                    // RIGHT BUTTON
                    Surface(
                        color = if (moveRight) NeonCyan else DarkCard,
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.5.dp, NeonCyan),
                        modifier = Modifier
                            .size(60.dp)
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onPress = {
                                        moveRight = true
                                        tryAwaitRelease()
                                        moveRight = false
                                    }
                                )
                            }
                            .testTag("game_btn_right")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.ArrowForward,
                                contentDescription = "Move Right",
                                tint = if (moveRight) DarkBackground else Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }

                // Action Controls: DASH & JUMP
                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    // DASH BUTTON
                    Surface(
                        color = DarkSurfaceElevated,
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, NeonPurple),
                        modifier = Modifier
                            .size(56.dp)
                            .clickable { dash() }
                            .testTag("game_btn_dash")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Speed, contentDescription = "Dash", tint = NeonPurple, modifier = Modifier.size(28.dp))
                        }
                    }

                    // JUMP BUTTON (Big primary action)
                    Button(
                        onClick = { jump() },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .size(68.dp)
                            .testTag("game_btn_jump")
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Jump", tint = DarkBackground, modifier = Modifier.size(32.dp))
                            if (userProfile?.hasDoubleJump == true) {
                                Text(text = "2X", color = DarkBackground, fontWeight = FontWeight.Black, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }

        // Stage Cleared Celebration Overlay
        if (isStageCleared) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.8f)),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    color = DarkCard,
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(2.dp, NeonGreen),
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = RobuxGold,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "STAGE $stage CLEARED!",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp
                        )
                        Text(
                            text = "Earned +$coinsCollected R$ Coins!",
                            color = RobuxGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                if (stage < 5) {
                                    stage++
                                    isStageCleared = false
                                } else {
                                    onExitGame()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (stage < 5) "NEXT STAGE" else "VICTORY! BACK TO HUB",
                                color = DarkBackground,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
            }
        }

        // Game Over Overlay
        if (isGameOver) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.8f)),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    color = DarkCard,
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(2.dp, LaserRed),
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "💥 OOF!", color = LaserRed, fontWeight = FontWeight.Black, fontSize = 28.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "You fell into the laser grid!", color = TextSecondary, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                lives = 3
                                isGameOver = false
                                respawnPlayer()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "TRY AGAIN", color = DarkBackground, fontWeight = FontWeight.Black)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedButton(
                            onClick = onExitGame,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "RETURN TO HUB", color = TextSecondary)
                        }
                    }
                }
            }
        }
    }
}

private class RectF(val left: Float, val top: Float, val right: Float, val bottom: Float) {
    fun intersects(other: RectF): Boolean {
        return left < other.right && right > other.left && top < other.bottom && bottom > other.top
    }
}
