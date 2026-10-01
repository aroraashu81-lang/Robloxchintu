package com.example.data.local

import androidx.room.*
import com.example.data.model.AvatarItem
import com.example.data.model.DailyRewardItem
import com.example.data.model.LeaderboardEntry
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(profile: UserProfile)

    @Query("UPDATE user_profile SET robuxBalance = :newBalance WHERE id = 1")
    suspend fun updateRobux(newBalance: Int)

    @Query("UPDATE user_profile SET currentStreak = :newStreak, lastClaimDateMillis = :claimTime WHERE id = 1")
    suspend fun updateDailyStreak(newStreak: Int, claimTime: Long)

    @Query("UPDATE user_profile SET hasDoubleJump = :enabled WHERE id = 1")
    suspend fun setDoubleJump(enabled: Boolean)

    @Query("UPDATE user_profile SET hasVipPass = :enabled WHERE id = 1")
    suspend fun setVipPass(enabled: Boolean)

    @Query("UPDATE user_profile SET equippedOutfit = :outfit, equippedVisor = :visor, equippedWeapon = :weapon, equippedWings = :wings WHERE id = 1")
    suspend fun updateEquipped(outfit: String, visor: String, weapon: String, wings: String)
}

@Dao
interface LeaderboardDao {
    @Query("SELECT * FROM leaderboards WHERE category = :category ORDER BY rank ASC")
    fun getLeaderboard(category: String): Flow<List<LeaderboardEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entries: List<LeaderboardEntry>)

    @Query("SELECT COUNT(*) FROM leaderboards")
    suspend fun getCount(): Int

    @Query("UPDATE leaderboards SET scoreText = :scoreText, numericScore = :numericScore WHERE category = :category AND isCurrentUser = 1")
    suspend fun updateCurrentUserScore(category: String, scoreText: String, numericScore: Long)
}

@Dao
interface DailyRewardDao {
    @Query("SELECT * FROM daily_rewards ORDER BY day ASC")
    fun getAllDailyRewards(): Flow<List<DailyRewardItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(rewards: List<DailyRewardItem>)

    @Query("SELECT COUNT(*) FROM daily_rewards")
    suspend fun getCount(): Int

    @Query("UPDATE daily_rewards SET isClaimed = 1 WHERE day = :day")
    suspend fun markClaimed(day: Int)

    @Query("UPDATE daily_rewards SET isClaimed = 0")
    suspend fun resetAll()
}

@Dao
interface AvatarItemDao {
    @Query("SELECT * FROM avatar_items ORDER BY priceRobux ASC")
    fun getAllItems(): Flow<List<AvatarItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<AvatarItem>)

    @Query("SELECT COUNT(*) FROM avatar_items")
    suspend fun getCount(): Int

    @Query("UPDATE avatar_items SET isPurchased = 1 WHERE id = :id")
    suspend fun markPurchased(id: String)

    @Query("UPDATE avatar_items SET isEquipped = 0 WHERE category = :category")
    suspend fun unequipCategory(category: String)

    @Query("UPDATE avatar_items SET isEquipped = 1 WHERE id = :id")
    suspend fun equipItem(id: String)
}
