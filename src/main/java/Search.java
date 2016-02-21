import Models.Message;
import Models.User;
import com.j256.ormlite.dao.Dao;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackUser;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class Search {

    private static int maxRows = 10;

    public void searchCommand(SlackSession session, SlackMessagePosted event, Dao<Message, String> messageDao, Dao<User, String> userDao) throws SQLException {
        String searchedText = event.getMessageContent().replace("<@" + session.sessionPersona().getId() + ">: ", "").trim();
        String[] searchedTextArray = searchedText.split(" ", 2);
        searchedText = searchedTextArray[1];
        String searchCommand = searchedTextArray[0];


        String[] pageNumber = searchCommand.split("-", 2);
        Integer pageCount = 1;
        Long offsetCount = new Long(0);
        if(pageNumber.length > 1){
            pageCount = Integer.parseInt(pageNumber[1]);
            offsetCount = new Long((pageCount-1) * this.maxRows);
        }


        String channelId = event.getChannel().getId();
        List<Message> messages = messageDao.queryBuilder().offset(offsetCount).limit(new Long(this.maxRows + 2)).where().like("message", "%" + searchedText + "%").and().ne("user_id", session.sessionPersona().getId()).query();
        String textMessage = "*The following results where found:*\n";
        for(int i = 0; i < messages.size(); i++){
            if(i > this.maxRows){
                continue;
            }
            Message message = messages.get(i);
            User user = userDao.queryForId(message.getUser_id());
//            if(user.getId().equals(session.sessionPersona().getId())){
//                continue;
//            }
            textMessage = textMessage + "[" + message.getCreated_at() + "] " + user.getUserName() + ": " + message.getMessage() + "\n";
        }
        if(!messages.isEmpty()){
            if(messages.size() > this.maxRows){
                textMessage = textMessage + "*Limited results to " + this.maxRows + ". Use search-" + (pageCount+1) + " to view the next page*";
            }
        }

        if(messages.isEmpty()){
            textMessage = "*No items could be found* :open_mouth:";
        }
        session.sendMessage(event.getChannel(), textMessage, null);
    }

}
