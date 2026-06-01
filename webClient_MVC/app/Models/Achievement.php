<?php
class Achievement
{
    private PDO $db;
    public function __construct()
    {
        $this->db = Database::connect();
    }
    public function get($uid)
    {
        $this->db->prepare('INSERT IGNORE INTO achievements(user_id) VALUES(?)')->execute([$uid]);
        $st = $this->db->prepare('SELECT * FROM achievements WHERE user_id=?');
        $st->execute([$uid]);
        return $st->fetch() ?: ['stars' => 0, 'badges' => 0, 'streak_days' => 0];
    }
}
