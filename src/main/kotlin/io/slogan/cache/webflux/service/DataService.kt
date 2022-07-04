package io.slogan.cache.webflux.service

interface DataService {

    fun get(key: String): String?

    fun create(key: String, value: String): String?

    fun delete(key: String): Boolean

    fun flushCache(key: String): Boolean
}