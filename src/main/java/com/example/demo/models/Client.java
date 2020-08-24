package com.example.demo.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

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
    @Schema(hidden = true)
    private Long id;

    @NotBlank
    @Length(max = 255, message = "must be less than or equal to 255 characters")
    @Column(name = "NAME")
    @Schema(required = true, example = "Lucas")
    private String name;

    @NotBlank
    @Length(max = 255, message = "must be less than or equal to 255 characters")
    @Column(name = "LAST_NAME")
    @Schema(required = true, example = "Scarlatta")
    private String lastName;

    @NotNull
    @Past
    @Column(name = "BIRTHDAY")
    @Schema(required = true, example = "1992-10-16")
    private LocalDate birthday;

    @Transient
    @Schema(hidden = true)
    private Integer age;

    public Integer getAge() {
        return Period.between(this.birthday, LocalDate.now()).getYears();
    }
}
