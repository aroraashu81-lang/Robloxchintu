package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val username: String = "CyberGamer99",
    val tag: String = "@cyber_nexus_pro",
    val level: Int = 42,
    val isVip: Boolean = true,
    val robuxBalance: Int = 1450,
    val playtimeHours: Int = 480,
    val badgesCount: Int = 184,
    val friendsCount: Int = 142,
    val favoritesCount: Int = 38,
    val currentStreak: Int = 3,
    val lastClaimDateMillis: Long = 0L,
    val hasDoubleJump: Boolean = false,
    val hasVipPass: Boolean = false,
    val equippedOutfit: String = "Cyber-Ninja v2",
    val equippedVisor: String = "Holo-Visor MK-IV",
    val equippedWeapon: String = "Cyber Katana Blade",
    val equippedWings: String = "None"
)

@Entity(tableName = "leaderboards")
data class LeaderboardEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String, // "OBBY_SPEEDRUN", "OBBY_STAGES", "RIVALS_PVP", "ROBUX_RICH"
    val rank: Int,
    val username: String,
    val scoreText: String,
    val numericScore: Long,
    val isCurrentUser: Boolean = false,
    val badge: String = "PRO"
)

@Entity(tableName = "daily_rewards")
data class DailyRewardItem(
    @PrimaryKey val day: Int,
    val title: String,
    val rewardRobux: Int,
    val rewardItemName: String,
    val isClaimed: Boolean = false
)

@Entity(tableName = "avatar_items")
data class AvatarItem(
    @PrimaryKey val id: String,
    val name: String,
    val category: String, // "Outfits", "Hats", "Hair", "Faces", "Weapons", "Wings"
    val priceRobux: Int,
    val rating: Float,
    val isPurchased: Boolean,
    val isEquipped: Boolean,
    val description: String,
    val accentColor: Long = 0xFF00E5FF
)

data class GameExperience(
    val id: String,
    val title: String,
    val studioName: String,
    val ratingPercent: Int,
    val activePlayers: String,
    val totalVisits: String,
    val genre: String,
    val description: String,
    val isTrending: Boolean = false,
    val trendingRank: Int = 1,
    val currentSeason: String = "Season 4",
    val promoCode: String = "CYBERBOOST",
    val promoReward: String = "300 R$ & Neon Glider"
)

data class ChatMessage(
    val id: String,
    val senderName: String,
    val message: String,
    val timeAgo: String,
    val isUser: Boolean,
    val unread: Boolean = false
)

data class FriendStatus(
    val id: String,
    val name: String,
    val isOnline: Boolean,
    val currentActivity: String,
    val isInGame: Boolean,
    val gameTitle: String = ""
)
