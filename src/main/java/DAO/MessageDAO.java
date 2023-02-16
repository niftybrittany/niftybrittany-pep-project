package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    // Create or Insert New Message
    public Message createMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try {

            String sql = "INSERT INTO Message (posted_by,message_text,time_posted_epoch) VALUES(?,?,?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());

            ps.executeUpdate();

            ResultSet pkeyResultSet = ps.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_message_id = (int) pkeyResultSet.getLong(1);
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    // Get message by message ID
    public Message getMessageByID(int message_id) {
        Connection connection = ConnectionUtil.getConnection();

        try {

            String sql = "SELECT * FROM Message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, message_id);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                return message;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    // Get a list of All messages
    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try{
            String sql = "SELECT * FROM Message";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }            
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }
    

    // Delete Message by ID
    public Message deleteMessageByID(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        try {

            String sql = "DELETE * WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, message_id);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Message deletedMessage = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));

                return deletedMessage;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    // Update Message By ID
    public Message updateMessageByID(int message_id, Message message){
        Connection connection = ConnectionUtil.getConnection();
        try {
        
            String sql = "UPDATE Message SET message_text=? WHERE message_id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
        
                
                ps.setString(1,message.message_text);
                ps.setInt(2,message_id);
                ps.executeUpdate();
            
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    // Get All Messages By Account ID
    public Message getAllMessageByAccountID(int account_id) {
        Connection connection = ConnectionUtil.getConnection();
        try {

            String sql = "SELECT (Accounts.account_id, Accounts.username, Messages.message_id, Messages.message_text, Message.time_posted_epoch) FROM Accounts INNER JOIN Messages ON Accounts.account_id = Messages.posted_by";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                return message;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
