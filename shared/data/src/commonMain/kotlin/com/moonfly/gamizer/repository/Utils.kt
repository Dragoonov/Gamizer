package com.moonfly.gamizer.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.SerializationException

suspend inline fun <reified T> HttpClient.safeGet(
    url: String,
    block: HttpRequestBuilder.() -> Unit,
): Response<T> =
    try {
        val response = get(url, block)
        Response.Success(response.body())
    } catch (e: ClientRequestException) {
        Response.Error.HttpError(e.response.status.value, e.message)
    } catch (e: ServerResponseException) {
        Response.Error.HttpError(e.response.status.value, e.message)
    } catch (e: IOException) {
        Response.Error.NetworkError
    } catch (e: SerializationException) {
        Response.Error.SerializationError
    }