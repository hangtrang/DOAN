<?php
require_once __DIR__ . '/helpers.php';
class Controller
{
    protected function view($view, $data = [])
    {
        render($view, $data);
    }
}
