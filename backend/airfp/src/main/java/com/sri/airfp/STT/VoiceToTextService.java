package com.sri.airfp.STT;
import org.springframework.stereotype.Service;
import org.vosk.LogLevel;
import org.vosk.Model;
import org.vosk.Recognizer;
import org.vosk.LibVosk;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class VoiceToTextService {

    private final Model model;

    public VoiceToTextService() {
        LibVosk.setLogLevel(LogLevel.DEBUG);

        try {
            this.model = new Model("src/main/resources/STTmodel/vosk-model-small-en-us-0.15");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String convertVoiceToText(String wavFilePath) {
        try (InputStream ais = new FileInputStream(wavFilePath);
             Recognizer recognizer = new Recognizer(model, 16000)) {

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = ais.read(buffer)) > 0) {
                recognizer.acceptWaveForm(buffer, bytesRead);
            }

            return recognizer.getResult();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error processing audio";
        }
    }
}

