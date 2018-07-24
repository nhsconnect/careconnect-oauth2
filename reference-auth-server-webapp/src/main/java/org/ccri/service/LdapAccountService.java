package org.ccri.service;

import org.ccri.model.LdapAccount;
import org.ccri.repository.LdapAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LdapAccountService {

    @Autowired
    private LdapAccountRepository accountRepository;

    public void createAccount(LdapAccount account){
        accountRepository.createAccount(account);
    }
}
