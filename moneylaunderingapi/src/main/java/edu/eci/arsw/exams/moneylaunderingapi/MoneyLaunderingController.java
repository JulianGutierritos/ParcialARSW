package edu.eci.arsw.exams.moneylaunderingapi;


import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;
import edu.eci.arsw.exams.moneylaunderingapi.service.MoneyLaunderingService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import edu.eci.arsw.exams.moneylaunderingapi.service.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
public class MoneyLaunderingController
{
    @Autowired
    @Qualifier("stub")
    MoneyLaunderingService moneyLaunderingService;

    @RequestMapping( value = "/fraud-bank-accounts" , method = RequestMethod.GET)
    public ResponseEntity<String> offendingAccountsGetAll() {
        ResponseEntity r = null;
		try{
			Set<SuspectAccount> sc = moneyLaunderingService.getSuspectAccounts();
			Gson gson = new Gson();
			String json = gson.toJson(sc);
			r = new ResponseEntity<>(json, HttpStatus.OK);
		} catch (AccountNotFoundException e){
			r = new ResponseEntity<>("No accounts available", HttpStatus.NOT_FOUND);
		}
		return r;
    }

    @RequestMapping( value = "/fraud-bank-accounts" , method = RequestMethod.POST)
    public ResponseEntity<String> offendingAccountAdd(@RequestBody SuspectAccount sc) {
        ResponseEntity r = null;
		try{
			moneyLaunderingService.addAccount(sc);
			r = new ResponseEntity<>("New account registreted", HttpStatus.CREATED);
		} catch (AccountNotFoundException e){
			r = new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
		}
		return r;
    }

    @RequestMapping( value = "/fraud-bank-accounts/{accountId}", method = RequestMethod.GET)
    public ResponseEntity<String> offendingAccountGet(@PathVariable String accountId) {
        ResponseEntity r = null;
		try{
			SuspectAccount sc = moneyLaunderingService.getAccountStatus(accountId);
			Gson gson = new Gson();
			String json = gson.toJson(sc);
			r = new ResponseEntity<>(json, HttpStatus.OK);
		} catch (AccountNotFoundException e){
			r = new ResponseEntity<>("No account", HttpStatus.NOT_FOUND);
		}
		return r;
    }

    @RequestMapping( value = "/fraud-bank-accounts/{accountId}", method = RequestMethod.PUT)
    public ResponseEntity<String> offendingAccountPut(@RequestBody SuspectAccount sc) {
        ResponseEntity r = null;
		try{
			moneyLaunderingService.updateAccountStatus(sc);
			r = new ResponseEntity<>("Updated", HttpStatus.OK);
		} catch (AccountNotFoundException e){
			r = new ResponseEntity<>("No account", HttpStatus.BAD_REQUEST);
		}
		return r;
    }


}
