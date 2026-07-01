<?php
class AdminController extends Controller {
    private function model(){ return new AdminModel(); }

    private function uploadFolder($table){
        if ($table === 'topics') return 'topics';
        if ($table === 'vocabularies') return 'words';
        if ($table === 'quiz_questions') return 'quiz';
        return 'others';
    }

    private function handleUpload($table, $currentValue = ''){
        if (empty($_FILES['image_file']) || empty($_FILES['image_file']['name'])) {
            return $currentValue;
        }

        if ($_FILES['image_file']['error'] !== UPLOAD_ERR_OK) {
            return $currentValue;
        }

        $allowed = [
            'image/png' => 'png',
            'image/jpeg' => 'jpg',
            'image/webp' => 'webp'
        ];

        $mime = mime_content_type($_FILES['image_file']['tmp_name']);
        if (!isset($allowed[$mime])) {
            return $currentValue;
        }

        $folder = $this->uploadFolder($table);

        $dir = __DIR__ . '/../../../server_api/uploads/' . $folder . '/';
        if (!is_dir($dir)) {
            mkdir($dir, 0777, true);
        }

        $base = pathinfo($_FILES['image_file']['name'], PATHINFO_FILENAME);
        $base = preg_replace('/[^a-zA-Z0-9_-]+/', '_', strtolower($base));
        $filename = time() . '_' . $base . '.' . $allowed[$mime];
        $target = $dir . $filename;

        if (move_uploaded_file($_FILES['image_file']['tmp_name'], $target)) {
            // LƯU DATABASE THEO DẠNG NGẮN GIỐNG DÒNG ĐẦU TIÊN CỦA BẠN
            return 'uploads/' . $folder . '/' . $filename;
        }

        return $currentValue;
    }

    public function index(){
        require_admin();
        $m=$this->model();
        $table=$_GET['table']??'topics';
        if(!$m->allowed($table)) $table='topics';
        if(isset($_GET['delete'])){
            $m->delete($table,$_GET['delete']);
            redirect_to('admin.php?table='.urlencode($table).'&deleted=1');
        }
        $search=trim($_GET['q']??'');
        $this->view('admin/index',[
            'TABLES'=>$m->tables,
            'table'=>$table,
            'columns'=>$m->columns($table),
            'pk'=>$m->pk($table),
            'rows'=>$m->rows($table,$search),
            'total'=>$m->countRows($table,$search),
            'stats'=>$m->stats(),
            'search'=>$search,
            'admin'=>current_admin()
        ]);
    }

    public function form(){
        require_admin();
        $m=$this->model();
        $table=$_GET['table']??$_POST['table']??'topics';
        if(!$m->allowed($table)) $table='topics';
        $pk=$m->pk($table);
        $columns=$m->columns($table);
        $id=$_GET['id']??$_POST['id']??null;
        $isEdit=!empty($id);
        $row=$isEdit?$m->find($table,$id):[];

        if($_SERVER['REQUEST_METHOD']==='POST'){
            $data=$_POST;
            if (array_key_exists('image_url', $data)) {
                $data['image_url'] = trim((string)$data['image_url']);
            }
            $data['image_url'] = $this->handleUpload($table, $data['image_url'] ?? ($row['image_url'] ?? ''));
            $m->save($table,$data,$isEdit?$id:null);
            redirect_to('admin.php?table='.urlencode($table).'&saved=1');
        }

        $topics=(new Topic())->all();
        $this->view('admin/form',compact('table','pk','columns','id','isEdit','row','topics'));
    }
}
