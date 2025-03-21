[![CI (feat. Java CI with Gradle)](https://github.com/TravelLaboratory/travel-laboratory-was/actions/workflows/ci.yml/badge.svg)](https://github.com/TravelLaboratory/travel-laboratory-was/actions/workflows/ci.yml)
[![Deploy to Production](https://github.com/TravelLaboratory/travel-laboratory-was/actions/workflows/main-deploy.yml/badge.svg)](https://github.com/TravelLaboratory/travel-laboratory-was/actions/workflows/main-deploy.yml)

![20240905_170038](https://github.com/user-attachments/assets/0b35cbf0-4099-4662-8508-fad514e19d0f)
## 📑 Introduction
Trablock은 사용자가 여행 계획을 작성하고 리뷰를 추가하며 이를 다른 사용자들과 공유할 수 있는 여행 계획 및 일정 공유 플랫폼입니다. 이 플랫폼을 통해 여행자들은 자신의 여행 경험을 기록하고, 다른 사용자들과 소통하며 여행 정보를 나눌 수 있습니다.

Trablock이라는 이름은 Travel과 Block의 합성어로, 사용자가 개별 일정들을 마치 블록처럼 조립하여 하나의 완성된 여행 계획을 만드는 것을 의미합니다.

## ⭐ Architecture
![모니터링서버 구축](https://github.com/user-attachments/assets/661f70f6-d2b3-4048-8b9d-09a1ea732d51)

## 🛠 Backend Tech Stack

- **프레임워크:** Spring Boot(3.3.0), Spring Data JPA, Spring Data Redis
- **언어 및 환경:** Java 17, Lombok
- **데이터베이스:** AWS RDS (MySQL), AWS ElastiCache (Redis), AWS S3
- **보안:** JWT, Kakao OAuth 2.0
- **이미지 처리:** Thumbnailator
- **API 문서화:** Swagger
- **테스트:** JUnit5, & Mockito, H2 Database
- **배포:** AWS EC2
- **CI/CD:** GitHub Actions, AWS CodeDeploy
- **성능 테스트:** nGrinder
- **모니터링:** Prometheus, Grafana


## 🧶 What I learn  
### [설계 및 개발을 진행하면서] 

#### 1. **스프링 기본 CRUD 및 예외 처리**

첫 번째 스프링 프로젝트였기 때문에, 기본적인 **CRUD**와 **예외 처리**에 많은 신경을 썼습니다. 이를 통해 스프링의 핵심 기능을 이해하고 안정적인 애플리케이션 개발의 중요성을 배웠습니다.

#### 2. **N+1 문제와 해결 방법**

개발 과정에서 **N+1 문제**를 직면하였고, 이를 **Fetch Join**을 통해 해결했습니다. 하지만 Fetch Join을 사용하면서 때로는 **데이터베이스 부하**가 증가할 위험이 있다는 점을 깨달았습니다. 또한 **코드 가독성** 측면에서 Fetch Join을 사용하지 않는 방식이 더 적합할 때가 있음을 고려했습니다. 이러한 트레이드오프를 통해, 상황에 맞는 적절한 해결책을 선택하는 것이 중요하다는 교훈을 얻었습니다.

#### 3. **테이블 설계 전략 학습**

프로젝트에서 여행 일정의 3가지 주요 타입을 설계하면서, 다양한 테이블 설계 전략(단일 테이블, 구현체 테이블, **DTYPE을 통한 조인 전략**)을 학습했습니다. 최종적으로 **DTYPE을 통한 조인 전략**을 선택하여, 보다 객체지향적이고 효율적인 데이터베이스 테이블 설계를 적용할 수 있었습니다.

#### 4. **CI/CD의 필요성**

**AWS CodeDeploy**를 사용해 CD(Continuous Deployment)를 구현하고, 배포 자동화를 경험했습니다. CI(Continuous Integration)는 프로젝트 초기에 도입되었지만, 개발 일정에 맞추기 위해 테스트 코드 작성을 생략하는 결정을 내렸습니다. 하지만 이는 장기적으로 좋지 않은 판단임을 깨달았고, 이후 CI를 통해 테스트 자동화를 구축하여 코드 품질을 높이고, 배포 전 검증의 중요성을 절실히 느꼈습니다.


### [개발 후 테스트 및 배포 과정에서]

#### 1. **인덱싱의 중요성**

테스트 서버에서 10만 개의 더미 데이터를 기준으로 API 성능을 테스트하는 과정에서 성능 저하 문제가 발생했습니다. 특히, 데이터베이스 조회 성능이 큰 이슈였는데, 이를 해결하기 위해 **DB 인덱싱**을 도입했습니다. 인덱싱을 통해 쿼리 성능이 크게 향상되었으며, 데이터베이스 최적화가 성능에 미치는 영향을 깊이 이해하게 되었습니다.

#### 2. **캐싱 적용**

실시간성이 크게 요구되지 않는 데이터(예: 홈 배너)에 **Redis 캐싱**을 적용했습니다. 이를 통해 서버와 데이터베이스의 부하를 줄일 수 있었으며, 캐시를 효과적으로 활용함으로써 시스템 성능을 최적화할 수 있다는 중요한 교훈을 얻었습니다.

#### 3. **테스트 코드와 리팩토링의 중요성**

테스트 코드를 작성하면서 각 클래스가 **단일 책임 원칙**을 위반하는지, **의존성 결합**이 강하지 않은지 깊이 고민하고 이를 개선하는 과정에서, 테스트 코드가 단순한 검증 도구를 넘어 리팩토링을 유도하는 중요한 역할을 한다는 것을 알게 되었습니다. 이를 통해 서비스 코드를 리팩토링할 필요성을 느꼈으며, 테스트 코드가 애플리케이션 전반의 품질을 높이는 중요한 도구임을 깨달았습니다.

#### 4. 리소스 최적화를 통한 이미지 리사이징의 중요성
초기에는 사용자가 업로드한 이미지를 원본 그대로 S3에 저장했는데, 용량이 큰 이미지들이 계속 쌓이면서 S3 저장 공간을 빠르게 소모하게 되었고, 이에 따라 비용 문제가 발생할 가능성이 높다는 것을 인지하게 되었습니다. 특히, 프로필 이미지와 게시글 이미지는 크게 확대할 필요가 없었고, 기획적으로도 고해상도를 유지할 이유가 없다고 판단했습니다.

이미지 리사이징을 통해 이미지 품질에 큰 영향을 주지 않으면서도 S3 저장 공간을 절감할 수 있었고, 결과적으로 비용 절감에도 큰 효과를 보았습니다. 이 과정을 통해 리소스 최적화의 중요성을 실감하게 되었으며, 이미지 처리 및 저장 공간 관리에서 최적화를 적용하는 것이 얼마나 중요한지를 깊이 이해하게 되었습니다.

현재는 API 서버 내에서 이미지 리사이징을 처리 중이지만, 추후 성능 및 확장성을 고려해 별도의 이미지 서버를 구축할 계획도 세우고 있습니다.

#### 5. 모니터링 서버 구축과 서버 안정성

서버의 성능과 안정성을 모니터링하기 위해 Prometheus와 Grafana를 사용해 별도의 모니터링 서버를 구축하였습니다. 이를 통해 서버 상태를 실시간으로 모니터링하고, 주요 경고나 에러 발생 시 디스코드를 통해 알림을 받아 빠른 대응이 가능하도록 했습니다.

이를 통해 시스템 장애 발생 시 즉각적인 조치가 가능해져 시스템 안정성이 크게 향상되었습니다. 더불어 시스템 유지보수와 운영 효율성에 큰 기여를 했습니다.


## ⛓ Link
### API 명세서 및 관련 링크
![image](https://github.com/user-attachments/assets/759d9fec-1323-4324-a7f1-646f40c21115)
[API 명세서 링크](https://docs.google.com/spreadsheets/d/1dbc9NR9iWJA5QqbcnxBggA11os2IF-6P/edit?gid=403544037#gid=403544037)

### ERD 및 관련 링크
![20240905_180244](https://github.com/user-attachments/assets/acb3f8ba-0ae9-4546-ad50-d67ac4d6c2b9)
[ERD 링크](https://www.erdcloud.com/d/WAacjyYwg2zGhtC98)
