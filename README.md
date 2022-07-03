# Spring Local Cache 적용하기

## Local Cache 란?

Redis 나 memcached 와 같은 인메모리 데이터 스토어와는 다르게 별도의 서비스를 설치 하지 않고 애플리케이션 내에서 사용하는 캐시 서비스이다.

## Pros.

- 별도의 서비스를 설치하지 않고 라이브러리 추가만으로 사용할 수 있다.
- 쉽게 구현할 수 있다.
- 다른 외부 캐시 서비스를 이용하는 것 보다 더 나은 성능을 보여준다.

## Cons.

- 서버 간 데이터 정합을 기대하기 힘들다.

## How to Implement.

```kotlin
@Cacheable(cacheNames = ["file"], key = "#key")
fun get(key: String): String? {
    // --- 중략 ---
    return result
}
```

### Reference.

- https://spring.io/guides/gs/caching/