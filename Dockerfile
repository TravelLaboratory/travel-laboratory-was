# ---------- [1단계: 빌드 전용 스테이지] ----------
FROM gradle:8.5-jdk21 AS builder
WORKDIR /app

# 불필요한 파일은 제외하여 캐시 효율 증가
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
RUN gradle dependencies --no-daemon || return 0

# 나머지 소스 복사
COPY . .
RUN gradle clean build -x test --no-daemon

# ---------- [2단계: 실행 전용 스테이지] ----------
FROM openjdk:21-slim
WORKDIR /app

# 빌드된 jar만 복사 → 이미지 최소화
COPY --from=builder /app/build/libs/*.jar app.jar

# 실행
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
