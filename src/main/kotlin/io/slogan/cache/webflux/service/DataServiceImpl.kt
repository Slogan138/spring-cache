package io.slogan.cache.webflux.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class DataServiceImpl : DataService {
    val log: Logger = LoggerFactory.getLogger(javaClass)

    @Cacheable(cacheNames = ["file"], key = "#key")
    override fun get(key: String): String? {
        TODO("Not yet implemented")
    }

    override fun create(request: Map<String, Any>): String? {
        TODO("Not yet implemented")
    }

    override fun update(request: Map<String, Any>): String? {
        TODO("Not yet implemented")
    }

    override fun delete(key: String): Boolean? {
        TODO("Not yet implemented")
    }
}