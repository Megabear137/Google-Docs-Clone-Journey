BEGIN TRANSACTION;

INSERT INTO users (id, email, password_hash, created_at)
VALUES (1, 'test@example.com', 'fake_hash', NOW());

COMMIT;