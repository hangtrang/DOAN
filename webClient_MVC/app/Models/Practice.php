<?php
class Practice
{
    private PDO $db;
    public function __construct()
    {
        $this->db = Database::connect();
    }
    public function save($uid, $word, $spoken, $score, $label)
    {
        $this->db->prepare('INSERT INTO practice_history(user_id,word,spoken_text,score,result_label) VALUES(?,?,?,?,?)')->execute([$uid, $word, $spoken, $score, $label]);
        $this->db->prepare('INSERT INTO achievements(user_id,stars,badges,streak_days) VALUES(?,?,?,1) ON DUPLICATE KEY UPDATE stars=stars+?, badges=badges+IF(?>=90,1,0), streak_days=streak_days+1')->execute([$uid, max(1, intdiv($score, 20)), ($score >= 90 ? 1 : 0), max(1, intdiv($score, 20)), $score]);
    }
    public function history($uid)
    {
        $st = $this->db->prepare('SELECT * FROM practice_history WHERE user_id=? ORDER BY id DESC LIMIT 10');
        $st->execute([$uid]);
        return $st->fetchAll();
    }
}
