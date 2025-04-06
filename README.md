
# DomiCare Website - Cleaning Service

## Giới thiệu
DomiCare là một nền tảng web chuyên cung cấp dịch vụ dọn dẹp và bảo trì, giúp khách hàng dễ dàng tìm kiếm và đặt dịch vụ. Đồng thời, hệ thống hỗ trợ quản lý thông qua các vai trò như nhân viên sales, nhân viên thi công và admin. Website hiện đang trong giai đoạn phát triển và hoàn thiện.

## Các chức năng chính

### 1. Khách hàng
- **Tìm kiếm dịch vụ**: Khách hàng có thể tìm kiếm dịch vụ dựa trên giá cả, chất lượng, thời gian và thông tin chi tiết khác.
- **Đặt dịch vụ**: Khách hàng chọn dịch vụ, nhập số điện thoại để nhận OTP, đăng ký dịch vụ và trở thành thành viên.
- **Lịch sử dịch vụ**: Xem lịch sử các dịch vụ đã sử dụng.
- **Đánh giá dịch vụ**: Đánh giá dịch vụ bằng số sao.
- **Đặt lịch**: Đặt lịch dịch vụ qua form hoặc liên lạc trực tiếp.

### 2. Nhân viên Sales
- **Đăng nhập**: Đăng nhập để tiếp nhận đơn hàng từ khách hàng.
- **Quản lý đơn hàng**: Chọn đơn hàng, chốt đơn và theo dõi doanh thu theo thời gian.

### 3. Nhân viên Thi Công (Trưởng Bộ Phận Kỹ Thuật)
- **Đăng nhập**: Đăng nhập và tiếp nhận thông tin công việc.
- **Xác nhận hoàn thành**: Chụp hình và xác nhận công việc sau khi hoàn tất.
- **Đánh giá chất lượng**: Yêu cầu khách hàng đánh giá chất lượng dịch vụ.

### 4. Admin
- **Đăng nhập**: Quản lý hệ thống qua tài khoản quản trị.
- **Quản lý nhân viên sales**: Thêm, sửa hoặc xóa nhân viên sales.
- **Quản lý đơn hàng**: Theo dõi tất cả đơn hàng và doanh thu.
- **Quản lý khách hàng**: Quản lý thông tin khách hàng.
- **Quản lý dịch vụ**: Quản lý các dịch vụ dọn dẹp vệ sinh (CRUD).
- **Quản lý tin tức và bài viết**: Đăng bài viết, tin tức để quảng bá dịch vụ và công ty.

## Công nghệ sử dụng

### Backend
- **Spring Boot**: Framework Java để phát triển ứng dụng backend.
- **Spring Security**: Bảo mật ứng dụng, bao gồm xác thực và phân quyền.
- **JWT (JSON Web Token)**: Cơ chế xác thực an toàn cho API.
- **OAuth2 với Google**: Hỗ trợ đăng nhập bằng tài khoản Google.
- **Spring JPA**: Quản lý dữ liệu với tính năng query và phân trang.
- **Cloud PostgreSQL Database**: Cơ sở dữ liệu PostgreSQL trên nền tảng đám mây (Heroku, AWS RDS, v.v.).
- **OpenAPI 3**: Tài liệu hóa API bằng OpenAPI Specification.
- **SSL Certificate**: Mã hóa kết nối API để bảo mật.
- **Postman**: Kiểm thử API RESTful.
- **Email Service**: Sử dụng JavaMail trong Spring Boot để gửi email (xác nhận đăng ký, thông báo đơn hàng, v.v.).

### Frontend
- **ReactJS**: Framework JavaScript để xây dựng giao diện người dùng.
- **Axios**: Gọi API từ backend.
- **React Router**: Định tuyến các trang trong ứng dụng React.

## Kết quả ban đầu
- **Triển khai giao diện**: DomiCare Frontend

    - **Login Page**:
    - **Signup Page**:
    - **Homepage**:
    - **Service Dashboard**:

## Cấu hình dự án

### Cài đặt Backend (Spring Boot)

**Yêu cầu**:
- Cài đặt JDK 11+ và Maven.

**Cấu hình `application.properties`:**
- Kết nối tới cơ sở dữ liệu PostgreSQL trên đám mây.
- Cấu hình JWT để bảo vệ API.
- Cấu hình OAuth2 để đăng nhập qua Google.
- Cấu hình JavaMail để gửi email.

**Tạo cơ sở dữ liệu**:
- Sử dụng Cloud PostgreSQL (Heroku, AWS RDS, Google Cloud).

**API**: Tạo API với Spring JPA, hỗ trợ phân trang bằng `PagingAndSortingRepository`.

**Bảo mật**: Mã hóa kết nối API bằng SSL Certificate.

