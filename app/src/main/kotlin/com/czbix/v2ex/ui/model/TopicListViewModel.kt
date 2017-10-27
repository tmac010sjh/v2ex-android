package com.czbix.v2ex.ui.model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.crashlytics.android.Crashlytics
import com.czbix.v2ex.dao.NodeDao
import com.czbix.v2ex.dao.TopicDao
import com.czbix.v2ex.model.Node
import com.czbix.v2ex.model.Page
import com.czbix.v2ex.model.Topic
import com.czbix.v2ex.network.RequestHelper
import com.czbix.v2ex.util.LogUtils
import com.czbix.v2ex.util.await
import com.czbix.v2ex.util.dispose
import io.reactivex.disposables.Disposable

class TopicListViewModel : ViewModel() {
    private val tag = javaClass.simpleName
    private var mPage: Page? = null

    private val mFavorite: MutableLiveData<Boolean> = MutableLiveData()
    private val mOnceToken: MutableLiveData<String> = MutableLiveData()
    private val mTopics: MutableLiveData<List<Topic>> = MutableLiveData()

    private val mDisposables: MutableList<Disposable> = mutableListOf()

    val page: Page
        get() = mPage!!

    val favorite: LiveData<Boolean> = mFavorite
    val onceToken: LiveData<String> = mOnceToken
    val topics: LiveData<List<Topic>> = mTopics

    fun init(page: Page) {
        if (this.mPage == null) {
            return
        }

        @Suppress("NAME_SHADOWING")
        var page = page
        if (page is Node && !page.hasInfo()) {
            NodeDao.get(page.name)?.let {
                page = it
            }
        }

        this.mPage = page
        loadData()
    }

    fun loadData() {
        val log = String.format("Load list: %s", page.title)
        Crashlytics.log(log)
        LogUtils.d(tag, log)

        mDisposables += RequestHelper.getTopics(page).map { topics ->
            for (topic in topics) {
                val lastRead = TopicDao.getLastReadReply(topic.id)
                if (lastRead >= topic.replyCount) {
                    topic.setHasRead()
                }
            }

            topics
        }.await(this::setResult)
    }

    fun setResult(result: TopicList) {
        mFavorite.value = result.favorite
        mOnceToken.value = result.onceToken
        mTopics.value = result
    }

    fun toggleFavorite(value: Boolean) {
        mFavorite.postValue(value)
    }

    override fun onCleared() {
        mDisposables.dispose()
    }

    class TopicList(
            list: List<Topic>,
            val favorite: Boolean,
            val onceToken: String? = null
    ) : List<Topic> by list
}
