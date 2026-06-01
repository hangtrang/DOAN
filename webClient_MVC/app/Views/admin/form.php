<?php function input_type_for_mvc($field, $type)
{
    $f = strtolower($field);
    $t = strtolower($type);
    if (str_contains($f, 'email')) return 'email';
    if (str_contains($t, 'int') || str_contains($t, 'decimal') || str_contains($t, 'float')) return 'number';
    if (str_contains($f, 'date')) return 'date';
    return 'text';
} ?>
<!doctype html>
<html lang="vi">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Form Admin</title>
    <link rel="stylesheet" href="assets/style.css">
</head>

<body>
    <div class="form-page">
        <div class="form-card">
            <div class="form-head">
                <div>
                    <h1><?= $isEdit ? '✏️ Sửa dữ liệu' : '➕ Thêm dữ liệu' ?></h1>
                    <p>Bảng: <b><?= h($table) ?></b></p>
                </div><a class="btn secondary" href="admin.php?table=<?= h($table) ?>">← Quay lại</a>
            </div>
            <form method="post" enctype="multipart/form-data" class="crud-form"><input type="hidden" name="table" value="<?= h($table) ?>"><?php if ($isEdit): ?><input type="hidden" name="id" value="<?= h($id) ?>"><?php endif; ?><?php foreach ($columns as $col): $field = $col['Field'];
                                                                                                                                                                                                                                            $type = $col['Type'];
                                                                                                                                                                                                                                            $extra = strtolower($col['Extra'] ?? '');
                                                                                                                                                                                                                                            if ($field === $pk && str_contains($extra, 'auto_increment')) continue;
                                                                                                                                                                                                                                            $value = $row[$field] ?? ''; ?><div class="field"><label><?= h($field) ?> <small><?= h($type) ?></small></label><?php if ($field === 'image_url'): ?><?php if ($value): ?><img class="preview-img" src="<?= h(normalize_image_url($value)) ?>" onerror="this.style.display='none'"> <?php endif; ?><input type="text" name="image_url" value="<?= h($value) ?>" placeholder="URL ảnh từ server_api/uploads/..."><input type="file" name="image_file" accept="image/png,image/jpeg,image/webp"><small>Upload ảnh mới hoặc nhập URL ảnh.</small><?php elseif (str_contains(strtolower($type), 'text') || $field === 'description' || $field === 'example'): ?><textarea name="<?= h($field) ?>" rows="4"><?= h($value) ?></textarea><?php elseif ($table === 'users' && $field === 'role'): ?><select name="role"><?php foreach (['user', 'admin', 'super_admin'] as $r): ?><option value="<?= $r ?>" <?= $value === $r ? 'selected' : '' ?>><?= $r ?></option><?php endforeach; ?></select><?php elseif ($table === 'users' && $field === 'password'): ?><input type="password" name="password" value="" placeholder="<?= $isEdit ? 'Để trống nếu không đổi mật khẩu' : 'Nhập mật khẩu' ?>"><?php elseif ($table === 'vocabularies' && $field === 'topic_id'): ?><select name="topic_id"><?php foreach ($topics as $t): ?><option value="<?= h($t['id']) ?>" <?= $value == $t['id'] ? 'selected' : '' ?>><?= h($t['name']) ?></option><?php endforeach; ?></select><?php else: ?><input type="<?= h(input_type_for_mvc($field, $type)) ?>" name="<?= h($field) ?>" value="<?= h($value) ?>"><?php endif; ?></div><?php endforeach; ?><div class="form-actions"><button class="btn primary" type="submit">Lưu dữ liệu</button><a class="btn secondary" href="admin.php?table=<?= h($table) ?>">Hủy</a></div>
            </form>
        </div>
    </div>
</body>

</html>