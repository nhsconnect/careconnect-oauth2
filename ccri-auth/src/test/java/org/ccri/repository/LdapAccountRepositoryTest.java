package org.ccri.repository;


import org.ccri.model.LdapAccount;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.LdapTemplate;

import javax.naming.ldap.LdapContext;

import static junit.framework.TestCase.fail;
import static org.mockito.Mockito.mock;

public class LdapAccountRepositoryTest {

    private LdapAccountRepository accountRepository;
    private ContextSource contextSourceMock;
    private LdapContext dirContextMock;

    @Before
    public void setup() {

        // Setup ContextSource mock
        contextSourceMock = mock(ContextSource.class);

        // Setup LdapContext mock
        dirContextMock = mock(LdapContext.class);

        LdapTemplate ldapTemplate = new LdapTemplate(contextSourceMock);

        accountRepository = new LdapAccountRepository(ldapTemplate);
    }

//    @Test
    public void testCreateAccount() {
        LdapAccount account = new LdapAccount();
        account.setUserId("test09");
        account.setPassword("test1234");
        account.setFirstName("Test");
        account.setSurname("Unit");
        account.setDisplayName("Unit Test User");
        account.setEmailAddress("unittest01@test.com");
        accountRepository.createAccount(account);

        fail("Insert should have failed");
    }


}
