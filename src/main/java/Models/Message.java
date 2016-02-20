package Models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "message")
public class Message {

    @DatabaseField(id = true)
    private Integer hash_id;
    @DatabaseField
    private String user_id;
    @DatabaseField
    private String channel_id;
    @DatabaseField
    private String message;
    @DatabaseField
    private Date created_at;

    public Message(){
        //Empty constructor
    }

    public Integer getHashId()
    {
        return this.hash_id;
    }

    public void setHashId(Integer hash){
        this.hash_id = hash;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
