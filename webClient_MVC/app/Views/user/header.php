<?php $user = current_user();
$admin = current_admin(); ?>
<!doctype html>
<html lang="vi">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><?= isset($page_title) ? h($page_title) . ' - ' : '' ?>Tiếng Anh Cho Bé</title>
    <link rel="stylesheet" href="assets/style.css">
</head>

<body>
    <header class="site-header"><a class="brand" href="index.php"><span class="brand-icon">ABC</span><span><b>Tiếng Anh Cho Bé</b><small>Kids English AI</small></span></a><input class="nav-toggle" id="nav-toggle" type="checkbox"><label class="menu-btn" for="nav-toggle">☰</label>
        <nav class="main-nav"><a href="index.php">Trang chủ</a><a href="topics.php">Học từ vựng</a><a href="quiz.php">Quiz Game</a><a href="practice.php">Luyện nói</a><?php if ($admin): ?><a class="admin-link" href="admin.php">Quản trị</a><?php endif; ?><?php if ($user): ?><a href="profile.php">Tài khoản</a><a class="nav-logout" href="logout.php">Đăng xuất</a><?php else: ?><a href="login.php">Đăng nhập</a><a class="nav-primary" href="register.php">Đăng ký</a><?php endif; ?></nav>
    </header>
    <main>