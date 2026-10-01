package com.example.ui.screens.studio

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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun CreatorStudioScreen(
    onBack: () -> Unit,
    onTestPlay: () -> Unit
) {
    var selectedStudioTab by remember { mutableStateOf("Dashboard") }
    var selectedScriptTab by remember { mutableStateOf("CombatSystem.server.lua") }

    var luaCode by remember {
        mutableStateOf(
            """local Players = game:GetService("Players")
local ReplicatedStorage = game:GetService("ReplicatedStorage")
local CombatEvents = ReplicatedStorage:WaitForChild("CombatEvents")

local function onPlayerHit(player, targetPart, damage)
    if not targetPart or not targetPart.Parent then return end
    local humanoid = targetPart.Parent:FindFirstChild("Humanoid")
    if humanoid and humanoid.Health > 0 then
        humanoid:TakeDamage(damage)
        print("[Combat]: " .. player.Name .. " dealt " .. damage .. " damage!")
        
        CombatEvents.ClientUpdate:FireAllClients({
            Attacker = player.Name,
            Target = targetPart.Parent.Name,
            Damage = damage,
            Timestamp = os.time()
        })
    end
end

CombatEvents.RegisterHit.OnServerEvent:Connect(onPlayerHit)

task.spawn(function()
    while true do
        task.wait(5)
        -- Garbage collection cycle for arena
    end
end)"""
        )
    }

    var consoleLogs by remember {
        mutableStateOf(
            listOf(
                "[00:12:44] [CLIENT] Client viewport initialized at 120Hz native refresh rate.",
                "[00:12:45] [SERVER] Weapon damage replicated successfully across 16 active nodes.",
                "[00:12:46] [NETWORK] Input latency stable at 14ms (Packet loss: 0.0%)."
            )
        )
    }

    fun runScript() {
        consoleLogs = consoleLogs + listOf(
            "[00:13:01] [RUN] Executing $selectedScriptTab...",
            "[00:13:01] [SERVER] CombatSystem verified: 0 syntax errors, 1 event listener active.",
            "[00:13:02] [COMBAT]: CyberGamer99 dealt 45 damage to TestDummy!"
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .testTag("creator_studio_screen"),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        // Studio Top Bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Text(
                        text = "Nexus Creator Studio",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 17.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(Icons.Default.Verified, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(16.dp))
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(onClick = { /* settings */ }) {
                        Icon(Icons.Default.Settings, contentDescription = null, tint = TextSecondary)
                    }
                }
            }
        }

        // Active Place selector
        item {
            Surface(
                color = DarkCard,
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, DarkCardBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
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
                                .background(NeonCyan.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.SportsEsports, contentDescription = null, tint = NeonCyan)
                        }
                        Column {
                            Text(text = "Active Place", color = TextSecondary, fontSize = 10.sp)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "RIVALS: Red vs Blue", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(modifier = Modifier.size(6.dp).background(NeonGreen, CircleShape))
                            }
                        }
                    }

                    Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = TextSecondary)
                }
            }
        }

        // Quick Studio Action Buttons
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { /* publish */ },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.CloudUpload, contentDescription = null, tint = DarkBackground, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Publish", color = DarkBackground, fontWeight = FontWeight.Black, fontSize = 12.sp)
                }

                Button(
                    onClick = { selectedStudioTab = "Dashboard" },
                    colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceElevated),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Analytics, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Analytics", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                Button(
                    onClick = onTestPlay,
                    colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceElevated),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Test Play", color = NeonCyan, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }

        // Tab Selector: Dashboard vs Lua Scripting
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .background(DarkCard, RoundedCornerShape(12.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                listOf("Dashboard", "Lua Scripting").forEach { tab ->
                    val isSelected = selectedStudioTab == tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) NeonGreen else Color.Transparent)
                            .clickable { selectedStudioTab = tab }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tab,
                            color = if (isSelected) DarkBackground else Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        if (selectedStudioTab == "Dashboard") {
            // Live Performance Cards
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(text = "Live Performance • Real-time", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        StudioMetricCard("Active CCU", "125,400", "↗ +12.4% vs peak", NeonCyan, Modifier.weight(1f))
                        StudioMetricCard("Today's Revenue", "14,250 R$", "↗ +8.1% today", RobuxGold, Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        StudioMetricCard("Total Visits", "12.4M", "All-time record", NeonGreen, Modifier.weight(1f))
                        StudioMetricCard("30-Day Retention", "34.8%", "↗ +2.3%", NeonPurple, Modifier.weight(1f))
                    }
                }
            }

            // 24-Hour Traffic Stream Chart
            item {
                Surface(
                    color = DarkCard,
                    shape = RoundedCornerShape(18.dp),
                    border = BorderStroke(1.dp, DarkCardBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "24-Hour Traffic Stream", color = Color.White, fontWeight = FontWeight.Black, fontSize = 14.sp)
                                Text(text = "Hourly CCU Distribution", color = TextSecondary, fontSize = 11.sp)
                            }
                            Surface(color = NeonGreen.copy(alpha = 0.15f), shape = RoundedCornerShape(6.dp)) {
                                Text(text = "Peak: 142.1k", color = NeonGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Custom Canvas Bezier Traffic Curve
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val w = size.width
                                val h = size.height

                                // Draw gradient area under curve
                                val path = Path().apply {
                                    moveTo(0f, h * 0.7f)
                                    cubicTo(w * 0.25f, h * 0.65f, w * 0.45f, h * 0.8f, w * 0.55f, h * 0.15f)
                                    cubicTo(w * 0.7f, h * 0.2f, w * 0.85f, h * 0.45f, w, h * 0.5f)
                                    lineTo(w, h)
                                    lineTo(0f, h)
                                    close()
                                }
                                drawPath(
                                    path = path,
                                    brush = Brush.verticalGradient(
                                        listOf(NeonGreen.copy(alpha = 0.35f), Color.Transparent)
                                    )
                                )

                                // Stroke curve
                                val strokePath = Path().apply {
                                    moveTo(0f, h * 0.7f)
                                    cubicTo(w * 0.25f, h * 0.65f, w * 0.45f, h * 0.8f, w * 0.55f, h * 0.15f)
                                    cubicTo(w * 0.7f, h * 0.2f, w * 0.85f, h * 0.45f, w, h * 0.5f)
                                }
                                drawPath(path = strokePath, color = NeonGreen, style = Stroke(width = 4f))

                                // Peak dot
                                drawCircle(color = Color.White, radius = 6f, center = Offset(w * 0.55f, h * 0.15f))
                                drawCircle(color = NeonGreen, radius = 3f, center = Offset(w * 0.55f, h * 0.15f))
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "00:00", color = TextMuted, fontSize = 9.sp)
                            Text(text = "06:00", color = TextMuted, fontSize = 9.sp)
                            Text(text = "12:00", color = TextMuted, fontSize = 9.sp)
                            Text(text = "18:00", color = TextMuted, fontSize = 9.sp)
                            Text(text = "Now", color = NeonGreen, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Monetization breakdown
            item {
                Surface(
                    color = DarkCard,
                    shape = RoundedCornerShape(18.dp),
                    border = BorderStroke(1.dp, DarkCardBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Monetization (Last 30 Days)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(10.dp))

                        // Colored segmented bar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(RoundedCornerShape(5.dp))
                        ) {
                            Box(modifier = Modifier.weight(0.62f).fillMaxHeight().background(NeonGreen))
                            Box(modifier = Modifier.weight(0.28f).fillMaxHeight().background(NeonCyan))
                            Box(modifier = Modifier.weight(0.10f).fillMaxHeight().background(RobuxGold))
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "• Gamepasses: 62% (8,835 R$)", color = NeonGreen, fontSize = 11.sp)
                            Text(text = "• Dev Products: 28%", color = NeonCyan, fontSize = 11.sp)
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            color = DarkSurfaceElevated,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = RobuxGold)
                                    Column {
                                        Text(text = "Next DevEx Payout", color = TextSecondary, fontSize = 10.sp)
                                        Text(text = "45,000 R$", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    }
                                }
                                Surface(color = NeonGreen.copy(alpha = 0.2f), shape = RoundedCornerShape(6.dp)) {
                                    Text(text = "In 4 Days", color = NeonGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Interactive Lua Scripting Editor (Image 8)
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    // Script Tabs
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("CombatSystem.server.lua", "Main.client.lua").forEach { tab ->
                            val isSel = selectedScriptTab == tab
                            Surface(
                                color = if (isSel) NeonGreen.copy(alpha = 0.2f) else DarkCard,
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, if (isSel) NeonGreen else DarkCardBorder),
                                modifier = Modifier.clickable { selectedScriptTab = tab }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(Icons.Default.Code, contentDescription = null, tint = if (isSel) NeonGreen else TextSecondary, modifier = Modifier.size(14.dp))
                                    Text(text = tab, color = if (isSel) NeonGreen else TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Snippet shortcuts & Run button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("print()", "game:GetService()", "Connect()").forEach { snip ->
                                Surface(
                                    color = DarkCard,
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.clickable { luaCode += "\n$snip" }
                                ) {
                                    Text(text = snip, color = NeonCyan, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp))
                                }
                            }
                        }

                        Button(
                            onClick = { runScript() },
                            colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier.height(30.dp)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = DarkBackground, modifier = Modifier.size(16.dp))
                            Text(text = "Run", color = DarkBackground, fontWeight = FontWeight.Black, fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Code Editor area
                    OutlinedTextField(
                        value = luaCode,
                        onValueChange = { luaCode = it },
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            color = Color(0xFF67E8F9),
                            lineHeight = 16.sp
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF090D14),
                            unfocusedContainerColor = Color(0xFF090D14),
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = DarkCardBorder
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Console Diagnostics Log
                    Surface(
                        color = Color(0xFF090D14),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, DarkCardBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "CONSOLE DIAGNOSTICS", color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Text(text = "14ms Ping", color = NeonGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            consoleLogs.takeLast(4).forEach { log ->
                                Text(
                                    text = log,
                                    color = if (log.contains("RUN")) NeonGreen else if (log.contains("SERVER")) NeonCyan else TextSecondary,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StudioMetricCard(title: String, value: String, subtitle: String, tint: Color, modifier: Modifier = Modifier) {
    Surface(
        color = DarkCard,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, DarkCardBorder),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = title, color = TextSecondary, fontSize = 11.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, color = Color.White, fontWeight = FontWeight.Black, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = subtitle, color = tint, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
}
