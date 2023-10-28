package com.mz_dev.myapplication;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    private final MutableLiveData<String> textToTranslate = new MutableLiveData<>();
    private final MutableLiveData<String> textResultTranslate = new MutableLiveData<>();
    private final MutableLiveData<String> buttonText = new MutableLiveData<>();

    public LiveData<String> getTextToTranslate() {
        return textToTranslate;
    }

    public LiveData<String> getTextResultTranslate() {
        return textResultTranslate;
    }

    public LiveData<String> getButtonText() {
        return buttonText;
    }

    private final TextToSpeechManager textToSpeechManager;
    private final String[] buttonTextOptions;
    private int options = 0;

    public MainViewModel(Context context) {
        textToSpeechManager = new TextToSpeechManager(context);
        buttonTextOptions  = new String[]{
                context.getString(R.string.english_to_spanish),
                context.getString(R.string.spanish_to_english)
        };
    }

    private final TranslatorManager translator = new TranslatorManager(
            new TranslatorManager.onResultReady() {
                @Override
                public void setText(String text) {
                    textResultTranslate.setValue(text);
                }
                @Override
                public void setTextAndSpeak(String text) {
                    textResultTranslate.setValue(text);
                    readTextAndSpeak(text);
                }
            });

    public void onTranslate(String text) {
        textToTranslate.setValue(text);
        translator.translate(options, text);
    }

    public void onOptionChanged() {
        options = options == 0 ? 1 : 0;
        buttonText.setValue(buttonTextOptions[options]);
        String currentTextToTranslate = textToTranslate.getValue();
        String currentTextResultTranslate = textResultTranslate.getValue();
        textToTranslate.setValue(currentTextResultTranslate);
        textResultTranslate.setValue(currentTextToTranslate);
    }

    public void onRead() {
        String textToRead = textResultTranslate.getValue();
        readText(textToRead);
    }

    public void onResultVoice(String text) {
        textToTranslate.setValue(text);
        translator.translateAndSpeak(options, text);
    }

    public void readText(String textToRead) {
        if (textToSpeechManager.isInitialized()) {
            textToSpeechManager.speak(options, textToRead);
        }
    }

    public void readTextAndSpeak(String textToRead) {
        if (textToSpeechManager.isInitialized()) {
            textToSpeechManager.speakFromVoice(options, textToRead);
        }
    }

    public void shutDown() {
        if (textToSpeechManager.isInitialized()) {
            textToSpeechManager.shutDown();
        }
    }

}
