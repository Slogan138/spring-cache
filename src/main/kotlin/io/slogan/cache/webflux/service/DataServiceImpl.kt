package io.slogan.cache.webflux.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service

@Service
class DataServiceImpl : DataService {
    val log: Logger = LoggerFactory.getLogger(javaClass)
    val dataFile: ClassPathResource = ClassPathResource("data.txt")

    @Cacheable(cacheNames = ["file"], key = "#key")
    override fun get(key: String): String? {
        log.info("Found Key: {}", key)
        val data = dataFile.file.useLines { it.toList() }
        log.info("Data: {}", data)
        return data[0]
    }

    override fun create(request: Map<String, Any>): String? {
        TODO("Not yet implemented")
    }

    override fun update(request: Map<String, Any>): String? {
        TODO("Not yet implemented")
    }

    @CacheEvict(cacheNames = ["file"], key = "#key")
    override fun delete(key: String): Boolean? {
        TODO("Not yet implemented")
    }
}