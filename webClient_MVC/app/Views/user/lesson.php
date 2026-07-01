<?php require __DIR__ . '/header.php'; ?><section class="page-head">
    <h1><?= h($topic['name'] ?? 'Bài học') ?> 🌟</h1>
    <p><?= h($topic['description'] ?? 'Học từ vựng với hình ảnh lớn và phát âm.') ?></p>
</section>
<section class="section"><?php if (!$word): ?><div class="empty">Chủ đề này chưa có từ vựng. Admin hãy thêm từ trong trang quản trị.</div><?php else: ?><div class="lesson-card">
            <div class="progress-line"><span style="width:<?= h(round((($index + 1) / max(1, $total)) * 100)) ?>%"></span></div>
            <p class="muted">Từ <?= h($index + 1) ?> / <?= h($total) ?></p>
            <div class="word-layout"><img class="word-img" src="<?= h(normalize_image_url($word['image_url'])) ?>" onerror="this.src='assets/no-image.svg'" alt="<?= h($word['word']) ?>">
                <div class="word-main">
                    <h1><?= h($word['word']) ?></h1>
                    <h2><?= h($word['meaning']) ?></h2>
                    <div class="example">💬 <?= h($word['example'] ?: 'Let us learn this word!') ?></div>
                    <div class="word-controls"><span class="speak-note">🔤 Phát âm: <?= h($word['word']) ?></span><?php if ($index > 0): ?><a class="btn ghost" href="lesson.php?topic_id=<?= h($topicId) ?>&i=<?= h($index - 1) ?>">← Từ trước</a><?php endif; ?><?php if ($index < $total - 1): ?><a class="btn ghost" href="lesson.php?topic_id=<?= h($topicId) ?>&i=<?= h($index + 1) ?>">Từ tiếp →</a><?php endif; ?><a class="btn ghost" href="practice.php?word=<?= urlencode($word['word']) ?>">🎤 Luyện nói</a></div>
                </div>
            </div>
        </div><?php endif; ?></section><?php require __DIR__ . '/footer.php'; ?>