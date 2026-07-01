<?php require __DIR__ . '/header.php'; ?><section class="page-head">
    <h1>👧 Tài khoản của bé</h1>
    <p>Xem thông tin cá nhân và thành tích học tập.</p>
</section>
<section class="section profile-grid">
    <div class="profile-card profile-top">
        <div class="avatar"><?= h(mb_substr(current_user()['name'], 0, 1, 'UTF-8')) ?></div>
        <h2><?= h(current_user()['name']) ?></h2>
        <p class="muted">Tuổi: <?= h(current_user()['age']) ?></p>
        <p class="muted">Email phụ huynh: <?= h(current_user()['email']) ?></p><a class="btn danger" href="logout.php">Đăng xuất</a>
        <div class="stats">
            <div class="stat"><b><?= h($ach['stars']) ?></b><span>⭐ Sao</span></div>
            <div class="stat"><b><?= h($ach['badges']) ?></b><span>🏅 Huy hiệu</span></div>
            <div class="stat"><b><?= h($ach['streak_days']) ?></b><span>🔥 Ngày học</span></div>
        </div>
    </div>
    <div class="profile-card">
        <h2>🎮 Lịch sử Quiz</h2><?php if (!$quiz): ?><p class="muted">Chưa có kết quả quiz.</p><?php else: ?><table class="table">
                <tr>
                    <th>Điểm</th>
                    <th>Tổng</th>
                    <th>Thời gian</th>
                </tr><?php foreach ($quiz as $r): ?><tr>
                        <td><?= h($r['score']) ?></td>
                        <td><?= h($r['total']) ?></td>
                        <td><?= h($r['created_at']) ?></td>
                    </tr><?php endforeach; ?>
            </table><?php endif; ?><h2 style="margin-top:28px">🎤 Lịch sử luyện nói</h2><?php if (!$practice): ?><p class="muted">Chưa có lịch sử luyện nói.</p><?php else: ?><table class="table">
                <tr>
                    <th>Từ</th>
                    <th>Bé nói</th>
                    <th>Điểm</th>
                    <th>Kết quả</th>
                </tr><?php foreach ($practice as $r): ?><tr>
                        <td><?= h($r['word']) ?></td>
                        <td><?= h($r['spoken_text']) ?></td>
                        <td><?= h($r['score']) ?></td>
                        <td><?= h($r['result_label']) ?></td>
                    </tr><?php endforeach; ?>
            </table><?php endif; ?>
    </div>
</section><?php require __DIR__ . '/footer.php'; ?>