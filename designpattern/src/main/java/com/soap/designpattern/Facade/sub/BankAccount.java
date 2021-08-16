package com.soap.designpattern.Facade.sub;

import com.soap.designpattern.Facade.domain.Bank;
import com.soap.designpattern.Facade.repository.TestRepository;
import org.springframework.stereotype.Component;

//스케줄
@Component
public class BankAccount {

    TestRepository testRepository;

    //기존 은행계좌 존재여부 체크
    public Long findBankAccount(){
        return 1L;
    }

    //기존 은행 정보 삭제
    public void delete(){

    }

    //새로운 은행 정보 삽입
    public void insert(Bank bank){
        testRepository.insertBackAccount();
    }


}
