<?php require __DIR__.'/header.php'; ?>
<section class="page-head"><h1>🎮 Quiz Game</h1><p>Chọn đáp án đúng. Kết quả sẽ được lưu vào tài khoản.</p></section>
<section class="section">
<?php if($result): ?>
  <div class="quiz-result"><h2>Hoàn thành!</h2><p>Điểm của bé</p><div class="score-box"><strong><?=h($result['score'])?>/<?=h($result['total'])?></strong></div><div class="hero-actions"><a class="btn primary" href="quiz.php">Làm lại</a><a class="btn yellow" href="profile.php">Xem thành tích</a></div></div>
  <?php foreach($result['review'] as $i=>$item): $q=$item['question']; ?>
    <div class="quiz-card" style="margin-bottom:16px">
      <?php if(!empty($q['image_url'])): ?><img class="quiz-img" src="<?=h(normalize_image_url($q['image_url']))?>" onerror="this.src='assets/no-image.svg'" alt="quiz"><?php endif; ?>
      <div class="quiz-question"><?=h($i+1)?>. <?=h($q['question'])?></div>
      <?php foreach(['A'=>$q['option_a'],'B'=>$q['option_b'],'C'=>$q['option_c'],'D'=>$q['option_d']] as $key=>$text):
        $class = $key===$item['correct'] ? ' correct' : (($key===$item['chosen'] && !$item['is_correct']) ? ' wrong' : ''); ?>
        <div class="option-row<?=$class?>"><?=h($key)?>. <?=h($text)?><?= $key===$item['correct'] ? ' ✓' : '' ?></div>
      <?php endforeach; ?>
    </div>
  <?php endforeach; ?>
<?php elseif(!$questions): ?><div class="empty">Chưa có câu hỏi quiz. Admin hãy thêm câu hỏi trong trang quản trị.</div>
<?php else: ?>
<form method="post">
<?php foreach($questions as $i=>$q): ?>
  <div class="quiz-card" style="margin-bottom:18px">
    <?php if(!empty($q['image_url'])): ?><img class="quiz-img" src="<?=h(normalize_image_url($q['image_url']))?>" onerror="this.src='assets/no-image.svg'" alt="quiz"><?php endif; ?>
    <div class="quiz-question"><?=h($i+1)?>. <?=h($q['question'])?></div>
    <div class="options">
      <?php foreach(['A'=>$q['option_a'],'B'=>$q['option_b'],'C'=>$q['option_c'],'D'=>$q['option_d']] as $key=>$text): ?>
        <label class="option-row"><input type="radio" name="answers[<?=h($q['id'])?>]" value="<?=h($key)?>" required> <?=h($key)?>. <?=h($text)?></label>
      <?php endforeach; ?>
    </div>
  </div>
<?php endforeach; ?>
<div class="form-actions" style="justify-content:center"><button class="btn primary" type="submit">Nộp bài</button></div>
</form>
<?php endif; ?>
</section><?php require __DIR__.'/footer.php'; ?>
