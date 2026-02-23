package com.personalized_learning_hub.dto.session;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DtoProgressWrapper {
    private Long sessionId;
    private Date startDate;
    private List<DtoProgressResponse> progressList;


    // bu constructor eksikse, eklemelisin:
    public DtoProgressWrapper(Long sessionId, Date startDate) {
        this.sessionId = sessionId;
        this.startDate = startDate;
        this.progressList = new ArrayList<>();
    }
}

