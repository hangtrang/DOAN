package com.trang.kidsenglishai.ui;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.trang.kidsenglishai.api.GeminiService;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.trang.kidsenglishai.R;
import com.trang.kidsenglishai.adapter.ChatMessageAdapter;
import com.trang.kidsenglishai.model.ChatMessage;
import com.trang.kidsenglishai.viewmodel.ChatViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChatbotActivity extends AppCompatActivity {

    private static final int REQ_AUDIO = 2201;

    private final List<ChatMessage> messages = new ArrayList<>();

    private ChatMessageAdapter adapter;
    private RecyclerView recyclerView;
    private EditText edtMessage;
    private ChatViewModel chatViewModel;

    private TextToSpeech tts;
    private boolean ttsReady = false;

    private String lastBotMessage = "";

    private final ActivityResultLauncher<Intent> speechLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                            ArrayList<String> results =
                                    result.getData().getStringArrayListExtra(
                                            RecognizerIntent.EXTRA_RESULTS
                                    );

                            if (results != null && !results.isEmpty()) {
                                String bestText = results.get(0).trim();

                                edtMessage.setText(bestText);
                                edtMessage.setSelection(edtMessage.getText().length());
                            }
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        recyclerView = findViewById(R.id.recyclerChat);
        edtMessage = findViewById(R.id.edtMessage);

        ImageView btnSend = findViewById(R.id.btnSend);
        ImageView btnBack = findViewById(R.id.btnBack);
        ImageView btnMic = findViewById(R.id.btnMicChat);
        ImageView btnVoice = findViewById(R.id.btnVoiceToggle);

        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        observeViewModel();

        setupTextToSpeech();

        adapter = new ChatMessageAdapter(messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        String welcome = "Hello little learner! 🤖\n"
                + "I am your KidsEnglishAI chatbot.\n"
                + "Bé có thể hỏi: 'Apple nghĩa là gì?', "
                + "'Make a sentence with dog', hoặc "
                + "'Give me 5 animal words'.";

        addBotMessage(welcome);

        btnBack.setOnClickListener(v -> finish());

        btnSend.setOnClickListener(v -> sendMessage());

        btnMic.setOnClickListener(v -> startVoiceInput());

        btnVoice.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(lastBotMessage)) {
                speak(lastBotMessage);
            } else {
                speak("Hi friend! Ask me anything about English.");
            }
        });

        edtMessage.setOnEditorActionListener((v, actionId, event) -> {
            boolean isEnter =
                    event != null
                            && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                            && event.getAction() == KeyEvent.ACTION_DOWN;

            if (actionId == EditorInfo.IME_ACTION_SEND || isEnter) {
                sendMessage();
                return true;
            }

            return false;
        });
    }

    private void setupTextToSpeech() {
        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {

                int result = tts.setLanguage(Locale.US);

                tts.setPitch(1.0f);
                tts.setSpeechRate(0.9f);

                ttsReady =
                        result != TextToSpeech.LANG_MISSING_DATA
                                && result != TextToSpeech.LANG_NOT_SUPPORTED;
            }
        });
    }

    private void observeViewModel() {
        chatViewModel.getBotReply().observe(this, response -> {
            if (response != null && !response.trim().isEmpty()) {
                addBotMessage(response);
            }
        });

        chatViewModel.getError().observe(this, err -> {
            addBotMessage("AI teacher is busy now. Please try again in a moment ✨");
        });

        chatViewModel.getLoading().observe(this, isLoading -> {
        });
    }

    private void sendMessage() {
        String userMessage = edtMessage.getText().toString().trim();

        if (TextUtils.isEmpty(userMessage)) {
            Toast.makeText(this, "Bé hãy nhập câu hỏi nhé", Toast.LENGTH_SHORT).show();
            return;
        }

        messages.add(new ChatMessage(userMessage, true));
        adapter.notifyItemInserted(messages.size() - 1);
        scrollToBottom();

        edtMessage.setText("");

        addBotMessage("AI teacher is thinking... ✨");

        GeminiService.askGemini(userMessage, new GeminiService.GeminiCallback() {
            @Override
            public void onSuccess(String reply) {
                runOnUiThread(() -> {
                    removeThinkingMessage();
                    addBotMessage(reply);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    removeThinkingMessage();
                    addBotMessage("AI teacher is busy now. Please try again ✨\n" + error);
                });
            }
        });
    }

    private void removeThinkingMessage() {
        if (!messages.isEmpty()) {
            int lastIndex = messages.size() - 1;

            if (!messages.get(lastIndex).isFromUser()
                    && messages.get(lastIndex).getText().contains("thinking")) {

                messages.remove(lastIndex);
                adapter.notifyItemRemoved(lastIndex);
            }
        }
    }

    private void startVoiceInput() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
        ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQ_AUDIO
            );

            return;
        }

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        );

        intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                "en-US"
        );

        intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                "en-US"
        );

        intent.putExtra(
                RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE,
                "en-US"
        );

        intent.putExtra(
                RecognizerIntent.EXTRA_MAX_RESULTS,
                5
        );

        intent.putExtra(
                RecognizerIntent.EXTRA_PROMPT,
                "Speak your English question"
        );

        try {
            speechLauncher.launch(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(
                    this,
                    "Thiết bị chưa hỗ trợ nhập bằng giọng nói",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void addBotMessage(String text) {
        messages.add(new ChatMessage(text, false));
        adapter.notifyItemInserted(messages.size() - 1);
        scrollToBottom();

        if (text != null
                && !text.trim().isEmpty()
                && !text.contains("thinking")) {
            lastBotMessage = text;
        }
    }

    private void speak(String text) {
        if (!ttsReady) return;
        if (tts == null) return;
        if (text == null || text.trim().isEmpty()) return;

        String cleanText = text
                .replaceAll("[\\n•]", " ")
                .replaceAll("\\s+", " ")
                .trim();

        tts.stop();

        tts.speak(
                cleanText,
                TextToSpeech.QUEUE_FLUSH,
                null,
                "AI_TTS"
        );
    }

    private void scrollToBottom() {
        recyclerView.smoothScrollToPosition(Math.max(messages.size() - 1, 0));
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQ_AUDIO
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            startVoiceInput();
        }
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }

        super.onDestroy();
    }
}