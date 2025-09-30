# 1. 베이스 이미지 선택 (Java 17이 설치된 가벼운 리눅스)
FROM openjdk:17-jdk-slim

# 2. 컨테이너 내부의 작업 폴더를 /app으로 지정
WORKDIR /app

# 3. 빌드된 .jar 파일을 컨테이너 안으로 복사
# build/libs/ 폴더 안의 모든 .jar 파일을 app.jar 라는 이름으로 복사합니다.
COPY build/libs/*.jar app.jar

# 4. 컨테이너가 시작될 때 실행될 명령어 설정
# "java -jar app.jar" 명령으로 우리 앱을 실행합니다.
ENTRYPOINT ["java", "-jar", "app.jar"]

# 5. 우리 앱이 8080 포트를 사용한다고 외부에 알림
EXPOSE 8080
