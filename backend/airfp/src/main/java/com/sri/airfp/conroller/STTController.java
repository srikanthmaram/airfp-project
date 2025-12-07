package com.sri.airfp.conroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sri.airfp.STT.VoiceToTextService;
import com.sri.airfp.model.RfpResponse;
import com.sri.airfp.model.STTResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/voice")
@CrossOrigin(origins ="http://localhost:5173")
@RequiredArgsConstructor
public class STTController {

    private final VoiceToTextService voiceToTextService;
    private final ObjectMapper mapper = new ObjectMapper();

    @PostMapping("/transcribe")
    public STTResponse transcribe(@RequestParam("file") MultipartFile file) throws Exception {

        Path temp = Files.createTempFile("audio", ".wav");
        file.transferTo(temp.toFile());

        String jsonString=voiceToTextService.convertVoiceToText(temp.toString());

        return mapper.readValue(jsonString, STTResponse.class);
    }
}
