package io.slogan.cache.webflux.service

interface DataService {

    fun get(key: String): String?

    fun create(request: Map<String, Any>): String?

    fun update(request: Map<String, Any>): String?

    fun delete(key: String): Boolean?
}