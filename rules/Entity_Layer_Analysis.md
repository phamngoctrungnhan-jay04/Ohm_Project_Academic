# ENTITY_LAYER_ANALYSIS.md - PHÂN TÍCH LỚP THỰC THỂ (ENTITY LAYER)
## Dự án: Hệ thống Quản lý Bảo dưỡng Xe Điện (Project_01)
---

> [!IMPORTANT]
> **Triết lý thiết kế Entity Layer:** 
> * **JPA/Hibernate Standards:** Viết thực thể Java ánh xạ chính xác 1-1 với Database Design, tối ưu hóa các chú thích (Annotations) để tận dụng tối đa sức mạnh của Hibernate.
> * **Loose Coupling (Độ liên kết lỏng lẻo):** Áp dụng triệt để quy tắc tham chiếu bằng ID giữa các module khác nhau, loại bỏ `@ManyToOne` hay `@OneToMany` liên kết chéo qua Bounded Context.
> * **Performance-first:** Thiết lập cơ chế `FetchType.LAZY` cho tất cả các quan hệ để tránh lỗi N+1 Query kinh điển trong Spring Boot.

---

## 1. Naming Conventions (Quy ước đặt tên thực thể)

Lớp Entity tuân thủ nghiêm ngặt quy chuẩn đặt tên Java và JPA mapping:
* **Tên Class (Java Classes):** Viết hoa chữ cái đầu theo chuẩn CamelCase (danh từ số ít). Ví dụ: `User`, `SparePart`, `RepairOrder`.
* **Tên thuộc tính (Java Fields):** Viết thường chữ đầu theo camelCase. Ví dụ: `id`, `partCode`, `licensePlate`, `deletedAt`.
* **Ánh xạ cột (JPA Mapping):** Sử dụng `@Table(name = "...")` và `@Column(name = "...")` định dạng Snake_case của MySQL. Ví dụ: `@Column(name = "license_plate")`.

---

## 2. Danh sách các Entities cốt lõi cho MVP Phase 1

Trong Phase 1, chúng ta thiết lập **11 Entities chính** được chia theo 4 package module nghiệp vụ chính:

```
src/main/java/org/ohm_project/modules/
│
├── user/entity/
│   ├── User.java                   # Thông tin tài khoản
│   └── Role.java                   # Vai trò hệ thống
│
├── booking/entity/
│   └── Booking.java                # Lịch hẹn bảo dưỡng
│
├── inventory/entity/
│   └── SparePart.java              # Danh mục linh kiện kho
│
└── maintenance/entity/
    ├── VehicleReception.java       # Phiếu tiếp nhận xe
    ├── RepairOrder.java            # Lệnh sửa chữa (Aggregate Root)
    ├── ChecklistTemplate.java      # Mẫu checklist (Aggregate Root)
    ├── ChecklistItemTemplate.java  # Mục checklist mẫu
    ├── MaintenanceChecklist.java   # Checklist thực tế
    └── MaintenanceChecklistItem.java# Kết quả checklist thực tế
```

---

## 3. Chiến lược liên kết chéo ID (Loose Coupling Strategy)

Đối với các thực thể liên kết chéo giữa các Domain Bounded Context khác nhau, chúng ta sử dụng **Tham chiếu ID trực tiếp (Primitive/Raw ID)** thay vì dùng Object mapping của JPA.

### Chi tiết thiết lập thuộc tính Entity:

* **Trong `Booking` Entity (Module Booking):**
  * Sử dụng: `private Long customerId;`
  * *Tránh dùng:* `@ManyToOne User customer;`
* **Trong `VehicleReception` Entity (Module Maintenance):**
  * Sử dụng: `private Long bookingId;`, `private Long customerId;`
  * *Tránh dùng:* `@OneToOne Booking booking;`, `@ManyToOne User customer;`
* **Trong `RepairOrder` Entity (Module Maintenance):**
  * Sử dụng: `private Long assignedTechnicianId;`
  * *Tránh dùng:* `@ManyToOne User technician;`

> [!TIP]
> **Lợi ích thực tế:** Cách thiết kế này giúp mỗi module hoàn toàn độc lập. Bạn có thể thay đổi, bảo trì hoặc tách module `User` hay `Booking` thành một Service/Database riêng chạy trên server khác mà không làm lỗi biên dịch (Compilation Error) hay vỡ JPA mappings của module `Maintenance`.

---

## 4. Aggregate Roots & Hard Relationships (Trong nội bộ Module)

Trong nội bộ một Bounded Context, chúng ta sử dụng các liên kết `@ManyToOne`, `@OneToMany` hoặc `@OneToOne` của JPA để quản lý các quan hệ cha - con chặt chẽ:

