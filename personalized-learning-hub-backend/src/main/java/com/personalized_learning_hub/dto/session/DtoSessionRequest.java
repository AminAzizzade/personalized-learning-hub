package com.personalized_learning_hub.dto.session;

import com.personalized_learning_hub.enums.SessionStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DtoSessionRequest {
    private SessionStatus status;
}

