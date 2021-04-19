package com.averniko.messager.conversationlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.averniko.messager.data.conversations.ConversationsRepository
import com.averniko.messager.data.model.Conversation

class ConversationListViewModel(private val conversationsRepository: ConversationsRepository) : ViewModel() {

    val conversations = conversationsRepository.getAllConversations()

//
//    val startButtonVisible = Transformations.map(currentConversation) { tonight ->
//        tonight == null
//    }
//    val stopButtonVisible = Transformations.map(currentConversation) { tonight ->
//        tonight != null
//    }
//    val clearButtonVisible = Transformations.map(conversations) { nights ->
//        nights != null && nights.isNotEmpty()
//    }
//
//    init {
//        initializeTonight()
//    }
//
//    private fun initializeTonight() {
//        uiScope.launch {
//            currentConversation.value = getTonightFromDatabase()
//        }
//    }
//
//    fun doneNavigating() {
//        _navigateToSleepQuality.value = null
//    }
//
//    private suspend fun getTonightFromDatabase(): SleepNight? {
//        return withContext(Dispatchers.IO) {
//            var night = dao.getTonight()
//            if (night?.endTimeMillis != night?.startTimeMillis) {
//                night = null
//            }
//            night
//        }
//    }
//
//    fun onStartTracking() {
//        uiScope.launch {
//            val newNight = SleepNight()
//            insert(newNight)
//            currentConversation.value = getTonightFromDatabase()
//        }
//    }
//
//    private suspend fun insert(night: SleepNight) {
//        withContext(Dispatchers.IO) {
//            dao.insert(night)
//        }
//    }
//
//
//    private suspend fun update(night: SleepNight) {
//        withContext(Dispatchers.IO) {
//            dao.update(night)
//        }
//    }
//
//    fun onClear() {
//        uiScope.launch {
//            clear()
//            currentConversation.value = null
//        }
//    }
//
//    private suspend fun clear() {
//        withContext(Dispatchers.IO) {
//            dao.clear()
//        }
//    }
//
//    override fun onCleared() {
//        super.onCleared()
//        viewModelJob.cancel()
//    }
}