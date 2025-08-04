-- =========================================================
-- Your Car Your Way - Schéma MySQL 
-- =========================================================

CREATE DATABASE IF NOT EXISTS yourcaryourway
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE yourcaryourway;

-- Bonnes pratiques globales
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- =========================
-- TABLE: users
-- =========================
CREATE TABLE IF NOT EXISTS users (
  id              BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  email           VARCHAR(255) NOT NULL UNIQUE,
  password_hash   VARCHAR(255) NOT NULL,
  first_name      VARCHAR(100),
  last_name       VARCHAR(100),
  birth_date      DATE,
  address_line    VARCHAR(255),
  city            VARCHAR(120),
  country         VARCHAR(120),
  created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at      DATETIME NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE INDEX idx_users_email ON users(email);

-- =========================
-- TABLE: locations (agences)
-- =========================
CREATE TABLE IF NOT EXISTS locations (
  id          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  country     VARCHAR(120) NOT NULL,
  city        VARCHAR(120) NOT NULL,
  address     VARCHAR(255) NOT NULL
) ENGINE=InnoDB;

CREATE INDEX idx_locations_city ON locations(city);

-- =========================
-- TABLE: vehicles
-- =========================
CREATE TABLE IF NOT EXISTS vehicles (
  id              BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  brand           VARCHAR(100) NOT NULL,
  model           VARCHAR(100) NOT NULL,
  acriss_code     VARCHAR(10),                -- conformité ACRISS
  category        VARCHAR(50),                -- redondant lisible
  seats           SMALLINT UNSIGNED,
  transmission    VARCHAR(50),                -- automatic/manual
  fuel_type       VARCHAR(50),
  price_per_day   DECIMAL(10,2) NOT NULL,
  image_url       VARCHAR(500)
) ENGINE=InnoDB;

CREATE INDEX idx_vehicles_category ON vehicles(category);

-- =========================
-- TABLE: reservations
-- =========================
-- status: CONFIRMED / CANCELLED / COMPLETED / PENDING
CREATE TABLE IF NOT EXISTS reservations (
  id                    BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  user_id               BIGINT UNSIGNED NOT NULL,
  vehicle_id            BIGINT UNSIGNED NOT NULL,
  pickup_location_id    BIGINT UNSIGNED NOT NULL,
  return_location_id    BIGINT UNSIGNED NOT NULL,
  pickup_date           DATETIME NOT NULL,
  return_date           DATETIME NOT NULL,
  total_price           DECIMAL(10,2) NOT NULL,
  status                ENUM('PENDING','CONFIRMED','CANCELLED','COMPLETED') DEFAULT 'PENDING',
  created_at            DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at            DATETIME NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_res_user      FOREIGN KEY (user_id)            REFERENCES users(id)       ON DELETE CASCADE,
  CONSTRAINT fk_res_vehicle   FOREIGN KEY (vehicle_id)         REFERENCES vehicles(id)    ON DELETE RESTRICT,
  CONSTRAINT fk_res_pickup    FOREIGN KEY (pickup_location_id) REFERENCES locations(id)   ON DELETE RESTRICT,
  CONSTRAINT fk_res_return    FOREIGN KEY (return_location_id) REFERENCES locations(id)   ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE INDEX idx_res_user    ON reservations(user_id);
CREATE INDEX idx_res_vehicle ON reservations(vehicle_id);
CREATE INDEX idx_res_dates   ON reservations(pickup_date, return_date);
CREATE INDEX idx_res_status  ON reservations(status);

-- =========================
-- TABLE: payments
-- =========================
-- status: SUCCESS / FAILED / REFUNDED
CREATE TABLE IF NOT EXISTS payments (
  id               BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  user_id          BIGINT UNSIGNED NOT NULL,
  reservation_id   BIGINT UNSIGNED NOT NULL,
  amount           DECIMAL(10,2) NOT NULL,
  payment_date     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  provider         VARCHAR(50) NOT NULL DEFAULT 'Stripe',
  provider_ref     VARCHAR(255),                 -- id Stripe (intent/charge)
  status           ENUM('SUCCESS','FAILED','REFUNDED') NOT NULL,
  CONSTRAINT fk_pay_user  FOREIGN KEY (user_id)        REFERENCES users(id)         ON DELETE CASCADE,
  CONSTRAINT fk_pay_res   FOREIGN KEY (reservation_id) REFERENCES reservations(id)  ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE INDEX idx_pay_user     ON payments(user_id);
CREATE INDEX idx_pay_res      ON payments(reservation_id);
CREATE INDEX idx_pay_status   ON payments(status);

-- =========================
-- TABLE: chat_sessions (support client / chat)
-- =========================
CREATE TABLE IF NOT EXISTS chat_sessions (
  session_id    BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  user_id       BIGINT UNSIGNED NOT NULL,        
  agent_id      BIGINT UNSIGNED NULL,            
  status        ENUM('OPEN','CLOSED') DEFAULT 'OPEN',
  created_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  closed_at     DATETIME NULL DEFAULT NULL,
  CONSTRAINT fk_cs_user  FOREIGN KEY (user_id)  REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT fk_cs_agent FOREIGN KEY (agent_id) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE INDEX idx_cs_user_status ON chat_sessions(user_id, status);
CREATE INDEX idx_cs_created_at  ON chat_sessions(created_at);

-- =========================
-- TABLE: messages (modifiée)
-- =========================
CREATE TABLE IF NOT EXISTS messages (
  id           BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  session_id   BIGINT UNSIGNED NOT NULL,        
  sender_id    BIGINT UNSIGNED NOT NULL,         
  role         ENUM('CLIENT','AGENT','SYSTEM') NOT NULL,  
  text         TEXT NOT NULL,
  created_at   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_read      BOOLEAN NOT NULL DEFAULT FALSE,
  CONSTRAINT fk_msg_session FOREIGN KEY (session_id) REFERENCES chat_sessions(session_id) ON DELETE CASCADE,
  CONSTRAINT fk_msg_sender  FOREIGN KEY (sender_id)  REFERENCES users(id)         ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE INDEX idx_msg_session_time ON messages(session_id, created_at);
CREATE INDEX idx_msg_sender       ON messages(sender_id);

-- =========================
-- TABLE: documents (pièces justificatives)
-- =========================
-- type: ID / DRIVING_LICENSE / OTHER
CREATE TABLE IF NOT EXISTS documents (
  id           BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  user_id      BIGINT UNSIGNED NOT NULL,
  type         ENUM('ID','DRIVING_LICENSE','OTHER') NOT NULL,
  file_url     VARCHAR(500) NOT NULL,            -- URL S3
  uploaded_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_doc_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE INDEX idx_doc_user ON documents(user_id);

-- =========================
-- FIN
-- =========================
