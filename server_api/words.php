<?php
require_once 'db.php';

function api_base_url()
{
    $scheme = (!empty($_SERVER['HTTPS']) && $_SERVER['HTTPS'] !== 'off') ? 'https' : 'http';
    $host = $_SERVER['HTTP_HOST'] ?? 'localhost';
    $path = rtrim(str_replace('\\', '/', dirname($_SERVER['SCRIPT_NAME'] ?? '')), '/');

    return $scheme . '://' . $host . $path . '/';
}

$topicId = isset($_GET['topic_id']) ? (int)$_GET['topic_id'] : 0;

if ($topicId <= 0) {
    fail('Thiếu topic_id');
}

$base = api_base_url();

$stmt = $pdo->prepare(
    'SELECT id, topic_id, word, meaning, image_url, example
     FROM vocabularies
     WHERE topic_id = ?
     ORDER BY id ASC'
);

$stmt->execute([$topicId]);
$rows = $stmt->fetchAll(PDO::FETCH_ASSOC);

foreach ($rows as &$row) {
    $img = trim((string)($row['image_url'] ?? ''));

    if ($img === '') {
        $img = 'uploads/default.png';
    }

    $img = str_replace('../server_api/', '', $img);
    $img = str_replace('server_api/', '', $img);
    $img = ltrim($img, '/');

    if (!preg_match('/^https?:\/\//i', $img)) {
        $img = $base . $img;
    }

    $row['image_url'] = $img;
}

ok($rows);