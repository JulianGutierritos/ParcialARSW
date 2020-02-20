package edu.eci.arsw.exams.moneylaunderingapi.service;

import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component("stub")
public class MoneyLaunderingServiceStub implements MoneyLaunderingService {

    List<SuspectAccount> cuentasSospechosas;

    public MoneyLaunderingServiceStub(){
        cuentasSospechosas = new ArrayList<SuspectAccount>();
    }


    @Override
    void updateAccountStatus(SuspectAccount suspectAccount) throws AccountNotFoundException {
        boolean esta = false;
        int res = 0;
        for (int i = 0; i < cuentasSospechosas.size(); i++){
            if (cuentasSospechosas.get(i).accountId.equals(suspectAccount.accountId)){
                esta = true;
                res = i;
                break;
            }
        }
        if (esta){
            cuentasSospechosas.set(res, suspectAccount);
        }
        else{
            throw new AccountNotFoundException("The given account don't exist");
        }
    }

    @Override
    void addAccount(SuspectAccount suspectAccount) throws AccountNotFoundException{
        boolean esta = false;
        for (int i = 0; i < cuentasSospechosas.size(); i++){
            if (cuentasSospechosas.get(i).accountId.equals(suspectAccount.accountId)){
                esta = true;
                break;
            }
        }
        if (!esta){
            cuentasSospechosas.add(suspectAccount);
        }
        else{
            throw new AccountNotFoundException("The given account alredy exist");
        }
    }

    @Override
    public SuspectAccount getAccountStatus(String accountId) throws AccountNotFoundException{
        int res = 0;
        boolean esta = false;
        for (int i = 0; i < cuentasSospechosas.size(); i++){
            if (cuentasSospechosas.get(i).accountId.equals(accountId)){
                esta = true;
                i = res;
                break;
            }
        }
        if (esta){
            return cuentasSospechosas.get(res);
        }
        else{
            throw new AccountNotFoundException("The given account don't exist");
        }
        
    }

    @Override
    public List<SuspectAccount> getSuspectAccounts() throws AccountNotFoundException{
        if (cuentasSospechosas.size() > 0){
            return cuentasSospechosas;
        }
        else{
            throw new AccountNotFoundException("There's no accounts available");
        }
        
    }
}
