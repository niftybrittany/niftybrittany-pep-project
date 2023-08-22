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
    // Create (Insert) New Message
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
        String selectSql = "SELECT * FROM Message WHERE message_id = ?";
        PreparedStatement selectStatement = connection.prepareStatement(selectSql);
        selectStatement.setInt(1, message_id);

        ResultSet rs = selectStatement.executeQuery();
        if (rs.next()) {
            Message deletedMessage = new Message(
                rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch")
            );

            String deleteSql = "DELETE FROM Message WHERE message_id = ?";
            PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);
            deleteStatement.setInt(1, message_id);

            int rowsAffected = deleteStatement.executeUpdate();

            if (rowsAffected > 0) {
                return deletedMessage; // Return the deleted message details
            }
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
            
            String fetchSql = "SELECT * FROM Message WHERE message_id=?";
            PreparedStatement ps = connection.prepareStatement(fetchSql);
            ps.setInt(1, message_id);
        
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String existingText = rs.getString("message_text");
                if (message.getMessage_text() != null) {
                    existingText = message.getMessage_text();
                }

        // Create an instance of the updated message
            Message updatedMessage = new Message(
                message_id,
                rs.getInt("posted_by"),
                existingText,
                rs.getLong("time_posted_epoch")
            );

            String updateSQL = "UPDATE Message SET message_text=? WHERE message_id=?";
            PreparedStatement updateStatement = connection.prepareStatement(updateSQL);
        
                
                updateStatement.setString(1,existingText);
                updateStatement.setInt(2,message_id);
                updateStatement.executeUpdate();
               
                return updatedMessage;
            }
            
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    // Get All Messages By Account ID
    public List<Message> getAllMessagesByAccountID(int account_id) {
        List<Message> messages = new ArrayList<>();
        Connection connection = ConnectionUtil.getConnection();
        try {

            String sql = "SELECT Account.account_id, Message.message_id, Message.posted_by, Message.message_text, Message.time_posted_epoch FROM Account LEFT JOIN Message ON Account.account_id = Message.posted_by WHERE Account.account_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, account_id);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                messages.add(message);
                
            }
            return messages;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean isUserExists(int posted_by) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT COUNT(*) FROM Account WHERE account_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, posted_by);
    
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0; // If count is greater than 0, user exists
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
