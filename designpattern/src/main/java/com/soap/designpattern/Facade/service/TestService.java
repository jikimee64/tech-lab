package com.soap.designpattern.Facade.service;

import com.soap.designpattern.Facade.SchedulerFacade;
import com.soap.designpattern.Facade.dto.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestService {

    private final SchedulerFacade schedulerFacade;

    public void updateUser(UserUpdateRequest reqDTO){
        schedulerFacade.updateUser(reqDTO);
    }

}