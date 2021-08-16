package com.soap.designpattern.Facade.controller;


import com.soap.designpattern.Facade.dto.UserUpdateRequest;
import com.soap.designpattern.Facade.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class TestController {

    TestService testService;


    @PostMapping("user/update")
    public void updateUser(
       @RequestBody UserUpdateRequest reqDTO
    ) {
        testService.updateUser(reqDTO);
    }

}
