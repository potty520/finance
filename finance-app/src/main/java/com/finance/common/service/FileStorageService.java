package com.finance.common.service;

import com.finance.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final Set<String> ALLOWED_EXT = new HashSet<>(Arrays.asList(
            "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx",
            "wps", "et", "dps", "txt", "rtf", "odt", "ods", "odp"
    ));

    @Value("${file.upload-path:./data/files/}")
    private String uploadPath;

    public Map<String, Object> store(MultipartFile file, String module) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("上传文件不能为空");
        }
        String originalName = StringUtils.cleanPath(file.getOriginalFilename() == null ? "file" : file.getOriginalFilename());
        if (originalName.contains("..")) {
            throw new BusinessException("文件名不合法");
        }
        String ext = getExtension(originalName);
        if (!ALLOWED_EXT.contains(ext)) {
            throw new BusinessException("不支持的文件类型: ." + ext + "，请上传 PDF、Word、Excel 等文档");
        }

        String safeModule = StringUtils.hasText(module) ? module.replaceAll("[^a-zA-Z0-9_-]", "") : "common";
        String subDir = safeModule + "/" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        Path dir = Paths.get(uploadPath, subDir).toAbsolutePath().normalize();
        Files.createDirectories(dir);

        String storedName = UUID.randomUUID().toString().replace("-", "") + "_" + originalName;
        Path target = dir.resolve(storedName).normalize();
        file.transferTo(target.toFile());

        String relativePath = (subDir + "/" + storedName).replace("\\", "/");
        Map<String, Object> result = new HashMap<>();
        result.put("fileName", originalName);
        result.put("path", relativePath);
        result.put("size", file.getSize());
        result.put("ext", ext);
        return result;
    }

    public Path resolve(String relativePath) {
        if (!StringUtils.hasText(relativePath) || relativePath.contains("..")) {
            throw new BusinessException("文件路径不合法");
        }
        Path base = Paths.get(uploadPath).toAbsolutePath().normalize();
        Path file = base.resolve(relativePath).normalize();
        if (!file.startsWith(base)) {
            throw new BusinessException("文件路径不合法");
        }
        if (!Files.exists(file) || !Files.isRegularFile(file)) {
            throw new BusinessException("文件不存在");
        }
        return file;
    }

    public String getExtension(String filename) {
        if (!StringUtils.hasText(filename) || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
    }

    public String contentType(String ext) {
        switch (ext) {
            case "pdf": return "application/pdf";
            case "doc": return "application/msword";
            case "docx": return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "xls": return "application/vnd.ms-excel";
            case "xlsx": return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "ppt": return "application/vnd.ms-powerpoint";
            case "pptx": return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
            case "txt": return "text/plain";
            case "rtf": return "application/rtf";
            default: return "application/octet-stream";
        }
    }
}
