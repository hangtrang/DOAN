<?php require_once 'db.php';
ok($pdo->query('SELECT * FROM quiz_questions ORDER BY RAND() LIMIT 10')->fetchAll(PDO::FETCH_ASSOC));
