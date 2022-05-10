package com.example.sledipari.api

import com.example.sledipari.jsonInstance
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.http.*
import io.ktor.utils.io.*
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader

private val engine = MockEngine { request ->

    val filePath = when (request.url.parameters["month"]) {
        "2022-1-8" -> "/getMonth8"
        "2022-1-7" -> "/getMonth7"
        else -> "/getAllMonths"
    }

    respond(
        content = ByteReadChannel(getJsonResult(filePath)),
        status = HttpStatusCode.OK,
        headers = headersOf(HttpHeaders.ContentType, "application/json")
    )
}

private const val RESOURCES_PATH = "./src/test/res"
private const val FILE_EXTENSION = ".json"

private fun getJsonResult(filePath: String): String {

    println("Read File")
    val fullFilePath =
        StringBuilder(RESOURCES_PATH).append(filePath).append(FILE_EXTENSION).toString()

    val br = BufferedReader(InputStreamReader(FileInputStream(fullFilePath)))
    val sb = StringBuilder()
    br.forEachLine { sb.append(it) }
    return sb.toString()
}

val mockHttpClient = HttpClient(engine) {
    install(JsonFeature) {
        serializer = KotlinxSerializer(jsonInstance)
    }
}