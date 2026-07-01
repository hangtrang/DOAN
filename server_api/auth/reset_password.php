<?php require_once '../db.php';
$d = input();
$st = $pdo->prepare('UPDATE users SET password=MD5(?) WHERE email=? AND role="user"');
$st->execute([$d['new_password'] ?? '', $d['email'] ?? '']);
$st->rowCount() ? ok(null, 'Đổi mật khẩu thành công') : fail('Email chưa được đăng ký');
