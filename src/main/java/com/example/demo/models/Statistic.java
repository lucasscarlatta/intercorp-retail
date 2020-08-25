package com.example.demo.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Statistic {

    @Schema(description = "A number expressing the central or typical value in a set of data", example = "24")
    private Double averageAge;
    @Schema(description = "A quantity expressing by how much the members of a group differ from the mean " +
            "value for the group.", example = "24")
    private Double standardDeviation;
}
