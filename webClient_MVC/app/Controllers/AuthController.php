<?php
class AuthController extends Controller
{
    public function login()
    {
        $error = '';
        if ($_SERVER['REQUEST_METHOD'] === 'POST') {
            $email = trim($_POST['email'] ?? '');
            $password = (string)($_POST['password'] ?? '');
            $user = (new User())->findByEmail($email);
            if ($user && password_matches($password, $user['password'])) {
                if (in_array((string)$user['role'], ['admin', 'super_admin'], true)) {
                    $_SESSION['admin_user'] = $user;
                    redirect_to('admin.php');
                }
                $_SESSION['user'] = $user;
                redirect_to('index.php');
            } else $error = 'Email hoặc mật khẩu không đúng.';
        }
        $this->view('user/login', ['page_title' => 'Đăng nhập', 'error' => $error]);
    }
    public function register()
    {
        $error = '';
        if ($_SERVER['REQUEST_METHOD'] === 'POST') {
            $name = trim($_POST['name'] ?? '');
            $age = intval($_POST['age'] ?? 0);
            $email = trim($_POST['email'] ?? '');
            $password = (string)($_POST['password'] ?? '');
            if ($name === '' || $email === '' || $password === '') $error = 'Vui lòng nhập đầy đủ thông tin.';
            elseif ($age < 6 || $age > 10) $error = 'Tuổi của bé phải từ 6 đến 10.';
            elseif (!filter_var($email, FILTER_VALIDATE_EMAIL)) $error = 'Email phụ huynh không đúng định dạng.';
            elseif (!preg_match('/^(?=.*[A-Za-z])(?=.*\d)(?=.*[^A-Za-z0-9]).{6,}$/', $password)) $error = 'Mật khẩu phải từ 6 ký tự, có chữ, số và ký tự đặc biệt.';
            else {
                try {
                    (new User())->create($name, $age, $email, $password);
                    redirect_to('login.php?registered=1');
                } catch (PDOException $e) {
                    $error = 'Email này đã tồn tại hoặc dữ liệu chưa hợp lệ.';
                }
            }
        }
        $this->view('user/register', ['page_title' => 'Đăng ký', 'error' => $error]);
    }
    public function logout()
    {
        session_destroy();
        redirect_to('login.php');
    }
}
