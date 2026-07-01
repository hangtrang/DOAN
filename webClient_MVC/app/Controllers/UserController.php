<?php
class UserController extends Controller
{
    public function home()
    {
        $topics = (new Topic())->limit(4);
        $this->view('user/home', ['page_title' => 'Trang chủ', 'topics' => $topics, 'user' => current_user()]);
    }
    public function topics()
    {
        $topics = (new Topic())->all();
        $this->view('user/topics', ['page_title' => 'Chủ đề học', 'topics' => $topics]);
    }
    public function lesson()
    {
        $topicId = intval($_GET['topic_id'] ?? 0);
        $topicModel = new Topic();
        $topic = $topicModel->find($topicId);
        $words = (new Vocabulary())->byTopic($topicId);
        $index = max(0, intval($_GET['i'] ?? 0));
        $total = count($words);
        if ($index >= $total) $index = max(0, $total - 1);
        $word = $words[$index] ?? null;
        $this->view('user/lesson', compact('topicId', 'topic', 'words', 'index', 'total', 'word') + ['page_title' => 'Bài học từ vựng']);
    }
    public function practice()
    {
        require_user();
        $wordParam = trim($_GET['word'] ?? 'Apple');
        if ($_SERVER['REQUEST_METHOD'] === 'POST') {
            $word = trim($_POST['word'] ?? '');
            $spoken = trim($_POST['spoken_text'] ?? '');
            $score = 0;
            if ($word !== '' && $spoken !== '') {
                similar_text(strtolower($word), strtolower($spoken), $percent);
                $score = min(100, (int)round($percent));
            }
            $label = $score >= 90 ? 'Xuất sắc' : ($score >= 70 ? 'Tốt' : ($score >= 50 ? 'Cần luyện thêm' : 'Thử lại nhé'));
            (new Practice())->save(intval(current_user()['id']), $word, $spoken, $score, $label);
            redirect_to('practice.php?word=' . urlencode($word) . '&score=' . $score . '&label=' . urlencode($label));
        }
        $this->view('user/practice', ['page_title' => 'Luyện nói', 'wordParam' => $wordParam]);
    }
    public function quiz()
    {
        require_user();
        $quizModel = new Quiz();
        $result = null;
        $questions = $_SESSION['quiz_questions'] ?? [];

        if ($_SERVER['REQUEST_METHOD'] === 'POST') {
            $answers = $_POST['answers'] ?? [];
            $score = 0;
            $review = [];
            foreach ($questions as $q) {
                $qid = (string)$q['id'];
                $chosen = strtoupper(trim((string)($answers[$qid] ?? '')));
                $correct = strtoupper(trim((string)$q['correct_answer']));
                if ($chosen === $correct) $score++;
                $review[] = ['question' => $q, 'chosen' => $chosen, 'correct' => $correct, 'is_correct' => $chosen === $correct];
            }
            $total = count($questions);
            if ($total > 0) {
                $quizModel->saveResult(intval(current_user()['id']), $score, $total);
            }
            $result = ['score' => $score, 'total' => $total, 'review' => $review];
            unset($_SESSION['quiz_questions']);
        }

        if ($result === null) {
            $questions = $quizModel->randomQuestions(10);
            $_SESSION['quiz_questions'] = $questions;
        }

        $this->view('user/quiz', ['page_title' => 'Quiz Game', 'questions' => $questions, 'result' => $result]);
    }
    public function saveQuiz()
    {
        header('Content-Type: application/json; charset=utf-8');
        if (!current_user()) {
            echo json_encode(['success' => false]);
            return;
        }
        $d = json_decode(file_get_contents('php://input'), true) ?: $_POST;
        (new Quiz())->saveResult(intval(current_user()['id']), intval($d['score'] ?? 0), intval($d['total'] ?? 0));
        echo json_encode(['success' => true]);
    }
    public function profile()
    {
        require_user();
        $uid = intval(current_user()['id']);
        $ach = (new Achievement())->get($uid);
        $quiz = (new Quiz())->history($uid);
        $practice = (new Practice())->history($uid);
        $this->view('user/profile', compact('ach', 'quiz', 'practice') + ['page_title' => 'Tài khoản']);
    }
}
