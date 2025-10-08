package com.reliaquest.api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeInput {
    /*
            name (String | not blank),
            salary (Integer | greater than zero),
            age (Integer | min = 16, max = 75),
            title (String | not blank)
     */

    private String name;
    private Integer salary;
    private Integer age;
    private String title;
}
