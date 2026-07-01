<?php
class Topic
{
    private PDO $db;
    public function __construct()
    {
        $this->db = Database::connect();
    }
    public function all()
    {
        return $this->db->query('SELECT * FROM topics ORDER BY id ASC')->fetchAll();
    }
    public function limit($n)
    {
        return $this->db->query('SELECT * FROM topics ORDER BY id ASC LIMIT ' . intval($n))->fetchAll();
    }
    public function find($id)
    {
        $st = $this->db->prepare('SELECT * FROM topics WHERE id=?');
        $st->execute([$id]);
        return $st->fetch();
    }
}
