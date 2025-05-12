package nox.uala.challenge.core.util

import nox.uala.challenge.core.exception.AppError
import nox.uala.challenge.features.home.domain.util.Resource
import java.io.IOException
import java.net.SocketTimeoutException

object ErrorMapper {
    fun mapResourceErrorToAppError(result: Resource.Error<*>): AppError {
        return when {
            result.message?.contains("network", ignoreCase = true) == true ||
                    result.message?.contains("internet", ignoreCase = true) == true ->
                AppError.NetworkError(result.message ?: "Error de red")

            result.message?.contains("api", ignoreCase = true) == true ||
                    result.message?.contains("server", ignoreCase = true) == true ->
                AppError.ApiError(result.message ?: "Error de API", 0)

            result.message?.contains("database", ignoreCase = true) == true ||
                    result.message?.contains("db", ignoreCase = true) == true ->
                AppError.DatabaseError(result.message ?: "Error de base de datos")

            else -> AppError.UnknownError(Exception(result.message))
        }
    }

    fun mapExceptionToAppError(e: Exception): AppError {
        return when (e) {
            is IOException, is SocketTimeoutException ->
                AppError.NetworkError("Error de conexión: ${e.message}", e)

            else -> AppError.UnknownError(e)
        }
    }
}