<?php
class User
{
    private PDO $db;
    public function __construct()
    {
        $this->db = Database::connect();
    }
    public function findByEmail($email)
    {
        $st = $this->db->prepare('SELECT * FROM users WHERE email=? LIMIT 1');
        $st->execute([$email]);
        return $st->fetch();
    }
    public function create($name, $age, $email, $password, $role = 'user')
    {
        $st = $this->db->prepare('INSERT INTO users(name,age,email,password,role) VALUES(?,?,?,?,?)');
        return $st->execute([$name, $age, $email, password_hash($password, PASSWORD_DEFAULT), $role]);
    }
}
