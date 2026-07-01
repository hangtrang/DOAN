<?php require_once '../db.php';
$d = input();
$st = $pdo->prepare('UPDATE users SET name=?,age=?,email=? WHERE id=? AND role="user"');
$st->execute([$d['name'] ?? '', intval($d['age'] ?? 0), $d['email'] ?? '', intval($d['user_id'] ?? 0)]);
$q = $pdo->prepare('SELECT id,name,age,email FROM users WHERE id=?');
$q->execute([intval($d['user_id'] ?? 0)]);
ok($q->fetch(PDO::FETCH_ASSOC), 'Đã cập nhật');
