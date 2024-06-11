package com.kejden.limitlesspouch.controller;

import com.kejden.limitlesspouch.model.File;
import com.kejden.limitlesspouch.service.FileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/file")
@CrossOrigin("*")
public class FileController {

    public final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        long maxSize = 10 * 1024 * 1024;
        if(file.getSize() > maxSize) {
            return ResponseEntity.badRequest().body("File is too large");
        }
        return ResponseEntity.ok().body(fileService.uploadFile(file));
    }

    @GetMapping()
    public ResponseEntity<List<String>> getAllFile() throws IOException {
        List<String> allIDs = fileService.listAllFiles();
        return ResponseEntity.ok().body(allIDs);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String id) throws IOException {
        File file = fileService.downloadFile(id);

        if (file.getFile() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getFiletype()))
                .contentLength(Long.parseLong(file.getFilesize()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+file.getFilename()+"\"")
                .body(file.getFile());
    }
}
