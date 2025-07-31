
# DomiCare Website

![Version](https://img.shields.io/badge/version-1.0.0-blue.svg) ![Status](https://img.shields.io/badge/status-in%20development-yellow.svg) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7+-green.svg) ![JDK](https://img.shields.io/badge/JDK-11+-orange.svg)

> **Nền tảng kết nối khách hàng với dịch vụ dọn dẹp và bảo trì chuyên nghiệp, hiện đại, minh bạch và an toàn.**

---

## 📑 Mục lục
- [Giới thiệu](#giới-thiệu)
- [Tính năng chính](#tính-năng-chính)
- [Kiến trúc hệ thống](#kiến-trúc-hệ-thống)
- [Công nghệ sử dụng](#công-nghệ-sử-dụng)
- [Cấu trúc cơ sở dữ liệu](#cấu-trúc-cơ-sở-dữ-liệu)
- [Hướng dẫn cài đặt](#hướng-dẫn-cài-đặt)
- [Hướng dẫn chạy nhanh](#hướng-dẫn-chạy-nhanh)
- [Quy trình triển khai](#quy-trình-triển-khai)
- [API Documentation](#api-documentation)
- [Bảo mật](#bảo-mật)
- [Demo](#demo)
- [Đóng góp](#đóng-góp)
- [Liên hệ](#liên-hệ)
- [Giấy phép](#giấy-phép)


## 🌟 Giới thiệu

**DomiCare** là nền tảng web giúp khách hàng dễ dàng đặt dịch vụ dọn dẹp, bảo trì nhà cửa với quy trình minh bạch, giá cả rõ ràng, chất lượng đảm bảo và đội ngũ nhân viên chuyên nghiệp. Hệ thống sử dụng công nghệ hiện đại (Spring Boot, ReactJS) để tối ưu trải nghiệm và hiệu quả quản lý.

**Giá trị cốt lõi:**
- 🚀 **Tiện lợi**: Đặt dịch vụ nhanh chóng, mọi lúc mọi nơi
- 🔎 **Minh bạch**: Thông tin dịch vụ, giá cả rõ ràng
- ⭐ **Chất lượng**: Đội ngũ chuyên nghiệp, quy trình kiểm soát chất lượng
- 🌱 **Bền vững**: Ưu tiên sản phẩm thân thiện môi trường


## 🚀 Tính năng chính

### 👤 Khách hàng
- **Đăng nhập/Đăng ký:**
  - Tạo tài khoản bằng email, số điện thoại
  - Hỗ trợ quên mật khẩu, đặt lại mật khẩu qua email
- **Quản lý tài khoản cá nhân:**
  - Cập nhật thông tin cá nhân
  - Xem lịch sử đặt dịch vụ
- **Xem danh mục dịch vụ:**
  - Hiển thị danh sách dịch vụ (hình ảnh, giá, mô tả)
  - Tìm kiếm, lọc, sắp xếp dịch vụ theo từ khóa, danh mục, giá, đánh giá, phổ biến
- **Đặt lịch:**
  - Chọn dịch vụ, nhập thông tin địa chỉ, ghi chú, thời gian mong muốn
- **Đánh giá dịch vụ:**
  - Đánh giá, bình luận về dịch vụ đã đặt
  - Hệ thống xếp hạng dịch vụ theo đánh giá khách hàng

### 🕵️ Khách vãng lai
- Xem, tìm kiếm dịch vụ
- Đăng ký/đăng nhập
- Để lại thông tin để được tư vấn

### 💼 Nhân viên Tiếp thị (Sales)
- Quản lý đơn đặt lịch, khách hàng
- Quản lý tài khoản cá nhân, cập nhật thông tin
- Quản lý tư vấn khách hàng, xem danh sách đơn đã tư vấn
- Báo cáo doanh thu, nhận thông báo realtime

### 🛠️ Nhân viên Thi Công
- Quản lý lịch trình, cập nhật tiến độ
- Xác nhận hoàn thành, báo cáo sự cố

### 🛡️ Quản trị viên (Admin)
- Quản lý danh mục dịch vụ (thêm, sửa, xóa, hình ảnh, giá, mô tả)
- Quản lý khách hàng (xem danh sách, thông tin)
- Quản lý giảm giá, khuyến mãi (thêm, sửa, xóa)
- Quản lý nhân viên (xem danh sách, phân quyền)
- Quản lý tư vấn khách hàng
- Xử lý khiếu nại, hỗ trợ khách hàng

---

## 👥 Các tác nhân hệ thống & phân quyền

| Tác nhân             | Chức năng chính                                                                                 |
|----------------------|------------------------------------------------------------------------------------------------|
| Khách vãng lai       | Đăng ký/đăng nhập, xem/tìm kiếm dịch vụ, để lại thông tin tư vấn                                 |
| Khách đã đăng ký     | Đặt dịch vụ, quản lý tài khoản, cập nhật thông tin, đánh giá dịch vụ, xem lịch sử               |
| Nhân viên tiếp thị   | Quản lý đơn đặt lịch, tư vấn khách hàng, quản lý tài khoản, cập nhật thông tin, báo cáo         |
| Nhân viên thi công   | Quản lý lịch trình, cập nhật tiến độ, xác nhận hoàn thành, báo cáo sự cố                        |
| Quản trị viên (Admin)| Quản lý dịch vụ, khách hàng, nhân viên, giảm giá, tư vấn, khiếu nại, hỗ trợ, phân quyền        |

---
## ⚡ Hướng dẫn chạy nhanh

```bash
# Backend
mvnw spring-boot:run -Dspring-boot.run.profiles=local

# Frontend (giả sử đã cài Node.js)
cd frontend
npm install && npm start
```

> Xem chi tiết cấu hình trong phần "Hướng dẫn cài đặt" bên dưới.

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
Danh mục dịch vụ sẽ hiển thị các danh mục hiện có của hệ thống và trong mỗi danh mục sẽ hiển thị ra các dịch vụ để khách hàng có thể lựa chọn. Ngoài ra hệ thống còn hỗ trợ tìm kiếm các dịch vụ theo tên, sắp xếp các dịch vụ theo nhiều tiêu chí khác nhau giúp khách hàng dễ dàng thuận tiện trong việc căn nhắc lựa chọn dịch vụ.
<img width="852" height="1114" alt="image" src="https://github.com/user-attachments/assets/4dbe0b06-b39e-4abd-a753-f3b7dbfc0242" />
Chi tiết về dịch vụ sẽ được miêu tả và được minh họa thông qua các hình ảnh được chụp chân thật từ những lần thực hiện dịch vụ trước đó. Đồng thời còn hiển thị giá cả và giảm giá để khách hàng có thể biết.
<img width="860" height="574" alt="image" src="https://github.com/user-attachments/assets/067ebd72-a9bf-45da-893b-c826262aa8c7" />
<img width="862" height="436" alt="image" src="https://github.com/user-attachments/assets/2a28922c-2d41-45cd-bfb2-fa9ca7169aae" />
<img width="864" height="428" alt="image" src="https://github.com/user-attachments/assets/a43ee8b6-dd92-4b04-863b-c0037e514d94" />
<img width="864" height="458" alt="image" src="https://github.com/user-attachments/assets/4f69d4d7-0c7d-4bce-9de3-989fcfa36fd7" />

ii.Phần lịch sử dịch vụ: nơi hiển thi các dịch vụ mà khách hàng đã từng sử dụng. 
<img width="852" height="1210" alt="image" src="https://github.com/user-attachments/assets/4378f121-c8e8-4917-978b-07055e9556ee" />

6.2.3.Đối với nhân viên sale :
a.Xem danh sách tất cả đơn hàng:
Danh sách giúp nhân viên sale dễ dàng nắm bắt được đơn hàng nào đang cần được tư vấn để có thể kịp thời liên hệ tránh trường hợp để đơn hàng tồn đọng quá lâu gây mất thiện cảm từ khách hàng.
<img width="912" height="862" alt="image" src="https://github.com/user-attachments/assets/28f79f0c-71fe-45b6-ac76-234286985d0b" />

<img width="872" height="210" alt="image" src="https://github.com/user-attachments/assets/9e930bd7-1be5-4288-8a25-0463b297d569" />
<img width="888" height="978" alt="image" src="https://github.com/user-attachments/assets/bece93f7-693e-47de-b12e-cc465c023ef7" />

Giao diện email gửi đến khách hàng 
<img width="748" height="460" alt="image" src="https://github.com/user-attachments/assets/2c415aca-68f1-4197-b4f8-906e3a60b3f7" />

Giao diện báo cáo & thống kê tổng quan cung cấp cho quản trị viên và nhân viên sale cái nhìn trực quan về hiệu suất hoạt động của hệ thống thông qua các số liệu như doanh thu, số lượng đơn đặt, lượng khách hàng mới và biểu đồ đánh giá dịch vụ.
<img width="854" height="856" alt="image" src="https://github.com/user-attachments/assets/49ce3d2c-6f2c-41a6-a440-823bfa3ca94b" />

Quản lý nhân viên: bao gồm các chức năng thêm, sửa, xóa nhân viên trên hệ thống, ngoài ra còn hỗ trợ hiển thị thông tin.
<img width="846" height="474" alt="image" src="https://github.com/user-attachments/assets/cf7a3507-5a46-43cf-9f25-1e718f9818a5" />
<img width="854" height="474" alt="image" src="https://github.com/user-attachments/assets/43354584-ca98-4a02-a524-8320130647a9" />

ii.Dịch vụ: bao gồm các chức năng thêm, sửa, xóa danh mục trên hệ thống, ngoài ra còn hỗ trợ tìm kiếm và hiển thị thông tin.
<img width="846" height="926" alt="image" src="https://github.com/user-attachments/assets/b6aff725-d7a3-4840-912d-1dfe7d884505" />
Webisite Cho phép admin tạo mới một dịch vụ bằng cách nhập tên, mô tả, giá, mức giảm giá, chọn danh mục tương ứng và tải lên hình ảnh minh hoạ. Dữ liệu sẽ được lưu vào hệ thống và cập nhật giao diện sau khi nhấn “Thêm mới”.
<img width="872" height="684" alt="image" src="https://github.com/user-attachments/assets/99bbc15a-5867-428e-9cb7-5e8b34d37333" />
Để hỗ trợ quản trị viên dễ dàng cập nhật thông tin dịch vụ, hệ thống cung cấp một giao diện chỉnh sửa trực quan và thân thiện. Form này cho phép thay đổi các thuộc tính cơ bản như tên, mô tả, giá cả, mức giảm giá và hình ảnh minh họa của dịch vụ.
<img width="872" height="684" alt="image" src="https://github.com/user-attachments/assets/c3dd130b-35ac-45b0-997b-db47dc804bc6" />

Danh sách giúp nhân viên sale dễ dàng nắm bắt được đơn hàng nào đang cần được tư vấn để có thể kịp thời liên hệ tránh trường hợp để đơn hàng tồn đọng quá lâu gây mất thiện cảm từ khách hàng.
<img width="872" height="1126" alt="image" src="https://github.com/user-attachments/assets/f67d3206-797d-40a9-a915-b8ed756eddec" />





