package Controller;

import java.util.List;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::createNewUserHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::createNewMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("messages/{message_id}", this::getMessageByIDHandler);
        app.delete("messages/{message_id}", this::deleteMessageByIDHandler);
        app.patch("messages/{message_id}", this::updateMessageByIDHandler);
        app.get("/accounts/{account_id}/messages", this::getAllAccountMessagesHandler);
        app.start(8080);
        

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    //#1 Create New User
    private void createNewUserHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);

        if(addedAccount!=null){
            ctx.json(mapper.writeValueAsString(addedAccount));
        }else{
            ctx.status(400);
        }
    }

    //#2 Login
    private void loginHandler(Context ctx) throws JsonProcessingException{
            ObjectMapper mapper = new ObjectMapper();
            Account account = mapper.readValue(ctx.body(), Account.class);
            Account newLogin = accountService.authenticates(account.username, account.password);
        if(newLogin != null){
            ctx.json(mapper.writeValueAsString(newLogin));
            ctx.status(200);
        }else{
            ctx.status(401);
        }

        
    //#3 Create New Message
        private void createNewMessageHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        Message message = om.readValue(ctx.body(), Message.class);
        if(message.getMessage_text().isBlank()){
        ctx.json(om.writeValueAsString(message));
        ctx.status(400);
        return;
    }
    Message newMessage = messageService.addMessage(message);
    if(newMessage != null) {
        ctx.json(om.writeValueAsString(newMessage));
        ctx.status(200);
    }
    else{
        ctx.status(400);
    }
    
    }
    //#4 Get All Messages
    private void getAllMessagesHandler (Context ctx) throws JsonProcessingException {
       List<Message> messages = messageService.getAllMessages();
       ctx.json(messages);
    }

    //#5 Get Messages By ID
    private void getMessageByIDHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        Message message = om.readValue(ctx.body(), Message.class);
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        if(message != null) {
            ctx.status(200);
        }else{
            ctx.status(400);
        }

    }
    //#6 Delete Message By ID
    private void deleteMessageByIDHandler (Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message deleteMessage = messageService.deleteMessageById(message_id);
        if(deleteMessage!=null){
            ctx.json(mapper.writeValueAsString(deleteMessage));
            ctx.status(200);
        }else{
            ctx.status (400);
        }
    };
 
    //#7
    private void updateMessageByIDHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message updatedMessage = messageService.updateMessage(message_id, message_text);
        System.out.println(updatedMessage);
        if(updatedMessage == null){
            ctx.status(400);
        }else{
            ctx.json(mapper.writeValueAsString(updatedMessage));
        }
    //#8
    private void getAllAccountMessagesHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account accountMessages = mapper.readValue(ctx.body(), Account.class);
    }


}