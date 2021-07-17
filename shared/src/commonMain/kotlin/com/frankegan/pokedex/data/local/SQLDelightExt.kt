package com.frankegan.pokedex.data.local

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