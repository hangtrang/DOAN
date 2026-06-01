<?php
class Quiz
{
    private PDO $db;
    public function __construct()
    {
        $this->db = Database::connect();
    }
    public function randomQuestions($limit = 10)
    {
        return $this->db->query('SELECT * FROM quiz_questions ORDER BY RAND() LIMIT ' . intval($limit))->fetchAll();
    }
    public function saveResult($uid, $score, $total)
    {
        $this->db->prepare('INSERT INTO quiz_results(user_id,score,total) VALUES(?,?,?)')->execute([$uid, $score, $total]);
        $this->db->prepare('INSERT INTO achievements(user_id,stars,badges,streak_days) VALUES(?,?,?,1) ON DUPLICATE KEY UPDATE stars=stars+?, badges=badges+IF(?>=?,1,0), streak_days=streak_days+1')->execute([$uid, $score, ($score == $total ? 1 : 0), $score, $score, $total]);
    }
    public function history($uid)
    {
        $st = $this->db->prepare('SELECT * FROM quiz_results WHERE user_id=? ORDER BY id DESC LIMIT 10');
        $st->execute([$uid]);
        return $st->fetchAll();
    }
}
