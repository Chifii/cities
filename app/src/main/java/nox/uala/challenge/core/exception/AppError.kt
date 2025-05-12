package nox.uala.challenge.core.exception

sealed class AppError(val message: String, val cause: Throwable? = null) {
    class NetworkError(message: String, cause: Throwable? = null) :
        AppError(message, cause)

    class ApiError(message: String, val statusCode: Int, cause: Throwable? = null) :
        AppError(message, cause)

    class DatabaseError(message: String, cause: Throwable? = null) :
        AppError(message, cause)

    class UnknownError(cause: Throwable) :
        AppError(cause.message ?: "Error desconocido", cause)

    fun getUserFriendlyMessage(): String {
        return when (this) {
            is NetworkError -> "Error de red: $message"
            is ApiError -> "Error de API: $message (Código: $statusCode)"
            is DatabaseError -> "Error de base de datos: $message"
            is UnknownError -> "Error desconocido: $message"
        }
    }
}