package com.trang.kidsenglishai.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.trang.kidsenglishai.R;
import com.trang.kidsenglishai.viewmodel.PhotoTranslateViewModel;

import java.io.File;
import java.io.IOException;

public class PhotoTranslateActivity extends AppCompatActivity {

    private ImageView imgPreview;
    private TextView tvDetectedText;
    private TextView tvTranslatedText;

    private PhotoTranslateViewModel viewModel;

    private Uri photoUri;
    private String currentPhotoPath;

    private final ActivityResultLauncher<String> cameraPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    openCamera();
                } else {
                    Toast.makeText(this, "Bạn cần cấp quyền camera để chụp ảnh", Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && photoUri != null) {
                    processCapturedImage();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_translate);

        viewModel = new ViewModelProvider(this).get(PhotoTranslateViewModel.class);

        imgPreview = findViewById(R.id.imgPreview);
        tvDetectedText = findViewById(R.id.tvDetectedText);
        tvTranslatedText = findViewById(R.id.tvTranslatedText);

        viewModel.getTranslation().observe(this, result -> tvTranslatedText.setText(result));

        viewModel.getLoading().observe(this, isLoading -> {
            if (Boolean.TRUE.equals(isLoading)) {
                tvTranslatedText.setText("Đang dịch bằng AI...");
            }
        });

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        findViewById(R.id.btnCapture).setOnClickListener(v -> checkCameraAndOpen());

        findViewById(R.id.btnRetry).setOnClickListener(v -> {
            if (photoUri != null) {
                processCapturedImage();
            } else {
                checkCameraAndOpen();
            }
        });
    }

    private void checkCameraAndOpen() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void openCamera() {
        try {
            File photoFile = createImageFile();

            photoUri = FileProvider.getUriForFile(
                    this,
                    getPackageName() + ".fileprovider",
                    photoFile
            );

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            cameraLauncher.launch(intent);

        } catch (Exception e) {
            Toast.makeText(this, "Không mở được camera: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException {
        File storageDir = new File(getCacheDir(), "camera");

        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        File image = File.createTempFile(
                "photo_translate_",
                ".jpg",
                storageDir
        );

        currentPhotoPath = image.getAbsolutePath();

        return image;
    }

    private void processCapturedImage() {
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);

            if (bitmap == null) {
                tvDetectedText.setText("Không đọc được ảnh");
                tvTranslatedText.setText("Bé hãy chụp lại ảnh rõ hơn nhé.");
                return;
            }

            Bitmap resizedBitmap = resizeBitmap(bitmap, 1600);

            imgPreview.setImageBitmap(resizedBitmap);

            recognizeText(resizedBitmap);

        } catch (Exception e) {
            tvDetectedText.setText("Không xử lý được ảnh");
            tvTranslatedText.setText(e.getMessage());
        }
    }

    private Bitmap resizeBitmap(Bitmap bitmap, int maxSize) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if (width <= maxSize && height <= maxSize) {
            return bitmap;
        }

        float ratio = Math.min(
                (float) maxSize / width,
                (float) maxSize / height
        );

        int newWidth = Math.round(width * ratio);
        int newHeight = Math.round(height * ratio);

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
    }

    private void recognizeText(Bitmap bitmap) {
        tvDetectedText.setText("Đang nhận diện chữ...");
        tvTranslatedText.setText("Đang chuẩn bị dịch...");

        InputImage image = InputImage.fromBitmap(bitmap, 0);

        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                .process(image)
                .addOnSuccessListener(visionText -> {
                    String detected = cleanDetectedText(visionText.getText());

                    if (detected.isEmpty() || detected.length() < 2) {
                        tvDetectedText.setText("Không đọc được chữ trong ảnh");
                        tvTranslatedText.setText("Bé hãy chụp gần hơn, đủ sáng hơn và để chữ nằm thẳng trong ảnh.");
                        return;
                    }

                    tvDetectedText.setText(detected);
                    viewModel.translateDetectedText(detected);
                })
                .addOnFailureListener(e -> {
                    tvDetectedText.setText("Không đọc được chữ trong ảnh");
                    tvTranslatedText.setText("Lỗi nhận diện: " + e.getMessage());
                });
    }

    private String cleanDetectedText(String text) {
        if (text == null) return "";

        return text
                .replaceAll("[|]", "I")
                .replaceAll("[‘’]", "'")
                .replaceAll("[“”]", "\"")
                .replaceAll("\\s+", " ")
                .trim();
    }
}