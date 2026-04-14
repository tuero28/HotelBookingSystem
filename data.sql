
-- 1. Bảng Vai trò
CREATE TABLE Roles (
    RoleID INT PRIMARY KEY AUTO_INCREMENT,
    RoleName VARCHAR(50) NOT NULL UNIQUE
);

-- 2. Bảng Người dùng
CREATE TABLE Users (
    UserID INT PRIMARY KEY AUTO_INCREMENT,
    Username VARCHAR(50) UNIQUE NOT NULL,
    Password VARCHAR(255) NOT NULL,
    FullName VARCHAR(100),
    Email VARCHAR(100) UNIQUE,
    Phone VARCHAR(20) UNIQUE,
    RoleID INT,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (RoleID) REFERENCES Roles(RoleID) ON DELETE SET NULL
);

-- 3. Bảng Loại phòng
CREATE TABLE RoomTypes (
    RoomTypeID INT PRIMARY KEY AUTO_INCREMENT,
    TypeName VARCHAR(100) NOT NULL,
    Description TEXT,
    BasePrice DECIMAL(12,2) NOT NULL,
    Capacity INT DEFAULT 2
);

-- 4. Bảng Phòng
CREATE TABLE Rooms (
    RoomID INT PRIMARY KEY AUTO_INCREMENT,
    RoomNumber VARCHAR(10) UNIQUE NOT NULL,
    RoomTypeID INT,
    Status ENUM('Available','Occupied','Cleaning','Maintenance') DEFAULT 'Available',
    FOREIGN KEY (RoomTypeID) REFERENCES RoomTypes(RoomTypeID) ON DELETE CASCADE
);

-- 5. Bảng Hình ảnh phòng
CREATE TABLE RoomImages (
    ImageID INT PRIMARY KEY AUTO_INCREMENT,
    RoomTypeID INT,
    ImageURL VARCHAR(255) NOT NULL,
    isPrimary BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (RoomTypeID) REFERENCES RoomTypes(RoomTypeID) ON DELETE CASCADE
);

-- 6. Bảng Tiện nghi
CREATE TABLE Amenities (
    AmenityID INT PRIMARY KEY AUTO_INCREMENT,
    AmenityName VARCHAR(100) NOT NULL,
    IconURL VARCHAR(255)
);

-- 7. Liên kết loại phòng - tiện nghi
CREATE TABLE RoomTypeAmenities (
    RoomTypeID INT,
    AmenityID INT,
    PRIMARY KEY (RoomTypeID, AmenityID),
    FOREIGN KEY (RoomTypeID) REFERENCES RoomTypes(RoomTypeID) ON DELETE CASCADE,
    FOREIGN KEY (AmenityID) REFERENCES Amenities(AmenityID) ON DELETE CASCADE
);

-- 8. Bảng Đặt phòng
CREATE TABLE Bookings (
    BookingID INT PRIMARY KEY AUTO_INCREMENT,
    CustomerID INT,
    RoomID INT,
    CheckInDate DATE NOT NULL,
    CheckOutDate DATE NOT NULL,
    GuestCount INT DEFAULT 1,
    SpecialRequest TEXT,
    ActualCheckIn DATETIME,
    ActualCheckOut DATETIME,
    TotalPrice DECIMAL(12,2),
    DepositAmount DECIMAL(12,2) DEFAULT 0,
    Status ENUM('Pending','Confirmed','Cancelled','In-progress','Completed') DEFAULT 'Pending',
    BookingType ENUM('Online','PayAtHotel') DEFAULT 'Online',
    ExpirationTime DATETIME,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (CustomerID) REFERENCES Users(UserID) ON DELETE SET NULL,
    FOREIGN KEY (RoomID) REFERENCES Rooms(RoomID) ON DELETE SET NULL
);

-- 9. Bảng Thanh toán
CREATE TABLE Payments (
    PaymentID INT PRIMARY KEY AUTO_INCREMENT,
    BookingID INT,
    PaymentMethod ENUM('Cash','Credit Card','Bank Transfer','E-Wallet'),
    Amount DECIMAL(12,2),
    PaymentDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    PaymentStatus ENUM('Pending','Completed','Failed','Refunded') DEFAULT 'Pending',
    TransactionID VARCHAR(100),
    FOREIGN KEY (BookingID) REFERENCES Bookings(BookingID) ON DELETE CASCADE
);

