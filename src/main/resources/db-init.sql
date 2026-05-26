-- =============================================================
-- Mini Release Tracker — Database Initialisation Script
-- Run this script once to create the database before starting
-- the Spring Boot application.
-- =============================================================

CREATE DATABASE IF NOT EXISTS release_tracker_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE release_tracker_db;

-- The `releases` table is created automatically by Hibernate
-- (spring.jpa.hibernate.ddl-auto=update).
-- The script below pre-populates sample data for quick testing.

-- Sample data (run after first application start so table exists)
INSERT IGNORE INTO releases
    (id, project_name, version, environment, release_date, status, description, created_at, updated_at)
VALUES
    (1, 'InventoryService', 'v1.0.0', 'DEV',  '2024-01-15', 'DEPLOYED', 'Initial DEV deployment',              NOW(), NOW()),
    (2, 'InventoryService', 'v1.0.0', 'QA',   '2024-01-20', 'PENDING',  'Awaiting QA sign-off',                NOW(), NOW()),
    (3, 'PaymentGateway',   'v2.3.1', 'PROD', '2024-01-22', 'DEPLOYED', 'Hotfix for payment timeout issue',    NOW(), NOW()),
    (4, 'UserAuthService',  'v3.0.0', 'DEV',  '2024-01-25', 'FAILED',   'Build failed — dependency conflict',  NOW(), NOW()),
    (5, 'PaymentGateway',   'v2.3.0', 'PROD', '2024-01-10', 'ROLLBACK', 'Rolled back due to prod instability', NOW(), NOW());
