package io.slogan.cache.webflux.exception

class DuplicateKeyException(override val message: String) : Exception(message)