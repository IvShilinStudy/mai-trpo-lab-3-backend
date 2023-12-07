package ru.ivn_sln.tools

// Created by Ponomariov E. (eugenenumbernine@gmail.com)

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

/**
 * Pattern that wrap some work with states: Loading, Success and Failure.
 */
sealed class Resource<out R> {

    class Loading : Resource<Nothing>() {
        override fun toString() = "Resource(Loading)"
    }

    class Success<out T>(val data: T) : Resource<T>() {
        override fun toString() = "Resource(Success)"
    }

    class Failure(val cause: Throwable) : Resource<Nothing>() {
        override fun toString() = "Resource(Failure(${cause.localizedMessage}))"
    }

    companion object {
        /**
         * Wrap given block with [Resource] pattern.
         * Exceptions was handled and passed by [Failure] state.
         */
        inline fun <T> on(function: () -> T): Resource<T> = try {
            Success(function())
        } catch (ex: Throwable) {
            // todo make logger
            Failure(ex)
        }
    }
}

fun failureResourceOf(cause: String) = Resource.Failure(IllegalStateException(cause))

inline fun <reified R> Resource<R>.whenSuccess(foo: (data: R) -> Unit): Resource<R> {
    if (this is Resource.Success) foo(this.data)
    return this
}

inline fun <reified R> Resource<R>.whenLoading(foo: () -> Unit): Resource<R> {
    if (this is Resource.Loading) foo()
    return this
}

inline fun <reified R> Resource<R>.whenFailure(foo: (exception: Throwable) -> Unit): Resource<R> {
    if (this is Resource.Failure) foo(this.cause)
    return this
}


/**
 * Return [Resource] data if it is Success. Otherwise return null.
 */
inline fun <reified R> Resource<R>.takeIfSuccess(): R? {
    return if (this is Resource.Success) this.data else null
}


/**
 * Apply given transformation to data when Resource is Success
 */
inline fun <T, K> Resource<T>.map(transform: (value: T) -> K): Resource<K> {
    return when (this) {
        is Resource.Failure -> Resource.Failure(this.cause)
        is Resource.Loading -> Resource.Loading()
        is Resource.Success -> Resource.Success(transform(this.data))
    }
}


/**
 * Wrap given block with [Resource] pattern.
 */
inline fun <T> resource(producer: () -> T) = Resource.on { producer() }


/**
 * Creates a cold flow from the given suspendable block
 * and wrap block with [Resource]
 * @param loading emit [Resource.Loading] state when flow start
 */
inline fun <T> resourceFlow(
    loading: Boolean = true,
    crossinline producer: suspend () -> T
) = flow {
    if (loading) emit(Resource.Loading())
    emit(Resource.on { producer() })
}


inline fun <T> Flow<Resource<T>>.onEachSuccess(
    crossinline action: suspend (T) -> Unit
): Flow<Resource<T>> = onEach { resource ->
    if (resource is Resource.Success)
        action(resource.data)
}

inline fun <T> Flow<Resource<T>>.onEachFailure(
    crossinline action: suspend (Throwable) -> Unit
): Flow<Resource<T>> = onEach { resource ->
    if (resource is Resource.Failure)
        action(resource.cause)
}

inline fun <T> Flow<Resource<T>>.onEachLoading(
    crossinline action: suspend () -> Unit
): Flow<Resource<T>> = onEach { resource ->
    if (resource is Resource.Loading)
        action()
}


/**
 * Returns a resource flow containing the results of applying the given
 * transform function to each value of the original flow if Resource is Success
 */
inline fun <T, K> Flow<Resource<T>>.transform(
    crossinline transform: suspend (resource: T) -> K
): Flow<Resource<K>> {
    return map { resource -> resource.map { transform(it) } }
}


class RepeatOfResourceTimeoutException(
    override val message: String = "The condition was not reached after the expiration of the repeatable operation."
) : Exception()

/**
 * Wrap given block with [Resource] pattern.
 * You may set delay and attempts count for repeating work depending of predicate.
 *
 * @param attempts Work repeat count
 * @param delay Delay between attempts
 * @param predicate Function with boolean return type that determines end of repeating
 */
suspend inline fun <T> repeatableResource(
    attempts: Int = 3,
    delay: Long = 2000,
    throwTimeoutException: Boolean = false,
    crossinline predicate: ((Resource<T>) -> Boolean) = { it is Resource.Success },
    crossinline block: suspend () -> T
): Resource<T> {
    var attempt = 1
    while (true) {
        val resource = Resource.on { block() }
        if (predicate(resource)) {
            return resource
        } else {
            delay(delay)
            attempt++
        }
        if (attempt > attempts) {
            return if (throwTimeoutException)
                Resource.Failure(RepeatOfResourceTimeoutException())
            else resource
        }
    }
}


/**
 * Creates a cold flow from the given suspendable block
 * and wrap block with [Resource]
 * You may set delay and attempts count for repeating work depending of predicate.
 *
 * @param attempts Work repeat count
 * @param delay Delay between attempts
 * @param predicate Function with boolean return type that determines end of repeating
 */
inline fun <T> repeatableResourceFlow(
    loading: Boolean = true,
    attempts: Int = 3,
    delay: Long = 2000,
    throwTimeoutException: Boolean = false,
    crossinline predicate: ((Resource<T>) -> Boolean) = { it is Resource.Success },
    crossinline block: suspend () -> T
) = flow {
    if (loading) emit(Resource.Loading())
    var attempt = 1
    while (true) {
        val resource = Resource.on { block() }
        if (predicate(resource)) {
            emit(resource)
            break
        } else {
            delay(delay)
            attempt++
        }
        if (attempt > attempts) {
            emit(
                if (throwTimeoutException)
                    Resource.Failure(RepeatOfResourceTimeoutException())
                else resource
            )
            break
        }
    }
}

