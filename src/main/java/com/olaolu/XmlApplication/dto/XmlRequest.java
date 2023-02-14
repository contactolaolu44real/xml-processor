package com.olaolu.XmlApplication.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

@Data
public class XmlRequest {
    @NotNull(message = "File can bot be null")
    private MultipartFile file;
}
