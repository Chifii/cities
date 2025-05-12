package nox.uala.challenge.core.util

import timber.log.Timber
import nox.uala.challenge.core.exception.AppError

object AppLogger {
    fun init() {
        Timber.plant(Timber.DebugTree())
    }

    fun d(message: String) = Timber.d(message)

    fun i(message: String) = Timber.i(message)

    fun w(message: String, t: Throwable? = null) = Timber.w(t, message)

    fun e(message: String, t: Throwable? = null) = Timber.e(t, message)

    fun e(error: AppError) {
        val prefix = when (error) {
            is AppError.NetworkError -> "Error de red"
            is AppError.ApiError -> "Error de API (${error.statusCode})"
            is AppError.DatabaseError -> "Error de base de datos"
            is AppError.UnknownError -> "Error desconocido"
        }
        Timber.e(error.cause, "$prefix: ${error.message}")
    }
}