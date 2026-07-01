<?php require_once '../db.php';
$d = input();
$st = $pdo->prepare("SELECT id,name,age,email FROM users WHERE email=? AND password=MD5(?) AND role='user'");
$st->execute([$d['email'] ?? '', $d['password'] ?? '']);
$u = $st->fetch(PDO::FETCH_ASSOC);
$u ? ok($u, 'Đăng nhập thành công') : fail('Sai email hoặc mật khẩu');
