# JPA N+1 문제 발생 및 해결 Repository
- 어느날 `N+1문제를 DTO로 해결했다는 말을 들었다`는 이야기를 들은 경험이 있다.
  - 음.. `@BatchSize` 나 fetch join은 아는데.. DTO 자체로도 해결이 된다고? 알지 못하는 뭔가가 더 있나? 하는 궁금증이 들었다.
  - 결론부터 말하자면 DTO로 해결했다고 하는 것은 상당히 부족한 답변이 아닐까 싶다.
    - 왜 이렇게 생각했는지는 블로그에서 확인할 수 있다!

# 상세 구현 과정 설명은 블로그 글 참조
- [JPA N+1 삽질일지(확실히 알고 넘어가기..!)](https://velog.io/@puar12/JPA-N1-%EC%82%BD%EC%A7%88%EC%9D%BC%EC%A7%80%ED%99%95%EC%8B%A4%ED%9E%88-%EC%95%8C%EA%B3%A0-%EB%84%98%EC%96%B4%EA%B0%80%EA%B8%B0)

## 프로젝트 세팅
### 버전 및 의존성
- SpringBoot 3.3.4 / java 17 / 
- Lombok, SpringWeb, JPA, H2 DB, Querydsl
- DB 생성하지 않고도 발생하는 쿼리를 보기 위해 H2 DB in memory 모드를 사용

### 실행해보기
- 해당 Repo full 받기
- 각 TestCase 실행하며 쿼리 확인하기

### 초기 데이터 세팅
![](https://velog.velcdn.com/images/puar12/post/e439a3d9-813b-4062-8d9b-1e08be14e8ed/image.png)

## 각 Case별 실험
### 해결 Case 0 : Join하면 연관 관계에 있는 Entity를 넣어주지 않을까?
- (미해결) Join 쿼리가 나가긴 하지만 연관 관계에 있는 Category는 가져오지 않았음
  - select 메서드에 Product만 사용했기 때문
  - join 자체는 단순 테이블을 연관만 맺어주도록 동작
- [feat : Case 0 : Join하면 연관 관계에 있는 Entity를 조회할 것 같지만 조회하지 않음 & @transactional class단으로 옮겨 모든 메서드에 자동 적용 되도록](https://github.com/CheorHyeon/JPANplusOneProblemSolveExam/commit/fcc1135234a7639d8ae9124fb744bd2af36f9983)

### 해결 Case 1 : FetchJoin을 통해 조회 대상이 되는 엔티티를 가져올 때 연관된 엔티티까지 가져오자(ManyToOne)
- (해결) Category를 가져와서 product에 실제 Entity를 갖도록 적용되어 쿼리가 1개만 동작
  - Fetch Join : 연관된 엔티티나 컬렉션을 한 번에 같이 조회하는 기능
    - 연관된 엔티티까지 영속성 컨텍스트에 전부 올려버림
  - N+1 문제 해결!
[feat : Case 1 : FetchJoin을 통해 조회 대상이 되는 엔티티를 가져올 때 연관된 엔티티까지 가져오도록 하여 N+1 문제 해결(ManyToOne)](https://github.com/CheorHyeon/JPANplusOneProblemSolveExam/commit/96930b6c201602f74ba72ad3d5cba4648528e85f)

### 해결 Case 2 : FetchJoin에서 Paging을 할 때 ManyToOne 관계라면 offset과 limit이 정상 동작한다
- (해결) Paging : Limit, Offset을 이용하여 데이터를 추출
- ManyToOne일때는 페이징이 문제가 안된다.
  - 하나의 엔티티에 해당하는 데이터가 실제 DB Table에서도 하나의 행이기에 전체 데이터를 메모리에 가져오지 않아도 되기에 JPA가 메모리에 전체 데이터를 로드시키지 않는다.
  - limit, offset이 정상 동작함
- [feat : Case 2 : FetchJoin에서 Paging을 할 때 ManyToOne 관계라면 offset과 limit이 정상 동작한다](https://github.com/CheorHyeon/JPANplusOneProblemSolveExam/commit/d8fbb8d02d695180122c7150e861aac9a79a441e)
  
### 해결 Case 3 : FetchJoin에서 Paging을 할 때 OneToMany 관계라면 offset과 limit이 쿼리에서 작동하지 않고, 전체 데이터를 불러와 메모리에서 페이징 처리를 해준다.
- (해결, But 메모리 부하 문제) OneToMany에서 fetchJoin과 Paging을 같이 사용한다면 메모리에 전체 데이터를 로드하고 메모리에서 페이징 처리를 해준다.
  - 하나의 엔티티의 데이터가 실제 DB에서는 여러 행으로 이뤄짐
    - ex) 카테고리 A : 상품 1, 2, 3 => DB에서는 3개의 Row
  - offset, limit이 적용되지 않음
- 100만건의 데이터가 있다면? 100만개를 다 가져와서 페이징을 해주는 매우 비효율 & 메모리 부하
- ManyToOne에서 페이징 사용, @BatchSize 사용 등의 방법이 권장됨
- [feat : Case 3 : FetchJoin에서 Paging을 할 때 OneToMany 관계라면 offset과 limit이 동작하지 않아 메모리 부하가 일어날 수 있다](https://github.com/CheorHyeon/JPANplusOneProblemSolveExam/commit/d90b6bd4466a98a7315094df4a585026ff971a32)

### 해결 Case 4 : @BatchSize를 이용하여 여러 select 쿼리들을 지정된 갯수만큼 In Query로 모아 N+1문제 해결
- (해결) BatchSize 설정 값에 따라 하나의 In 쿼리로 묶어줌
- 지정 Size 만큼 한번에 가져오지 않고 Hibernate에 의해 쿼리가 나눠서 나가는 경우도 있음
  - [JPA N+1튜닝과정에서 선언한 배치 사이즈와 다르게 쿼리 분할 되어 수행되는 이유](https://42class.com/dev/jpa-batchsize/)
- @BatchSize 어노테이션으로 특정 엔티티에만 적용할 수도 있고, yml에 설정하여 전역으로 설정할 수도 있음
  - `spring.jpa.properties.hibernate.default_batch_fetch_size=10`
  - **이 경우 ManyToOne 관계에서 발생하는 N+1 문제도 해결해준다.(fetchJoin 안했을때도)**
- [feat : Case 4 : @batchsize를 이용하여 여러 select 쿼리들을 지정된 갯수만큼 In Query로 모아 N+1문제 해결](https://github.com/CheorHyeon/JPANplusOneProblemSolveExam/commit/7fa4bec9eef1ba1996613c0e4b5910f8c4fc44ac)

### 해결 Case 5 : select 메서드에 필요한 속성을 직접 기입하여 DTO로 받기
- (해결) Proxy 객체의 메서드를 호출해서 실제 Entity 객체를 불러오지 않고 select 메서드에 필요한 속성을 명시하여 DTO로 받을 수 있는 방식 사용
- [feat : Case 5 : select 메서드에 필요한 속성을 직접 기입하여 DTO로 받아 Proxy 객체의 메서드를 호출하지 않아 N개의 추가 쿼리가 발생하지 않게 회피](https://github.com/CheorHyeon/JPANplusOneProblemSolveExam/commit/785867f892fee861b012e6352b7a1ca76707b49c)

### 해결 Case 6 : select 메서드에 필요한 속성을 직접 기입하여 Tuple로 받기
- (해결) Proxy 객체의 메서드를 호출해서 실제 Entity 객체를 불러오지 않고 select 메서드에 필요한 속성을 명시하여 Tuple로 받을 수 있는 방식 사용
- [feat : Case 6 : select 메서드에 필요한 속성을 직접 기입하여 Tuple로 받고, Proxy 객체의 메서드를 호출하지 않게 하여 N개의 추가 쿼리가 발생하지 않게 회피](https://github.com/CheorHyeon/JPANplusOneProblemSolveExam/commit/e921ac70c1292631de1e5f1e11cfe34437600a33)
- 
## 그래서 결론은?
- **Proxy객체의 메서드를 호출하면 실제 객체가 필요하기 때문에 N개의 추가 쿼리가 발생한다.**
- 하지만 쿼리를 날릴 때 애초에 **필요한 속성들을 가져온다던가, 아니면 fetchJoin으로 한방에 가져온다던가 하면 추가 쿼리는 발생하지 않는다.**
  - 단 OneToMany 관계일때는 Paging을 사용할 때 fetchJoin 사용 지양(메모리 과부하 문제 가능)

### 연관관계에 있는 Entity의 특정 속성을 사용하여 JPQL 쿼리를 생성하면 속성값만 가져오기 때문에 프록시 객체를 굳이 실제 Entity 객체로 불러오지 않는다.
- Product의 Category Name이 필요하다면 select 절에 `product.category.name` 이런식으로 특정 컬럼을 지정
- 그러면 나중에 DTO라던가 Tuple을 활용할 때 연관관계에 있는 Category 객체가 필요한 것이 아니니 **Proxy 객체를 실제 객체로 바꾸지 않고 name 값만 사용**

## 그렇다면, 맨 위에서 말한 `DTO를 활용하는 것이 N개의 쿼리를 무조건 발생 안할까?`
- 여태 위에서 Proxy 객체의 메서드를 호출하면 실제 객체가 필요하니 추가 쿼리가 필요함을 알았다.
- 그래서 프록시 객체로 필요한 속성 값을 가져오는 것이 아닌 DTO나 Tuple을 활용해서 N개의 쿼리가 추가로 발생하는 것을 회피하는 방식을 바로 직전에 알아봤다.
  - **그렇다면 DTO를 사용하는데 N+1 문제가 발생하는 경우도 있지않을까? 물론 있다!**
  - [feat : Querydsl select 메서드에 DTO를 사용해도 N+1 문제 발생하는 예시 추가
](https://github.com/CheorHyeon/JPANplusOneProblemSolveExam/commit/c358dc5a1fd0f99dc5f68f24a1a94bcf60dd67f9) 