### 1. Phân vùng Checklist Mẫu (Checklist Template Aggregate):
* **Aggregate Root:** `ChecklistTemplate`
* **Thành phần phụ thuộc:** `ChecklistItemTemplate`
* **Quan hệ JPA:** `ChecklistTemplate` chứa `@OneToMany(mappedBy = "template", cascade = CascadeType.ALL, orphanRemoval = true) List<ChecklistItemTemplate> items`.
* *Ý nghĩa:* Khi xóa `ChecklistTemplate`, toàn bộ `ChecklistItemTemplate` con bên trong sẽ bị xóa sạch khỏi database tự động (`orphanRemoval = true`).

### 2. Phân vùng Checklist Thực tế (Maintenance Checklist Aggregate):
* **Aggregate Root:** `MaintenanceChecklist`
* **Thành phần phụ thuộc:** `MaintenanceChecklistItem`
* **Quan hệ JPA:** `MaintenanceChecklist` chứa `@OneToMany(mappedBy = "checklist", cascade = CascadeType.ALL, orphanRemoval = true) List<MaintenanceChecklistItem> items`.

---

## 5. Chiến lược kế thừa & Bảng chung (Base Entity Strategy)

Để tránh lặp code (DRY Principle) và thống nhất các trường audit, xóa mềm trên tất cả các bảng database, chúng ta thiết kế một cấu trúc lớp kế thừa sử dụng **`@MappedSuperclass`** của JPA.

```
                  ┌──────────────────────────────┐
                  │          BaseEntity          │
                  │  - id (BIGINT)               │
                  │  - deletedAt (LocalDateTime) │
                  └──────────────┬───────────────┘
                                 │
                                 ▼
                  ┌──────────────────────────────┐
                  │     BaseAuditableEntity      │
                  │  - createdAt, updatedAt      │
                  │  - createdBy, updatedBy      │
                  └──────────────┬───────────────┘
                                 │
         ┌───────────────────────┴───────────────────────┐
         │                                               │
         ▼                                               ▼
┌──────────────────┐                            ┌──────────────────┐
│   User Entity    │                            │  Booking Entity  │
└──────────────────┘                            └──────────────────┘
```

### Chi tiết cấu trúc Lớp Base:

1. **`BaseEntity` (Lớp nền tảng):**
   * Định nghĩa trường `@Id` với `@GeneratedValue(strategy = GenerationType.IDENTITY)`.
   * Định nghĩa trường `deletedAt` (LocalDateTime) dùng cho chiến lược xóa mềm.
2. **`BaseAuditableEntity` (Lớp Audit - Kế thừa từ `BaseEntity`):**
   * Sử dụng annotation `@EntityListeners(AuditingEntityListener.class)` để Spring Data JPA tự động nạp dữ liệu thời gian.
   * Chứa các trường `@CreatedDate LocalDateTime createdAt`, `@LastModifiedDate LocalDateTime updatedAt`, `@CreatedBy String createdBy`, `@LastModifiedBy String updatedBy`.

---

## 6. Chiến lược Xóa mềm trong JPA (Soft Delete Strategy)

Để việc xóa mềm diễn ra tự động và tự lọc bỏ dữ liệu cũ khi thực hiện các hàm Repository (như `findAll()`, `findById()`), chúng ta cấu hình các chú thích của Hibernate trực tiếp trên các Entity kế thừa `BaseEntity`.

### Cấu hình Annotations trên các Entities:
* **`@SQLDelete(sql = "UPDATE [table_name] SET deleted_at = NOW() WHERE id = ?")`**: Ghi đè câu lệnh SQL Delete vật lý mặc định của Hibernate thành câu lệnh UPDATE cập nhật thời gian xóa mềm.
* **`@Where(clause = "deleted_at IS NULL")`**: Tự động chèn thêm điều kiện lọc `AND deleted_at IS NULL` vào tất cả các câu lệnh SQL Select phát sinh từ Repository.

---

## 7. Chiến lược ánh xạ Enums (Enum Mapping Strategy)

Tất cả các trường trạng thái (Status) bắt buộc phải sử dụng kiểu dữ liệu **Enum** trong Java để đảm bảo tính an toàn kiểu dữ liệu (Type-safety), tránh việc hardcode String hoặc Integer tùy tiện.

