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

import java.io.File;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

public class Main {

    protected static Store storeMethod = new Store();
    protected static Search searchMethod = new Search();
    protected static Property propertyMethod = new Property();

    public static void main(String[] args) throws Exception {
        File f = new File(propertyMethod.propertyPath);
        if(!f.exists() || f.isDirectory()){
            propertyMethod.createPropertyFile();
        }

        Properties prop = propertyMethod.getProperties();

        ConnectionSource connectionSource = null;
        if(prop.getProperty("database_type").equals("mysql")){
            String databaseUrl = "jdbc:mysql://" + prop.getProperty("mysql_host") + ":" + prop.getProperty("mysql_port") + "/" + prop.getProperty("mysql_database");
            connectionSource = new JdbcConnectionSource(databaseUrl, prop.getProperty("mysql_username"), prop.getProperty("mysql_password"));
        }else{
            String databaseUrl = "jdbc:sqlite:./sqlite.db";
            connectionSource = new JdbcConnectionSource(databaseUrl);
        }
        Dao<User, String> userDao = DaoManager.createDao(connectionSource, User.class);
        Dao<Message, String> messageDao = DaoManager.createDao(connectionSource, Message.class);
        Dao<Channel, String> channelDao = DaoManager.createDao(connectionSource, Channel.class);
        TableUtils.createTableIfNotExists(connectionSource, User.class);
        TableUtils.createTableIfNotExists(connectionSource, Message.class);
        TableUtils.createTableIfNotExists(connectionSource, Channel.class);

//        for(int i = 0; i < 296001; i++){
//            String uuid = UUID.randomUUID().toString();
//            Channel channel = new Channel();
//            channel.setName(uuid);
//            channel.setChannel_id(uuid);
//            channelDao.create(channel);
//
//            User user = new User();
//            user.setUserName(uuid);
//            user.setUserMail(uuid);
//            user.setRealName(uuid);
//            user.setId(uuid);
//            userDao.create(user);
//
//            Message message = new Message();
//            message.setHashId(9999999);
//            message.setCreated_at(new Date());
//            message.setMessage(uuid + uuid + uuid + uuid + uuid);
//            message.setChannel_id(uuid);
//            message.setUser_id(uuid);
//            messageDao.create(message);
//        }

        SlackSession session = SlackSessionFactory.createWebSocketSlackSession("xoxb-22329050294-0s0jFV1a4Ekh6YbykPzfQmqQ");
        session.connect();

        session.addMessagePostedListener(new SlackMessagePostedListener() {
            @Override
            public void onEvent(SlackMessagePosted event, SlackSession session) {
                System.out.println("New event - Message received: " + event.getMessageContent() + "; Message sended by: " + event.getSender().getUserName() + " from channel: " + event.getChannel().getName());

                if(event.getMessageContent().startsWith("<@" + session.sessionPersona().getId() + ">: search") && event.getSender().getId() != session.sessionPersona().getId()){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                searchMethod.searchCommand(session, event, messageDao, userDao);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }else if(event.getSender().getId() != session.sessionPersona().getId()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                storeMethod.storeNewMessage(event, messageDao, userDao, channelDao);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });
    }
}