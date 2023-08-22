package Service;


import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    AccountDAO accountDAO;
   

    //Create New AccountService with a new AccountDAO
    public AccountService(){
    accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }
    
    public boolean authenticates(Account account){
        
        int minPasswordLength = 4;
        
        return account!=null && account.getUsername()!= null 
        && !account.getUsername().isEmpty() && account.getPassword().length() >= minPasswordLength;
    }
        
    
    //Persist Account to DB
    public Account addAccount(Account account) {
        return accountDAO.addAccount(account);

    }

    //Login to Account in Database
    public Account loginAccount(Account account){
        return accountDAO.loginAccount(account);
    }

}