-- 10. Bảng Đánh giá
CREATE TABLE Reviews (
    ReviewID INT PRIMARY KEY AUTO_INCREMENT,
    BookingID INT UNIQUE,
    Rating INT CHECK (Rating BETWEEN 1 AND 5),
    Comment TEXT,
    Reply TEXT,
    ReviewDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (BookingID) REFERENCES Bookings(BookingID) ON DELETE CASCADE
);


use bookinghotel;
-- 1. Bảng Vai trò
INSERT INTO Roles (RoleID, RoleName) VALUES
(1, 'ADMIN'),
(2, 'STAFF'),
(3, 'CUSTOMER');
 
-- ============================================================
-- 2. USERS
-- Password đã được BCrypt encode (plain: Admin@123 / Staff@123 / Customer@123)
-- ============================================================
INSERT INTO Users (UserID, Username, Password, FullName, Email, Phone, RoleID) VALUES
(1,  'admin',      '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'Quản trị viên',        'admin@hotel.com',          '0900000001', 1),
(2,  'staff01',    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'Nguyễn Văn Lễ Tân',    'staff01@hotel.com',        '0900000002', 2),
(3,  'staff02',    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'Trần Thị Lễ Tân',      'staff02@hotel.com',        '0900000003', 2),
(4,  'customer01', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'Lê Văn An',            'levan.an@gmail.com',       '0911111111', 3),
(5,  'customer02', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2ubookingsheWG/igi.', 'Phạm Thị Bình',        'phamthi.binh@gmail.com',   '0922222222', 3),
(6,  'customer03', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'Hoàng Minh Cường',     'hoang.cuong@gmail.com',    '0933333333', 3),
(7,  'customer04', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'Vũ Thị Dung',          'vu.dung@gmail.com',        '0944444444', 3);
 
-- ============================================================
-- 3. AMENITIES
-- ============================================================
INSERT INTO Amenities (AmenityID, AmenityName, IconURL) VALUES
(1,  'WiFi miễn phí',       'icons/wifi.svg'),
(2,  'Điều hoà nhiệt độ',   'icons/ac.svg'),
(3,  'TV màn hình phẳng',   'icons/tv.svg'),
(4,  'Minibar',             'icons/minibar.svg'),
(5,  'Bồn tắm',             'icons/bathtub.svg'),
(6,  'Phòng tắm vòi sen',   'icons/shower.svg'),
(7,  'Két an toàn',         'icons/safe.svg'),
(8,  'Bàn làm việc',        'icons/desk.svg'),
(9,  'Ban công / Terrace',  'icons/balcony.svg'),
(10, 'Jacuzzi',             'icons/jacuzzi.svg'),
(11, 'Bếp nhỏ',             'icons/kitchen.svg'),
(12, 'Phòng khách riêng',   'icons/living.svg');
 
-- ============================================================
-- 4. ROOM TYPES
-- ============================================================
INSERT INTO RoomTypes (RoomTypeID, TypeName, Description, BasePrice, Capacity) VALUES
(1, 'Standard Room',
    'Phòng tiêu chuẩn thoải mái với đầy đủ tiện nghi cơ bản, phù hợp cho kỳ nghỉ ngắn ngày.',
    800000.00, 2),
(2, 'Deluxe Room',
    'Phòng deluxe rộng rãi với tầm nhìn đẹp, trang bị minibar và két an toàn.',
    1200000.00, 2),
(3, 'Superior Room',
    'Phòng superior sang trọng với bồn tắm riêng, view thành phố tuyệt đẹp.',
    1600000.00, 2),
(4, 'Junior Suite',
    'Suite rộng rãi với phòng khách riêng biệt, jacuzzi và ban công view biển.',
    2500000.00, 3),
(5, 'Presidential Suite',
    'Suite tổng thống đẳng cấp 5 sao, đầy đủ mọi tiện nghi cao cấp, phù hợp cho gia đình hoặc đoàn VIP.',
    5000000.00, 4);
 
-- ============================================================
-- 5. ROOM TYPE AMENITIES
-- ============================================================
-- Standard: WiFi, AC, TV, Shower, Desk
INSERT INTO RoomTypeAmenities (RoomTypeID, AmenityID) VALUES
(1, 1), (1, 2), (1, 3), (1, 6), (1, 8);
 
-- Deluxe: WiFi, AC, TV, Minibar, Shower, Safe, Desk, Balcony
INSERT INTO RoomTypeAmenities (RoomTypeID, AmenityID) VALUES
(2, 1), (2, 2), (2, 3), (2, 4), (2, 6), (2, 7), (2, 8), (2, 9);
 
-- Superior: WiFi, AC, TV, Minibar, Bathtub, Shower, Safe, Balcony
INSERT INTO RoomTypeAmenities (RoomTypeID, AmenityID) VALUES
(3, 1), (3, 2), (3, 3), (3, 4), (3, 5), (3, 6), (3, 7), (3, 9);
 
-- Junior Suite: WiFi, AC, TV, Minibar, Bathtub, Jacuzzi, Safe, Balcony, Living Room
INSERT INTO RoomTypeAmenities (RoomTypeID, AmenityID) VALUES
(4, 1), (4, 2), (4, 3), (4, 4), (4, 5), (4, 10), (4, 7), (4, 9), (4, 12);
 
-- Presidential Suite: tất cả tiện nghi
INSERT INTO RoomTypeAmenities (RoomTypeID, AmenityID) VALUES
(5, 1), (5, 2), (5, 3), (5, 4), (5, 5), (5, 6),
(5, 7), (5, 8), (5, 9), (5, 10), (5, 11), (5, 12);
 
-- ============================================================
-- 6. ROOM IMAGES
-- ============================================================
INSERT INTO RoomImages (ImageID, RoomTypeID, ImageURL, isPrimary) VALUES
-- Standard
(1,  1, 'https://example.com/images/standard-main.jpg',         TRUE),
(2,  1, 'https://example.com/images/standard-bathroom.jpg',     FALSE),
(3,  1, 'https://example.com/images/standard-view.jpg',         FALSE),
-- Deluxe
(4,  2, 'https://example.com/images/deluxe-main.jpg',           TRUE),
(5,  2, 'https://example.com/images/deluxe-balcony.jpg',        FALSE),
(6,  2, 'https://example.com/images/deluxe-bathroom.jpg',       FALSE),
-- Superior
(7,  3, 'https://example.com/images/superior-main.jpg',         TRUE),
(8,  3, 'https://example.com/images/superior-bathtub.jpg',      FALSE),
(9,  3, 'https://example.com/images/superior-view.jpg',         FALSE),
-- Junior Suite
(10, 4, 'https://example.com/images/junior-suite-main.jpg',     TRUE),
(11, 4, 'https://example.com/images/junior-suite-jacuzzi.jpg',  FALSE),
(12, 4, 'https://example.com/images/junior-suite-living.jpg',   FALSE),
-- Presidential Suite
(13, 5, 'https://example.com/images/presidential-main.jpg',     TRUE),
(14, 5, 'https://example.com/images/presidential-living.jpg',   FALSE),
(15, 5, 'https://example.com/images/presidential-kitchen.jpg',  FALSE),
(16, 5, 'https://example.com/images/presidential-bath.jpg',     FALSE);
 
-- ============================================================
-- 7. ROOMS
-- ============================================================
INSERT INTO Rooms (RoomID, RoomNumber, RoomTypeID, Status) VALUES
-- Tầng 1 - Standard
(1,  '101', 1, 'Available'),
(2,  '102', 1, 'Available'),
(3,  '103', 1, 'Occupied'),
(4,  '104', 1, 'Cleaning'),
(5,  '105', 1, 'Available'),
-- Tầng 2 - Deluxe
(6,  '201', 2, 'Available'),
(7,  '202', 2, 'Available'),
(8,  '203', 2, 'Occupied'),
(9,  '204', 2, 'Maintenance'),
-- Tầng 3 - Superior
(10, '301', 3, 'Available'),
(11, '302', 3, 'Available'),
(12, '303', 3, 'Occupied'),
-- Tầng 4 - Junior Suite
(13, '401', 4, 'Available'),
(14, '402', 4, 'Available'),
-- Tầng 5 - Presidential Suite
(15, '501', 5, 'Available'),
(16, '502', 5, 'Occupied');
 
-- ============================================================
-- 8. BOOKINGS
-- ============================================================
INSERT INTO Bookings
    (BookingID, CustomerID, RoomID, CheckInDate, CheckOutDate,
     GuestCount, SpecialRequest, ActualCheckIn, ActualCheckOut,
     TotalPrice, DepositAmount, Status, BookingType, ExpirationTime)
VALUES
-- B1: COMPLETED - customer01, phòng 101 (Standard, 3 đêm = 2.400.000)
(1, 4, 1,
    DATE_SUB(CURDATE(), INTERVAL 10 DAY),
    DATE_SUB(CURDATE(), INTERVAL 7 DAY),
    2, 'Tầng cao, view đẹp',
    DATE_SUB(NOW(), INTERVAL 10 DAY),
    DATE_SUB(NOW(), INTERVAL 7 DAY),
    2400000.00, 720000.00, 'Completed', 'Online', NULL),
 
-- B2: COMPLETED - customer02, phòng 201 (Deluxe, 2 đêm = 2.400.000)
(2, 5, 6,
    DATE_SUB(CURDATE(), INTERVAL 5 DAY),
    DATE_SUB(CURDATE(), INTERVAL 3 DAY),
    2, NULL,
    DATE_SUB(NOW(), INTERVAL 5 DAY),
    DATE_SUB(NOW(), INTERVAL 3 DAY),
    2400000.00, 720000.00, 'Completed', 'Online', NULL),
 
-- B3: IN_PROGRESS - customer03, phòng 103 (Standard, 3 đêm = 2.400.000)
(3, 6, 3,
    DATE_SUB(CURDATE(), INTERVAL 1 DAY),
    DATE_ADD(CURDATE(), INTERVAL 2 DAY),
    1, 'Giường đơn',
    DATE_SUB(NOW(), INTERVAL 1 DAY), NULL,
    2400000.00, 720000.00, 'In-progress', 'PayAtHotel', NULL),
 
-- B4: IN_PROGRESS - customer01, phòng 203 (Deluxe, 3 đêm = 3.600.000)
(4, 4, 8,
    CURDATE(),
    DATE_ADD(CURDATE(), INTERVAL 3 DAY),
    2, NULL,
    NOW(), NULL,
    3600000.00, 1080000.00, 'In-progress', 'Online', NULL),
 
-- B5: CONFIRMED - customer04, phòng 301 (Superior, 3 đêm = 4.800.000)
(5, 7, 10,
    DATE_ADD(CURDATE(), INTERVAL 3 DAY),
    DATE_ADD(CURDATE(), INTERVAL 6 DAY),
    2, 'Kỷ niệm ngày cưới, cần hoa trang trí',
    NULL, NULL,
    4800000.00, 1440000.00, 'Confirmed', 'Online', NULL),
 
-- B6: CONFIRMED - customer02, phòng 401 (Junior Suite, 3 đêm = 7.500.000)
(6, 5, 13,
    DATE_ADD(CURDATE(), INTERVAL 7 DAY),
    DATE_ADD(CURDATE(), INTERVAL 10 DAY),
    3, NULL,
    NULL, NULL,
    7500000.00, 2250000.00, 'Confirmed', 'Online', NULL),
 
-- B7: PENDING - customer03, phòng 302 (Superior, 2 đêm = 3.200.000)
(7, 6, 11,
    DATE_ADD(CURDATE(), INTERVAL 14 DAY),
    DATE_ADD(CURDATE(), INTERVAL 16 DAY),
    2, NULL,
    NULL, NULL,
    3200000.00, 960000.00, 'Pending', 'Online',
    DATE_ADD(NOW(), INTERVAL 25 MINUTE)),
 
-- B8: CANCELLED - customer04, phòng 102 (Standard, 2 đêm = 1.600.000)
(8, 7, 2,
    DATE_ADD(CURDATE(), INTERVAL 1 DAY),
    DATE_ADD(CURDATE(), INTERVAL 3 DAY),
    2, NULL,
    NULL, NULL,
    1600000.00, 0.00, 'Cancelled', 'PayAtHotel', NULL);
 
-- ============================================================
-- 9. PAYMENTS
-- ============================================================
INSERT INTO Payments
    (PaymentID, BookingID, PaymentMethod, Amount, PaymentDate, PaymentStatus, TransactionID)
VALUES
-- B1: Đặt cọc (Bank Transfer) + Thanh toán nốt (Credit Card)
(1, 1, 'Bank Transfer',  720000.00,  DATE_SUB(NOW(), INTERVAL 12 DAY), 'Completed', 'TXN-B1-DEPOSIT'),
(2, 1, 'Credit Card',   1680000.00,  DATE_SUB(NOW(), INTERVAL 7 DAY),  'Completed', 'TXN-B1-FINAL'),
 
-- B2: Đặt cọc (E-Wallet) + Thanh toán nốt (Cash)
(3, 2, 'E-Wallet',       720000.00,  DATE_SUB(NOW(), INTERVAL 6 DAY),  'Completed', 'TXN-B2-DEPOSIT'),
(4, 2, 'Cash',          1680000.00,  DATE_SUB(NOW(), INTERVAL 3 DAY),  'Completed', 'TXN-B2-FINAL'),
 
-- B3: Đặt cọc tại quầy (Cash)
(5, 3, 'Cash',           720000.00,  DATE_SUB(NOW(), INTERVAL 1 DAY),  'Completed', 'TXN-B3-DEPOSIT'),
 
-- B4: Đặt cọc online (Bank Transfer)
(6, 4, 'Bank Transfer', 1080000.00,  DATE_SUB(NOW(), INTERVAL 2 DAY),  'Completed', 'TXN-B4-DEPOSIT'),
 
-- B5: Đặt cọc (E-Wallet)
(7, 5, 'E-Wallet',      1440000.00,  DATE_SUB(NOW(), INTERVAL 1 DAY),  'Completed', 'TXN-B5-DEPOSIT'),
 
-- B6: Đặt cọc (Bank Transfer)
(8, 6, 'Bank Transfer', 2250000.00,  NOW(),                             'Completed', 'TXN-B6-DEPOSIT');
 
-- ============================================================
-- 10. REVIEWS
-- ============================================================
INSERT INTO Reviews (ReviewID, BookingID, Rating, Comment, Reply, ReviewDate) VALUES
(1, 1, 5,
    'Phòng rất sạch sẽ, nhân viên thân thiện. Tôi rất hài lòng với dịch vụ!',
    'Cảm ơn quý khách đã tin tưởng lưu trú tại khách sạn chúng tôi. Rất mong được đón tiếp quý khách lần sau!',
    DATE_SUB(NOW(), INTERVAL 6 DAY)),
 
(2, 2, 4,
    'Phòng đẹp, view tốt. Tiếc là wifi hơi chậm vào buổi tối. Nhìn chung vẫn rất đáng tiền.',
    'Cảm ơn quý khách đã phản hồi. Chúng tôi sẽ nâng cấp hệ thống WiFi trong thời gian sớm nhất!',
    DATE_SUB(NOW(), INTERVAL 2 DAY));
 
-- ============================================================
-- VERIFY
-- ============================================================
SELECT 'Roles'    AS TableName, COUNT(*) AS Total FROM Roles    UNION ALL
SELECT 'Users',                  COUNT(*)          FROM Users    UNION ALL
SELECT 'Amenities',              COUNT(*)          FROM Amenities UNION ALL
SELECT 'RoomTypes',              COUNT(*)          FROM RoomTypes UNION ALL
SELECT 'RoomTypeAmenities',      COUNT(*)          FROM RoomTypeAmenities UNION ALL
SELECT 'RoomImages',             COUNT(*)          FROM RoomImages UNION ALL
SELECT 'Rooms',                  COUNT(*)          FROM Rooms    UNION ALL
SELECT 'Bookings',               COUNT(*)          FROM Bookings UNION ALL
SELECT 'Payments',               COUNT(*)          FROM Payments UNION ALbookingsL
SELECT 'Reviews',                COUNT(*)          FROM Reviews;