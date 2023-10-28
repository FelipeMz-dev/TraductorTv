package com.mz_dev.myapplication;

import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.google.mlkit.nl.translate.TranslateLanguage;

public class TranslatorManager {

    private final onResultReady onResultReady;
    private boolean errorTranslator = false;

    final Translator[] translator = {
            Translation.getClient(new TranslatorOptions.Builder() //0 = English to Spanish
                    .setSourceLanguage(TranslateLanguage.ENGLISH)
                    .setTargetLanguage(TranslateLanguage.SPANISH)
                    .build()),
            Translation.getClient(new TranslatorOptions.Builder() //1 = Spanish to English
                    .setSourceLanguage(TranslateLanguage.SPANISH)
                    .setTargetLanguage(TranslateLanguage.ENGLISH)
                    .build())
    };

    public TranslatorManager(TranslatorManager.onResultReady onResultReady) {
        this.onResultReady = onResultReady;
        downloadModel();
    }

    private void downloadModel() {
        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();
        translator[0].downloadModelIfNeeded(conditions)
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    errorTranslator = true;
                });
    }

    public void translate(int options, String textToTranslate) {
        if (errorTranslator) return;

        translator[options].translate(textToTranslate)
                .addOnSuccessListener(onResultReady::setText)
                .addOnFailureListener(Throwable::printStackTrace);
    }

    public void translateAndSpeak(int options, String textToTranslate) {
        if (errorTranslator) return;

        translator[options].translate(textToTranslate)
                .addOnSuccessListener(onResultReady::setTextAndSpeak)
                .addOnFailureListener(Throwable::printStackTrace);
    }

    public interface onResultReady {
        void setText(String text);
        void setTextAndSpeak(String text);
    }
}
