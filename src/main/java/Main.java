import Models.Channel;
import Models.Message;
import Models.User;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;

import java.sql.SQLException;
import java.util.Date;

public class Main {

    protected static Store storeMethod = new Store();
    protected static Search searchMethod = new Search();

    public static void main(String[] args) throws Exception {
        String databaseUrl = "jdbc:sqlite:./sqlite.db";
        ConnectionSource connectionSource = new JdbcConnectionSource(databaseUrl);
        Dao<User, String> userDao = DaoManager.createDao(connectionSource, User.class);
        Dao<Message, String> messageDao = DaoManager.createDao(connectionSource, Message.class);
        Dao<Channel, String> channelDao = DaoManager.createDao(connectionSource, Channel.class);
        TableUtils.createTableIfNotExists(connectionSource, User.class);
        TableUtils.createTableIfNotExists(connectionSource, Message.class);
        TableUtils.createTableIfNotExists(connectionSource, Channel.class);

        SlackSession session = SlackSessionFactory.createWebSocketSlackSession("xoxb-22329050294-0s0jFV1a4Ekh6YbykPzfQmqQ");
        session.connect();

        session.addMessagePostedListener(new SlackMessagePostedListener() {
            @Override
            public void onEvent(SlackMessagePosted event, SlackSession session) {
                System.out.println("New event - Message received: " + event.getMessageContent() + "; Message sended by: " + event.getSender().getUserName() + " from channel: " + event.getChannel().getName());

                if(event.getMessageContent().startsWith("<@" + session.sessionPersona().getId() + ">: search") && event.getSender().getId() != session.sessionPersona().getId()){
                    try {
                        searchMethod.searchCommand(session, event, messageDao, userDao);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        User user = userDao.queryForId(event.getSender().getId());
                        Channel channel = channelDao.queryForId(event.getChannel().getId());

                        if (user == null) {
                            user = storeMethod.getFilledUserModelByEvent(event);
                            userDao.create(user);
                        }
                        if (channel == null) {
                            channel = storeMethod.getFilledChannelModelByEvent(event);
                            channelDao.create(channel);
                        }

                        Message message = storeMethod.getFilledMessageModelByEvent(event, user, channel);
                        messageDao.create(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}