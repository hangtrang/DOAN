<?php
header('Content-Type: application/json; charset=utf-8');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Headers: Content-Type');
header('Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS');
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') exit;
$host = 'localhost';
$db = 'kids_english_ai';
$user = 'root';
$pass = '';
try {
    $pdo = new PDO("mysql:host=$host;dbname=$db;charset=utf8mb4", $user, $pass, [PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION]);
} catch (Exception $e) {
    echo json_encode(['success' => false, 'message' => 'DB connection failed: ' . $e->getMessage(), 'data' => null]);
    exit;
}
function input()
{
    return json_decode(file_get_contents('php://input'), true) ?: $_POST;
}
function ok($data = null, $msg = 'OK')
{
    echo json_encode(['success' => true, 'message' => $msg, 'data' => $data], JSON_UNESCAPED_UNICODE);
    exit;
}
function fail($msg = 'Error')
{
    echo json_encode(['success' => false, 'message' => $msg, 'data' => null], JSON_UNESCAPED_UNICODE);
    exit;
}
