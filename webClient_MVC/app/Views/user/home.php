<?php require __DIR__ . '/header.php'; ?>
<section class="hero">
    <div class="hero-card">
        <h1>Học tiếng Anh thật vui cùng bé! 🌈</h1>
        <p>Website user dành cho trẻ em: học từ vựng bằng hình ảnh, nghe phát âm, luyện nói và làm quiz để tích lũy sao thành tích.</p>
        <div class="hero-actions"><a class="btn primary" href="topics.php">📚 Bắt đầu học</a><a class="btn yellow" href="quiz.php">🎮 Làm Quiz</a><?php if (!$user): ?><a class="btn ghost" href="register.php">👧 Tạo tài khoản</a><?php endif; ?></div>
    </div>
    <div class="hero-card mascot">
        <div class="bubble one">Hello!</div>
        <div class="face">🦊</div>
        <div class="bubble two">Let's learn ABC!</div>
    </div>
</section>
<section class="section">
    <div class="section-title">
        <div>
            <h2>Chức năng chính</h2>
            <p>Thiết kế sinh động, dễ dùng cho học sinh nhỏ tuổi.</p>
        </div>
    </div>
    <div class="grid"><a class="feature-card" href="topics.php">
            <div class="feature-icon">📚</div>
            <h3>Học từ vựng</h3>
            <p>Chọn chủ đề, xem ảnh lớn, nghĩa tiếng Việt và ví dụ tiếng Anh.</p>
        </a><a class="feature-card" href="practice.php">
            <div class="feature-icon">🎤</div>
            <h3>Luyện nói</h3>
            <p>Đọc từ tiếng Anh, tự chấm điểm mẫu và lưu lịch sử luyện tập.</p>
        </a><a class="feature-card" href="quiz.php">
            <div class="feature-icon">🎮</div>
            <h3>Quiz Game</h3>
            <p>Trắc nghiệm vui, tự tính điểm và lưu kết quả vào tài khoản.</p>
        </a><a class="feature-card" href="profile.php">
            <div class="feature-icon">⭐</div>
            <h3>Thành tích</h3>
            <p>Theo dõi sao, huy hiệu, streak và lịch sử học tập.</p>
        </a></div>
</section>
<section class="section">
    <div class="section-title">
        <div>
            <h2>Chủ đề nổi bật</h2>
            <p>Do admin thêm/sửa từ trang quản trị.</p>
        </div><a class="btn ghost" href="topics.php">Xem tất cả</a>
    </div>
    <div class="grid"><?php foreach ($topics as $topic): ?><a class="topic-card" href="lesson.php?topic_id=<?= h($topic['id']) ?>"><img class="topic-img" src="<?= h(normalize_image_url($topic['image_url'])) ?>" onerror="this.src='assets/no-image.svg'" alt="<?= h($topic['name']) ?>">
                <h3><?= h($topic['name']) ?></h3>
                <p><?= h($topic['description']) ?></p>
            </a><?php endforeach; ?></div>
</section>
<?php require __DIR__ . '/footer.php'; ?>