# DomiCare Website - Cleaning Service Platform

![Version](https://img.shields.io/badge/version-1.0.0-blue.svg)
![Status](https://img.shields.io/badge/status-in%20development-yellow.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7+-green.svg)
![JDK](https://img.shields.io/badge/JDK-11+-orange.svg)

## 📑 Mục lục
- [Giới thiệu](#giới-thiệu)
- [Tính năng chính](#tính-năng-chính)
- [Kiến trúc hệ thống](#kiến-trúc-hệ-thống)
- [Công nghệ sử dụng](#công-nghệ-sử-dụng)
- [Cấu trúc cơ sở dữ liệu](#cấu-trúc-cơ-sở-dữ-liệu)
- [Hướng dẫn cài đặt](#hướng-dẫn-cài-đặt)
- [Quy trình triển khai](#quy-trình-triển-khai)
- [API Documentation](#api-documentation)
- [Bảo mật](#bảo-mật)
- [Demo](#demo)
- [Đóng góp](#đóng-góp)
- [Liên hệ](#liên-hệ)
- [Giấy phép](#giấy-phép)

## 🌟 Giới thiệu

**DomiCare** là nền tảng web chuyên cung cấp dịch vụ dọn dẹp và bảo trì chất lượng cao, kết nối khách hàng với đội ngũ nhân viên chuyên nghiệp. Hệ thống được xây dựng trên kiến trúc hiện đại với backend Spring Boot và frontend ReactJS, mang đến trải nghiệm người dùng tối ưu và quản lý hiệu quả.

Dự án hướng đến các giá trị:
- **Tiện lợi**: Đơn giản hóa việc đặt dịch vụ dọn dẹp
- **Minh bạch**: Thông tin dịch vụ, giá cả rõ ràng
- **Chất lượng**: Đảm bảo dịch vụ đạt tiêu chuẩn cao
- **Phát triển bền vững**: Sử dụng sản phẩm thân thiện với môi trường

## 🚀 Tính năng chính

### 1. Khách hàng
- **Tìm kiếm dịch vụ**: Tìm kiếm dựa trên nhiều tiêu chí (giá cả, đánh giá, thời gian, vị trí)
- **Đặt dịch vụ**: Quy trình đặt dịch vụ liền mạch, xác thực qua OTP
- **Quản lý tài khoản**: Đăng ký, đăng nhập (hỗ trợ OAuth2), khôi phục mật khẩu
- **Lịch sử dịch vụ**: Xem chi tiết lịch sử và trạng thái các dịch vụ đã sử dụng
- **Đánh giá dịch vụ**: Đánh giá chất lượng bằng hệ thống sao và bình luận
- **Đặt lịch linh hoạt**: Đặt lịch qua form trực tuyến hoặc liên hệ trực tiếp
- **Thanh toán trực tuyến**: Hỗ trợ nhiều phương thức thanh toán an toàn

### 2. Nhân viên Sales
- **Quản lý đơn hàng**: Tiếp nhận, xử lý đơn hàng từ khách hàng
- **Chốt đơn**: Xác nhận đơn hàng, điều phối nhân viên kỹ thuật
- **Báo cáo doanh thu**: Theo dõi doanh thu theo nhiều khung thời gian
- **Quản lý khách hàng**: Xem thông tin và lịch sử giao dịch của khách hàng
- **Thông báo**: Nhận thông báo realtime khi có đơn hàng mới

### 3. Nhân viên Thi Công (Trưởng Bộ Phận Kỹ Thuật)
- **Quản lý lịch trình**: Xem và quản lý lịch trình công việc
- **Cập nhật tiến độ**: Cập nhật trạng thái công việc theo thời gian thực
- **Xác nhận hoàn thành**: Ghi nhận hình ảnh trước và sau khi hoàn thành
- **Đánh giá chất lượng**: Yêu cầu khách hàng đánh giá sau khi hoàn thành
- **Báo cáo sự cố**: Báo cáo các vấn đề phát sinh trong quá trình thực hiện

### 4. Admin
- **Quản lý toàn diện**: Bảng điều khiển thống kê tổng quan
- **Quản lý nhân viên**: Thêm, sửa, xóa, phân quyền nhân viên sales và kỹ thuật
- **Quản lý đơn hàng**: Giám sát toàn bộ đơn hàng và doanh thu
- **Quản lý khách hàng**: Quản lý thông tin và lịch sử giao dịch khách hàng
- **Quản lý dịch vụ**: CRUD các dịch vụ, phân loại, cập nhật giá
- **Quản lý nội dung**: Quản lý tin tức, bài viết, hình ảnh quảng cáo
- **Báo cáo phân tích**: Xuất báo cáo kinh doanh theo nhiều tiêu chí

## 🏗️ Kiến trúc hệ thống

DomiCare được xây dựng theo kiến trúc microservices, với các thành phần chính:

### Backend (Spring Boot)
- **API Layer**: RESTful API endpoints, xử lý request/response
- **Service Layer**: Chứa business logic của ứng dụng
- **Repository Layer**: Tương tác với database
- **Security Layer**: Xác thực và phân quyền
- **Email Service**: Xử lý gửi email tự động
- **File Storage Service**: Quản lý lưu trữ file/hình ảnh

### Frontend (ReactJS)
- **Public Portal**: Giao diện cho khách hàng
- **Admin Portal**: Giao diện cho admin quản lý hệ thống
- **Sales Portal**: Giao diện cho nhân viên sales
- **Technical Portal**: Giao diện cho nhân viên kỹ thuật

### Infrastructure
- **Database**: PostgreSQL trên Cloud
- **File Storage**: Cloudinary
- **Authentication**: JWT + OAuth2
- **Caching**: Redis (optinoal)
- **CI/CD**: GitHub Actions/Jenkins

## 💻 Công nghệ sử dụng

### Backend
- **Spring Boot**: Framework Java toàn diện để phát triển ứng dụng
- **Spring Security**: Bảo mật ứng dụng với xác thực và phân quyền
- **Spring Data JPA**: ORM framework để tương tác với database
- **JWT**: Cơ chế xác thực stateless cho REST API
- **OAuth2**: Hỗ trợ đăng nhập qua Google, Facebook
- **PostgreSQL**: Hệ quản trị cơ sở dữ liệu quan hệ
- **Hibernate**: ORM framework
- **Swagger/OpenAPI 3**: Tài liệu hóa API
- **Maven**: Quản lý dependency và build
- **JUnit & Mockito**: Unit testing
- **JavaMail**: Gửi email thông báo
- **Cloudinary**: Lưu trữ hình ảnh trên cloud
- **Lombok**: Giảm boilerplate code

### Frontend
- **ReactJS**: Framework JavaScript xây dựng UI
- **Redux**: Quản lý state
- **Axios**: HTTP client gọi API
- **React Router**: Định tuyến
- **Material-UI**: Component library
- **Formik**: Quản lý form
- **React Query**: Data fetching và caching
- **i18next**: Đa ngôn ngữ
- **Jest & React Testing Library**: Testing
- **ESLint & Prettier**: Code quality

### DevOps & Tools
- **Docker**: Containerization
- **Git**: Version control
- **Postman**: API testing
- **SonarQube**: Code quality analysis
- **Prometheus & Grafana**: Monitoring (optional)
- **ELK Stack**: Logging (optional)

## 📊 Cấu trúc cơ sở dữ liệu

### Các bảng chính
- **users**: Thông tin người dùng (khách hàng, nhân viên sales, kỹ thuật, admin)
- **roles**: Vai trò người dùng trong hệ thống
- **services**: Thông tin các dịch vụ
- **service_categories**: Phân loại dịch vụ
- **orders**: Đơn hàng dịch vụ
- **order_items**: Chi tiết đơn hàng
- **payments**: Thông tin thanh toán
- **reviews**: Đánh giá dịch vụ
- **notifications**: Thông báo hệ thống
- **media**: Lưu trữ thông tin hình ảnh, tài liệu
- **posts**: Bài viết, tin tức

### Sơ đồ quan hệ
```
users --(1:N)--> orders
orders --(N:1)--> services
users --(1:N)--> reviews
services --(1:N)--> reviews
services --(N:1)--> service_categories
orders --(1:1)--> payments
users --(N:M)--> roles
```

## 🛠️ Hướng dẫn cài đặt

### Yêu cầu hệ thống
- JDK 11 trở lên
- Maven 3.6+
- Node.js 14+ và npm 6+
- PostgreSQL 12+
- Git

### Cài đặt Backend

#### 1. Clone repository
```bash
git clone https://github.com/your-username/domicare.git
cd domicare/backend
```

#### 2. Cấu hình database
Tạo database PostgreSQL:
```sql
CREATE DATABASE domicare;
```

#### 3. Cấu hình application.properties
Tạo file `src/main/resources/application-local.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/domicare
spring.datasource.username=your-db-username
spring.datasource.password=your-db-password
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# JWT Configuration
jwt.secret=your-jwt-secret-key
jwt.expiration=86400000

# Mail Configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-email-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# OAuth2 Configuration
spring.security.oauth2.client.registration.google.client-id=your-google-client-id
spring.security.oauth2.client.registration.google.client-secret=your-google-client-secret
spring.security.oauth2.client.registration.google.scope=profile,email

# Cloudinary Configuration
cloudinary.cloud-name=your-cloud-name
cloudinary.api-key=your-api-key
cloudinary.api-secret=your-api-secret

# Server Configuration
server.port=8080
```

#### 4. Build và chạy ứng dụng
```bash
mvn clean install
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

Hoặc chạy bằng JAR file:
```bash
java -jar target/domicare-0.0.1-SNAPSHOT.jar --spring.profiles.active=local
```

### Cài đặt Frontend

#### 1. Di chuyển đến thư mục frontend
```bash
cd ../frontend
```

#### 2. Cài đặt dependencies
```bash
npm install
```

#### 3. Cấu hình API endpoint
Tạo file `.env.local`:
```
REACT_APP_API_BASE_URL=http://localhost:8080/api
REACT_APP_CLOUDINARY_URL=https://api.cloudinary.com/v1_1/your-cloud-name/upload
REACT_APP_GOOGLE_CLIENT_ID=your-google-client-id
```

#### 4. Chạy ứng dụng frontend
```bash
npm start
```

#### 5. Build cho production
```bash
npm run build
```

## 🚢 Quy trình triển khai

### Môi trường phát triển
1. **Local**: Phát triển trên máy tính cá nhân
2. **Development**: Môi trường test nội bộ
3. **Staging**: Môi trường kiểm thử UAT
4. **Production**: Môi trường chính thức

### CI/CD Pipeline
- **Commit Code**: Push code lên Git repository
- **Automated Tests**: Chạy unit tests, integration tests
- **Code Quality**: Kiểm tra code quality với SonarQube
- **Build**: Build ứng dụng với Maven/npm
- **Deploy**: Deploy lên môi trường tương ứng
- **Monitor**: Giám sát ứng dụng sau khi deploy

## 📚 API Documentation

API được tài liệu hóa bằng Swagger/OpenAPI. Sau khi chạy backend, truy cập:
```
http://localhost:8080/swagger-ui.html
```

### Các nhóm API chính
- **/api/auth**: Xác thực và phân quyền
- **/api/users**: Quản lý người dùng
- **/api/services**: Quản lý dịch vụ
- **/api/orders**: Quản lý đơn hàng
- **/api/payments**: Xử lý thanh toán
- **/api/reviews**: Đánh giá dịch vụ
- **/api/media**: Upload và quản lý media
- **/api/posts**: Quản lý bài viết

## 🔒 Bảo mật

### Nguyên tắc bảo mật:
- **Authentication**: JWT + OAuth2
- **Authorization**: Role-based Access Control (RBAC)
- **Data Protection**: HTTPS/SSL
- **Password Storage**: BCrypt hashing
- **Input Validation**: Kiểm tra và sanitize input
- **CORS Configuration**: Bảo vệ từ cross-origin requests
- **Rate Limiting**: Bảo vệ chống DOS attacks

### Quản lý Secrets:
- Sử dụng biến môi trường hoặc vault service
- Không commit secrets lên Git
- File nhạy cảm cần thêm vào .gitignore:
```
# .gitignore
application-local.properties
application-dev.properties
application-prod.properties
.env.local
.env.development
.env.production
```

## 🖥️ Demo

### Screenshots
- **Trang chủ**: [Screenshot URL]
- **Trang đăng nhập**: [Screenshot URL]
- **Trang đặt dịch vụ**: [Screenshot URL]
- **Bảng điều khiển admin**: [Screenshot URL]

### Demo Online
- **Website**: [https://domicare.example.com](https://domicare.example.com)
- **Admin Portal**: [https://admin.domicare.example.com](https://admin.domicare.example.com)

## 👥 Đóng góp

Chúng tôi chào đón mọi đóng góp! Để tham gia phát triển:

1. Fork repository
2. Tạo nhánh cho tính năng mới: `git checkout -b feature/amazing-feature`
3. Commit thay đổi: `git commit -m 'Add amazing feature'`
4. Push lên nhánh của bạn: `git push origin feature/amazing-feature`
5. Tạo Pull Request

### Quy tắc đóng góp
- Tuân thủ coding standards
- Viết unit tests cho code mới
- Cập nhật tài liệu khi cần thiết
- Mô tả chi tiết về thay đổi trong Pull Request

## 📞 Liên hệ

- **Website**: [https://domicare.example.com](https://domicare.example.com)
## 📄 Giấy phép

Dự án này được phân phối dưới giấy phép MIT. Xem file `LICENSE` để biết thêm chi tiết.