### Quy tắc ánh xạ JPA:
Bắt buộc dùng chú thích **`@Enumerated(EnumType.STRING)`** trên các thuộc tính Enum.
* *Tại sao không dùng `EnumType.ORDINAL` (mặc định)?* ORDINAL sẽ lưu giá trị index của Enum (0, 1, 2) vào DB. Nếu trong tương lai ta thêm mới một giá trị Enum vào giữa danh sách, toàn bộ chỉ số trong DB sẽ bị lệch hoàn toàn, gây sai hỏng dữ liệu tài chính/nghiệp vụ. `EnumType.STRING` lưu trữ chuỗi text trực tiếp (ví dụ: `IN_PROGRESS`), cực kỳ an toàn và dễ đọc trực tiếp dưới database.

### Danh sách 6 Enums cốt lõi của MVP:
1. `UserStatus`: `ACTIVE`, `BLOCKED`
2. `RoleName`: `ROLE_CUSTOMER`, `ROLE_SERVICE_ADVISOR`, `ROLE_TECHNICIAN`, `ROLE_MANAGER`, `ROLE_ADMIN`
3. `BookingStatus`: `PENDING`, `CONFIRMED`, `CANCELLED`, `IN_SERVICE`
4. `RepairOrderStatus`: `IN_PROGRESS`, `READY_FOR_QC`, `QC_PASSED`, `COMPLETED`
5. `ChecklistItemStatus`: `PENDING`, `PASS`, `FAIL`
6. `InvoiceStatus`: `UNPAID`, `PAID`

---

## 8. Chiến lược tải dữ liệu (Lazy Loading Strategy)

Để đảm bảo hiệu năng tải trang tối ưu và không tốn bộ nhớ RAM của Server Spring Boot để lưu trữ các dữ liệu dư thừa:

> [!WARNING]
> **Quy tắc hiệu năng Hibernate:**
> * Tất cả các mối quan hệ liên kết `@OneToMany`, `@ManyToMany` và `@ManyToOne` bắt buộc phải cấu hình thuộc tính **`fetch = FetchType.LAZY`**.
> * Tuyệt đối không dùng `FetchType.EAGER` (đặc biệt là `@ManyToOne` mặc định là EAGER). Nếu không, khi truy vấn 1 danh sách 100 Lệnh sửa chữa, Spring Boot sẽ thực hiện 100 câu lệnh truy vấn lẻ lẻ để lấy thông tin các bảng liên quan, gây nghẽn Server lập tức (Lỗi N+1 Query).
> * Khi thực sự cần lấy cả đối tượng liên quan (ví dụ: Lấy checklist kèm theo danh sách các mục con để hiển thị màn hình sửa xe), chúng ta sử dụng giải pháp **Entity Graph** hoặc viết **`JOIN FETCH`** ở tầng Repository.

---

## 9. Chiến lược Kiểm chuẩn dữ liệu Entity (Validation Strategy)

Để tránh dữ liệu rác hoặc dữ liệu lỗi lưu vào Database gây treo hệ thống, chúng ta thực hiện kiểm chuẩn dữ liệu (Validation) 2 lớp:

* **Lớp 1: Bean Validation Annotations (Tại lớp DTO & Entity):** Tích hợp thư viện `spring-boot-starter-validation`.
  * `@NotBlank(message = "...")`: Bắt buộc không được để trống trường String.
  * `@NotNull(message = "...")`: Thuộc tính không được phép nhận giá trị null.
  * `@Size(min = 8, max = 50)`: Ràng buộc độ dài chuỗi ký tự.
  * `@Email`: Định dạng email chuẩn.
  * `@Pattern`: Sử dụng biểu thức chính quy (Regex) để kiểm tra định dạng đặc thù (ví dụ: Số điện thoại Việt Nam bắt đầu bằng số 0, dài 10 ký tự).
* **Lớp 2: JPA/Database Constraints:** Tương thích trực tiếp với database schema.
  * `@Column(nullable = false, length = 100)`: Khai báo ràng buộc NOT NULL và giới hạn chiều dài ký tự ở mức Database Schema Generator.

---

> [!TIP]
> **Bản phân tích Entity Layer chi tiết đã hoàn thành!**
> 
> Tài liệu phân tích này đã được lưu trữ an toàn tại [ENTITY_LAYER_ANALYSIS.md](file:///Users/dev.trungnhan/.gemini/antigravity/brain/44940b1d-846c-4719-bf4a-9547dc788c17/ENTITY_LAYER_ANALYSIS.md).
> 
> Nếu bản phân tích và định hướng Entity này đã hoàn toàn chuẩn mực theo đúng triết lý Clean Code và ý tưởng của bạn, hãy phản hồi hoặc gõ **"accept"** để tôi chuẩn bị:
> 1. Viết script SQL khởi tạo các bảng MySQL thực tế.
> 2. Bắt đầu viết code các thực thể Entity Java đầu tiên cho Module **User & Auth**!
