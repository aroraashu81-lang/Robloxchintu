package com.example.ui.screens.social

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Send
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
import com.example.data.model.ChatMessage
import com.example.ui.theme.*

@Composable
fun SocialScreen(
    chatMessages: List<ChatMessage>,
    onSendMessage: (String) -> Unit,
    onLaunchGame: () -> Unit
) {
    var searchInput by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All Friends") }
    var activeChatInput by remember { mutableStateOf("") }
    var showChatDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .testTag("social_screen"),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        // Search & QR Scan
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = searchInput,
                    onValueChange = { searchInput = it },
                    placeholder = { Text("Search friends, squads, or chats...", color = TextMuted, fontSize = 13.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = DarkCardBorder,
                        focusedContainerColor = DarkCard,
                        unfocusedContainerColor = DarkCard
                    ),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = { /* QR scan */ },
                    modifier = Modifier
                        .size(48.dp)
                        .background(DarkCard, RoundedCornerShape(14.dp))
                        .border(1.dp, DarkCardBorder, RoundedCornerShape(14.dp))
                ) {
                    Icon(Icons.Default.QrCodeScanner, contentDescription = "Scan QR", tint = NeonCyan)
                }
            }
        }

        // Filter Pills: All Friends (12), Direct Messages (3), Party & Squad
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    Pair("All Friends", "12"),
                    Pair("Direct Messages", "3"),
                    Pair("Party & Squad", "")
                ).forEach { (label, count) ->
                    val isSelected = selectedFilter == label
                    Surface(
                        color = if (isSelected) NeonGreen else DarkCard,
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, if (isSelected) NeonGreen else DarkCardBorder),
                        modifier = Modifier.clickable { selectedFilter = label }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = label,
                                color = if (isSelected) DarkBackground else Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                            if (count.isNotEmpty()) {
                                Surface(
                                    color = if (isSelected) DarkBackground else DarkSurfaceElevated,
                                    shape = CircleShape
                                ) {
                                    Text(
                                        text = count,
                                        color = if (isSelected) NeonGreen else Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Active Voice Squad Card
        item {
            Surface(
                color = DarkCard,
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, NeonGreen.copy(alpha = 0.5f)),
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).background(NeonGreen, CircleShape))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "RIVALS Pro Squad (3/4)", color = Color.White, fontWeight = FontWeight.Black, fontSize = 14.sp)
                        }

                        Surface(color = NeonGreen.copy(alpha = 0.15f), shape = RoundedCornerShape(12.dp)) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.Mic, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(14.dp))
                                Text(text = "Voice Active", color = NeonGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Stacked avatar thumbnails
                        Row {
                            listOf(Color(0xFF00E5FF), Color(0xFFEC4899), Color(0xFFA855F7)).forEachIndexed { idx, col ->
                                Box(
                                    modifier = Modifier
                                        .offset(x = (-idx * 10).dp)
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(col)
                                        .border(2.dp, DarkCard, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = DarkBackground, modifier = Modifier.size(18.dp))
                                }
                            }
                        }

                        Text(text = "Playing RIVALS Ranked", color = TextSecondary, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onLaunchGame,
                            colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = DarkBackground)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Quick Join", color = DarkBackground, fontWeight = FontWeight.Black, fontSize = 12.sp)
                        }

                        OutlinedButton(
                            onClick = { /* invite */ },
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, DarkCardBorder),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.PersonAdd, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Invite Squad", color = Color.White, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Online Friends Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Online Friends (12)", color = Color.White, fontWeight = FontWeight.Black, fontSize = 14.sp)
                    Text(text = "View All", color = NeonCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                val onlineFriends = listOf(
                    Pair("VortexQueen", "Playing RIVALS - Match #42"),
                    Pair("PixelNinja_99", "Exploring Cyber Obby World"),
                    Pair("xX_Shadow_Xx", "Speedrunning Obby Stage 12")
                )

                onlineFriends.forEach { (name, status) ->
                    Surface(
                        color = DarkCard,
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, DarkCardBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
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
                                        .background(NeonPurple.copy(alpha = 0.25f))
                                        .border(1.dp, NeonPurple, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(name.take(2).uppercase(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Box(modifier = Modifier.size(6.dp).background(NeonGreen, CircleShape))
                                    }
                                    Text(text = status, color = TextSecondary, fontSize = 11.sp)
                                }
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                IconButton(
                                    onClick = { showChatDialog = true },
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(DarkSurfaceElevated, CircleShape)
                                ) {
                                    Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = "Chat", tint = Color.White, modifier = Modifier.size(16.dp))
                                }
                                Button(
                                    onClick = onLaunchGame,
                                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                    modifier = Modifier.height(32.dp)
                                ) {
                                    Text(text = "Join", color = DarkBackground, fontWeight = FontWeight.Black, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Direct Messages Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Direct Messages", color = Color.White, fontWeight = FontWeight.Black, fontSize = 14.sp)
                    Surface(color = NeonCyan.copy(alpha = 0.2f), shape = CircleShape) {
                        Text(text = "3 Unread", color = NeonCyan, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                chatMessages.forEach { msg ->
                    Surface(
                        color = DarkCard,
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, if (msg.unread) NeonCyan else DarkCardBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { showChatDialog = true }
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(if (msg.isUser) NeonGreen.copy(alpha = 0.3f) else NeonCyan.copy(alpha = 0.3f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(msg.senderName.take(2).uppercase(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = msg.senderName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(text = msg.timeAgo, color = TextMuted, fontSize = 10.sp)
                                    }
                                    Text(
                                        text = msg.message,
                                        color = if (msg.unread) Color.White else TextSecondary,
                                        fontSize = 11.sp,
                                        maxLines = 1
                                    )
                                }
                            }

                            if (msg.unread) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .background(NeonCyan, CircleShape)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Chat Conversation Modal Dialog
    if (showChatDialog) {
        androidx.compose.ui.window.Dialog(onDismissRequest = { showChatDialog = false }) {
            Surface(
                color = DarkCard,
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.5.dp, NeonCyan.copy(alpha = 0.6f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(480.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Chat header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(DarkSurfaceElevated)
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(modifier = Modifier.size(8.dp).background(NeonGreen, CircleShape))
                            Text(text = "Party Chat: Squad 99", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        IconButton(onClick = { showChatDialog = false }, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                        }
                    }

                    // Messages area
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(chatMessages) { msg ->
                            val isMe = msg.isUser
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
                            ) {
                                Text(
                                    text = msg.senderName,
                                    color = if (isMe) NeonGreen else NeonCyan,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Surface(
                                    color = if (isMe) NeonGreen.copy(alpha = 0.2f) else DarkSurfaceElevated,
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(1.dp, if (isMe) NeonGreen.copy(alpha = 0.5f) else DarkCardBorder)
                                ) {
                                    Text(
                                        text = msg.message,
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Input row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(DarkSurfaceElevated)
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = activeChatInput,
                            onValueChange = { activeChatInput = it },
                            placeholder = { Text("Type message...", color = TextMuted, fontSize = 12.sp) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = NeonCyan,
                                unfocusedBorderColor = DarkCardBorder,
                                focusedContainerColor = DarkBackground,
                                unfocusedContainerColor = DarkBackground
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        )

                        IconButton(
                            onClick = {
                                if (activeChatInput.isNotBlank()) {
                                    onSendMessage(activeChatInput)
                                    activeChatInput = ""
                                }
                            },
                            modifier = Modifier
                                .size(44.dp)
                                .background(NeonGreen, CircleShape)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = DarkBackground)
                        }
                    }
                }
            }
        }
    }
}
