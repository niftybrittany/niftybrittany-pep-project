package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO{

//Create or Insert New Account
public Account addAccount(Account account){
    Connection connection = ConnectionUtil.getConnection();
    try {
    
        String sql = "INSERT INTO Account (username, password) VALUES(?,?)";
        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        

        ps.setString(1, account.getUsername());
        ps.setString(2, account.getPassword());
        ps.executeUpdate();

        ResultSet pkrs = ps.getGeneratedKeys();
        if(pkrs.next()) {
            int generated_account_id = (int) pkrs.getLong(1);
           return new Account(generated_account_id, account.getUsername(), account.getPassword());
    
        }
    }
    
    catch(SQLException e){
        System.out.println(e.getMessage());
    }
    return null;
}
}