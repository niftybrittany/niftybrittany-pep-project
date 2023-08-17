package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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

    public Account loginAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try {
    
            String sql = "SELECT * FROM Account WHERE username = ? AND password = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
        
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            ps.executeQuery();

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Account loginAccount = new Account(rs.getInt("account_id"), rs.getString("username"),
                        rs.getString("password"));
                return loginAccount;
            }
    
        }
    
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Account> getAllAccounts(){
    Connection connection = ConnectionUtil.getConnection();
        List<Account> accounts = new ArrayList<>();
        try {
            
            String sql = "SELECT * FROM Account";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account account = new Account(
                    rs.getInt("account_id"), 
                    rs.getString("username"), 
                    rs.getString("password"));
                    accounts.add(account);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return accounts;
    
    }}