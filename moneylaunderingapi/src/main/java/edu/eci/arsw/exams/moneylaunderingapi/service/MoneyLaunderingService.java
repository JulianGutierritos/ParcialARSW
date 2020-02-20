package edu.eci.arsw.exams.moneylaunderingapi.service;

import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface MoneyLaunderingService {
    void updateAccountStatus(SuspectAccount suspectAccount) throws AccountNotFoundException;
    void addAccount(SuspectAccount suspectAccount) throws AccountNotFoundException;
    SuspectAccount getAccountStatus(String accountId) throws AccountNotFoundException;
    List<SuspectAccount> getSuspectAccounts() throws AccountNotFoundException;
}
