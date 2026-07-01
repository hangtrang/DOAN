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
    $url = preg_replace('#^(\.\./)+#', '', $url);   // ../server_api/uploads/... -> server_api/uploads/...
    $url = preg_replace('#^server_api/#', '', $url); // server_api/uploads/... -> uploads/...
    $url = ltrim($url, '/');
    return $url;
}

$base = api_base_url();
$rows = $pdo->query('SELECT id, name, description, image_url FROM topics ORDER BY id ASC')->fetchAll(PDO::FETCH_ASSOC);

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
