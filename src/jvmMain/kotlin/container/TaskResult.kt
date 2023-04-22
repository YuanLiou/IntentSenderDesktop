package container

sealed class TaskResult<out T, out E> {
    data class Success<out T>(val value: T) : TaskResult<T, Nothing>()
    data class Failed<out E>(val error: E) : TaskResult<Nothing, E>()

    inline fun <C> fold(onSuccess: (T) -> C, onFailure: (E) -> C): C = when (this) {
        is Success -> onSuccess(value)
        is Failed -> onFailure(error)
    }
}

typealias SimpleResult<T> = TaskResult<T, Throwable>