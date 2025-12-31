package org.spring.prophet.controller;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.spring.prophet.service.TextFileService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RequestMapping("api/chat")
@RestController
public class ChatController {
    private final TextFileService textFileService;

    public ChatController(TextFileService textFileService) {
        this.textFileService = textFileService;
    }

    /**
     * 此后示例用于ai部门给的新接口功能准确性及性能测试，后期可以不造轮子直接视野
     */
    @PostMapping(value = "v1/uploadTxt", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> uploadTxt(@RequestParam("appId") String appId,
                                                         @RequestPart("file") MultipartFile file) {
        Map<String, String> response = Maps.newHashMap();
        // 2. Check file
        if (file == null || file.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("error", "File is empty"));
        }

        String originalFilename = file.getOriginalFilename();
        try {
            // 3. Read content as String (UTF-8)
            String content = new String(file.getBytes(), StandardCharsets.UTF_8);
            textFileService.textToImg(appId, content);
            // 4. Generate uid and store in MongoDB, similar to dialogueInit
            log.info("uploadTxt - appId: {}, time: {}, length: {}", appId, LocalDateTime.now(), content.length());

            response.put("appId", appId);
            response.put("filename", originalFilename);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } catch (IOException e) {
            log.error("Failed to read uploaded txt file", e);
            return ResponseEntity
                    .internalServerError()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("error", "Failed to read file"));
        }
    }

}
