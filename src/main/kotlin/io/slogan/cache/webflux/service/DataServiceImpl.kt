package io.slogan.cache.webflux.service

import org.apache.logging.log4j.util.Strings
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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
    val filePath = "/Users/jinwoo/data.txt"
    // TODO: resources 파일 존재하여 Restart 혹은 Build 시에만 변경 사항이 적용됨. 프로젝트 소스코드 밖에 있는 파일로 테스트 필요
//    val dataFile: ClassPathResource = ClassPathResource("data.txt")

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

    // TODO: key 존재 시 데이터 입력 못하게 수정
    @CacheEvict(cacheNames = ["file"], key = "#key")
    override fun create(key: String, value: String): String? {
        val inputData = Strings.join(arrayListOf(key, value), ':') + "\n"
        log.debug("Input Data: {}", inputData)
        try {
            Files.write(Paths.get(filePath), inputData.toByteArray(), StandardOpenOption.APPEND)
        } catch (e: IOException) {
            log.error("File Write Error!!")
            return null
        }
        return inputData
    }

    @CacheEvict(cacheNames = ["file"], key = "#key")
    override fun deleteCache(key: String): Boolean {
        return true
    }
}