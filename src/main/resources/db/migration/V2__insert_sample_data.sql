-- Insert customers
INSERT INTO customers (id, email) VALUES
(1, 'alice@example.com'),
(2, 'bob@example.com');

-- Insert products
INSERT INTO products (name, stock_quantity, price) VALUES
('Laptop', 20, 1200.00),
('Headphones', 100, 150.00),
('Keyboard', 50, 75.00);

-- Insert orders
INSERT INTO orders (customer_id, total_amount, status) VALUES
(1, 1350.00, 'PAID'),
(2, 75.00, 'PENDING');

-- Insert order_items
INSERT INTO order_items (order_id, product_id, quantity, price) VALUES
(1, 1, 1, 1200.00),
(1, 2, 1, 150.00),
(1, 3, 1, 75.00);

-- Insert payments
INSERT INTO payments (order_id, amount, status, payment_method) VALUES
(1, 1350.00, 'SUCCESS', 'CREDIT_CARD'),
(2, 75.00, 'FAILED', 'PAYPAL');
