<?php require __DIR__ . '/header.php'; ?><section class="page-head">
    <h1>🎤 Luyện nói</h1>
    <p>Nhìn từ cần luyện, sau đó nhập lại từ bé đã nói để hệ thống chấm điểm mô phỏng.</p>
</section>
<section class="section"><?php if (isset($_GET['score'])): ?><div class="alert success">Kết quả: <?= h($_GET['score']) ?> điểm - <?= h($_GET['label'] ?? '') ?></div><?php endif; ?><div class="lesson-card practice-box">
        <div>
            <p class="muted">Từ cần luyện</p>
            <div class="practice-word"><?= h($wordParam) ?></div>
            <div class="hero-actions"><span class="speak-note">🔤 Phát âm: <?= h($wordParam) ?></span><a class="btn ghost" href="topics.php">Chọn từ khác</a></div>
        </div>
        <form method="post"><label>Từ tiếng Anh</label><input name="word" value="<?= h($wordParam) ?>" required><label>Bé đã nói / nhập lại từ</label><input name="spoken_text" placeholder="Ví dụ: Apple" required><button class="btn yellow" style="width:100%;margin-top:18px">⭐ Chấm điểm</button></form>
    </div>
</section><?php require __DIR__ . '/footer.php'; ?>