package com.personalized_learning_hub.dto.session;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DtoProgressResponse {
    String name;
    String type;
    int grade;
    Date date;
}
