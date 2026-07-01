<?php require_once 'db.php';
$d = input();
$pdo->prepare('INSERT INTO achievements(user_id,stars,badges,streak_days) VALUES(?,?,?,?) ON DUPLICATE KEY UPDATE stars=?,badges=?,streak_days=?')->execute([$d['user_id'], $d['stars'], $d['badges'], $d['streak_days'], $d['stars'], $d['badges'], $d['streak_days']]);
ok(null, 'Đã lưu thành tích');
