# PROJECT_REQUIREMENTS.md

# Project_01 – Electric Vehicle Maintenance Management System

---

# 1. Project Overview

## Project Name

Project_01 – Hệ thống Quản lý Bảo dưỡng Xe Điện

---

## Project Goal

Xây dựng hệ thống backend quản lý bảo dưỡng xe điện cho gara với các chức năng:

* Quản lý khách hàng
* Quản lý lịch hẹn bảo dưỡng
* Quản lý kỹ thuật viên
* Quản lý checklist bảo dưỡng
* Quản lý phụ tùng và kho hàng
* Quản lý hóa đơn và thanh toán
* Notification realtime
* Chat realtime
* Dashboard thống kê
* AI hỗ trợ tư vấn dịch vụ
* AI forecasting phụ tùng

Hệ thống cần:

* Dễ maintain
* Dễ mở rộng
* Dễ scale trong tương lai
* Tối ưu chi phí AWS
* Production-ready architecture
* Hỗ trợ nhiều gara trong tương lai

---

# 2. System Actors

## 2.1 Customer

Khách hàng sử dụng ứng dụng để:

* Đặt lịch bảo dưỡng
* Theo dõi trạng thái bảo dưỡng
* Nhận thông báo
* Chat với nhân viên tư vấn
* Xem lịch sử bảo dưỡng
* Xem hóa đơn
* Thanh toán online
* Đánh giá dịch vụ
* Nhận AI recommendation cho gói bảo dưỡng

---

## 2.2 Manager (Garage Owner)

Chủ gara quản lý:

* Nhân sự
* Kỹ thuật viên
* Phụ tùng
* Kho hàng
* Dashboard doanh thu
* Dashboard lợi nhuận
* Phân công công việc
* Checklist bảo dưỡng
* Dịch vụ bảo dưỡng
* Thống kê hiệu suất nhân viên
* AI forecasting phụ tùng

---

## 2.3 Technician

Kỹ thuật viên thực hiện:

* Nhận công việc bảo dưỡng
* Thực hiện checklist
* Cập nhật tiến độ
* Upload hình ảnh/video bằng chứng
* Đề xuất thay thế linh kiện
* Theo dõi trạng thái công việc
* Pause/Resume công việc
* Hoàn tất bảo dưỡng

---

## 2.4 Service Advisor

Nhân viên cố vấn dịch vụ thực hiện:

* Quản lý lịch hẹn
* Chat tư vấn khách hàng
* Tạo phiếu tiếp nhận xe
* Theo dõi tiến độ bảo dưỡng
* Xuất hóa đơn
* Quản lý order khách hàng

---

## 2.5 Admin

Admin hệ thống thực hiện:

* Quản lý tài khoản gara
* Quản lý permissions
* Backup dữ liệu
* Theo dõi logs hệ thống
* Theo dõi trạng thái server
* Quản lý notification templates

---

# 3. Functional Requirements

# 3.1 Customer Features

## Booking Maintenance

Khách hàng có thể:

* Đặt lịch bảo dưỡng
* Chọn thời gian
* Chọn dịch vụ
* Ghi chú tình trạng xe
* Đặt dịch vụ giao nhận xe tại nhà

---

## Maintenance Tracking

Khách hàng có thể:

* Theo dõi trạng thái bảo dưỡng realtime
* Xem checklist đã thực hiện
* Xem hình ảnh/video từ technician
* Nhận notification trạng thái

---

## Notification

Hệ thống gửi notification cho khách hàng khi:

* Đặt lịch thành công
* Lịch được xác nhận
* Technician bắt đầu kiểm tra
* Chờ phụ tùng
* Hoàn tất bảo dưỡng
* Có hóa đơn mới
* Nhắc lịch bảo dưỡng định kỳ

---

## AI Recommendation

AI hỗ trợ:

* Gợi ý gói bảo dưỡng
* Gợi ý kiểm tra linh kiện
* Dự đoán lịch bảo dưỡng tiếp theo

---

## Realtime Chat

Khách hàng có thể:

* Chat với service advisor
* Nhận hỗ trợ realtime

---

# 3.2 Manager Features

## Dashboard

Manager có thể xem:

* Doanh thu theo tháng/năm
* Lợi nhuận
* Top dịch vụ phổ biến
* Hiệu suất technician
* Thời gian sửa chữa trung bình

