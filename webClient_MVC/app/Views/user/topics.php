<?php require __DIR__ . '/header.php'; ?><section class="page-head">
    <h1>📚 Chọn chủ đề</h1>
    <p>Bé hãy chọn một chủ đề để bắt đầu học từ vựng.</p>
</section>
<section class="section">
    <div class="grid"><?php foreach ($topics as $topic): ?><a class="topic-card" href="lesson.php?topic_id=<?= h($topic['id']) ?>"><img class="topic-img" src="<?= h(normalize_image_url($topic['image_url'])) ?>" onerror="this.src='assets/no-image.svg'" alt="<?= h($topic['name']) ?>">
                <h3><?= h($topic['name']) ?></h3>
                <p><?= h($topic['description']) ?></p>
            </a><?php endforeach; ?></div>
</section><?php require __DIR__ . '/footer.php'; ?>