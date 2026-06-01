<!doctype html>
<html lang="vi">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Tiếng Anh Cho Bé</title>
    <link rel="stylesheet" href="assets/style.css">
</head>

<body>
    <div class="app-shell">
        <aside class="sidebar">
            <div class="logo"><span>ABC</span>
                <div><b>Tiếng Anh Cho Bé</b><small>Admin Dashboard</small></div>
            </div>
            <nav><?php foreach ($TABLES as $key => $meta): ?><a class="nav-item <?= $key === $table ? 'active' : '' ?>" href="admin.php?table=<?= h($key) ?>"><span class="nav-icon"><?= h($meta['icon']) ?></span><span><?= h($meta['label']) ?></span><em><?= h($stats[$key]) ?></em></a><?php endforeach; ?></nav>
        </aside>
        <main class="main">
            <header class="topbar">
                <div>
                    <h1><?= h($TABLES[$table]['icon'] . ' ' . $TABLES[$table]['label']) ?></h1>
                    <p><?= h($TABLES[$table]['description']) ?></p>
                </div>
                <div class="admin-pill"><span><?= h($admin['name'] ?? 'Admin') ?></span><small><?= h($admin['role'] ?? 'admin') ?></small><a href="logout.php">Đăng xuất</a></div>
            </header>
            <section class="stats-grid"><?php foreach ($TABLES as $key => $meta): ?><a class="stat-card <?= $key === $table ? 'selected' : '' ?>" href="admin.php?table=<?= h($key) ?>"><span><?= h($meta['icon']) ?></span><b><?= h($stats[$key]) ?></b><small><?= h($meta['label']) ?></small></a><?php endforeach; ?></section><?php if (isset($_GET['saved'])): ?><div class="alert success">Đã lưu dữ liệu thành công.</div><?php endif; ?><?php if (isset($_GET['deleted'])): ?><div class="alert success">Đã xóa dữ liệu thành công.</div><?php endif; ?><section class="panel">
                <div class="panel-head">
                    <form class="search" method="get"><input type="hidden" name="table" value="<?= h($table) ?>"><input type="text" name="q" value="<?= h($search) ?>" placeholder="Tìm kiếm trong bảng <?= h($table) ?>..."><button class="btn secondary" type="submit">Tìm</button></form><a class="btn primary" href="form.php?table=<?= h($table) ?>">+ Thêm mới</a>
                </div>
                <div class="table-wrap">
                    <table>
                        <thead>
                            <tr><?php foreach ($columns as $col): ?><th><?= h($col['Field']) ?></th><?php endforeach; ?><th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody><?php if (!$rows): ?><tr>
                                    <td colspan="<?= count($columns) + 1 ?>" class="empty">Chưa có dữ liệu.</td>
                                </tr><?php endif; ?><?php foreach ($rows as $row): ?><tr><?php foreach ($columns as $col): $field = $col['Field'];
                                                                                                $value = $row[$field] ?? ''; ?><td><?php if ($field === 'image_url' && $value): ?><div class="image-cell"><img src="<?= h(normalize_image_url($value)) ?>" onerror="this.style.display='none'"><span title="<?= h($value) ?>"><?= h($value) ?></span></div><?php elseif ($field === 'password'): ?><span class="muted">••••••</span><?php else: ?><?= h(mb_strimwidth((string)$value, 0, 80, '...')) ?><?php endif; ?></td><?php endforeach; ?><td class="actions"><a class="btn small edit" href="form.php?table=<?= h($table) ?>&id=<?= h($row[$pk]) ?>">Sửa</a><a class="btn small danger" href="admin.php?table=<?= h($table) ?>&delete=<?= h($row[$pk]) ?>">Xóa</a></td>
                                </tr><?php endforeach; ?></tbody>
                    </table>
                </div>
                <div class="panel-foot">Hiển thị <?= count($rows) ?> / <?= h($total) ?> bản ghi. Quyền hiện tại: thêm, sửa, xóa toàn bộ.</div>
            </section>
        </main>
    </div>
</body>

</html>