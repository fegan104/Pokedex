package com.frankegan.pokedex.data.local

import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.TransactionWithReturn
import com.squareup.sqldelight.TransactionWithoutReturn
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

suspend fun <R> AppDatabase.transactionWithContext(
    coroutineContext: CoroutineContext,
    noEnclosing: Boolean = false,
    body: AppDatabase.() -> R
): R {
    return withContext(coroutineContext) {
        transactionWithResult(noEnclosing) {
            body()
        }
    }
}