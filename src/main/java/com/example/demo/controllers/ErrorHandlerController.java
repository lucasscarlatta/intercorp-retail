package com.example.demo.controllers;

import com.example.demo.exception.NotFoundException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@ApiIgnore
public class ErrorHandlerController implements ErrorController {

    @RequestMapping(method = GET, value = "/error")
    public String customError() {
        throw new NotFoundException("Path not found");
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}