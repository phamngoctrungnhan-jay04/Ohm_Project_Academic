# DATABASE_DESIGN.md - THIẾT KẾ CƠ SỞ DỮ LIỆU TỔNG THỂ
## Dự án: Hệ thống Quản lý Bảo dưỡng Xe Điện (Project_01)
---

> [!IMPORTANT]
> **Triết lý thiết kế Database:** 
> * Sử dụng **MySQL 8.0** làm RDBMS chính.
> * Tuân thủ quy tắc phân rã nghiệp vụ theo **Bounded Context (Domain-Driven Design)**. 
> * Hạn chế tối đa các ràng buộc cứng (Foreign Keys) liên kết chéo giữa các Module nghiệp vụ khác nhau để sẵn sàng chia tách thành các database độc lập khi hệ thống phát triển lên **Microservices**.
> * Giải quyết triệt để các vấn đề đồng thời (Concurrency) và an toàn tài chính ở cấp độ thiết kế bảng.

---

## 1. Quy ước đặt tên (Naming Conventions)

Để đảm bảo mã nguồn và database đồng bộ, dễ đọc, toàn bộ thực thể database tuân theo quy chuẩn đặt tên sau:
* **Tên bảng (Table Names):** Sử dụng danh từ số nhiều, viết thường, cách nhau bằng dấu gạch dưới (Snake_case). Ví dụ: `users`, `spare_parts`, `repair_orders`.
* **Tên cột (Column Names):** Viết thường, sử dụng Snake_case. Ví dụ: `first_name`, `created_at`, `deleted_flag`.
* **Khóa chính (Primary Key):** Luôn đặt tên là `id` với kiểu dữ liệu `BIGINT`, cơ chế tự động tăng (`AUTO_INCREMENT`).
* **Khóa ngoại (Foreign Key):** Tên bảng số ít + `_id`. Ví dụ: `user_id`, `booking_id`.
* **Cờ Boolean (Boolean Flags):** Sử dụng tiền tố `is_` hoặc hậu tố `_flag`. Ví dụ: `deleted_flag`, `is_active`.

---

## 2. Danh sách toàn bộ các bảng trong hệ thống (Table List)

Hệ thống được chia thành **8 nhóm bảng** tương ứng với 8 Bounded Context:

```
                  ┌───────────────────────────────┐
                  │      1. USER & AUTH CONTEXT   │
                  │  - users, roles, user_roles   │
                  └──────────────┬────────────────┘
                                 │
  ┌──────────────────────────────┼──────────────────────────────┐
  │                              │                              │
  ▼                              ▼                              ▼
┌──────────────────────┐ ┌──────────────────────┐ ┌──────────────────────┐
│  2. BOOKING CONTEXT  │ │  3. RECEPTION & RO   │ │ 4. INVENTORY CONTEXT │
│  - bookings          │ │ - vehicle_receptions │ │ - spare_parts        │
└──────────────────────┘ │ - repair_orders      │ │ - inv_transactions   │
                         └───────┬──────────────┘ └──────────────────────┘
                                 │
  ┌──────────────────────────────┼──────────────────────────────┐
  │                              │                              │
  ▼                              ▼                              ▼
┌──────────────────────┐ ┌──────────────────────┐ ┌──────────────────────┐
│  5. CHECKLIST CTX    │ │  6. BILLING CONTEXT  │ │ 7. REALTIME & COMM   │
│  - chk_templates     │ │ - quotations         │ │ - chat_rooms         │
│  - chk_item_temps    │ │ - quotation_details  │ │ - chat_messages      │
│  - mt_checklists     │ │ - invoices           │ │ - notifications      │
│  - mt_checklist_items│ └──────────────────────┘ └──────────────────────┘
└──────────────────────┘
```

---

## 3. Chi tiết cấu trúc và mục đích từng bảng (Table Explanation)

### 3.1 Bounded Context 1: User & Authorization (Quản lý Người dùng & Phân quyền)