#### a. Database Configuration
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/domicare
spring.datasource.username=your-db-username
spring.datasource.password=your-db-password
spring.datasource.driver-class-name=org.postgresql.Driver
```
Thay `your-db-username` và `your-db-password` bằng thông tin đăng nhập của bạn.

#### b. JWT Configuration
```properties
jwt.secretKey=your-jwt-secret-key
jwt.expirationMinutes=15
```
Thay `your-jwt-secret-key` bằng khóa bí mật của bạn.

#### c. Email Configuration
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-email-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.ssl.trust=smtp.gmail.com
spring.mail.properties.mail.smtp.ssl.protocols=TLSv1.2
spring.mail.properties.mail.smtp.ssl.checkserveridentity=false
```
Thay `your-email@gmail.com` và `your-email-password` bằng thông tin email của bạn.

#### d. Cloudinary Configuration
```properties
cloudinary.url=cloudinary://your-api-key:your-api-secret@your-cloud-name
```
Thay `your-api-key`, `your-api-secret`, và `your-cloud-name` bằng thông tin từ Cloudinary.

#### e. OAuth2 Configuration (Google)
```properties
spring.security.oauth2.client.registration.google.client-id=your-google-client-id
spring.security.oauth2.client.registration.google.client-secret=your-google-client-secret
spring.security.oauth2.client.registration.google.scope=profile,email
```
Thay `your-google-client-id` và `your-google-client-secret` bằng thông tin từ Google Developer Console.

#### f. Cài đặt và chạy
- **Cài đặt phụ thuộc**:
    ```bash
    mvn clean install
    ```

- **Chạy ứng dụng**:
    ```bash
    mvn spring-boot:run
    ```

- **Kích hoạt profile (ví dụ: dev)**:
    ```bash
    java -jar domicare.jar --spring.profiles.active=dev
    ```

#### g. Cấu hình SSL
```properties
server.ssl.enabled=true
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=your-keystore-password
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=your-key-alias
```

#### h. Kiểm tra API
- Truy cập Swagger UI sau khi chạy ứng dụng: `http://localhost:8080/swagger-ui.html`

### Cài đặt Frontend (ReactJS)

**Yêu cầu**:
- Cài đặt Node.js và npm.

**Cài đặt dự án**:
```bash
cd frontend
npm install
npm start
```

## Bảo mật
Không commit thông tin nhạy cảm (mật khẩu, khóa API) lên Git. Thêm vào `.gitignore`:
```bash
# .gitignore
application.properties
application-dev.properties
```


## Bảo mật
Không commit thông tin nhạy cảm (mật khẩu, khóa API) lên Git. Thêm vào `.gitignore`:
```bash
# .gitignore
application.properties
application-dev.properties
```

#### a. Database Configuration
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/domicare
spring.datasource.username=your-db-username
spring.datasource.password=your-db-password
spring.datasource.driver-class-name=org.postgresql.Driver
```
Thay `your-db-username` và `your-db-password` bằng thông tin đăng nhập của bạn.

#### b. JWT Configuration
```properties
jwt.secretKey=your-jwt-secret-key
jwt.expirationMinutes=15
```
Thay `your-jwt-secret-key` bằng khóa bí mật của bạn.

#### c. Email Configuration
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-email-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.ssl.trust=smtp.gmail.com
spring.mail.properties.mail.smtp.ssl.protocols=TLSv1.2
spring.mail.properties.mail.smtp.ssl.checkserveridentity=false
```
Thay `your-email@gmail.com` và `your-email-password` bằng thông tin email của bạn.

#### d. Cloudinary Configuration
```properties
cloudinary.url=cloudinary://your-api-key:your-api-secret@your-cloud-name
```
Thay `your-api-key`, `your-api-secret`, và `your-cloud-name` bằng thông tin từ Cloudinary.

#### e. OAuth2 Configuration (Google)
```properties
spring.security.oauth2.client.registration.google.client-id=your-google-client-id
spring.security.oauth2.client.registration.google.client-secret=your-google-client-secret
spring.security.oauth2.client.registration.google.scope=profile,email
```
Thay `your-google-client-id` và `your-google-client-secret` bằng thông tin từ Google Developer Console.

#### f. Cài đặt và chạy
- **Cài đặt phụ thuộc**:
    ```bash
    mvn clean install
    ```

- **Chạy ứng dụng**:
    ```bash
    mvn spring-boot:run
    ```

- **Kích hoạt profile (ví dụ: dev)**:
    ```bash
    java -jar domicare.jar --spring.profiles.active=dev
    ```

#### g. Cấu hình SSL
```properties
server.ssl.enabled=true
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=your-keystore-password
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=your-key-alias
```

#### h. Kiểm tra API
- Truy cập Swagger UI sau khi chạy ứng dụng: `http://localhost:8080/swagger-ui.html`
