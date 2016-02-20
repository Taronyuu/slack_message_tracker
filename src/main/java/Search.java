import Models.Message;
import Models.User;
import com.j256.ormlite.dao.Dao;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Zander on 20/02/16.
 */
public class Search {

    public void searchCommand(SlackSession session, SlackMessagePosted event, Dao<Message, String> messageDao, Dao<User, String> userDao) throws SQLException {
        String searchedText = event.getMessageContent().replace("<@" + session.sessionPersona().getId() + ">: search", "").trim();
        System.out.println("SEARCHED TEXT: " + searchedText);
        String channelId = event.getChannel().getId();
        List<Message> messages = messageDao.queryBuilder().where().like("message", "%" + searchedText + "%").query();

        String textMessage = "The following results where found:\n";
        for(int i = 0; i < messages.size(); i++){
            Message message = messages.get(i);
            User user = userDao.queryForId(message.getUser_id());
            if(user.getId().equals(session.sessionPersona().getId())){
                continue;
            }
            textMessage = textMessage + "[" + message.getCreated_at() + "] " + user.getUserName() + ": " + message.getMessage() + "\n";
        }
        if(messages.size() == 0){
            textMessage = textMessage + "*No items could be found* :open_mouth:";
        }
        session.sendMessage(event.getChannel(), textMessage, null);
    }

}