#### 1. Bảng `users` (Thông tin người dùng)
* **Mục đích:** Lưu trữ thông tin tài khoản xác thực và hồ sơ cá nhân của tất cả các Actors.
* **Chi tiết cấu trúc:**

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
| :--- | :--- | :--- | :--- |
| `id` | BIGINT | PK, AUTO_INCREMENT | Khóa chính của bảng. |
| `username` | VARCHAR(50) | UNIQUE, NOT NULL | Tên đăng nhập của tài khoản. |
| `password` | VARCHAR(255) | NOT NULL | Mật khẩu đã mã hóa bằng BCrypt. |
| `email` | VARCHAR(100) | UNIQUE, NOT NULL | Email liên hệ. |
| `phone` | VARCHAR(15) | UNIQUE, NOT NULL | Số điện thoại liên hệ. |
| `full_name` | VARCHAR(100) | NOT NULL | Họ và tên đầy đủ. |
| `status` | VARCHAR(20) | NOT NULL | Trạng thái tài khoản (`ACTIVE`, `BLOCKED`). |
| `created_at` | DATETIME | NOT NULL | Thời gian tạo dòng dữ liệu. |
| `updated_at` | DATETIME | NOT NULL | Thời gian cập nhật gần nhất. |
| `created_by` | VARCHAR(50) | NULL | Người tạo. |
| `updated_by` | VARCHAR(50) | NULL | Người sửa đổi gần nhất. |
| `deleted_flag` | TINYINT(1) | NOT NULL, DEFAULT 0 | Cờ xóa mềm. |

#### 2. Bảng `roles` (Vai trò quyền hạn)
* **Mục đích:** Lưu danh sách các vai trò cốt lõi trong hệ thống.
* **Chi tiết cấu trúc:** `id` (PK), `name` (VARCHAR(50), UNIQUE - ví dụ: `ROLE_CUSTOMER`, `ROLE_TECHNICIAN`), `description` (VARCHAR(255)).

#### 3. Bảng `user_roles` (Liên kết Nhiều - Nhiều)
* **Mục đích:** Bảng liên kết trung gian giữa người dùng và vai trò.
* **Chi tiết cấu trúc:** `user_id` (FK), `role_id` (FK). Cấu thành Primary Key kép `(user_id, role_id)`.

---

### 3.2 Bounded Context 2: Booking & Appointment (Đặt lịch hẹn)

#### 4. Bảng `bookings` (Đặt lịch bảo dưỡng)
* **Mục đích:** Quản lý lịch hẹn bảo dưỡng do Khách hàng đặt hoặc Cố vấn tạo hộ.
* **Chi tiết cấu trúc:**

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
| :--- | :--- | :--- | :--- |
| `id` | BIGINT | PK, AUTO_INCREMENT | Khóa chính. |
| `customer_id` | BIGINT | NOT NULL | ID khách hàng (Không FK cứng để dễ tách DB). |
| `license_plate` | VARCHAR(20) | NOT NULL | Biển số xe cần bảo dưỡng. |
| `appointment_time`| DATETIME | NOT NULL | Ngày giờ hẹn mang xe đến. |
| `service_type` | VARCHAR(50) | NOT NULL | Loại dịch vụ (`MAINTENANCE_30K`, `BATTERY_CHECK`,...). |
| `notes` | TEXT | NULL | Ghi chú yêu cầu của khách hàng. |
| `status` | VARCHAR(30) | NOT NULL | Trạng thái đặt lịch (`PENDING`, `CONFIRMED`, `CANCELLED`). |
| *Audit Fields* | - | - | Các trường audit thông thường. |

---

### 3.3 Bounded Context 3: Vehicle Reception & Repair Order (Tiếp nhận & Lệnh sửa chữa)

#### 5. Bảng `vehicle_receptions` (Hồ sơ tiếp nhận xe)
* **Mục đích:** Ghi nhận hiện trạng xe khi mang tới gara.
* **Chi tiết cấu trúc:** `id` (PK), `booking_id` (BIGINT, Nullable), `customer_id` (BIGINT), `license_plate` (VARCHAR(20)), `odometer` (INT - Số km hiện tại), `battery_soh` (DECIMAL(5,2) - Sức khỏe pin %), `obd_scan_result` (TEXT - Lỗi quét từ máy OBD), `exterior_status` (TEXT - Tình trạng vỏ xe, trầy xước), *Audit Fields*.

