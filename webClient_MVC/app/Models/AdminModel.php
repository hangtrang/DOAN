<?php
class AdminModel
{
    public array $tables = [
        'topics' => ['label' => 'Chủ đề học', 'icon' => '📚', 'description' => 'Quản lý chủ đề Animals, Fruits, Colors...'],
        'vocabularies' => ['label' => 'Từ vựng', 'icon' => '🧠', 'description' => 'Quản lý từ vựng, nghĩa, ví dụ, ảnh minh họa'],
        'quiz_questions' => ['label' => 'Câu hỏi Quiz', 'icon' => '🎮', 'description' => 'Quản lý câu hỏi, đáp án, đáp án đúng'],
        'users' => ['label' => 'Người dùng', 'icon' => '👧', 'description' => 'Quản lý tài khoản học sinh/admin'],
        'quiz_results' => ['label' => 'Kết quả Quiz', 'icon' => '🏆', 'description' => 'Xem/sửa/xóa kết quả làm quiz'],
        'practice_history' => ['label' => 'Lịch sử luyện nói', 'icon' => '🎤', 'description' => 'Quản lý lịch sử luyện phát âm'],
        'achievements' => ['label' => 'Thành tích', 'icon' => '⭐', 'description' => 'Quản lý huy hiệu/thành tích của bé'],
    ];
    private PDO $db;
    public function __construct()
    {
        $this->db = Database::connect();
    }
    public function allowed($t)
    {
        return isset($this->tables[$t]);
    }
    public function columns($table)
    {
        return $this->db->query("SHOW COLUMNS FROM `$table`")->fetchAll();
    }
    public function pk($table)
    {
        foreach ($this->columns($table) as $c) {
            if (($c['Key'] ?? '') === 'PRI') return $c['Field'];
        }
        return 'id';
    }
    public function stats()
    {
        $s = [];
        foreach ($this->tables as $t => $m) {
            try {
                $s[$t] = (int)$this->db->query("SELECT COUNT(*) FROM `$t`")->fetchColumn();
            } catch (Exception $e) {
                $s[$t] = 0;
            }
        }
        return $s;
    }
    public function rows($table, $search = '')
    {
        $cols = $this->columns($table);
        $params = [];
        $where = '';
        if ($search !== '') {
            $likes = [];
            foreach ($cols as $c) {
                $type = strtolower($c['Type'] ?? '');
                if (str_contains($type, 'char') || str_contains($type, 'text')) {
                    $likes[] = '`' . $c['Field'] . '` LIKE ?';
                    $params[] = '%' . $search . '%';
                }
            }
            if ($likes) $where = ' WHERE ' . implode(' OR ', $likes);
        }
        $pk = $this->pk($table);
        $st = $this->db->prepare("SELECT * FROM `$table` $where ORDER BY `$pk` DESC LIMIT 100");
        $st->execute($params);
        return $st->fetchAll();
    }
    public function countRows($table, $search = '')
    {
        $cols = $this->columns($table);
        $params = [];
        $where = '';
        if ($search !== '') {
            $likes = [];
            foreach ($cols as $c) {
                $type = strtolower($c['Type'] ?? '');
                if (str_contains($type, 'char') || str_contains($type, 'text')) {
                    $likes[] = '`' . $c['Field'] . '` LIKE ?';
                    $params[] = '%' . $search . '%';
                }
            }
            if ($likes) $where = ' WHERE ' . implode(' OR ', $likes);
        }
        $st = $this->db->prepare("SELECT COUNT(*) FROM `$table` $where");
        $st->execute($params);
        return (int)$st->fetchColumn();
    }
    public function find($table, $id)
    {
        $pk = $this->pk($table);
        $st = $this->db->prepare("SELECT * FROM `$table` WHERE `$pk`=? LIMIT 1");
        $st->execute([$id]);
        return $st->fetch() ?: [];
    }
    public function delete($table, $id)
    {
        $pk = $this->pk($table);
        $st = $this->db->prepare("DELETE FROM `$table` WHERE `$pk`=?");
        $st->execute([$id]);
    }
    public function save($table, $data, $id = null)
    {
        $pk = $this->pk($table);
        unset($data['table'], $data['id']);
        if ($table === 'users' && empty($data['password'])) unset($data['password']);
        elseif ($table === 'users' && isset($data['password'])) $data['password'] = password_hash($data['password'], PASSWORD_DEFAULT);
        if ($id) {
            $sets = [];
            $vals = [];
            foreach ($data as $k => $v) {
                $sets[] = "`$k`=?";
                $vals[] = $v;
            }
            $vals[] = $id;
            $this->db->prepare("UPDATE `$table` SET " . implode(',', $sets) . " WHERE `$pk`=?")->execute($vals);
        } else {
            $fields = array_keys($data);
            $marks = array_fill(0, count($fields), '?');
            $this->db->prepare("INSERT INTO `$table` (`" . implode('`,`', $fields) . "`) VALUES (" . implode(',', $marks) . ")")->execute(array_values($data));
        }
    }
}
