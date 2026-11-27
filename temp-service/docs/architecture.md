# 구조와 확장 가이드

현재 프로젝트는 단일 Gradle 모듈이자 독립 실행형 Spring MVC 서비스다.
모놀리스는 기능별 패키지를 추가해 하나의 JAR로 배포하고, MSA는 템플릿을 서비스별로 사용해 별도 JAR를 만든다.
Gradle 멀티모듈이나 API Gateway는 필수 조건이 아니다.

| 패키지 | 책임 |
| --- | --- |
| config | 보안 필터, JWT 검증, 권한 변환, OpenAPI |
| auth | 자체 인증 모드의 예제 로그인 및 키 관리 |
| common.web | 공통 오류, 페이징 응답, 서비스 정보 |
| note | 예제 Controller, Service, Repository, Entity, DTO |
| db/migration | 서비스가 소유하는 DB 스키마 |

## JWT 흐름

자체 인증은 AuthController에서 AuthenticationManager로 비밀번호를 확인하고 JwtEncoder로 서명한다.
외부 인증 모드에서는 해당 발급 기능을 생성하지 않는다.
두 방식 모두 Spring Security Resource Server가 요청의 Bearer 토큰을 읽고 JwtDecoder를 통해 검증한다.
JwtAuthenticationConverter는 scope/scp를 SCOPE_*, roles를 ROLE_* 권한으로 매핑한다.
Controller의 PreAuthorize는 서비스별 접근 조건을 설정한다.
인증 세션은 만들지 않는다. 따라서 서버 재시작이나 수평 확장 시에도 동일한 공개 키를 사용하는 운영 인스턴스에서 검증할 수 있다.

외부 발급자는 고유한 sub, issuer, audience, exp, scope를 제공해야 한다. ID Token 대신 Access Token을 사용한다.
인증 서버의 키 교체, Refresh Token 저장 및 Rotation, 폐기와 로그아웃, 계정 잠금 및 다중 사용자 관리는 인증 서버의 책임이다.
단독 서비스의 자체 인증 예제에는 이러한 수명주기 로직이 없다. 실제 사용자 식별은 변경되지 않는 회원 ID로 바꾸고, 모든 비즈니스 데이터에 접근 소유권을 확인한다.

## 비즈니스 요청의 흐름

Security -> Controller(입력 검증) -> Service(트랜잭션) -> Repository(JPA) -> DB.
엔티티는 DTO로 변환하고 Open Session In View를 사용하지 않는다.
공통 예외 처리기는 개별 도메인 예외 클래스에 직접 의존하지 않는다.
예제 메모 수정은 마지막 쓰기가 반영되는 구조이며 충돌 감지가 필요한 경우 낙관적 잠금을 적용한다.

## MSA로 확장할 때

- 서비스별 DB 소유권을 분리하고 다른 서비스의 Repository나 Entity를 직접 참조하지 않는다.
- 서비스별 application.name, SERVER_PORT, DB_URL, JWT_AUDIENCE를 지정한다.
- 공통 인증 규칙은 동일한 발급자 및 claim 계약으로 관리한다. 여러 저장소에서 공유 코드가 필요해지면 별도 버전 관리되는 라이브러리로 분리한다.
- 서비스 간 HTTP 호출이 필요하면 RestClient를 사용하고, 연결/응답 Timeout 및 호출 대상 URL을 외부화한다.
- 서비스 간 인증은 호출 대상의 audience와 최소 권한을 갖는 서비스 토큰을 사용하도록 설계한다. 사용자 토큰의 무조건 전달은 피한다.
- Gateway, Redis, 메시지큐, Circuit Breaker, SAGA/Outbox는 요구사항에 따라 도입한다.
- liveness는 외부 DB 장애로 프로세스가 재시작되는 것을 막기 위해 DB 상태를 포함하지 않는다. readiness는 DB를 확인한다.
- 운영 로그는 ECS 형식의 구조화된 JSON으로 출력하며 서비스별 Trace ID/분산 추적은 관측 인프라 선택 시 연결한다. 토큰 및 비밀번호는 로그에 남기지 않는다.

## 화면을 추가할 때

서버 렌더링이 필요하면 spring-boot-starter-thymeleaf를 추가한다. JAR 포맷은 그대로 유지할 수 있다.
브라우저 로그인과 쿠키를 도입한다면 API 인증 정책 및 CSRF/CORS 설정을 함께 검토한다.