#### 6. Bảng `repair_orders` (Lệnh sửa chữa thực tế - Xương sống vận hành)
* **Mục đích:** Quản lý vòng đời sửa chữa/bảo dưỡng của một chiếc xe tại khoang máy.
* **Chi tiết cấu trúc:**

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
| :--- | :--- | :--- | :--- |
| `id` | BIGINT | PK, AUTO_INCREMENT | Khóa chính. |
| `reception_id` | BIGINT | UNIQUE, NOT NULL | Tham chiếu tới Phiếu tiếp nhận xe. |
| `assigned_technician_id`| BIGINT| NULL | ID Kỹ thuật viên phụ trách sửa chữa. |
| `status` | VARCHAR(30) | NOT NULL | Trạng thái (`IN_PROGRESS`, `READY_FOR_QC`, `QC_PASSED`, `COMPLETED`). |
| `started_at` | DATETIME | NULL | Thời gian thực tế thợ bắt đầu làm việc. |
| `completed_at` | DATETIME | NULL | Thời gian thực tế hoàn thành sửa chữa. |
| *Audit Fields* | - | - | Các trường audit thông thường. |

---

### 3.4 Bounded Context 4: Maintenance Checklist (Quy trình Checklist bảo dưỡng)

#### 7. Bảng `checklist_templates` (Mẫu Checklist Dịch vụ)
* **Mục đích:** Định nghĩa các bộ câu hỏi checklist mẫu theo từng loại xe/gói dịch vụ (ví dụ: Bảo dưỡng pin cấp độ 1).
* **Chi tiết cấu trúc:** `id` (PK), `code` (VARCHAR(50), UNIQUE - ví dụ: `EV_BATTERY_LV1`), `name` (VARCHAR(100)), `description` (TEXT), *Audit Fields*.

#### 8. Bảng `checklist_item_templates` (Chi tiết các mục kiểm tra trong mẫu)
* **Mục đích:** Lưu danh mục các bước cần kiểm tra chi tiết của một mẫu checklist.
* **Chi tiết cấu trúc:** `id` (PK), `template_id` (FK cứng tới `checklist_templates`), `step_number` (INT), `title` (VARCHAR(150) - ví dụ: "Đo điện trở cách điện vỏ pin"), `description` (TEXT), `require_evidence` (BOOLEAN - Có bắt buộc thợ chụp ảnh không).

#### 9. Bảng `maintenance_checklists` (Checklist bảo dưỡng thực tế)
* **Mục đích:** Checklist được sinh ra thực tế gắn với một Lệnh sửa chữa cụ thể.
* **Chi tiết cấu trúc:** `id` (PK), `repair_order_id` (BIGINT, Index), `template_code` (VARCHAR(50)), `name` (VARCHAR(100)), *Audit Fields*.

#### 10. Bảng `maintenance_checklist_items` (Kết quả kiểm tra chi tiết của thợ)
* **Mục đích:** Lưu kết quả tích chọn thực tế của kỹ thuật viên cho từng đầu mục checklist.
* **Chi tiết cấu trúc:**

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
| :--- | :--- | :--- | :--- |
| `id` | BIGINT | PK, AUTO_INCREMENT | Khóa chính. |
| `maintenance_checklist_id`| BIGINT| FK cứng tới `maintenance_checklists`| Thuộc checklist thực tế nào. |
| `step_number` | INT | NOT NULL | Thứ tự bước thực hiện. |
| `title` | VARCHAR(150) | NOT NULL | Tên đầu mục kiểm tra. |
| `status` | VARCHAR(30) | NOT NULL | Trạng thái do thợ tích (`PENDING`, `PASS`, `FAIL`). |
| `notes` | TEXT | NULL | Ghi chú đo đạc kỹ thuật của thợ. |
| `evidence_url` | VARCHAR(512) | NULL | Đường dẫn ảnh bằng chứng lưu trữ trên AWS S3. |

---

### 3.5 Bounded Context 5: Quotation & Billing (Báo giá & Quyết toán)

#### 11. Bảng `quotations` (Bản báo giá gửi khách - An toàn tài chính)
* **Mục đích:** Quản lý chi phí dự kiến và sự phê duyệt của khách hàng trước khi thợ tiến hành thay đồ.
* **Chi tiết cấu trúc:** `id` (PK), `repair_order_id` (BIGINT, Index), `total_estimated_amount` (DECIMAL(15,2)), `customer_approved_status` (VARCHAR(30) - `PENDING`, `APPROVED`, `REJECTED`), `approved_at` (DATETIME), *Audit Fields*.

