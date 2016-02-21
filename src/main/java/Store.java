import Models.Channel;
import Models.Message;
import Models.User;
import com.j256.ormlite.dao.Dao;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;

import java.sql.SQLException;
import java.util.Date;

public class Store {

    public void storeNewMessage(SlackMessagePosted event, Dao<Message, String> messageDao, Dao<User, String> userDao, Dao<Channel, String> channelDao) throws SQLException {
        User user = userDao.queryForId(event.getSender().getId());
        Channel channel = channelDao.queryForId(event.getChannel().getId());

        if (user == null) {
            user = this.getFilledUserModelByEvent(event);
            userDao.create(user);
        }
        if (channel == null) {
            channel = this.getFilledChannelModelByEvent(event);
            channelDao.create(channel);
        }
        Message message = this.getFilledMessageModelByEvent(event, user, channel);
        messageDao.create(message);
    }

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
