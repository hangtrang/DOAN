<?php require __DIR__ . '/header.php'; ?><div class="auth-wrap">
    <div class="auth-card">
        <div class="brand-icon" style="margin:auto">ABC</div>
        <h1>Đăng ký tài khoản</h1>
        <p>Sau khi đăng ký thành công, bé sẽ đăng nhập để học.</p><?php if ($error): ?><div class="alert error"><?= h($error) ?></div><?php endif; ?><form method="post"><label>Họ tên bé</label><input name="name" required value="<?= h($_POST['name'] ?? '') ?>" placeholder="Ví dụ: Trang"><label>Tuổi</label><input type="number" name="age" min="6" max="10" required value="<?= h($_POST['age'] ?? '') ?>" placeholder="6 - 10"><label>Email phụ huynh</label><input type="email" name="email" required value="<?= h($_POST['email'] ?? '') ?>" placeholder="parent@gmail.com"><label>Mật khẩu</label><input type="password" name="password" required placeholder="Abc@123"><button class="btn primary" style="width:100%;margin-top:18px">Đăng ký</button></form>
        <p>Đã có tài khoản? <a href="login.php"><b>Đăng nhập</b></a></p>
    </div>
</div><?php require __DIR__ . '/footer.php'; ?>