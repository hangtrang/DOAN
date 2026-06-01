CREATE DATABASE IF NOT EXISTS kidsenglishai CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE kids_english_ai;

DROP TABLE IF EXISTS quiz_results;
DROP TABLE IF EXISTS quiz_questions;
DROP TABLE IF EXISTS practice_history;
DROP TABLE IF EXISTS achievements;
DROP TABLE IF EXISTS vocabularies;
DROP TABLE IF EXISTS topics;
DROP TABLE IF EXISTS users;

CREATE TABLE users(
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100),
  age INT,
  email VARCHAR(150) UNIQUE,
  password VARCHAR(255),
  role ENUM('user','admin') DEFAULT 'user',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE topics(
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  description TEXT,
  image_url VARCHAR(500) NOT NULL
);

CREATE TABLE vocabularies(
  id INT AUTO_INCREMENT PRIMARY KEY,
  topic_id INT NOT NULL,
  word VARCHAR(100) NOT NULL,
  meaning VARCHAR(255) NOT NULL,
  image_url VARCHAR(500) NOT NULL,
  example VARCHAR(255),
  FOREIGN KEY(topic_id) REFERENCES topics(id) ON DELETE CASCADE
);

CREATE TABLE achievements(
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT UNIQUE,
  stars INT DEFAULT 0,
  badges INT DEFAULT 0,
  streak_days INT DEFAULT 0,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE practice_history(
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT,
  word VARCHAR(100),
  spoken_text VARCHAR(255),
  score INT,
  result_label VARCHAR(100),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE quiz_questions(
  id INT AUTO_INCREMENT PRIMARY KEY,
  question VARCHAR(255),
  option_a VARCHAR(100),
  option_b VARCHAR(100),
  option_c VARCHAR(100),
  option_d VARCHAR(100),
  correct_answer CHAR(1),
  image_url VARCHAR(500) DEFAULT NULL
);

CREATE TABLE quiz_results(
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT,
  score INT,
  total INT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO users(name,age,email,password,role) VALUES
('Admin',10,'admin@gmail.com',MD5('Admin@123'),'admin');

INSERT INTO topics(id,name,description,image_url) VALUES
(1,'Animals','Chủ đề động vật','uploads/topics/animals.png'),
(2,'Fruits','Chủ đề trái cây','uploads/topics/fruits.png'),
(3,'Colors','Chủ đề màu sắc','uploads/topics/colors.png'),
(4,'Numbers','Chủ đề số đếm','uploads/topics/numbers.png'),
(5,'Greetings','Chủ đề lời chào','uploads/topics/greetings.png');

INSERT INTO vocabularies(topic_id,word,meaning,image_url,example) VALUES
(1,'Dog','Con chó','uploads/words/dog.png','This is a dog.'),
(1,'Cat','Con mèo','uploads/words/cat.png','This is a cat.'),
(1,'Lion','Con sư tử','uploads/words/lion.png','The lion is strong.'),
(1,'Elephant','Con voi','uploads/words/elephant.png','The elephant is big.'),
(1,'Tiger','Con hổ','uploads/words/tiger.png','The tiger runs fast.'),
(2,'Apple','Quả táo','uploads/words/apple.png','I eat an apple.'),
(2,'Banana','Quả chuối','uploads/words/banana.png','The banana is yellow.'),
(2,'Orange','Quả cam','uploads/words/orange.png','Orange juice is yummy.'),
(2,'Grape','Quả nho','uploads/words/grape.png','Grapes are small.'),
(2,'Mango','Quả xoài','uploads/words/mango.png','The mango is sweet.'),
(3,'Red','Màu đỏ','uploads/words/red.png','The apple is red.'),
(3,'Blue','Màu xanh dương','uploads/words/blue.png','The sky is blue.'),
(3,'Yellow','Màu vàng','uploads/words/yellow.png','The sun is yellow.'),
(3,'Green','Màu xanh lá','uploads/words/green.png','The leaf is green.'),
(4,'One','Số một','uploads/words/one.png','One star.'),
(4,'Two','Số hai','uploads/words/two.png','Two apples.'),
(4,'Three','Số ba','uploads/words/three.png','Three cats.'),
(4,'Four','Số bốn','uploads/words/four.png','Four dogs.'),
(4,'Five','Số năm','uploads/words/five.png','Five birds.'),
(5,'Hello','Xin chào','uploads/words/hello.png','Hello, my friend.'),
(5,'Good morning','Chào buổi sáng','uploads/words/good_morning.png','Good morning, teacher.'),
(5,'Goodbye','Tạm biệt','uploads/words/goodbye.png','Goodbye, see you again.'),
(5,'Thank you','Cảm ơn','uploads/words/thank_you.png','Thank you very much.'),
(5,'Sorry','Xin lỗi','uploads/words/sorry.png','I am sorry.');

INSERT INTO quiz_questions(question,option_a,option_b,option_c,option_d,correct_answer,image_url) VALUES
('Cat nghĩa là gì?','Con mèo','Con chó','Quả táo','Màu xanh','A','uploads/words/cat.png'),
('Apple là gì?','Con hổ','Quả táo','Số một','Xin chào','B','uploads/words/apple.png'),
('Goodbye nghĩa là gì?','Cảm ơn','Tạm biệt','Màu đỏ','Con voi','B','uploads/words/goodbye.png');
