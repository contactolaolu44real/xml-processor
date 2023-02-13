package com.olaolu.XmlApplication.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class XmlRequest {
    @NotNull(message = "File can bot be null")
    private MultipartFile file;
}
