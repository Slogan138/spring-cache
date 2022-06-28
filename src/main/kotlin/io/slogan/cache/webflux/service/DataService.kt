package io.slogan.cache.webflux.service

interface DataService {

    fun get(key: String): String?

    fun deleteCache(key: String): Boolean
}