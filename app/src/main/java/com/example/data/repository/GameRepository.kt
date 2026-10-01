package com.example.data.repository

import com.example.data.local.AvatarItemDao
import com.example.data.local.DailyRewardDao
import com.example.data.local.LeaderboardDao
import com.example.data.local.UserDao
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.util.concurrent.TimeUnit

class GameRepository(
    private val userDao: UserDao,
    private val leaderboardDao: LeaderboardDao,
    private val dailyRewardDao: DailyRewardDao,
    private val avatarItemDao: AvatarItemDao
) {
    val userProfile: Flow<UserProfile?> = userDao.getUserProfile()
    val dailyRewards: Flow<List<DailyRewardItem>> = dailyRewardDao.getAllDailyRewards()
    val avatarItems: Flow<List<AvatarItem>> = avatarItemDao.getAllItems()

    fun getLeaderboard(category: String): Flow<List<LeaderboardEntry>> {
        return leaderboardDao.getLeaderboard(category)
    }

    suspend fun initializeDefaultsIfNeeded() {
        // Init User Profile
        val currentProfile = userDao.getUserProfile().firstOrNull()
        if (currentProfile == null) {
            userDao.insertOrUpdate(
                UserProfile(
                    id = 1,
                    username = "CyberGamer99",
                    tag = "@cyber_nexus_pro",
                    level = 42,
                    isVip = true,
                    robuxBalance = 1450,
                    playtimeHours = 480,
                    badgesCount = 184,
                    friendsCount = 142,
                    favoritesCount = 38,
                    currentStreak = 3,
                    lastClaimDateMillis = System.currentTimeMillis() - TimeUnit.HOURS.toMillis(25), // Eligible to claim today
                    hasDoubleJump = false,
                    hasVipPass = false,
                    equippedOutfit = "Cyber-Ninja v2",
                    equippedVisor = "Holo-Visor MK-IV",
                    equippedWeapon = "Cyber Katana Blade",
                    equippedWings = "None"
                )
            )
        }

        // Init Daily Rewards
        if (dailyRewardDao.getCount() == 0) {
            val rewards = listOf(
                DailyRewardItem(1, "Day 1 Starter", 50, "50 R$ Coins", isClaimed = true),
                DailyRewardItem(2, "Day 2 Visor", 100, "Holo-Visor MK-IV", isClaimed = true),
                DailyRewardItem(3, "Day 3 Stash", 150, "150 R$ Coins", isClaimed = false),
                DailyRewardItem(4, "Day 4 Boost", 200, "Neon Energy Drink", isClaimed = false),
                DailyRewardItem(5, "Day 5 Mega", 300, "300 R$ Coins", isClaimed = false),
                DailyRewardItem(6, "Day 6 Gear", 400, "Double Jump Boots", isClaimed = false),
                DailyRewardItem(7, "Day 7 Crown", 1000, "Legendary Cyber Crown + 1,000 R$", isClaimed = false)
            )
            dailyRewardDao.insertAll(rewards)
        }

        // Init Leaderboards
        if (leaderboardDao.getCount() == 0) {
            val defaultEntries = mutableListOf<LeaderboardEntry>()

            // Obby Speedrun
            defaultEntries.add(LeaderboardEntry(category = "OBBY_SPEEDRUN", rank = 1, username = "xX_Shadow_Xx", scoreText = "01:14.22", numericScore = 74220, badge = "CHAMPION"))
            defaultEntries.add(LeaderboardEntry(category = "OBBY_SPEEDRUN", rank = 2, username = "ValkyrieQueen", scoreText = "01:21.05", numericScore = 81050, badge = "ELITE"))
            defaultEntries.add(LeaderboardEntry(category = "OBBY_SPEEDRUN", rank = 3, username = "NeonPulse", scoreText = "01:26.80", numericScore = 86800, badge = "MASTER"))
            defaultEntries.add(LeaderboardEntry(category = "OBBY_SPEEDRUN", rank = 4, username = "CyberGamer99", scoreText = "01:34.50", numericScore = 94500, isCurrentUser = true, badge = "PRO"))
            defaultEntries.add(LeaderboardEntry(category = "OBBY_SPEEDRUN", rank = 5, username = "PixelNinja_99", scoreText = "01:42.10", numericScore = 102100, badge = "PRO"))
            defaultEntries.add(LeaderboardEntry(category = "OBBY_SPEEDRUN", rank = 6, username = "ApexVelocity", scoreText = "01:48.33", numericScore = 108330, badge = "VET"))
            defaultEntries.add(LeaderboardEntry(category = "OBBY_SPEEDRUN", rank = 7, username = "GlitchMaster", scoreText = "01:55.90", numericScore = 115900, badge = "VET"))
            defaultEntries.add(LeaderboardEntry(category = "OBBY_SPEEDRUN", rank = 8, username = "VortexQueen", scoreText = "02:04.12", numericScore = 124120, badge = "RISING"))

            // Obby Stages
            defaultEntries.add(LeaderboardEntry(category = "OBBY_STAGES", rank = 1, username = "xX_Shadow_Xx", scoreText = "Stage 30 (Max)", numericScore = 30, badge = "MASTER"))
            defaultEntries.add(LeaderboardEntry(category = "OBBY_STAGES", rank = 2, username = "ValkyrieQueen", scoreText = "Stage 28", numericScore = 28, badge = "ELITE"))
            defaultEntries.add(LeaderboardEntry(category = "OBBY_STAGES", rank = 3, username = "NeonPulse", scoreText = "Stage 25", numericScore = 25, badge = "PRO"))
            defaultEntries.add(LeaderboardEntry(category = "OBBY_STAGES", rank = 4, username = "CyberGamer99", scoreText = "Stage 12", numericScore = 12, isCurrentUser = true, badge = "PRO"))
            defaultEntries.add(LeaderboardEntry(category = "OBBY_STAGES", rank = 5, username = "PixelNinja_99", scoreText = "Stage 10", numericScore = 10, badge = "PRO"))

            // Rivals PvP
            defaultEntries.add(LeaderboardEntry(category = "RIVALS_PVP", rank = 1, username = "GlitchMaster", scoreText = "3,420 ELO", numericScore = 3420, badge = "GRANDMASTER"))
            defaultEntries.add(LeaderboardEntry(category = "RIVALS_PVP", rank = 2, username = "VortexQueen", scoreText = "3,180 ELO", numericScore = 3180, badge = "MASTER"))
            defaultEntries.add(LeaderboardEntry(category = "RIVALS_PVP", rank = 3, username = "CyberGamer99", scoreText = "2,850 ELO", numericScore = 2850, isCurrentUser = true, badge = "DIAMOND"))
            defaultEntries.add(LeaderboardEntry(category = "RIVALS_PVP", rank = 4, username = "xX_Shadow_Xx", scoreText = "2,740 ELO", numericScore = 2740, badge = "PLATINUM"))

            // Robux Tycoons
            defaultEntries.add(LeaderboardEntry(category = "ROBUX_RICH", rank = 1, username = "RoAnime_Owner", scoreText = "850,000 R$", numericScore = 850000, badge = "DEV GOD"))
            defaultEntries.add(LeaderboardEntry(category = "ROBUX_RICH", rank = 2, username = "Wolfpaq_Dev", scoreText = "640,000 R$", numericScore = 640000, badge = "TYCOON"))
            defaultEntries.add(LeaderboardEntry(category = "ROBUX_RICH", rank = 3, username = "Axel_Games", scoreText = "490,000 R$", numericScore = 490000, badge = "CREATOR"))
            defaultEntries.add(LeaderboardEntry(category = "ROBUX_RICH", rank = 4, username = "CyberGamer99", scoreText = "1,450 R$", numericScore = 1450, isCurrentUser = true, badge = "VIP"))

            leaderboardDao.insertAll(defaultEntries)
        }

        // Init Avatar Items
        if (avatarItemDao.getCount() == 0) {
            val items = listOf(
                AvatarItem("item_1", "Cyber-Ninja v2", "Outfits", 0, 4.9f, isPurchased = true, isEquipped = true, "High-tech armored suit with pulsing neon veins", 0xFF00E5FF),
                AvatarItem("item_2", "Neon Street", "Outfits", 0, 4.8f, isPurchased = true, isEquipped = false, "Casual glowing cyber street hoodie", 0xFFA855F7),
                AvatarItem("item_3", "Golden Mech", "Outfits", 2200, 5.0f, isPurchased = false, isEquipped = false, "Imperial golden cybernetic exosuit", 0xFFFFB800),
                AvatarItem("item_4", "Skater Boi", "Outfits", 150, 4.6f, isPurchased = false, isEquipped = false, "Voxel street skater with orange jacket", 0xFFFF9800),
                AvatarItem("item_5", "Cyber Katana Blade", "Weapons", 350, 4.9f, isPurchased = true, isEquipped = true, "High-frequency plasma energized katana", 0xFF00E5FF),
                AvatarItem("item_6", "Holo-Visor MK-IV", "Hats", 250, 4.8f, isPurchased = true, isEquipped = true, "Tactical holographic HUD visor with targeting", 0xFF00E676),
                AvatarItem("item_7", "Quantum Neon Wings", "Wings", 850, 5.0f, isPurchased = false, isEquipped = false, "Wings woven from radiant photon light", 0xFFEC4899),
                AvatarItem("item_8", "Apex Pauldrons", "Hats", 180, 4.7f, isPurchased = true, isEquipped = false, "Spiked energy-charged cyber shoulder guards", 0xFF00E676),
                AvatarItem("item_9", "Legendary Cyber Crown", "Hats", 1000, 5.0f, isPurchased = false, isEquipped = false, "Exclusive crown forged from rare neon diamonds", 0xFFFFD700),
                AvatarItem("item_10", "Matrix Cyber Hair", "Hair", 120, 4.7f, isPurchased = false, isEquipped = false, "Spiky anime hairstyle with glowing green tips", 0xFF00E676),
                AvatarItem("item_11", "Smirk Cyber Visage", "Faces", 80, 4.5f, isPurchased = false, isEquipped = false, "Confident cybernetic smile with ocular glow", 0xFF2979FF)
            )
            avatarItemDao.insertAll(items)
        }
    }

    suspend fun claimDailyReward(day: Int): String {
        val profile = userDao.getUserProfile().firstOrNull() ?: return "Error: Profile not found"
        dailyRewardDao.markClaimed(day)

        var bonusRobux = 50
        var rewardName = "Robux"

        when (day) {
            1 -> { bonusRobux = 50; rewardName = "50 R$" }
            2 -> { bonusRobux = 100; rewardName = "Holo-Visor MK-IV + 100 R$" }
            3 -> { bonusRobux = 150; rewardName = "150 R$" }
            4 -> { bonusRobux = 200; rewardName = "Neon Energy Speed Boost + 200 R$" }
            5 -> { bonusRobux = 300; rewardName = "300 R$" }
            6 -> {
                bonusRobux = 400
                rewardName = "Double Jump Boots + 400 R$"
                userDao.setDoubleJump(true)
            }
            7 -> {
                bonusRobux = 1000
                rewardName = "Legendary Cyber Crown + 1,000 R$!"
                avatarItemDao.markPurchased("item_9")
            }
        }

        val newBalance = profile.robuxBalance + bonusRobux
        userDao.updateRobux(newBalance)
        userDao.updateDailyStreak(day, System.currentTimeMillis())

        // Also update leaderboard score for ROBUX_RICH
        leaderboardDao.updateCurrentUserScore(
            "ROBUX_RICH",
            "%,d R$".format(newBalance),
            newBalance.toLong()
        )

        return rewardName
    }

    suspend fun purchaseAvatarItem(item: AvatarItem): Boolean {
        val profile = userDao.getUserProfile().firstOrNull() ?: return false
        if (profile.robuxBalance < item.priceRobux) return false

        val newBalance = profile.robuxBalance - item.priceRobux
        userDao.updateRobux(newBalance)
        avatarItemDao.markPurchased(item.id)
        equipAvatarItem(item)

        // update leaderboard
        leaderboardDao.updateCurrentUserScore(
            "ROBUX_RICH",
            "%,d R$".format(newBalance),
            newBalance.toLong()
        )
        return true
    }

    suspend fun equipAvatarItem(item: AvatarItem) {
        val profile = userDao.getUserProfile().firstOrNull() ?: return
        avatarItemDao.unequipCategory(item.category)
        avatarItemDao.equipItem(item.id)

        when (item.category) {
            "Outfits" -> userDao.updateEquipped(item.name, profile.equippedVisor, profile.equippedWeapon, profile.equippedWings)
            "Hats" -> userDao.updateEquipped(profile.equippedOutfit, item.name, profile.equippedWeapon, profile.equippedWings)
            "Weapons" -> userDao.updateEquipped(profile.equippedOutfit, profile.equippedVisor, item.name, profile.equippedWings)
            "Wings" -> userDao.updateEquipped(profile.equippedOutfit, profile.equippedVisor, profile.equippedWeapon, item.name)
        }
    }

    suspend fun buyGamePass(type: String, cost: Int): Boolean {
        val profile = userDao.getUserProfile().firstOrNull() ?: return false
        if (profile.robuxBalance < cost) return false

        val newBalance = profile.robuxBalance - cost
        userDao.updateRobux(newBalance)

        if (type == "VIP") {
            userDao.setVipPass(true)
        } else if (type == "DOUBLE_JUMP") {
            userDao.setDoubleJump(true)
        }

        leaderboardDao.updateCurrentUserScore("ROBUX_RICH", "%,d R$".format(newBalance), newBalance.toLong())
        return true
    }

    suspend fun redeemPromoCode(code: String): Pair<Boolean, String> {
        val cleanCode = code.trim().uppercase()
        val profile = userDao.getUserProfile().firstOrNull() ?: return false to "Error"

        return when (cleanCode) {
            "CYBERBOOST" -> {
                val newBal = profile.robuxBalance + 300
                userDao.updateRobux(newBal)
                userDao.setDoubleJump(true)
                leaderboardDao.updateCurrentUserScore("ROBUX_RICH", "%,d R$".format(newBal), newBal.toLong())
                true to "Redeemed! +300 R$ and Double Jump Boots unlocked!"
            }
            "ROBLOXIA2026" -> {
                val newBal = profile.robuxBalance + 500
                userDao.updateRobux(newBal)
                leaderboardDao.updateCurrentUserScore("ROBUX_RICH", "%,d R$".format(newBal), newBal.toLong())
                true to "Redeemed! +500 R$ added to your wallet!"
            }
            "CHINTU" -> {
                val newBal = profile.robuxBalance + 250
                userDao.updateRobux(newBal)
                leaderboardDao.updateCurrentUserScore("ROBUX_RICH", "%,d R$".format(newBal), newBal.toLong())
                true to "Special Developer Code Redeemed! +250 R$!"
            }
            else -> false to "Invalid promo code. Try CYBERBOOST or ROBLOXIA2026!"
        }
    }

    suspend fun updateObbyCompleted(stage: Int, timeMs: Long, coins: Int) {
        val profile = userDao.getUserProfile().firstOrNull() ?: return
        val newBal = profile.robuxBalance + coins
        userDao.updateRobux(newBal)

        val seconds = (timeMs / 1000) % 60
        val minutes = (timeMs / 1000) / 60
        val millis = (timeMs % 1000) / 10
        val formattedTime = "%02d:%02d.%02d".format(minutes, seconds, millis)

        // Update leaderboard entry
        leaderboardDao.updateCurrentUserScore("OBBY_SPEEDRUN", formattedTime, timeMs)
        leaderboardDao.updateCurrentUserScore("OBBY_STAGES", "Stage $stage (Cleared!)", stage.toLong())
        leaderboardDao.updateCurrentUserScore("ROBUX_RICH", "%,d R$".format(newBal), newBal.toLong())
    }

    suspend fun claimRankReward(rewardAmount: Int): Int {
        val profile = userDao.getUserProfile().firstOrNull() ?: return 0
        val newBal = profile.robuxBalance + rewardAmount
        userDao.updateRobux(newBal)
        leaderboardDao.updateCurrentUserScore("ROBUX_RICH", "%,d R$".format(newBal), newBal.toLong())
        return newBal
    }
}
