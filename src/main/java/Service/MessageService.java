package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }
    
    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }
    
    public List<Message> getAllMessages() {
        List<Message> message = messageDAO.getAllMessages();
        return message;
    }

    public Message addMessage(Message message){
        return messageDAO.createMessage(message);
    }
    
    public Message updateMessageByID(int message_id, Message message){
        return messageDAO.updateMessageByID(message_id, message);
    }

    public Message getMessageByID(int message_id){
        return messageDAO.getMessageByID(message_id);
    }

    public List<Message> getAllMessagesByAccountID(int account_id){
        return messageDAO.getAllMessagesByAccountID(account_id);
    }
    
    public Message deleteMessageByID(int message_id) {
        return messageDAO.deleteMessageByID(message_id);
    }

    public boolean isUserExists(int posted_by){
        return messageDAO.isUserExists(posted_by);
    }
}   

