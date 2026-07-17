package com.iti.linguaquest.core.database

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteFullException
import com.iti.linguaquest.core.result.LinguaQuestDataError
import com.iti.linguaquest.core.result.LinguaQuestResult

suspend inline fun <T> safeDatabaseCall(
    crossinline dbCall: suspend () -> T
): LinguaQuestResult<T, LinguaQuestDataError.Local> {
    return try {
        LinguaQuestResult.Success(dbCall())
    } catch (e: SQLiteConstraintException) {
        LinguaQuestResult.Failure(LinguaQuestDataError.Local.CONSTRAINT_VIOLATION)
    } catch (e: SQLiteFullException) {
        LinguaQuestResult.Failure(LinguaQuestDataError.Local.DISK_FULL)
    } catch (e: Exception) {
        LinguaQuestResult.Failure(LinguaQuestDataError.Local.UNKNOWN)
    }
}