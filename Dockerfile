# Sử dụng image OpenJDK 17
FROM openjdk:25-jdk

# Đặt thông tin tác giả
LABEL maintainer="anhnon0106@gmail.com"

# Tạo thư mục làm việc trong container
WORKDIR /app

# Sao chép file keystore vào container (nếu cần HTTPS)
COPY src/main/resources/keystore.p12 /app/keystore.p12

# Sao chép file JAR vào container
COPY target/domicare-0.0.1-SNAPSHOT.jar app.jar

# Chạy ứng dụng Spring Boot với các biến môi trường từ Docker Compose
ENTRYPOINT ["java", "-Dserver.ssl.key-store=/app/keystore.p12", "-Dserver.ssl.key-store-password=151605", "-Dserver.ssl.key-alias=myalias", "-Dserver.ssl.key-store-type=PKCS12", "-jar", "/app/app.jar"]

# Mở cổng 8443 cho HTTPS
EXPOSE 8443