---

## Staff Management

Manager có thể:

* Tạo tài khoản nhân viên
* Gán role
* Gán kỹ năng cho technician
* Phân công công việc

---

## Inventory Management

Manager có thể:

* Quản lý tồn kho
* Nhập linh kiện
* Theo dõi lịch sử nhập xuất
* Theo dõi linh kiện sắp hết
* Nhận AI forecasting phụ tùng

---

## Checklist Management

Manager có thể:

* Tạo checklist bảo dưỡng
* Upload file Excel để tạo checklist
* Chỉnh sửa checklist

---

# 3.3 Technician Features

## Maintenance Workflow

Technician có thể:

* Start maintenance
* Pause maintenance
* Resume maintenance
* Finish maintenance

---

## Checklist Workflow

Technician có thể:

* Thực hiện checklist
* Đánh dấu completed
* Ghi chú kết quả
* Upload ảnh/video
* Đề xuất thay thế linh kiện

---

## Progress Tracking

Technician có thể cập nhật trạng thái:

* Checking
* Waiting for parts
* In progress
* Completed

---

# 3.4 Service Advisor Features

## Appointment Management

Service advisor có thể:

* Tạo lịch hẹn
* Chỉnh sửa lịch hẹn
* Hủy lịch hẹn
* Theo dõi lịch hẹn

---

## Vehicle Reception

Service advisor có thể:

* Tạo phiếu tiếp nhận xe
* Ghi nhận tình trạng xe
* Gửi phiếu cho technician

---

## Invoice Management

Service advisor có thể:

* Xuất hóa đơn
* Theo dõi thanh toán

---

# 3.5 Admin Features

## System Management

Admin có thể:

* Quản lý tài khoản gara
* Quản lý permissions
* Quản lý notification templates
* Backup dữ liệu

---

## Monitoring

Admin có thể:

* Xem system logs
* Xem server status
* Theo dõi hoạt động hệ thống

---

# 4. Business Rules

## Booking Rules

* Customer không thể đặt lịch trùng thời gian
* Booking chỉ được cancel trước khi technician bắt đầu kiểm tra
* Manager xác nhận lịch trước khi assign technician

---

## Maintenance Rules

* Technician phải hoàn thành checklist trước khi finish maintenance
* Maintenance không thể completed nếu còn checklist pending

---

## Inventory Rules

* Inventory tự động giảm khi checklist sử dụng linh kiện hoàn tất
* Không cho phép inventory âm

---

## Invoice Rules

* Invoice chỉ được tạo sau khi maintenance completed

---

# 5. Realtime Features

Realtime sử dụng cho:

* Chat
* Booking status updates
* Notification
* Technician assignment
* Maintenance progress updates

---

# 6. AI Features

AI hỗ trợ:

* Service recommendation
* Maintenance prediction
* Inventory forecasting
* Customer support assistant

AI không được:

* Tự động thay đổi dữ liệu hệ thống
* Tự động xử lý thanh toán
* Tự quyết định business critical actions

---

# 7. File Upload Requirements

Hệ thống hỗ trợ upload:

* Images
* Videos
* Documents

Upload dùng cho:

* Maintenance evidence
* Checklist attachments
* Invoice documents

---

# 8. Notification Requirements

Hệ thống hỗ trợ:

* Push notification
* Email notification
* In-app notification

Notification phải:

* Có retry strategy
* Có delivery tracking
* Có template system

---

# 9. Security Requirements

Hệ thống yêu cầu:

* JWT Authentication
* Role-based Authorization
* Input validation
* Secure file upload
* Audit logging

---

# 10. Scalability Goals

Tương lai hệ thống cần hỗ trợ:

* Multiple garages
* Large concurrent users
* High realtime traffic
* AI integrations
* Microservice migration nếu cần

---

# 11. Non-Functional Requirements

System cần:

* High availability
* Easy maintainability
* Scalable architecture
* Fast response time
* Cost optimization trên AWS
* Easy onboarding for developers

---

# 12. Initial Architecture Direction

Giai đoạn đầu:

* Monolithic Architecture
* Layered Architecture
* Spring Boot
* MySQL
* WebSocket
* AWS deployment

Ưu tiên:

* MVP development
* Fast iteration
* Simplicity
* Maintainability

Không over-engineering trong giai đoạn đầu.
