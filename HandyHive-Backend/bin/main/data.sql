-- HandyHive Final Mock Data
-- Password for ALL users is: password
-- Hash: $2a$10$eAccYoB7ygMLnrVDdpCNveSci71ZALRqqELatZff9.FCc8S/31.Y6

-- 1. Services
INSERT INTO service (name, base_price, description)
VALUES
    ('Plumbing', 50.00, 'General plumbing services'),
    ('Electrician', 60.00, 'Electrical repairs and installations'),
    ('Handyman', 40.00, 'General home repairs'),
    ('Deep Cleaning', 35.00, 'Full house deep cleaning')
ON CONFLICT (name) DO NOTHING;

-- 2. Subscriptions
INSERT INTO subscription (plan_name, price_czk, description)
SELECT 'Standard', 0.00, 'Basic'
WHERE NOT EXISTS (SELECT 1 FROM subscription WHERE plan_name = 'Standard');

INSERT INTO subscription (plan_name, price_czk, description)
SELECT 'Monthly', 400.00, 'Pro'
WHERE NOT EXISTS (SELECT 1 FROM subscription WHERE plan_name = 'Monthly');

INSERT INTO subscription (plan_name, price_czk, description)
SELECT 'Annual', 4000.00, 'Elite'
WHERE NOT EXISTS (SELECT 1 FROM subscription WHERE plan_name = 'Annual');

-- 3. Customers
INSERT INTO customer (first_name, last_name, email, password_hash, role, subscription_id, registration_date)
VALUES
    ('John', 'Doe', 'john.doe@customer.com', '$2a$10$eAccYoB7ygMLnrVDdpCNveSci71ZALRqqELatZff9.FCc8S/31.Y6', 'ROLE_ADMIN', 1, '2024-01-01'),
    ('Alice', 'Wonder', 'alice.w@mail.com', '$2a$10$eAccYoB7ygMLnrVDdpCNveSci71ZALRqqELatZff9.FCc8S/31.Y6', 'ROLE_USER', 1, '2024-02-01')
ON CONFLICT (email) DO UPDATE SET password_hash = EXCLUDED.password_hash;

-- 4. Providers
INSERT INTO provider (first_name, last_name, email, password_hash, is_vetted, avg_rating, subscription_id, bio)
VALUES
    ('Elena', 'Spark', 'elena.spark@handy.com', '$2a$10$eAccYoB7ygMLnrVDdpCNveSci71ZALRqqELatZff9.FCc8S/31.Y6', TRUE, 4.7, 1, 'Expert Electrician'),
    ('Peter', 'Plumber', 'peter.plumber@handy.com', '$2a$10$eAccYoB7ygMLnrVDdpCNveSci71ZALRqqELatZff9.FCc8S/31.Y6', TRUE, 4.8, 1, 'Master Plumber')
ON CONFLICT (email) DO UPDATE SET password_hash = EXCLUDED.password_hash;