#### 12. Bảng `quotation_details` (Chi tiết các hạng mục báo giá)
* **Mục đích:** Chi tiết tiền công dịch vụ hoặc linh kiện đề xuất thay thế kèm đơn giá.
* **Chi tiết cấu trúc:** `id` (PK), `quotation_id` (FK cứng tới `quotations`), `item_type` (VARCHAR(30) - `SERVICE`, `SPARE_PART`), `reference_id` (BIGINT - ID của dịch vụ hoặc ID của `spare_parts`), `quantity` (INT), `unit_price` (DECIMAL(15,2)), `total_price` (DECIMAL(15,2)).

#### 13. Bảng `invoices` (Hóa đơn quyết toán tài chính)
* **Mục đích:** Xuất hóa đơn thu tiền sau khi xe đã qua bước QC an toàn cách điện.
* **Chi tiết cấu trúc:**

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
| :--- | :--- | :--- | :--- |
| `id` | BIGINT | PK, AUTO_INCREMENT | Khóa chính. |
| `repair_order_id` | BIGINT | UNIQUE, NOT NULL | Gắn với Lệnh sửa chữa nào. |
| `invoice_number` | VARCHAR(50) | UNIQUE, NOT NULL | Mã hóa đơn định dạng (ví dụ: `INV-2026-00001`). |
| `sub_total` | DECIMAL(15,2)| NOT NULL | Giá trị trước thuế. |
| `tax_amount` | DECIMAL(15,2)| NOT NULL | Tiền thuế VAT. |
| `discount_amount` | DECIMAL(15,2)| NOT NULL | Tiền chiết khấu / giảm giá. |
| `grand_total` | DECIMAL(15,2)| NOT NULL | Tổng số tiền khách phải thanh toán. |
| `payment_status` | VARCHAR(30) | NOT NULL | Trạng thái thanh toán (`UNPAID`, `PAID`). |
| `payment_method` | VARCHAR(30) | NULL | Hình thức (`CASH`, `BANK_TRANSFER`, `CREDIT_CARD`). |
| `paid_at` | DATETIME | NULL | Ngày giờ xác nhận đã thu tiền. |
| *Audit Fields* | - | - | Các trường audit thông thường. |

---

### 3.6 Bounded Context 6: Inventory & Parts (Quản lý Kho phụ tùng)

#### 14. Bảng `spare_parts` (Danh mục phụ tùng EV)
* **Mục đích:** Lưu trữ thông tin phụ tùng xe điện và quản lý số lượng tồn kho.
* **Chi tiết cấu trúc:**

| Tên cột | Kiểu dữ liệu | Ràng buộc | Mô tả |
| :--- | :--- | :--- | :--- |
| `id` | BIGINT | PK, AUTO_INCREMENT | Khóa chính. |
| `part_code` | VARCHAR(50) | UNIQUE, NOT NULL | Mã phụ tùng (ví dụ: `EV-BMS-TES3`). |
| `name` | VARCHAR(150) | NOT NULL | Tên linh kiện. |
| `price` | DECIMAL(15,2)| NOT NULL | Đơn giá bán tiêu chuẩn. |
| `stock_quantity` | INT | NOT NULL, DEFAULT 0 | Số lượng thực tế đang nằm trong kho. |
| `warning_threshold`| INT | NOT NULL, DEFAULT 5 | Ngưỡng báo động sắp hết hàng để nhập kho. |
| `version` | INT | NOT NULL, DEFAULT 0 | Trường phục vụ khóa lạc quan (Optimistic Lock). |
| *Audit Fields* | - | - | Các trường audit thông thường. |

#### 15. Bảng `inventory_transactions` (Lịch sử biến động kho)
* **Mục đích:** Ghi log vết (Audit Trail) mọi hành động làm tăng/giảm số lượng tồn kho để phục vụ đối soát tài chính.
* **Chi tiết cấu trúc:** `id` (PK), `spare_part_id` (BIGINT, Index), `transaction_type` (VARCHAR(30) - `IMPORT` - nhập kho, `EXPORT_REPAIR` - xuất kho sửa chữa, `ADJUSTMENT` - cân đối kho), `quantity` (INT - số lượng thay đổi), `reference_order_id` (BIGINT - ID Lệnh sửa chữa liên quan), *Audit Fields*.

---

### 3.7 Bounded Context 7: Realtime Chat, Notification & AI Log

#### 16. Bảng `chat_messages` (Tin nhắn realtime giữa Khách & Cố vấn)
* **Mục đích:** Lưu lịch sử trao đổi của khách hàng phục vụ tra cứu khi cần thiết.
* **Chi tiết cấu trúc:** `id` (PK), `room_id` (VARCHAR(100), Index - Mã phòng chat sinh tự động), `sender_id` (BIGINT), `sender_role` (VARCHAR(30)), `message_content` (TEXT), `sent_at` (DATETIME).

