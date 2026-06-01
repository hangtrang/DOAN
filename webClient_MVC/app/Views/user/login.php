<?php require __DIR__ . '/header.php'; ?><div class="auth-wrap">
    <div class="auth-card">
        <div class="brand-icon" style="margin:auto">ABC</div>
        <h1>Đăng nhập</h1>
        <p>Đăng nhập để học và lưu thành tích.</p><?php if (isset($_GET['registered'])): ?><div class="alert success">Đăng ký thành công. Bé hãy đăng nhập nhé!</div><?php endif; ?><?php if ($error): ?><div class="alert error"><?= h($error) ?></div><?php endif; ?><form method="post"><label>Email</label><input type="email" name="email" required placeholder="parent@gmail.com"><label>Mật khẩu</label><input type="password" name="password" required placeholder="Abc@123"><button class="btn primary" style="width:100%;margin-top:18px">Đăng nhập</button></form>
        <p>Chưa có tài khoản? <a href="register.php"><b>Đăng ký</b></a></p>
    </div>
</div><?php require __DIR__ . '/footer.php'; ?>