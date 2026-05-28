# AI Development Rules – Project_01

## Electric Vehicle Maintenance Management System

---

# 1. Project Overview

Tên dự án: Project_01 – Hệ thống Quản lý Bảo dưỡng Xe Điện

Mục tiêu:
Xây dựng backend cho hệ thống quản lý bảo dưỡng xe điện với các chức năng:

- Quản lý khách hàng
- Đặt lịch bảo dưỡng
- Quản lý checklist bảo dưỡng
- Quản lý phụ tùng
- Chat realtime
- Notification
- Dashboard quản lý
- AI hỗ trợ tư vấn dịch vụ
- Quản lý hóa đơn và thanh toán

Project ưu tiên:

- Code clean
- Dễ maintain
- Dễ mở rộng
- Tối ưu chi phí AWS
- Phát triển nhanh
- Có khả năng scale sau này

---

# 2. Development Philosophy

Mọi code được tạo ra PHẢI tuân thủ:

- Clean Code
- SOLID Principles
- DRY (Don't Repeat Yourself)
- KISS (Keep It Simple)
- Separation of Concerns
- Modular Design
- Readability First
- Maintainability First

Không được:

- Hardcode dữ liệu
- Duplicate logic
- Viết code “chỉ để chạy được”
- Over-engineering khi chưa cần
- Viết business logic trực tiếp trong controller
- Viết file quá dài khó maintain
- Comment vô nghĩa

Ưu tiên:

- Simple solution trước
- Code dễ đọc
- Code dễ debug
- Code dễ mở rộng sau này

---

# 3. Architecture Rules

Kiến trúc giai đoạn đầu:

- Layered Architecture (Kiến trúc phân lớp)

Mục tiêu:

- Dễ học
- Dễ maintain
- Dễ debug
- Dễ phát triển nhanh
- Giảm complexity
- Tối ưu cho team nhỏ và MVP

Project phải chia rõ theo layer.

Ví dụ:

```bash
src/main/java/com/project/
├── controller/
├── service/
├── repository/
├── entity/
├── dto/
├── mapper/
├── validator/
├── config/
├── exception/
├── security/
└── utils/
```

Luồng xử lý:

Controller
→ Service
→ Repository
→ Database

Quy tắc:

- Controller chỉ xử lý request/response
- Service xử lý business logic
- Repository xử lý truy vấn database
- Entity dùng mapping database
- DTO dùng cho request/response
- Không viết business logic trong controller
- Không return entity trực tiếp ra API

Không được:

- Controller gọi database trực tiếp
- Business logic nằm trong repository
- Viết toàn bộ logic trong một class lớn
- Circular dependency giữa các layer

# 4. Technology Stack

Backend:

- Java Spring Boot

Database:

- MySQL

Authentication:

- JWT Authentication

Realtime:

- WebSocket

Cloud:

- AWS

AWS Services ưu tiên tối ưu chi phí:

- EC2 hoặc Lambda
- API Gateway
- S3
- CloudWatch
- RDS MySQL
- EventBridge
- SQS

Không sử dụng service AWS quá đắt nếu chưa cần thiết.

---

# 5. AI Coding Workflow Rules

TRƯỚC KHI GENERATE CODE:

AI PHẢI:

1. Giải thích rõ ràng:
   - Muốn làm gì
   - Mục tiêu của solution
   - Vì sao chọn cách làm đó
   - Luồng xử lý hoạt động như thế nào
   - Kiến trúc và design pattern sử dụng
   - Các file sẽ tạo
   - Vai trò của từng file
   - Database/table liên quan nếu có
   - API flow nếu có

2. Giải thích theo hướng:
   - Dễ học
   - Dễ hiểu
   - Dễ debug
   - Dễ maintain

3. Ưu tiên:
   - Layered Architecture đơn giản
   - Clean code
   - Readability
   - Maintainability
   - Reusable code

4. Không tự ý:
   - Chuyển sang microservice
   - Dùng Clean Architecture quá phức tạp
   - Dùng CQRS/Event Sourcing khi chưa cần
   - Over-engineering hệ thống
   - Tạo architecture phức tạp vượt quá scope hiện tại

5. Nếu yêu cầu lớn:
   - Phải chia thành step nhỏ
   - Giải thích từng step trước
   - Làm tuần tự từng phần

6. Chỉ sau khi người dùng xác nhận:
   - "accept"
   - "ok"
   - "generate"
   - "triển khai"

THÌ MỚI ĐƯỢC generate code.

Không được:

- Tự động generate code ngay
- Generate quá nhiều file không cần thiết
- Tự quyết định architecture phức tạp
- Tự thêm thư viện/framework không cần thiết

AI phải hoạt động như:

- Senior developer mentor
- Technical instructor
- Solution architect

## Không chỉ đơn giản là code generator.

# 6. Coding Standards

## Naming Rules

Tên phải rõ nghĩa.

Ví dụ tốt:

- getCustomerById()
- createBooking()
- calculateMaintenanceCost()

Không dùng:

- temp()
- data1()
- abc()

---

## Function Rules

Mỗi function:

- Chỉ nên có 1 trách nhiệm
- Không quá dài
- Dễ đọc
- Dễ debug

Nếu logic dài:

- Tách thành function nhỏ hơn

Không viết:

- Một function xử lý validate + save + notification + calculate

---

## Comment Rules

Chỉ comment khi:

- Logic khó hiểu
- Business rule quan trọng
- Technical note cần thiết

Ví dụ tốt:

```java
// Customer cannot cancel booking after technician starts inspection
```

Không comment vô nghĩa.

---

# 7. Spring Boot Rules

Không viết business logic trong:

- Controller
- Entity
- Repository

Ưu tiên:

- DTO pattern
- Service layer
- Repository layer
- Global exception handler

Không return entity trực tiếp ra API.

Sử dụng:

- Lombok hợp lý
- Validation annotation
- Constructor Injection

Không dùng:

- Field Injection
- God Service
- Static mutable state

Flow chuẩn:

Controller
→ Service
→ Repository
→ Database

---

# 8. Logging Rules

Các action quan trọng PHẢI có log:

- Login
- Booking
- Payment
- Inventory update
- Notification sending
- Error
- AI request

Log cần:

- timestamp
- userId
- action
- status
- error message nếu fail

Ví dụ:

```json
{
  "userId": "CUS_001",
  "action": "CREATE_BOOKING",
  "status": "SUCCESS",
  "timestamp": "2026-05-19T10:00:00"
}
```

Không log:

- Password
- JWT token
- Sensitive information

---

# 9. Error Handling Rules

Không được:

- throw Exception chung chung
- return null không rõ lý do
- swallow exception

Phải:

- Custom exception rõ nghĩa
- Message dễ debug
- Response error thống nhất

Ví dụ:

- BookingConflictException
- UnauthorizedException
- InventoryNotEnoughException

---

# 10. API Rules

API phải:

- RESTful
- Có versioning
- Response format thống nhất

Ví dụ:

```json
{
  "success": true,
  "message": "Booking created successfully",
  "data": {}
}
```

HTTP status phải đúng:

- 200 OK
- 201 CREATED
- 400 BAD REQUEST
- 401 UNAUTHORIZED
- 403 FORBIDDEN
- 404 NOT FOUND
- 500 INTERNAL SERVER ERROR

---

# 11. Database Rules

Database:

- MySQL

Thiết kế database phải:

- Normalize hợp lý
- Có index cho query quan trọng
- Dễ maintain
- Dễ mở rộng

Mọi table nên có:

- created_at
- updated_at
- created_by
- updated_by
- deleted_flag

Ưu tiên:

- Soft delete
- Transaction
- Query optimize

Không dùng:

- SELECT \*
- Query N+1
- Hard delete với dữ liệu quan trọng

---

# 12. Security Rules

Bắt buộc:

- JWT Authentication
- Role-based Authorization
- Input validation
- Password hashing
- SQL Injection prevention

Không hardcode:

- Secret key
- JWT secret
- Database credentials

---

# 13. AWS & Deployment Rules

Ưu tiên:

- Tối ưu chi phí
- Dễ deploy
- Dễ maintain

Deployment:

- Docker
- GitHub Actions
- AWS

Environment:

- dev
- staging
- production

Secrets:

- Dùng environment variables
- Không commit secrets lên Git

---

# 14. AWS Lambda Rules

Nếu dùng Lambda:

- Function phải stateless
- Không lưu state trong memory
- Không xử lý quá nặng trong request
- Reuse AWS SDK client nếu có thể

Ưu tiên dùng Lambda cho:

- Notification
- Email sending
- AI processing
- Background task

Không lạm dụng Lambda cho mọi thứ nếu EC2 đơn giản và rẻ hơn.

---

# 15. Async Processing Rules

Các tác vụ sau nên xử lý async:

- Notification sending
- Email sending
- AI processing
- Analytics processing

Ưu tiên:

- SQS
- EventBridge

Không xử lý task nặng trực tiếp trong API request.

---

# 16. File Upload Rules

Ảnh/video upload:

- Validate file type
- Validate file size
- Random file name
- Không dùng tên file người dùng upload

File nên upload lên:

- AWS S3

---

# 17. Performance Rules

Không được:

- Query dư thừa
- Load toàn bộ dữ liệu
- Viết API quá chậm

Phải:

- Pagination
- Lazy loading
- Async processing cho task nặng

Ưu tiên:

- Tối ưu query trước khi scale server

---

# 18. AI Integration Rules

AI chỉ hỗ trợ:

- Tư vấn dịch vụ
- Gợi ý bảo dưỡng
- Hỗ trợ khách hàng
- Forecast phụ tùng

Không để AI:

- Tự sửa dữ liệu
- Tự thanh toán
- Tự quyết định business critical actions

Mọi AI response:

- Phải validate
- Có timeout
- Có fallback

---

# 19. Realtime Rules

Realtime dùng cho:

- Chat
- Booking status
- Notification
- Technician assignment

Ưu tiên:

- WebSocket

Realtime event cần:

- Retry handling
- Reconnect handling
- Status tracking

---

# 20. AI Coding Behavior Rules

Khi AI generate code:

- Ưu tiên clean code
- Không generate code dư thừa
- Ưu tiên reusable code
- Ưu tiên simple architecture
- Tách module nếu logic lớn
- Giải thích reasoning ngắn gọn
- Nếu thiếu context → hỏi trước

Không over-engineering project khi chưa cần thiết.

---

# 21. Final Goal

Mục tiêu cuối cùng:

- Dễ phát triển
- Dễ maintain
- Dễ scale
- Tối ưu chi phí AWS
- Backend đủ sạch để production sau này
- Developer mới dễ onboard
- Có thể mở rộng cho nhiều gara trong tương lai
- Dễ migrate sang microservice nếu cần sau này

```

```
