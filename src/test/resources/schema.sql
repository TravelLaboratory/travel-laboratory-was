-- Users 테이블 생성 SQL
DROP TABLE if EXISTS users;
CREATE TABLE users
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    username        VARCHAR(100) NOT NULL,
    password        TEXT         NOT NULL,
    role            VARCHAR(10)  NOT NULL COMMENT 'USER, ADMIN',
    nickname        VARCHAR(10)  NOT NULL,
    profile_img_url VARCHAR(255),
    introduce       TEXT         NOT NULL,
    is_agreement    BOOLEAN      NOT NULL,
    created_at      TIMESTAMP    NOT NULL,
    updated_at      TIMESTAMP    NOT NULL,
    status          VARCHAR(10)  NOT NULL COMMENT 'ACTIVE, INACTIVE'
);

-- Article 테이블 생성 SQL
DROP TABLE if EXISTS article;
CREATE TABLE article
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT       NOT NULL,
    title         VARCHAR(255) NOT NULL,
    cover_img_url VARCHAR(255),
    start_at      TIMESTAMP    NOT NULL,
    end_at        TIMESTAMP    NOT NULL,
    expense       VARCHAR(10)  NOT NULL,
    travel_companion VARCHAR(10),
    created_at    TIMESTAMP    NOT NULL,
    updated_at    TIMESTAMP    NOT NULL,
    status        VARCHAR(10)  NOT NULL COMMENT 'ACTIVE, INACTIVE, PRIVATE',
    FOREIGN KEY (user_id) REFERENCES users (id)
);

-- Article Location 테이블 생성 SQL
DROP TABLE if EXISTS article_location;
CREATE TABLE article_location
(
    article_id BIGINT NOT NULL,
    place_id   VARCHAR(255) NOT NULL,
    address    VARCHAR(255) NOT NULL,
    city       VARCHAR(255) NOT NULL,
    PRIMARY KEY (article_id, place_id),
    FOREIGN KEY (article_id) REFERENCES article (id)
);

-- Article Style 테이블 생성 SQL
DROP TABLE if EXISTS article_travel_styles;
CREATE TABLE article_travel_styles
(
    article_id     BIGINT         NOT NULL,
    travel_styles  VARCHAR(30)    NOT NULL,
    PRIMARY KEY (article_id, travel_styles),
    FOREIGN KEY (article_id) REFERENCES article (id)
);

-- Review 테이블 생성 SQL
DROP TABLE if EXISTS review;
CREATE TABLE review
(
    id                     BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id                BIGINT       NOT NULL,
    article_id             BIGINT       NOT NULL,
    title                  VARCHAR(150) NOT NULL,
    representative_img_url VARCHAR(255),
    description            TEXT         NOT NULL,
    created_at             TIMESTAMP    NOT NULL,
    updated_at             TIMESTAMP    NOT NULL,
    status                 VARCHAR(10)  NOT NULL COMMENT 'ACTIVE, INACTIVE, PRIVATE',
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (article_id) REFERENCES article (id)
);

-- Comment 테이블 생성 SQL
DROP TABLE if EXISTS comment;
CREATE TABLE comment
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT      NOT NULL,
    review_id     BIGINT      NOT NULL,
    reply_comment TEXT        NOT NULL,
    status        VARCHAR(10) NOT NULL COMMENT 'ACTIVE, INACTIVE',
    created_at    TIMESTAMP   NOT NULL,
    updated_at    TIMESTAMP   NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (review_id) REFERENCES review (id)
);
