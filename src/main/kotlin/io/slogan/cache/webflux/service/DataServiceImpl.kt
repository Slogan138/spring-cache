package io.slogan.cache.webflux.service

import io.slogan.cache.webflux.dao.DataAccess
import io.slogan.cache.webflux.exception.DuplicateKeyException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.io.IOException

@Service
class DataServiceImpl(
    val dataAccess: DataAccess
) : DataService {
    val log: Logger = LoggerFactory.getLogger(javaClass)

    @Cacheable(cacheNames = ["file"], key = "#key")
    override fun get(key: String): String {
        val findValue = dataAccess.findKey(key)
        if (findValue.size != 1) {
            log.warn("Illegal Input: {}", key)
            throw IllegalArgumentException("Illegal Input!!")
        }
        return findValue[0].split(':')[1]
    }

    @CacheEvict(cacheNames = ["file"], key = "#key")
    override fun create(key: String, value: String): String {
        if (dataAccess.findKey(key).isNotEmpty()) {
            throw DuplicateKeyException("Duplicate Key detected!!")
        }

        return dataAccess.insertData(key, value)
    }

    @CacheEvict(cacheNames = ["file"], key = "#key")
    override fun update(key: String, value: String): String {
        if (dataAccess.findKey(key).size != 1) {
            throw IllegalArgumentException("Illegal Input!!")
        }

        if (!dataAccess.deleteData(key)) {
            throw IOException("Something was wrong, when key update")
        }

        return dataAccess.insertData(key, value)
    }

    @CacheEvict(cacheNames = ["file"], key = "#key")
    override fun delete(key: String): Boolean {
        return dataAccess.deleteData(key)
    }

    @CacheEvict(cacheNames = ["file"], key = "#key")
    override fun flushCache(key: String): Boolean {
        return true
    }
}