<?php require_once 'db.php';
$d = input();
$uid = intval($d['user_id'] ?? 0);
$score = intval($d['score'] ?? 0);
$pdo->prepare('INSERT INTO practice_history(user_id,word,spoken_text,score,result_label) VALUES(?,?,?,?,?)')->execute([$uid, $d['word'] ?? '', $d['spoken_text'] ?? '', $score, $d['result_label'] ?? '']);
$pdo->prepare('INSERT INTO achievements(user_id,stars,badges,streak_days) VALUES(?,?,?,1) ON DUPLICATE KEY UPDATE stars=stars+?, badges=badges+IF(?>=90,1,0), streak_days=streak_days+1')->execute([$uid, max(1, intdiv($score, 20)), ($score >= 90 ? 1 : 0), max(1, intdiv($score, 20)), $score]);
ok(null, 'Đã lưu luyện nói');
