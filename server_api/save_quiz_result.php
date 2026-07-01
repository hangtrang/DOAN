<?php require_once 'db.php';
$d = input();
$uid = intval($d['user_id'] ?? 0);
$score = intval($d['score'] ?? 0);
$total = intval($d['total'] ?? 0);
$pdo->prepare('INSERT INTO quiz_results(user_id,score,total) VALUES(?,?,?)')->execute([$uid, $score, $total]);
$pdo->prepare('INSERT INTO achievements(user_id,stars,badges,streak_days) VALUES(?,?,?,1) ON DUPLICATE KEY UPDATE stars=stars+?, badges=badges+IF(?>=?,1,0)')->execute([$uid, $score, ($score == $total ? 1 : 0), $score, $score, $total]);
ok(null, 'Đã lưu điểm GameQuiz');
