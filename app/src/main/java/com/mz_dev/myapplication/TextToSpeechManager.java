package com.mz_dev.myapplication;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class TextToSpeechManager implements TextToSpeech.OnInitListener {

    private final TextToSpeech[] textToSpeech;
    private boolean isInitialized = false;

    public TextToSpeechManager(Context context) {
        textToSpeech = new TextToSpeech[] {
                new TextToSpeech(context, this),
                new TextToSpeech(context, this)
        };
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int langResultSp = textToSpeech[0].setLanguage(new Locale("es", "US"));
            int langResultEn = textToSpeech[1].setLanguage(new Locale("en", "US"));
            if (langResultSp == TextToSpeech.LANG_MISSING_DATA ||
                    langResultSp == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Handle language not supported error
            } else if (langResultEn == TextToSpeech.LANG_MISSING_DATA ||
                    langResultEn == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Handle language not supported error 
            } else {
                isInitialized = true;
            }
        }
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    
    public void speak(int option, String text) {
        if (isInitialized && !text.isEmpty()) {
            if (textToSpeech[0].isSpeaking() || textToSpeech[1].isSpeaking()) {
                stopAll();
            }else {
            textToSpeech[option].speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        }
    }

    public void speakFromVoice(int option, String text) {
        if (isInitialized && !text.isEmpty()) {
            if (textToSpeech[0].isSpeaking() || textToSpeech[1].isSpeaking()) {
                stopAll();
            }
            textToSpeech[option].speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    private void stopAll() {
        textToSpeech[0].stop();
        textToSpeech[1].stop();
    }

    public void shutDown() {
        if (textToSpeech[0] != null) {
            textToSpeech[0].stop();
            textToSpeech[0].shutdown();
        }
        if (textToSpeech[1] != null) {
            textToSpeech[1].stop();
            textToSpeech[1].shutdown();
        }
    }
}