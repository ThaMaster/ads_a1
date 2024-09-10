package se.umu.cs.ads.a1.backend.rest.resources;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.resource.*;
import se.umu.cs.ads.a1.backend.InMemoryMessengerBackEnd;
import se.umu.cs.ads.a1.backend.JsonUtil;
import se.umu.cs.ads.a1.backend.rest.RestBackend;
import se.umu.cs.ads.a1.types.Message;

import java.io.IOException;
import java.util.List;

public class MessagesResource extends ServerResource {

    private InMemoryMessengerBackEnd backend;

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        backend = RestBackend.getBackend();
    }

    @Post
    public void batchStoreMessages(JacksonRepresentation<List<Message>> msgEntity) {
        try {
            if (msgEntity.getMediaType().equals(MediaType.APPLICATION_JSON)) {
                Message[] msgList = JsonUtil.parseMessages(msgEntity.getText());
                backend.store(msgList);
                setStatus(Status.SUCCESS_ACCEPTED);
            } else {
                setStatus(Status.CLIENT_ERROR_UNSUPPORTED_MEDIA_TYPE);
            }
        } catch (IOException e) {
            e.printStackTrace();
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
        }
    }

    @Get
    public JacksonRepresentation<String> handleGet() {
        String ids = (String) getRequestAttributes().get("messageIds");
        // TODO: Implement this function
        return null;
    }

    private JacksonRepresentation<String> getMessagesByIds(String ids) {
        // TODO: Implement this function
        return null;
    }


    @Delete
    public void deleteMessagesByIds() {
        String ids = (String) getRequestAttributes().get("messageIds");
        // TODO: Implement this function
    }
}
