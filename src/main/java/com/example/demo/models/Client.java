package com.example.demo.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "CLIENTS")
@Getter
@Setter
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(hidden = true)
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    @Column(name = "NAME")
    @ApiModelProperty(required = true, value = "Client name", example = "Lucas")
    private String name;

    @NotBlank(message = "Last name cannot be blank")
    @Column(name = "LAST_NAME")
    @ApiModelProperty(required = true, example = "Scarlatta")
    private String lastName;

    @NotNull(message = "Birthday cannot be null")
    @Past(message = "Birthday should be in the past")
    @Column(name = "BIRTHDAY")
    @ApiModelProperty(required = true, example = "1992-10-16")
    private LocalDate birthday;

    @Transient
    @ApiModelProperty(hidden = true)
    private Integer age;

    public Integer getAge() {
        return Period.between(this.birthday, LocalDate.now()).getYears();
    }
}
