<?php
if (session_status() === PHP_SESSION_NONE) session_start();
function h($v)
{
    return htmlspecialchars((string)$v, ENT_QUOTES, 'UTF-8');
}
function redirect_to($url)
{
    header('Location: ' . $url);
    exit;
}
function current_user()
{
    return $_SESSION['user'] ?? null;
}
function current_admin()
{
    return $_SESSION['admin_user'] ?? null;
}
function require_user()
{
    if (!current_user()) redirect_to('login.php');
}
function require_admin()
{
    if (!current_admin() || !in_array((string)(current_admin()['role'] ?? ''), ['admin', 'super_admin'], true)) redirect_to('login.php');
}
function asset_url($path)
{
    return 'assets/' . ltrim($path, '/');
}

function server_api_web_base_url()
{
    $script = str_replace('\\', '/', $_SERVER['SCRIPT_NAME'] ?? '');
    $marker = '/webClient_MVC/';
    $pos = strpos($script, $marker);
    if ($pos !== false) {
        return substr($script, 0, $pos + 1) . 'server_api/';
    }
    return '../../server_api/';
}

function clean_image_path($url)
{
    $url = trim((string)$url);
    if ($url === '') return '';
    if (preg_match('/^https?:\/\//i', $url)) return $url;

    $url = str_replace('\\', '/', $url);
    $url = preg_replace('#^(\.\./)+#', '', $url);       // bỏ ../ ở đầu nếu có
    $url = preg_replace('#^server_api/#', '', $url);      // bỏ server_api/ nếu có
    $url = ltrim($url, '/');
    return $url;
}

function normalize_image_url($url)
{
    $url = clean_image_path($url);

    if ($url === '') {
        return server_api_web_base_url() . 'uploads/default.png';
    }

    if (preg_match('/^https?:\/\//i', $url)) {
        return $url;
    }

    return server_api_web_base_url() . $url;
}

function password_matches($input, $hash)
{
    $input = trim($input);
    $hash = trim((string)$hash);
    return $input === $hash || md5($input) === $hash || password_verify($input, $hash);
}
function render($view, $data = [])
{
    extract($data);
    require __DIR__ . '/../Views/' . $view . '.php';
}
