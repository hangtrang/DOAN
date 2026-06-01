<?php
require_once 'db.php';

function api_base_url()
{
    $scheme = (!empty($_SERVER['HTTPS']) && $_SERVER['HTTPS'] !== 'off') ? 'https' : 'http';
    $host = $_SERVER['HTTP_HOST'] ?? 'localhost';
    $path = rtrim(str_replace('\\', '/', dirname($_SERVER['SCRIPT_NAME'] ?? '')), '/');
    return $scheme . '://' . $host . $path . '/';
}

function clean_api_image_path($url)
{
    $url = trim((string)$url);
    if ($url === '') return '';
    if (preg_match('/^https?:\/\//i', $url)) return $url;

    $url = str_replace('\\', '/', $url);
    $url = preg_replace('#^(\.\./)+#', '', $url);
    $url = preg_replace('#^server_api/#', '', $url);
    $url = ltrim($url, '/');
    return $url;
}

$base = api_base_url();
$topicId = isset($_GET['topic_id']) ? intval($_GET['topic_id']) : 0;

if ($topicId > 0) {
    $stmt = $pdo->prepare('SELECT id, topic_id, word, meaning, image_url, example FROM vocabularies WHERE topic_id = ? ORDER BY id ASC');
    $stmt->execute([$topicId]);
    $rows = $stmt->fetchAll(PDO::FETCH_ASSOC);
} else {
    $rows = $pdo->query('SELECT id, topic_id, word, meaning, image_url, example FROM vocabularies ORDER BY id ASC')->fetchAll(PDO::FETCH_ASSOC);
}

foreach ($rows as &$row) {
    $img = clean_api_image_path($row['image_url'] ?? '');

    if ($img === '') {
        $img = 'uploads/default.png';
    }

    if (!preg_match('/^https?:\/\//i', $img)) {
        $img = $base . $img;
    }

    $row['image_url'] = $img;
}

ok($rows);
