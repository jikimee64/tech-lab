package com.soap.designpattern.Facade;

import com.soap.designpattern.Facade.domain.Bank;
import com.soap.designpattern.Facade.dto.UserUpdateRequest;
import com.soap.designpattern.Facade.sub.BankAccount;
import com.soap.designpattern.Facade.sub.BirthDay;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

//퍼사드 패턴 잘쓴 클래스 다이어그램
//https://miro.medium.com/max/3202/1*4edYFtpMcK0sbb3nGSlDAQ.png
@Component
@RequiredArgsConstructor
public class SchedulerFacade {

    private final BankAccount bankAccount;
    private final BirthDay birthDay;

    /**
     * 유저정보 변경시 흐름(가상)
     * 1. 기존 은행계좌를 삭제하고 새로 삽입한다.
     * 2. 기존 생일정보를 가져오고 없으면 삭제, 있으면 새로 삽입한다.
     * 등등..
     * 새로운 로직 추가시 관련 객체만들고 객체안에 단위별로 메소드 만들기
     */
    public void updateUser(UserUpdateRequest reqDTO){
        Long id = reqDTO.getId();
        String name = reqDTO.getName();

        //BackAccount 관련
        Long bankId = this.bankAccount.findBankAccount();
        birthDay.delete();
        bankAccount.insert(new Bank());

        //BirthDay 관련
        //생일 조회시 은행계좌아이다가 있어야된다고 가정
        birthDay.findBirthDay(bankId);
        birthDay.delete();
        birthDay.insert();
    }

}