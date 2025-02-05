package com.example.Modeme.Google;

//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//@RestController
//@RequestMapping("/api/drive")
//public class GcsController {
//
//    private final GoogleDriveService googleDriveService;
//
//    public GcsController(GoogleDriveService googleDriveService) {
//        this.googleDriveService = googleDriveService;
//    }
//
//    @PostMapping("/upload")
//    public String uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
//        File convFile = new File(file.getOriginalFilename());
//        FileOutputStream fos = new FileOutputStream(convFile);
//        fos.write(file.getBytes());
//        fos.close();
//
//        String fileUrl = googleDriveService.uploadFile(convFile, "1su12kmjNyjYD8hj0LnDBqh8-xuZn5r-A");
//
//        convFile.delete(); // 임시 파일 삭제
//        return fileUrl;
//    }
//}

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/gcs")
public class GcsController {

    private final GcsService gcsService;

    public GcsController(GcsService gcsService) {
        this.gcsService = gcsService;
    }

    // ✅ 파일 업로드 API
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = gcsService.uploadFile(file);
            return ResponseEntity.ok(fileUrl);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("파일 업로드 실패: " + e.getMessage());
        }
    }
}

