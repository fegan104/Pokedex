package com.frankegan.pokedex.data.local

import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.TransactionWithReturn
import com.squareup.sqldelight.TransactionWithoutReturn
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

suspend fun <R> Transacter.transactionWithContext(
    coroutineContext: CoroutineContext,
    noEnclosing: Boolean = false,
    body: TransactionWithReturn<R>.() -> R
): R {
    return withContext(coroutineContext) {
        transactionWithResult(noEnclosing) {
            body()
        }
    }
}