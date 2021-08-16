package com.soap.designpattern.Facade.dto;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private Long id;
    private String name;
}