import Models.Channel;
import Models.Message;
import Models.User;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;

import java.util.Date;

/**
 * Created by Zander on 20/02/16.
 */
public class Store {

    public User getFilledUserModelByEvent(SlackMessagePosted event){
        User user = new User();
        user.setId(event.getSender().getId());
        user.setRealName(event.getSender().getRealName());
        user.setUserMail(event.getSender().getUserMail());
        user.setUserName(event.getSender().getUserName());
        return user;
    }

    public Channel getFilledChannelModelByEvent(SlackMessagePosted event){
        Channel channel = new Channel();
        channel.setChannel_id(event.getChannel().getId());
        channel.setName(event.getChannel().getName());
        return channel;
    }

    public Message getFilledMessageModelByEvent(SlackMessagePosted event, User user, Channel channel){
        Message message = new Message();
        message.setHashId(event.hashCode());
        message.setUser_id(user.getId());
        message.setChannel_id(channel.getChannel_id());
        message.setMessage(event.getMessageContent());
        message.setCreated_at(new Date());
        return message;
    }
}
