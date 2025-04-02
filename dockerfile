# 1단계: 빌드 단계 (Gradle wrapper 사용)
FROM gradle:8.5-jdk17 AS builder
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew clean build -x test

# 2단계: 실제 실행 이미지
FROM eclipse-temurin:17-jdk
WORKDIR /app

# 위에서 빌드된 JAR 복사
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]