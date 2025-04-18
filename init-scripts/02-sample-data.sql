-- ========== ROLES & PERMISSIONS ==========

-- Insert sample roles
INSERT INTO ROLES (name, description, active, create_by, create_at) 
VALUES 
  ('ADMIN', 'System administrator with all privileges', TRUE, 'system', CURRENT_TIMESTAMP),
  ('USER', 'Standard user account', TRUE, 'system', CURRENT_TIMESTAMP),
  ('CAREGIVER', 'Service provider account', TRUE, 'system', CURRENT_TIMESTAMP)
ON CONFLICT (name) DO NOTHING;

-- Insert sample permissions
INSERT INTO PERMISSIONS (name, description, active, create_by, create_at)
VALUES
  ('USER_READ', 'Permission to read user data', TRUE, 'system', CURRENT_TIMESTAMP),
  ('USER_WRITE', 'Permission to create/update user data', TRUE, 'system', CURRENT_TIMESTAMP),
  ('USER_DELETE', 'Permission to delete user data', TRUE, 'system', CURRENT_TIMESTAMP),
  ('BOOKING_READ', 'Permission to read booking data', TRUE, 'system', CURRENT_TIMESTAMP),
  ('BOOKING_WRITE', 'Permission to create/update booking data', TRUE, 'system', CURRENT_TIMESTAMP),
  ('BOOKING_DELETE', 'Permission to delete booking data', TRUE, 'system', CURRENT_TIMESTAMP),
  ('PRODUCT_READ', 'Permission to read product data', TRUE, 'system', CURRENT_TIMESTAMP),
  ('PRODUCT_WRITE', 'Permission to create/update product data', TRUE, 'system', CURRENT_TIMESTAMP),
  ('PRODUCT_DELETE', 'Permission to delete product data', TRUE, 'system', CURRENT_TIMESTAMP)
ON CONFLICT (name) DO NOTHING;

-- Associate permissions with roles
-- Admin has all permissions
INSERT INTO permissions_roles (role_id, permission_id)
SELECT r.id, p.id FROM ROLES r, PERMISSIONS p
WHERE r.name = 'ADMIN'
ON CONFLICT DO NOTHING;

-- User has limited permissions
INSERT INTO permissions_roles (role_id, permission_id)
SELECT r.id, p.id FROM ROLES r, PERMISSIONS p
WHERE r.name = 'USER' AND p.name IN ('USER_READ', 'BOOKING_READ', 'BOOKING_WRITE', 'PRODUCT_READ')
ON CONFLICT DO NOTHING;

-- Caregiver has service provider permissions
INSERT INTO permissions_roles (role_id, permission_id)
SELECT r.id, p.id FROM ROLES r, PERMISSIONS p
WHERE r.name = 'CAREGIVER' AND p.name IN ('USER_READ', 'BOOKING_READ', 'PRODUCT_READ')
ON CONFLICT DO NOTHING;

-- ========== USERS ==========

-- Insert sample users (password is 'password123' hashed with bcrypt)
INSERT INTO USERS (
  full_name, 
  email, 
  password, 
  phone, 
  address, 
  is_email_confirmed, 
  avatar, 
  create_by, 
  create_at
) 
VALUES 
  (
    'Admin User', 
    'admin@domicare.com', 
    '$2a$10$rPiEAgQNIT1TCoKi3Eqq8eVaRaAc2Xqi0L.CE4TYqhW9xOQlqHBfK', 
    '0901234567', 
    '123 Admin St, District 1, HCMC', 
    TRUE, 
    'https://res.cloudinary.com/drtizu6d3/image/upload/v1/avatar/default-avatar.png',
    'system',
    CURRENT_TIMESTAMP
  ),
  (
    'Regular User', 
    'user@example.com', 
    '$2a$10$rPiEAgQNIT1TCoKi3Eqq8eVaRaAc2Xqi0L.CE4TYqhW9xOQlqHBfK', 
    '0909876543', 
    '456 User Ave, District 2, HCMC', 
    TRUE,
    'https://res.cloudinary.com/drtizu6d3/image/upload/v1/avatar/default-avatar.png',
    'system',
    CURRENT_TIMESTAMP
  ),
  (
    'Care Provider', 
    'caregiver@example.com', 
    '$2a$10$rPiEAgQNIT1TCoKi3Eqq8eVaRaAc2Xqi0L.CE4TYqhW9xOQlqHBfK', 
    '0918765432', 
    '789 Care Blvd, District 3, HCMC', 
    TRUE,
    'https://res.cloudinary.com/drtizu6d3/image/upload/v1/avatar/default-avatar.png',
    'system',
    CURRENT_TIMESTAMP
  )
ON CONFLICT (email) DO NOTHING;

-- Associate users with roles
INSERT INTO users_roles (user_id, role_id)
SELECT u.id, r.id FROM USERS u, ROLES r 
WHERE u.email = 'admin@domicare.com' AND r.name = 'ADMIN'
ON CONFLICT DO NOTHING;

INSERT INTO users_roles (user_id, role_id)
SELECT u.id, r.id FROM USERS u, ROLES r 
WHERE u.email = 'user@example.com' AND r.name = 'USER'
ON CONFLICT DO NOTHING;

INSERT INTO users_roles (user_id, role_id)
SELECT u.id, r.id FROM USERS u, ROLES r 
WHERE u.email = 'caregiver@example.com' AND r.name = 'CAREGIVER'
ON CONFLICT DO NOTHING;

-- ========== CATEGORIES & PRODUCTS ==========