#### 17. Bảng `notifications` (Hệ thống thông báo và tracking trạng thái)
* **Mục đích:** Lưu lịch sử gửi thông báo và quản lý logic gửi lại (Retry Strategy) khi lỗi.
* **Chi tiết cấu trúc:** `id` (PK), `recipient_id` (BIGINT, Index), `channel` (VARCHAR(20) - `EMAIL`, `PUSH`), `title` (VARCHAR(150)), `content` (TEXT), `status` (VARCHAR(20) - `PENDING`, `SENT`, `FAILED`), `retry_count` (INT, Default 0), `error_message` (TEXT), *Audit Fields*.

#### 18. Bảng `ai_recommendations` (Gợi ý gói dịch vụ/dự báo từ AI)
* **Mục đích:** Lưu trữ các dự đoán bảo dưỡng/phụ tùng do AI phân tích để hiển thị cho Khách hàng & Manager.
* **Chi tiết cấu trúc:** `id` (PK), `target_type` (VARCHAR(30) - `CUSTOMER` - gợi ý bảo dưỡng pin, `MANAGER` - dự báo nhập phụ tùng), `target_id` (BIGINT - ID khách hàng hoặc ID phụ tùng), `recommendation_payload` (JSON - chứa nội dung gợi ý/tỷ lệ dự báo), *Audit Fields*.

---

## 4. Chiến lược liên kết dữ liệu sẵn sàng cho Microservices (Loose Coupling Strategy)

Để đảm bảo hệ thống có thể chuyển đổi thành công sang Microservices trong tương lai mà không cần đập đi xây lại database:

> [!WARNING]
> **Quy tắc vàng thiết kế mối liên hệ chéo giữa các Module:**
> * Không tạo khóa ngoại cứng (`FOREIGN KEY`) chéo giữa các bảng thuộc các Bounded Context khác nhau.
> * Việc kiểm soát tính toàn vẹn dữ liệu (Data Integrity) giữa các module khác nhau sẽ được xử lý ở **Tầng Service (Application-level Integrity)** thông qua các câu lệnh Validate Service.
> * Chỉ dùng khóa ngoại cứng (`FOREIGN KEY`) trong nội bộ của một Bounded Context (ví dụ: ràng buộc FK từ `checklist_item_templates` tới `checklist_templates`).

### Chi tiết cách ánh xạ ID liên kết chéo:

```
[Module Booking] --(Lưu trường customer_id)--> [Module User] (Không tạo FK cứng trong DB)
[Module Repair Order] --(Lưu trường assigned_technician_id)--> [Module User] (Không tạo FK cứng)
[Module Quotation Detail] --(Lưu trường reference_id đại diện spare_part_id)--> [Module Inventory] (Không tạo FK cứng)
```

---

## 5. Chiến lược Xóa mềm (Soft Delete Strategy) & Tránh lỗi Unique Key

Tất cả các bảng nghiệp vụ cốt lõi đều chứa trường `deleted_flag` (TINYINT(1), default 0) để thực hiện xóa mềm (chỉ đánh dấu ẩn dữ liệu, không xóa vật lý khỏi đĩa cứng để bảo toàn lịch sử dữ liệu).

### 🛠️ Giải pháp an toàn Production tránh xung đột chỉ mục UNIQUE:
* **Vấn đề:** Bảng `users` có chỉ mục duy nhất (`UNIQUE INDEX`) trên cột `email`. Nếu người dùng xóa tài khoản (xóa mềm `deleted_flag = 1`), sau đó họ đăng ký lại bằng chính `email` đó, MySQL sẽ báo lỗi trùng lặp dữ liệu (`Duplicate entry`), mặc dù tài khoản cũ đã bị xóa mềm!
* **Giải pháp tối ưu:** 
  Thay vì dùng cờ Boolean `deleted_flag`, chúng ta thay thế bằng cột `deleted_at` (DATETIME, default NULL).
  Thiết lập chỉ mục duy nhất kết hợp: **`UNIQUE INDEX uq_user_email (email, deleted_at)`**.
  * Khi tài khoản hoạt động: `deleted_at` nhận giá trị `NULL`. MySQL cho phép tồn tại nhiều dòng có cặp `(email, NULL)` trùng nhau không? **Không!** MySQL tính toán `NULL` trong unique key của một số phiên bản rất đặc biệt, nhưng theo chuẩn mực: chỉ có duy nhất một dòng hoạt động có giá trị `NULL`.
  * Khi xóa tài khoản: Cập nhật `deleted_at = CURRENT_TIMESTAMP`. Cặp `(email, deleted_at)` lúc này trở thành duy nhất và lịch sử lưu lại rõ ràng. Người dùng có thể đăng ký tài khoản mới với `email` đó (cặp `(email, NULL)` mới được tạo thành công).

