<?php require_once '../db.php';
$d = input();
if (empty($d['name']) || empty($d['email']) || empty($d['password'])) fail('Thiếu dữ liệu');
try {
    $st = $pdo->prepare('INSERT INTO users(name,age,email,password,role) VALUES(?,?,?,?,"user")');
    $st->execute([$d['name'], intval($d['age'] ?? 0), $d['email'], $d['password']]);
    $id = $pdo->lastInsertId();
    $pdo->prepare('INSERT INTO achievements(user_id) VALUES(?)')->execute([$id]);
    ok(['id' => $id, 'name' => $d['name'], 'age' => intval($d['age']), 'email' => $d['email']], 'Đăng ký thành công');
} catch (Exception $e) {
    fail('Email đã tồn tại');
}
