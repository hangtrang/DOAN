<?php require_once 'db.php';
$st = $pdo->prepare('SELECT id,word,spoken_text AS spokenText,score,result_label AS resultLabel FROM practice_history WHERE user_id=? ORDER BY id DESC LIMIT 30');
$st->execute([intval($_GET['user_id'] ?? 0)]);
ok($st->fetchAll(PDO::FETCH_ASSOC));
