DROP TABLE IF EXISTS USER;
  
CREATE TABLE USER (
  id INT PRIMARY KEY,
  first_name VARCHAR(50) NOT NULL,
  last_name VARCHAR(50) NOT NULL,
  email VARCHAR(50) DEFAULT NULL
);