-- Insert sample categories
INSERT INTO CATEGORY (name, description, create_by, create_at)
VALUES 
  ('Basic Care', 'Essential care services for daily needs', 'system', CURRENT_TIMESTAMP),
  ('Medical Care', 'Health-related care services', 'system', CURRENT_TIMESTAMP),
  ('Special Care', 'Specialized care services', 'system', CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

-- Insert sample products
INSERT INTO PRODUCT (
  name, 
  description, 
  price, 
  duration, 
  is_active, 
  category_id, 
  create_by, 
  create_at
)
VALUES 
  (
    'Daily Home Care', 
    'Assistance with daily activities like cleaning, cooking, and personal care',
    150000, 
    60, 
    TRUE, 
    (SELECT id FROM CATEGORY WHERE name = 'Basic Care'),
    'system',
    CURRENT_TIMESTAMP
  ),
  (
    'Medication Management', 
    'Help with taking medications on schedule and medication monitoring',
    200000, 
    90, 
    TRUE, 
    (SELECT id FROM CATEGORY WHERE name = 'Medical Care'),
    'system',
    CURRENT_TIMESTAMP
  ),
  (
    'Companionship Service', 
    'Friendly companionship and social interaction for elderly people',
    100000, 
    120, 
    TRUE, 
    (SELECT id FROM CATEGORY WHERE name = 'Basic Care'),
    'system',
    CURRENT_TIMESTAMP
  ),
  (
    'Dementia Care', 
    'Specialized care for people with dementia or Alzheimer''s disease',
    250000, 
    120, 
    TRUE, 
    (SELECT id FROM CATEGORY WHERE name = 'Special Care'),
    'system',
    CURRENT_TIMESTAMP
  ),
  (
    'Physical Therapy', 
    'In-home physical therapy services',
    180000, 
    60, 
    TRUE, 
    (SELECT id FROM CATEGORY WHERE name = 'Medical Care'),
    'system',
    CURRENT_TIMESTAMP
  )
ON CONFLICT DO NOTHING;

-- Insert sample files (product images)
INSERT INTO FILE (name, url, product_id, create_by, create_at)
VALUES
  (
    'daily-care.jpg', 
    'https://res.cloudinary.com/drtizu6d3/image/upload/v1/products/daily-care.jpg', 
    (SELECT id FROM PRODUCT WHERE name = 'Daily Home Care'), 
    'system', 
    CURRENT_TIMESTAMP
  ),
  (
    'medication.jpg', 
    'https://res.cloudinary.com/drtizu6d3/image/upload/v1/products/medication.jpg', 
    (SELECT id FROM PRODUCT WHERE name = 'Medication Management'), 
    'system', 
    CURRENT_TIMESTAMP
  ),
  (
    'companionship.jpg', 
    'https://res.cloudinary.com/drtizu6d3/image/upload/v1/products/companionship.jpg', 
    (SELECT id FROM PRODUCT WHERE name = 'Companionship Service'), 
    'system', 
    CURRENT_TIMESTAMP
  )
ON CONFLICT DO NOTHING;

-- ========== BOOKINGS & REVIEWS ==========

-- Insert sample bookings
INSERT INTO BOOKINGS (
  address,
  booking_date,
  total_hours,
  total_price,
  note,
  is_periodic,
  booking_status,
  user_id,
  create_by,
  create_at
)
SELECT 
  '123 Client Street, District 1, HCMC',
  CURRENT_TIMESTAMP + INTERVAL '1 day',
  2.5,
  375000, -- 2.5 hours * 150,000
  'Please arrive on time. The door code is 1234.',
  FALSE,
  'CONFIRMED',
  u.id,
  'system',
  CURRENT_TIMESTAMP
FROM USERS u
WHERE u.email = 'user@example.com'
ON CONFLICT DO NOTHING;

INSERT INTO BOOKINGS (
  address,
  booking_date,
  total_hours,
  total_price,
  note,
  is_periodic,
  booking_status,
  user_id,
  create_by,
  create_at
)
SELECT 
  '456 Client Avenue, District 2, HCMC',
  CURRENT_TIMESTAMP + INTERVAL '3 day',
  1.5,
  300000, -- 1.5 hours * 200,000
  'Medication assistance needed. Has difficulty swallowing pills.',
  TRUE,
  'PENDING',
  u.id,
  'system',
  CURRENT_TIMESTAMP
FROM USERS u
WHERE u.email = 'user@example.com'
ON CONFLICT DO NOTHING;

-- Associate bookings with products
INSERT INTO booking_products (booking_id, product_id)
SELECT b.id, p.id
FROM BOOKINGS b
CROSS JOIN PRODUCT p
WHERE p.name = 'Daily Home Care'
AND b.booking_status = 'CONFIRMED'
ON CONFLICT DO NOTHING;

INSERT INTO booking_products (booking_id, product_id)
SELECT b.id, p.id
FROM BOOKINGS b
CROSS JOIN PRODUCT p
WHERE p.name = 'Medication Management'
AND b.booking_status = 'PENDING'
ON CONFLICT DO NOTHING;

-- Insert sample reviews
INSERT INTO REVIEW (
  content,
  rating,
  product_id,
  user_id,
  create_by,
  create_at
)
SELECT
  'Great service! The caregiver was very professional and caring.',
  5,
  p.id,
  u.id,
  'system',
  CURRENT_TIMESTAMP
FROM PRODUCT p, USERS u
WHERE p.name = 'Daily Home Care' AND u.email = 'user@example.com'
ON CONFLICT DO NOTHING;

INSERT INTO REVIEW (
  content,
  rating,
  product_id,
  user_id,
  create_by,
  create_at
)
SELECT
  'Service was good but could be more thorough with the cleaning.',
  4,
  p.id,
  u.id,
  'system',
  CURRENT_TIMESTAMP
FROM PRODUCT p, USERS u
WHERE p.name = 'Companionship Service' AND u.email = 'user@example.com'
ON CONFLICT DO NOTHING;