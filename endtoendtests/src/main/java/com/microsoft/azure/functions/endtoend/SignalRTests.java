package com.microsoft.azure.functions.endtoend;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import java.util.*;

/**
 * Azure Functions with AzureSignalRService.
 */
public class SignalRTests {
    /**
     * This function will listen at HTTP endpoint "/api/negotiate".
     */
    @FunctionName("negotiate")
    public SignalRConnectionInfo SignalRGetInfo(
        @HttpTrigger(name = "req", authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
        @SignalRConnectionInfoInput(name = "connectionInfo", hubName = "simplechat", userId = "{headers.x-ms-signalr-userid}") SignalRConnectionInfo connectionInfo,
        final ExecutionContext context
    ) {
        return connectionInfo;
    }

    /**
     * This function will listen at HTTP endpoint "/api/messages".
     */
    @FunctionName("messages")
    public void SignalRSendMessage(
        @HttpTrigger(name = "req", methods = {HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
        @SignalROutput(name = "signalRMessages", hubName = "simplechat") SignalRMessage signalRMessage,
        final ExecutionContext context
    ) {
        
        signalRMessage.userId = request.getBody().orElse("recipient");
        signalRMessage.groupName = request.getBody().orElse("groupname");
        signalRMessage.target = "newMessage";
        String message = request.getBody().toString();
        Object [] messageList = {message};
        signalRMessage.arguments = Arrays.asList(messageList);
        
    }   
    
    /**
     * This function will listen at HTTP endpoint "/api/addToGroup".
     */
    @FunctionName("addToGroup")
    public void SignalRAddToGroup(
        @HttpTrigger(name = "req", methods = {HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
        @SignalROutput(name = "signalRGroupActions", hubName = "simplechat") SignalRGroupAction signalRGroupAction
    ) {
        signalRGroupAction.action = "add";
        signalRGroupAction.groupName = request.getBody().orElse("groupname");
        signalRGroupAction.userId = request.getBody().orElse("recipient");
    }

    /**
     * This function will listen at HTTP endpoint "/api/removeFromGroup".
     */
    @FunctionName("removeFromGroup")
    public void SignalRRemoveFromGroup(
        @HttpTrigger(name = "req", methods = { HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
        @SignalROutput(name = "signalRGroupActions", hubName = "simplechat") SignalRGroupAction signalRGroupAction,
        final ExecutionContext context
    ) {
        signalRGroupAction.action = "remove";
        signalRGroupAction.groupName = request.getBody().orElse("groupname");
        signalRGroupAction.userId = request.getBody().orElse("recipient");
    }
}
