# Spring Local Cache 적용하기

## Local Cache 란?

Redis 나 memcached 와 같은 인메모리 데이터 스토어와는 다르게 별도의 서비스를 설치 하지 않고 애플리케이션 내에서 사용하는 캐시 서비스이다.

## Pros.

- 별도의 서비스를 설치하지 않고 라이브러리 추가만으로 사용할 수 있다.
- 쉽게 구현할 수 있다.
- 다른 외부 캐시 서비스와의 I/O가 없기 때문에 를 이용하는 것 보다 더 나은 성능을 보여준다.

## Cons.

- 서버 간 데이터 정합을 기대하기 힘들다.

## How to Implement.

```kotlin
@Cacheable(cacheNames = ["file"], key = "#key")
fun get(key: String): String? {
    // --- 중략 --- //
    return result
}
```

@Cacheable 어노테이션을 이용하여 method 에서 return 해주는 결과를 Cache 에 저장한다.   
별도의 설정이 없다면 method 에 전달 받는 Parameter 값이 전달 받은 기록이 있다면 이전에 실행된 결과를 return 해준다.   
다만 @Cacheable 어노테이션에 key, condition 을 이용하여 Caching 된 값 전달하는 조건을 설정하여 유연하게 method 실행 여부를 정할 수 있다.

## Warning!!

Spring Cache 은 @Cacheable 어노테이션을 이용하여 Caching 처리를 하게 되는데 이때 Spring AOP 를 이용한다.   
Spring AOP 는 Proxy Class 를 통해 메소드를 호출하게 된다. 이 때 동일 Class 내에 선언된 method 를 Self-Invocation 하게 된다면 어노테이션으로 설정된 기능이 정상 동작하지
않는다.

### 해결 방안

Spring 에서 제안하는 Self-Invocation 제약사항을 해결하기 위해선 AspectJ 를 이용하여 해결하도록 하고 있다.   
다만 구현하기엔 난이도가 있고 추가적인 의존성과 컴파일을 위한 플러그인 설치 등 고려해야할 사항들이 많아 사용하기 쉽지않다.

이를 비교적 쉽게 해결할 수 있는 방법은 Bean 으로 등록된 Class 를 가져와 method 를 호출하는 방법이 있다.

### Reference.

- https://spring.io/guides/gs/caching/
- https://ifuwanna.tistory.com/202