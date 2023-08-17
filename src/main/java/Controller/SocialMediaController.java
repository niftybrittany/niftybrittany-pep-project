package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static Map<String, String> accounts = new HashMap<>();
    public static List<Message> messages = new ArrayList<>();

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postNewUserHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::postNewMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::patchMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByAccountIdHandler);
        
        //app.start(8080);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    //#1 Create New User
    private void postNewUserHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);

        if(accountService.authenticates(account) && !accounts.containsKey(account.getUsername())){
            accounts.put(account.getUsername(), account.getPassword());
            ctx.json(mapper.writeValueAsString(addedAccount));
            ctx.status(200);
        }else{
            ctx.status(400);
        }
    }

    //#2 Login
    private void postLoginHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account newLogin = accountService.loginAccount(account);
            if(accounts.containsKey(account.getUsername())){
                if(accounts.get(account.getUsername()).equals(account.getPassword())){
                    ctx.json(mapper.writeValueAsString(newLogin));
                    ctx.status(200);
                }else{ctx.status(401);
                }
            }else{
                ctx.status(401);
            } 
    }

    //#3 Create New Message
    private void postNewMessageHandler(Context ctx) throws JsonProcessingException{
        
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        int posted_by = message.getPosted_by();
        
        if(messageService.isUserExists(posted_by) && message.getMessage_text() != null && message.getMessage_text().length() < 255 
            && !message.getMessage_text().isEmpty()){
            Message addedMessage = messageService.addMessage(message);
            ctx.json(mapper.writeValueAsString(addedMessage));
            ctx.status(200);
        }else{
            ctx.status(400);
        }
    }

    // #4 GET All messages Handler
    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        ctx.json(mapper.writeValueAsString(messageService.getAllMessages()));

        ctx.status(200);
    }

    // #5 GET Message Given Message ID
    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException{
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        ObjectMapper mapper = new ObjectMapper();

        Message retrievedMessage = messageService.getMessageByID(message_id);
        
        if (retrievedMessage != null) {
            
            ctx.json(mapper.writeValueAsString(retrievedMessage));
        } else {
            ctx.result(""); // Empty response body
        }
        ctx.status(200);
    }
    
    // #6 Delete Message By Message Id
    private void deleteMessageByIdHandler(Context ctx) throws JsonProcessingException{
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        ObjectMapper mapper = new ObjectMapper();
        
        Message deletedMessage = messageService.deleteMessageByID(message_id);

        if (deletedMessage != null) {
            
            ctx.json(mapper.writeValueAsString(deletedMessage));
        } else {
            ctx.result(""); // Empty response body
        }
        ctx.status(200);
    }
    
    // #7 Update Message By Message Id
    private void patchMessageByIdHandler(Context ctx) throws JsonProcessingException{
            int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        
            ObjectMapper mapper = new ObjectMapper();
    
            Message message = mapper.readValue(ctx.body(), Message.class);
            Message updatedMessage = messageService.updateMessageByID(message_id, message);
    
            if (updatedMessage != null && !updatedMessage.getMessage_text().isEmpty()) {
                ctx.json(mapper.writeValueAsString(updatedMessage));
                ctx.status(200);
            }else{
                ctx.status(400);
            }
            
        
    }

    // #8 Get All Messages By Account Id
    private void getAllMessagesByAccountIdHandler(Context ctx) throws JsonProcessingException{

        int account_id = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getAllMessagesByAccountID(account_id);

        if (!messages.isEmpty()) {
            ObjectMapper mapper = new ObjectMapper();
            ctx.json(mapper.writeValueAsString(messages));
        }else{
           ctx.result("");
           
        
        }
        ctx.status(200);
        
    }

}