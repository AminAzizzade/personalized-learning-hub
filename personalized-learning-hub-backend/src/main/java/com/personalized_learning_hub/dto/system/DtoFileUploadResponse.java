package com.personalized_learning_hub.dto.system;

import lombok.Data;

@Data
public class DtoFileUploadResponse {
    String fileName;
    String downloadUri;
    Long size;
}
