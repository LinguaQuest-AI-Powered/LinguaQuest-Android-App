package com.iti.linguaquest.features.notification.data.datasource.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.iti.linguaquest.features.notification.data.datasource.remote.NotificationApiService
import com.iti.linguaquest.features.notification.data.mapper.toDomain
import com.iti.linguaquest.features.notification.domain.model.Notification
import com.iti.linguaquest.core.result.LinguaQuestDataError
import retrofit2.HttpException
import java.io.IOException

class NotificationPagingException(
    val error: LinguaQuestDataError
) : Exception("Paging error: $error")

class NotificationPagingSource(
    private val notificationApiService: NotificationApiService
) : PagingSource<Int, Notification>() {

    override fun getRefreshKey(state: PagingState<Int, Notification>): Int? {
        return state.anchorPosition?.let { anchor ->
            val anchorPage = state.closestPageToPosition(anchor)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Notification> {
        val page = params.key ?: 0
        val size = params.loadSize
        return try {
            val response = notificationApiService.getNotifications(page = page, size = size)
            if (response.isSuccessful) {
                val pageData = response.body()?.data
                if (pageData != null) {
                    val notifications = pageData.notifications.map { it.toDomain() }
                    val nextKey = if (notifications.isNotEmpty() && (page + 1) * pageData.size < pageData.totalElements) {
                        page + 1
                    } else {
                        null
                    }
                    val prevKey = if (page == 0) null else page - 1
                    LoadResult.Page(
                        data = notifications,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                } else {
                    LoadResult.Error(NotificationPagingException(LinguaQuestDataError.Remote.EMPTY_RESULT))
                }
            } else {
                val error = when (response.code()) {
                    400 -> LinguaQuestDataError.Remote.BAD_REQUEST
                    401, 403 -> LinguaQuestDataError.Remote.UNAUTHORIZED
                    408 -> LinguaQuestDataError.Remote.REQUEST_TIMEOUT
                    429 -> LinguaQuestDataError.Remote.TOO_MANY_REQUESTS
                    in 500..599 -> LinguaQuestDataError.Remote.SERVER
                    else -> LinguaQuestDataError.Remote.UNKNOWN
                }
                LoadResult.Error(NotificationPagingException(error))
            }
        } catch (e: IOException) {
            LoadResult.Error(NotificationPagingException(LinguaQuestDataError.Remote.NO_INTERNET))
        } catch (e: HttpException) {
            LoadResult.Error(NotificationPagingException(LinguaQuestDataError.Remote.SERVER))
        } catch (e: Exception) {
            LoadResult.Error(NotificationPagingException(LinguaQuestDataError.Remote.UNKNOWN))
        }
    }
}
