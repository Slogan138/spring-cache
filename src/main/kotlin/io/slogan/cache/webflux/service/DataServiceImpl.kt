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

    // TODO: resources 파일 존재하여 Restart 혹은 Build 시에만 변경 사항이 적용됨. 프로젝트 소스코드 밖에 있는 파일로 테스트 필요
    val dataFile: ClassPathResource = ClassPathResource("data.txt")

    @Cacheable(cacheNames = ["file"], key = "#key")
    override fun get(key: String): String? {
        log.info("Found Key: {}", key)
        val searchValue = arrayListOf<String>()
        dataFile.file.forEachLine { line ->
            log.debug("Read Line: {}", line)
            if (line.contains(key)) {
                searchValue.add(line)
            }
        }

        if (searchValue.size != 1) {
            log.warn("Illegal Input: {}", key)
            return null
        }

        log.info("Found Value: {}", searchValue[0].split(":")[1])
        return searchValue[0].split(":")[1]
    }

    @CacheEvict(cacheNames = ["file"], key = "#key")
    override fun deleteCache(key: String): Boolean {
        return true
    }
}