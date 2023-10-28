package com.mz_dev.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextWord;
    private TextView textViewTranslation;
    private Button btnOption;

    private MainViewModel mainViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainViewModel = new MainViewModel(this);
        mainViewModel.getTextToTranslate().observe(this, text-> {
            editTextWord.setText(text);
        });
        mainViewModel.getTextResultTranslate().observe(this, text-> {
            textViewTranslation.setText(text);
        });
        mainViewModel.getButtonText().observe(this, text-> {
            btnOption.setText(text);
        });

        initViews();

    }

    private void initViews() {
        editTextWord = findViewById(R.id.editTextWord);
        textViewTranslation = findViewById(R.id.textViewTranslation);
        btnOption = findViewById(R.id.btnOption);
    }

    public void onClickTranslate(View view) {
        String textToTranslate = editTextWord.getText().toString();
        mainViewModel.onTranslate(textToTranslate);
    }

    public void onClickOption(View view) {
        mainViewModel.onOptionChanged();
    }

    public void onClickRead(View view) {
        mainViewModel.onRead();
    }

    public void onClickSpeak(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.translate_your_voice));
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
            mainViewModel.onResultVoice(text);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        mainViewModel.shutDown();
        super.onDestroy();
    }
}