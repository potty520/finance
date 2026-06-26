package com.finance.module.system.controller;

import com.finance.common.response.Result;
import com.finance.common.service.FileStorageService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    private FileStorageService fileStorageService;

    @PostMapping("/upload")
    public Result<Map<String, Object>> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(defaultValue = "contract") String module) throws IOException {
        return Result.success(fileStorageService.store(file, module));
    }

    @GetMapping("/download")
    public void download(@RequestParam String path, HttpServletResponse response) throws IOException {
        Path file = fileStorageService.resolve(path);
        String fileName = file.getFileName().toString();
        int idx = fileName.indexOf('_');
        if (idx > 0 && idx < fileName.length() - 1) {
            fileName = fileName.substring(idx + 1);
        }
        String ext = fileStorageService.getExtension(fileName);
        response.setContentType(fileStorageService.contentType(ext));
        response.setHeader("Content-Disposition",
                "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8.name()));
        response.setContentLengthLong(Files.size(file));
        try (InputStream in = Files.newInputStream(file); OutputStream out = response.getOutputStream()) {
            byte[] buf = new byte[8192];
            int len;
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            out.flush();
        }
    }

    @GetMapping("/preview")
    public void preview(@RequestParam String path, HttpServletResponse response) throws IOException {
        Path file = fileStorageService.resolve(path);
        String fileName = file.getFileName().toString();
        String ext = fileStorageService.getExtension(fileName);
        if (!"pdf".equals(ext)) {
            download(path, response);
            return;
        }
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline;filename=" +
                URLEncoder.encode(fileName, StandardCharsets.UTF_8.name()));
        response.setContentLengthLong(Files.size(file));
        try (InputStream in = Files.newInputStream(file); OutputStream out = response.getOutputStream()) {
            byte[] buf = new byte[8192];
            int len;
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            out.flush();
        }
    }
}