---

## 6. Chiến lược thiết lập chỉ mục (Indexing Strategy)

Để đảm bảo các truy vấn API của ứng dụng Spring Boot chạy với tốc độ cực nhanh (< 50ms) trên MySQL, chúng ta thiết lập các chỉ mục tối ưu sau:

1. **Clustered Index (B-Tree):** Tự động tạo trên cột khóa chính `id` của tất cả các bảng.
2. **Unique Indexes (Chỉ mục duy nhất):**
   * Bảng `users`: `uq_username_deleted (username, deleted_at)`, `uq_email_deleted (email, deleted_at)`, `uq_phone_deleted (phone, deleted_at)`.
   * Bảng `spare_parts`: `uq_part_code_deleted (part_code, deleted_at)`.
3. **Composite/Single Secondary Indexes (Chỉ mục phụ hỗ trợ Query):**
   * Bảng `bookings`: Index trên cột `(customer_id, appointment_time)` phục vụ màn hình lịch sử đặt lịch.
   * Bảng `repair_orders`: Index trên cột `(status, assigned_technician_id)` phục vụ màn hình danh sách việc của thợ.
   * Bảng `maintenance_checklists`: Index trên cột `repair_order_id` để lấy nhanh toàn bộ checklist của xe đang bảo dưỡng.
   * Bảng `inventory_transactions`: Index trên cột `(spare_part_id, created_at)` phục vụ truy xuất lịch sử xuất nhập kho.
   * Bảng `notifications`: Index trên cột `(recipient_id, status)` để phục vụ quét gửi lại thông báo lỗi.

---

## 7. Ranh giới giao dịch (Transaction Boundaries)

Các nghiệp vụ phức tạp phải được bọc trong một ranh giới giao dịch duy nhất bằng chú thích `@Transactional(rollbackFor = Exception.class)` ở lớp Service của Spring Boot để đảm bảo tính nguyên tử (Atomicity):

* **Transaction 1: Quyết toán tiếp nhận xe (Vehicle Reception Transaction)**
  * Tạo phiếu tiếp nhận xe trong `vehicle_receptions`.
  * Sinh lệnh sửa chữa tương ứng trong `repair_orders`.
  * Cập nhật trạng thái của lịch hẹn `bookings` sang `CONFIRMED`.
  * *Tất cả phải thành công, nếu 1 bước lỗi phải rollback về trạng thái chưa tiếp nhận.*
* **Transaction 2: Phê duyệt báo giá & Xuất kho tạm thời**
  * Cập nhật trạng thái báo giá `quotations` thành `APPROVED`.
  * Duyệt các dòng trong `quotation_details`.
  * Với mỗi chi tiết là linh kiện: Gọi Service Kho để cập nhật tồn kho `spare_parts` (Giảm số lượng, tăng số lượng khóa tạm).
  * Viết log biến động kho vào `inventory_transactions`.
* **Transaction 3: Hoàn thành & Trừ kho vĩnh viễn**
  * Cập nhật trạng thái lệnh sửa chữa `repair_orders` thành `QC_PASSED`.
  * Trừ vĩnh viễn số lượng linh kiện đã khóa tạm trong `spare_parts`.

---

## 8. Chiến lược đảm bảo tính nhất quán kho hàng (Inventory Consistency Strategy)

Để tránh hiện tượng nhiều kỹ thuật viên cùng thao tác rút kho gây âm kho (Race Conditions), chúng ta áp dụng cơ chế khóa sau tại lớp Service:

