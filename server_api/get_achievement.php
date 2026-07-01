<?php require_once 'db.php';
$uid = intval($_GET['user_id'] ?? 0);
$pdo->prepare('INSERT IGNORE INTO achievements(user_id) VALUES(?)')->execute([$uid]);
$st = $pdo->prepare('SELECT * FROM achievements WHERE user_id=?');
$st->execute([$uid]);
ok($st->fetch(PDO::FETCH_ASSOC));
