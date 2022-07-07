package io.slogan.cache.webflux.service

import io.slogan.cache.webflux.exception.DuplicateKeyException
import org.apache.logging.log4j.util.Strings
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

@Service
class DataServiceImpl : DataService {
    val log: Logger = LoggerFactory.getLogger(javaClass)

    @Value("#{environment['data.store.path']}")
    lateinit var filePath: String

    @Cacheable(cacheNames = ["file"], key = "#key")
    override fun get(key: String): String? {
        log.info("Found Key: {}", key)
        val searchValue = arrayListOf<String>()
        File(filePath).forEachLine { line ->
            log.debug("Read Line: {}", line)
            if (line.contains(key)) {
                searchValue.add(line)
            }
        }

        if (searchValue.size != 1) {
            log.warn("Illegal Input: {}", key)
            return null
        }

        log.debug("Found Value: {}", searchValue[0].split(":")[1])
        return searchValue[0].split(":")[1]
    }

    @CacheEvict(cacheNames = ["file"], key = "#key")
    override fun create(key: String, value: String): String? {
        if (get(key) != null) {
            throw DuplicateKeyException("Input Key was already exist!!")
        }
        val inputData = "$key:$value\n"
        log.debug("Input Data: {}", inputData)
        try {
            Files.write(Paths.get(filePath), inputData.toByteArray(), StandardOpenOption.APPEND)
        } catch (e: IOException) {
            log.error("File Write Error!!")
            return null
        }
        return inputData
    }

    override fun update(key: String, value: String): String? {
        TODO("Not yet implemented")
    }

    @CacheEvict(cacheNames = ["file"], key = "#key")
    override fun delete(key: String): Boolean {
        val data = arrayListOf<String>()
        val file = File(filePath)
        file.forEachLine {
            if (key != it.split(":")[0]) {
                data.add(it)
            }
        }

        file.delete()
        // Warning!! : Performance Issue must cause, If File has huge data.
        Files.write(Paths.get(filePath), Strings.join(data, '\n').toByteArray(), StandardOpenOption.CREATE)
        return true
    }

    @CacheEvict(cacheNames = ["file"], key = "#key")
    override fun flushCache(key: String): Boolean {
        return true
    }
}