<?php
class Database
{
    private static ?PDO $pdo = null;
    public static function connect(): PDO
    {
        if (self::$pdo === null) {
            $host = 'localhost';
            $db = 'kids_english_ai';
            $user = 'root';
            $pass = '';
            self::$pdo = new PDO("mysql:host=$host;dbname=$db;charset=utf8mb4", $user, $pass, [
                PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
                PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
            ]);
        }
        return self::$pdo;
    }
}
