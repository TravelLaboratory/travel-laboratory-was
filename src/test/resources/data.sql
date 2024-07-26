-- Users 데이터 삽입
INSERT INTO users
(username, password, role, nickname, profile_img_url, introduce, is_agreement,created_at, updated_at, status)
VALUES
--  [활성] 유저
    ('user1@example.com', 'password1', 'USER', '활성화_유저', 'https://example.com/user1.png','Intro of User One', true, NOW(), NOW(), 'ACTIVE'),
--  [삭제] 유저
    ('deleteUser@example.com', 'password2', 'USER', '비활성화_유저', 'https://example.com/user2.png','Intro of User Two', true, NOW(), NOW(), 'INACTIVE'),
--  [활성] 유저2
    ('user3@example.com', 'password3', 'USER', '활성화_유저2', 'https://example.com/user3.png','Intro of User Three', true, NOW(), NOW(), 'ACTIVE');

-- Article 데이터 삽입
INSERT INTO article (user_id, title, cover_img_url, start_at, end_at, expense, travel_companion,created_at, updated_at, status)
VALUES
--  [활성] 유저의 [삭제] 여행 계획
    (1, 'Trip to Paris', 'https://example.com/paris.png', '2023-08-01', '2023-08-10', '1000', '가족들과', NOW(), NOW(), 'INACTIVE'),
--  [활성] 유저의 [활성] 여행 계획
    (1, 'Trip to Paris', 'https://example.com/paris.png', '2023-08-01', '2023-08-10', '1000', '가족들과', NOW(), NOW(), 'ACTIVE'),
--  [삭제] 유저의 [삭제] 여행 계획
    (2, 'Business Trip to New York', 'https://example.com/ny.png', '2023-09-05', '2023-09-10','500', '동료와', NOW(), NOW(), 'INACTIVE'),
--  [활성] 유저2의 [활성] 여행 계획 (나만보기)
    (3, 'Trip to London', 'https://example.com/london.png', '2023-07-01', '2023-07-07', '1500', '친구와', NOW(), NOW(), 'PRIVATE');

-- Article Location 데이터 삽입
INSERT INTO article_location (article_id, place_id, address, city)
VALUES
--  [활성] 유저의 [삭제] 여행 계획 locations
    (1, 'place1', 'Eiffel Tower', 'Paris'),
    (1, 'place2', 'Louvre Museum', 'Paris'),
--  [활성] 유저의 [활성] 여행 계획 locations
    (2, 'place1', 'Eiffel Tower', 'Paris'),
    (2, 'place2', 'Louvre Museum', 'Paris'),
--  [삭제] 유저의 [삭제] 여행 계획 locations
    (3, 'place3', 'Times Square', 'New York'),
--  [활성] 유저2의 [활성] 여행 계획 locations
    (4, 'place4', 'Big Ben', 'London'),
    (4, 'place5', 'London Eye', 'London');

-- Article Style 데이터 삽입
INSERT INTO article_travel_styles (article_id, travel_styles)
VALUES
--  [활성] 유저의 [삭제] 여행 계획 styles
    (1, '힐링'),
    (1, '스파'),
--  [활성] 유저의 [활성] 여행 계획 styles
    (2, '힐링'),
    (2, '스파'),
    (2, '쉴 틈 없이 관광'),
--  [삭제] 유저의 [삭제] 여행 계획 styles
    (3, '맛집 탐방'),
--  [활성] 유저2의 [활성] 여행 계획 styles
    (4, '핫플레이스'),
    (4, '액티비티');

-- Review 데이터 삽입
INSERT INTO review (user_id, article_id, title, representative_img_url, description, created_at,updated_at, status)
VALUES
--  [활성] 유저의 [삭제] 후기
    (1, 1, 'Amazing Paris Trip', 'https://example.com/paris_review.png','We had an amazing time visiting Paris!', NOW(), NOW(), 'INACTIVE'),
--  [활성] 유저의 [활성] 후기
    (1, 2, 'Amazing Paris Trip', 'https://example.com/paris_review.png','We had an amazing time visiting Paris!', NOW(), NOW(), 'ACTIVE'),
--  [삭제] 유저의 [삭제] 후기
    (2, 3, 'Business Trip to NY', 'https://example.com/ny_review.png','Business trip was successful.', NOW(), NOW(), 'INACTIVE'),
--  [활성] 유저2의 [활성] 후기 (나만보기)
    (3, 4, 'Wonderful London Trip', 'https://example.com/london_review.png','London was wonderful!', NOW(), NOW(), 'PRIVATE');
