package io.slogan.cache.webflux.dao

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

@Repository
class DataAccess {
    val log: Logger = LoggerFactory.getLogger(javaClass)

    @Value("#{environment['data.store.path']}")
    lateinit var filePath: String

    fun findKey(key: String): List<String> {
        log.info("Found Key: {}", key)
        val searchValue = arrayListOf<String>()
        File(filePath).forEachLine { line ->
            log.debug("Read Line: {}", line)
            if (line.contains(key)) {
                searchValue.add(line)
            }
        }

        searchValue.forEach { log.debug("Found value: {}", it.split(':')[1]) }
        return searchValue
    }

    fun insertData(key: String, value: String): String {
        val inputData = "$key:$value\n"
        log.debug("Input Data: {}", inputData)
        try {
            Files.write(Paths.get(filePath), inputData.toByteArray(), StandardOpenOption.APPEND)
        } catch (e: IOException) {
            log.error("File Write Error!!")
            throw IOException("File Write Error!!")
        }
        return inputData
    }

    fun deleteData(key: String): Boolean {
        var data = ""
        val file = File(filePath)
        file.forEachLine {
            if (key != it.split(":")[0]) {
                data += "$it\n"
            }
        }

        // Warning!! : Performance Issue must cause, If File has huge data.
        file.delete()
        Files.write(Paths.get(filePath), data.toByteArray(), StandardOpenOption.CREATE)
        return true
    }
}