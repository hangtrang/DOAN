<?php
require_once __DIR__ . '/Core/Database.php';
require_once __DIR__ . '/Core/Controller.php';
spl_autoload_register(function ($class) {
    foreach ([__DIR__ . '/Models/' . $class . '.php', __DIR__ . '/Controllers/' . $class . '.php'] as $file) if (file_exists($file)) require_once $file;
});