1. **Sử dụng Khóa Lạc Quan (Optimistic Locking) cho các thao tác thông thường:**
   * Thêm cột `version` (INT, default 0) vào bảng `spare_parts`.
   * Khi cập nhật số lượng tồn kho, Hibernate sẽ tự động sinh câu lệnh: `UPDATE spare_parts SET stock_quantity = ?, version = version + 1 WHERE id = ? AND version = ?`.
   * Nếu có một transaction khác đã cập nhật trước đó, `version` sẽ không khớp, Spring Boot sẽ ném ra `OptimisticLockingFailureException`. Hệ thống sẽ tự động bắt ngoại lệ này và tiến hành thử lại (Retry) tối đa 3 lần.
2. **Sử dụng Khóa Bi Quan (Pessimistic Locking) cho các linh kiện cực kỳ khan hiếm:**
   * Trong phương thức tìm kiếm linh kiện của Repository, sử dụng annotation `@Lock(LockModeType.PESSIMISTIC_WRITE)`.
   * Câu lệnh SQL sinh ra sẽ là: `SELECT * FROM spare_parts WHERE id = ? FOR UPDATE`.
   * Thao tác này sẽ khóa dòng linh kiện đó trong DB, buộc các kỹ thuật viên khác yêu cầu sau phải xếp hàng chờ cho đến khi transaction trước hoàn thành.

---

## 9. Khả năng mở rộng trong tương lai (Scalability Considerations)

* **Thiết kế sẵn sàng cho Chuỗi gara (Multi-garages System):**
  * Tất cả các bảng nghiệp vụ cốt lõi (`bookings`, `repair_orders`, `spare_parts`) đều được thiết kế sẵn sàng bổ sung cột `garage_id (BIGINT)` để phân biệt dữ liệu giữa các gara. Ở giai đoạn Monolith, ta có thể dùng Spring Data JPA Filter để tự động đính kèm điều kiện `WHERE garage_id = ?` vào mọi câu lệnh truy vấn.
* **Tối ưu hóa các bảng lịch sử dung lượng lớn (History Tables Partitioning):**
  * Các bảng như `inventory_transactions`, `chat_messages` và `notifications` sẽ tăng kích thước cực kỳ nhanh theo thời gian.
  * Giải pháp scale: Sử dụng tính năng **Table Partitioning của MySQL** để phân vùng các bảng này theo cột thời gian `created_at` theo từng tháng hoặc từng năm. Các truy vấn báo cáo cũ sẽ chỉ quét trên phân vùng lịch sử, không làm ảnh hưởng đến tốc độ hoạt động của các phân vùng hiện tại.

---

## 10. Xác định các bảng Core bắt buộc cho MVP Phase 1

Để tập trung phát triển nhanh nhất luồng giá trị cốt lõi, trong Phase 1 chúng ta chỉ cần tạo và làm việc với **11 bảng cốt lõi** sau:

1. `users` (Xác thực tài khoản người dùng)
2. `roles` (Định nghĩa vai trò hệ thống)
3. `user_roles` (Phân quyền người dùng)
4. `bookings` (Đặt lịch bảo dưỡng)
5. `vehicle_receptions` (Phiếu tiếp nhận xe)
6. `repair_orders` (Lệnh sửa chữa thực thi)
7. `checklist_templates` (Mẫu checklist nghiệp vụ)
8. `checklist_item_templates` (Chi tiết mẫu checklist)
9. `maintenance_checklists` (Phiếu kiểm tra thực tế trên RO)
10. `maintenance_checklist_items` (Kết quả tích chọn của thợ)
11. `spare_parts` (Kho linh kiện cơ bản)
12. `invoices` (Hóa đơn tính tiền)

*Các bảng chat_messages, quotations, quotations_details, inventory_transactions và notifications nâng cao sẽ được phát triển kế tiếp trong Phase 2 sau khi luồng MVP cốt lõi đã chạy mượt mà.*

---

> [!TIP]
> **Tài liệu thiết kế Database tổng thể đã hoàn thành!**
> 
> Bản thiết kế chi tiết này đã được lưu trữ tại [DATABASE_DESIGN.md](file:///Users/dev.trungnhan/.gemini/antigravity/brain/44940b1d-846c-4719-bf4a-9547dc788c17/DATABASE_DESIGN.md).
> 
> Nếu bạn đã hài lòng với thiết kế cơ sở dữ liệu chuyên nghiệp này, hãy gõ **"accept"** hoặc **"triển khai"** để tôi bắt đầu:
> 1. Viết script SQL khởi tạo Database MySQL hoàn chỉnh cho MVP.
> 2. Khởi tạo mã nguồn các Entity Class tương ứng trong thư mục dự án Spring Boot!
