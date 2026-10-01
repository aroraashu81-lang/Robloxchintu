package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.*
import com.example.data.repository.GameRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
enum class ScreenDestination {
    HOME,
    DISCOVER,
    AVATAR,
    LEADERBOARD,
    CHAT,
    EXPERIENCE_DETAILS,
    PLAYABLE_GAME,
    CREATOR_STUDIO,
    PROFILE
}

class RobloxiachintuViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: GameRepository

    init {
        val db = AppDatabase.getDatabase(application)
        repository = GameRepository(
            userDao = db.userDao(),
            leaderboardDao = db.leaderboardDao(),
            dailyRewardDao = db.dailyRewardDao(),
            avatarItemDao = db.avatarItemDao()
        )
        viewModelScope.launch {
            repository.initializeDefaultsIfNeeded()
        }
    }

    val userProfile: StateFlow<UserProfile?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val dailyRewards: StateFlow<List<DailyRewardItem>> = repository.dailyRewards
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val avatarItems: StateFlow<List<AvatarItem>> = repository.avatarItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedLeaderboardCategory = MutableStateFlow("OBBY_SPEEDRUN")
    val selectedLeaderboardCategory: StateFlow<String> = _selectedLeaderboardCategory.asStateFlow()

    private val _selectedLeaderboardTimeframe = MutableStateFlow("All Time")
    val selectedLeaderboardTimeframe: StateFlow<String> = _selectedLeaderboardTimeframe.asStateFlow()

    val currentLeaderboard: StateFlow<List<LeaderboardEntry>> = _selectedLeaderboardCategory
        .flatMapLatest { category -> repository.getLeaderboard(category) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentScreen = MutableStateFlow(ScreenDestination.HOME)
    val currentScreen: StateFlow<ScreenDestination> = _currentScreen.asStateFlow()

    private val _selectedExperience = MutableStateFlow("cyber_obby")
    val selectedExperience: StateFlow<String> = _selectedExperience.asStateFlow()

    private val _showDailyRewardsDialog = MutableStateFlow(false)
    val showDailyRewardsDialog: StateFlow<Boolean> = _showDailyRewardsDialog.asStateFlow()

    private val _showPromoDialog = MutableStateFlow(false)
    val showPromoDialog: StateFlow<Boolean> = _showPromoDialog.asStateFlow()

    private val _showRobuxDialog = MutableStateFlow(false)
    val showRobuxDialog: StateFlow<Boolean> = _showRobuxDialog.asStateFlow()

    private val _bannerNotification = MutableStateFlow<String?>(null)
    val bannerNotification: StateFlow<String?> = _bannerNotification.asStateFlow()

    private val _chatMessages = MutableStateFlow(
        listOf(
            ChatMessage("1", "VortexQueen", "Hey CyberGamer! Ready for RIVALS ranked match?", "10m ago", isUser = false),
            ChatMessage("2", "CyberGamer99", "Just warming up in Cyber Obby Escape!", "8m ago", isUser = true),
            ChatMessage("3", "VortexQueen", "Awesome, let's squad up once you finish Stage 5!", "5m ago", isUser = false),
            ChatMessage("4", "NeonPulse", "Let's run Cyber Obby at 8 PM! Got a new shortcut!", "2m ago", isUser = false, unread = true)
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    fun navigateTo(screen: ScreenDestination) {
        _currentScreen.value = screen
    }

    fun openExperienceDetails(expId: String) {
        _selectedExperience.value = expId
        _currentScreen.value = ScreenDestination.EXPERIENCE_DETAILS
    }

    fun launchPlayableGame() {
        _currentScreen.value = ScreenDestination.PLAYABLE_GAME
    }

    fun setLeaderboardCategory(category: String) {
        _selectedLeaderboardCategory.value = category
    }

    fun setLeaderboardTimeframe(timeframe: String) {
        _selectedLeaderboardTimeframe.value = timeframe
    }

    fun setDailyRewardsDialog(show: Boolean) {
        _showDailyRewardsDialog.value = show
    }

    fun setPromoDialog(show: Boolean) {
        _showPromoDialog.value = show
    }

    fun setRobuxDialog(show: Boolean) {
        _showRobuxDialog.value = show
    }

    fun clearNotification() {
        _bannerNotification.value = null
    }

    fun claimDailyReward(day: Int) {
        viewModelScope.launch {
            val rewardName = repository.claimDailyReward(day)
            _bannerNotification.value = "🎉 Claimed Day $day Reward: $rewardName"
        }
    }

    fun purchaseAvatarItem(item: AvatarItem) {
        viewModelScope.launch {
            val success = repository.purchaseAvatarItem(item)
            if (success) {
                _bannerNotification.value = "✨ Purchased & Equipped ${item.name}!"
            } else {
                _bannerNotification.value = "⚠️ Not enough Robux to purchase ${item.name}."
            }
        }
    }

    fun equipAvatarItem(item: AvatarItem) {
        viewModelScope.launch {
            repository.equipAvatarItem(item)
            _bannerNotification.value = "👔 Equipped ${item.name}!"
        }
    }

    fun buyGamePass(type: String, cost: Int, name: String) {
        viewModelScope.launch {
            val success = repository.buyGamePass(type, cost)
            if (success) {
                _bannerNotification.value = "🚀 Purchased $name successfully! In-game perks enabled!"
            } else {
                _bannerNotification.value = "⚠️ Insufficient Robux for $name."
            }
        }
    }

    fun redeemPromo(code: String) {
        viewModelScope.launch {
            val (success, msg) = repository.redeemPromoCode(code)
            _bannerNotification.value = msg
            if (success) {
                _showPromoDialog.value = false
            }
        }
    }

    fun claimRankReward(rewardAmount: Int) {
        viewModelScope.launch {
            repository.claimRankReward(rewardAmount)
            _bannerNotification.value = "🏆 Leaderboard Rank Reward Claimed! +$rewardAmount R$!"
        }
    }

    fun addRobux(amount: Int) {
        viewModelScope.launch {
            val profile = userProfile.value ?: return@launch
            val newBal = profile.robuxBalance + amount
            val db = AppDatabase.getDatabase(getApplication())
            db.userDao().updateRobux(newBal)
            _bannerNotification.value = "💰 Added +$amount R$ to wallet! New Balance: %,d R$".format(newBal)
            _showRobuxDialog.value = false
        }
    }

    fun onObbyCompleted(stage: Int, timeMs: Long, coins: Int) {
        viewModelScope.launch {
            repository.updateObbyCompleted(stage, timeMs, coins)
            _bannerNotification.value = "🌟 Obby Stage $stage Cleared in ${(timeMs / 1000f)}s! Earned +$coins R$!"
        }
    }

    fun sendChatMessage(text: String) {
        if (text.isBlank()) return
        val newMsg = ChatMessage(
            id = System.currentTimeMillis().toString(),
            senderName = "CyberGamer99",
            message = text.trim(),
            timeAgo = "Just now",
            isUser = true
        )
        _chatMessages.value = _chatMessages.value + newMsg
    }
}
