# DomiCare Website - Cleaning Service

## Giới Thiệu
- DomiCare là nền tảng web chuyên cung cấp dịch vụ dọn dẹp và bảo trì, cho phép khách hàng dễ dàng tìm kiếm và đặt dịch vụ, đồng thời hỗ trợ quản lý thông qua các vai trò như nhân viên sales, nhân viên thi công, và admin.
- Website đang trong quá trình phát triển và hoàn thiện.

## Các Chức Năng Chính

### 1. Khách Hàng
- **Tìm kiếm dịch vụ**: Khách hàng có thể tìm kiếm dịch vụ dựa trên các yếu tố như giá cả, chất lượng, thời gian và thông tin chi tiết khác.
- **Đặt dịch vụ**: Khách hàng chọn dịch vụ và nhập số điện thoại để nhận OTP, từ đó đăng ký dịch vụ và trở thành thành viên.
- **Lịch sử dịch vụ**: Khách hàng có thể xem lịch sử các dịch vụ đã sử dụng.
- **Đánh giá dịch vụ**: Khách hàng có thể đánh giá dịch vụ qua số sao.
- **Đặt lịch**: Khách hàng có thể đặt lịch dịch vụ qua một form hoặc liên lạc trực tiếp.

### 2. Nhân Viên Sales
- **Đăng nhập**: Nhân viên sales đăng nhập vào hệ thống để tiếp nhận đơn hàng từ khách hàng.
- **Quản lý đơn hàng**: Nhân viên sales có thể chọn đơn hàng, chốt đơn và theo dõi doanh thu theo thời gian.

### 3. Nhân Viên Thi Công (Trưởng Bộ Phận Kỹ Thuật)
- **Đăng nhập**: Nhân viên thi công đăng nhập và tiếp nhận thông tin công việc.
- **Xác nhận hoàn thành công việc**: Sau khi công việc hoàn tất, nhân viên thi công có thể chụp hình và xác nhận công việc đã hoàn thành.
- **Đánh giá chất lượng**: Nhân viên có thể yêu cầu khách hàng đánh giá chất lượng dịch vụ.

### 4. Admin
- **Đăng nhập**: Admin quản lý hệ thống qua tài khoản quản trị.
- **Quản lý nhân viên sales**: Thêm, sửa hoặc xóa nhân viên sales.
- **Quản lý đơn hàng**: Quản lý tất cả các đơn hàng và theo dõi doanh thu.
- **Quản lý khách hàng**: Quản lý thông tin khách hàng.
- **Quản lý dịch vụ**: Quản lý các dịch vụ dọn dẹp vệ sinh (CRUD).
- **Quản lý tin tức và bài viết**: Đăng các bài viết, tin tức quảng bá dịch vụ và công ty.

## Công Nghệ Sử Dụng

### Backend:
- **Spring Boot**: Framework Java để phát triển ứng dụng backend.
- **Spring Security**: Cung cấp bảo mật cho ứng dụng, bao gồm xác thực và phân quyền.
- **JWT (JSON Web Token)**: Cung cấp cơ chế xác thực an toàn cho API.
- **OAuth2 với Google**: Cho phép người dùng đăng nhập bằng tài khoản Google.
- **Spring JPA (Java Persistence API)**: Quản lý dữ liệu với các tính năng query và paging.
- **Cloud PostgreSQL Database**: Cơ sở dữ liệu PostgreSQL trên nền tảng đám mây (ví dụ: Heroku, AWS RDS).
- **OpenAPI 3**: Tài liệu hóa các API bằng OpenAPI Specification để dễ dàng kiểm thử và tích hợp.
- **SSL Certificate**: Mã hóa các kết nối API để bảo mật thông tin truyền tải.
- **Postman**: Dùng để kiểm thử các API RESTful.
- **Email Service**: Sử dụng thư viện **JavaMail** trong Spring Boot để gửi email, như xác nhận đăng ký khách hàng, thông báo về tình trạng đơn hàng, v.v.

### Frontend:
- **ReactJS**: Framework JavaScript để xây dựng giao diện người dùng.
- **Axios**: Dùng để gọi API từ backend.
- **React Router**: Định tuyến các trang trong ứng dụng React.

## Kết quả ban đầu :

### Triển khai giao diện trên vercel:https://domicare-frontend.vercel.app/

### Login Page
![Login Page](https://github.com/user-attachments/assets/95a231c9-1b09-4e98-80d1-8ede7da9c198)

### Signup Page
![Signup Page](https://github.com/user-attachments/assets/45dd4d0a-73ee-4b99-8a6c-b98313311787)

### Homepage
![Homepage](https://github.com/user-attachments/assets/ca54c76a-223f-4f3f-9de1-fde5788aa2f6)

### Service Dashboard
![Service Dashboard](https://github.com/user-attachments/assets/23343f70-32cc-4426-8f8c-871648849954)

## Cấu Hình Dự Án

### Cài Đặt Backend (Spring Boot)
1. Cài đặt JDK 11+ và Maven.
2. Cấu hình `application.properties` cho Spring Boot, bao gồm:
   - Thông tin kết nối tới cơ sở dữ liệu **PostgreSQL** trên đám mây.
   - Cấu hình **JWT** để bảo vệ các API.
   - Cấu hình **OAuth2** để cho phép đăng nhập qua Google.
   - Cấu hình **JavaMail** để gửi email, ví dụ: gửi email xác nhận đăng ký hoặc thông báo đơn hàng.
   
3. Tạo cơ sở dữ liệu trên **Cloud PostgreSQL** (Heroku, AWS RDS, hoặc Google Cloud).
4. Tạo API với Spring JPA và hỗ trợ phân trang với `PagingAndSortingRepository` trong Spring Data JPA.
5. Mã hóa kết nối API bằng **SSL Certificate**.

### Cài Đặt Frontend (ReactJS)
1. Cài đặt [Node.js](https://nodejs.org/) và [npm](https://www.npmjs.com/).
2. Clone repo và cài đặt các dependencies:
   ```bash
   cd frontend
   npm install
