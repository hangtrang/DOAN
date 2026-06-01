<?php
class Vocabulary
{
    private PDO $db;
    public function __construct()
    {
        $this->db = Database::connect();
    }
    public function byTopic($topicId)
    {
        $st = $this->db->prepare('SELECT * FROM vocabularies WHERE topic_id=? ORDER BY id ASC');
        $st->execute([$topicId]);
        return $st->fetchAll();
    }
}
