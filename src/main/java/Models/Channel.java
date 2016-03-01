package Models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "jsmt_channel")
public class Channel {

    @DatabaseField(id = true)
    private String channel_id;
    @DatabaseField
    private String name;

    public Channel(){
        //Empty constructor for Ormlite
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
