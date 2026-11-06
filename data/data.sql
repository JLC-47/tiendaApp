CREATE SCHEMA tienda_db;
USE tienda_db;
 
CREATE TABLE roles(
  id int primary key not null auto_increment,
  name varchar(50) not null
);
 
CREATE TABLE users(
  id int primary key not null auto_increment,
  username varchar(50) not null unique,
  email varchar(255) not null unique,
  password varchar(255) not null,
  active boolean not null default true,
  rol_id int not null,
  foreign key (rol_id) references roles (id)
);
 
CREATE TABLE categories(
  id int primary key not null auto_increment,
  name varchar(100) not null unique
);
 
CREATE TABLE products(
  id int primary key not null auto_increment,
  name varchar(150) not null,
  description varchar(500),
  price decimal(12,2) not null,
  stock int not null,
  category_id int not null,
  seller_id int not null,
  foreign key (category_id) references categories (id),
  foreign key (seller_id) references users (id)
);
 
CREATE TABLE orders(
  id int primary key not null auto_increment,
  client_id int not null,
  product_id int not null,
  quantity int not null,
  total decimal(12,2) not null,
  status varchar(20) not null default 'PENDIENTE',
  created_at datetime not null default current_timestamp,
  foreign key (client_id) references users (id),
  foreign key (product_id) references products (id)
);
 
INSERT INTO roles (name) VALUES ('ADMIN'), ('CLIENTE'), ('VENDEDOR');


INSERT INTO users (username, email, password, active, rol_id) VALUES
('admin', 'admin@tienda.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lHHG', true, 1),
('cliente1', 'cliente@tienda.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lHHG', true, 2),
('vendedor1', 'vendedor@tienda.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lHHG', true, 3);

INSERT INTO categories (name) VALUES
('Electronica'),
('Ropa'),
('Hogar');


INSERT INTO products (name, description, price, stock, category_id, seller_id) VALUES
('Celular Samsung', 'Samsung Galaxy A54', 1200000, 10, 1, 3),
('Camiseta Nike', 'Camiseta deportiva talla M', 80000, 25, 2, 3),
('Lámpara LED', 'Lámpara de escritorio', 45000, 15, 3, 3);

SELECT id, username, email, active, rol_id FROM users;
SELECT password FROM users WHERE username = 'admin';

UPDATE users 
SET password = '$2a$10$slYQmyNdgTY18LLaDgjgGeGA8A8p50JGcKbRMWpguBHapyLjjJqWq'
WHERE username = 'admin';
SELECT password FROM users WHERE username = 'admin';

UPDATE users u1
JOIN users u2 ON u2.username = 'temp'
SET u1.password = u2.password
WHERE u1.username = 'admin';

DELETE FROM users WHERE username = 'temp';

INSERT INTO users (username, email, password, active, rol_id) 
VALUES ('admin2', 'admin2@tienda.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lHHG', true, 1);

UPDATE users SET rol_id = 1 WHERE username = 'superadmin';
select*from users; 