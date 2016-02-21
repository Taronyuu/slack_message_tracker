import java.io.*;
import java.util.Properties;

public class Property {

    public String propertyPath = "config.properties";
    Properties prop = new Properties();

    public void createPropertyFile() throws IOException {
        OutputStream output;

        output = new FileOutputStream(propertyPath);

        prop.setProperty("database_type", "sqlite");
        prop.setProperty("mysql_host", "127.0.0.1");
        prop.setProperty("mysql_database", "database");
        prop.setProperty("mysql_username", "admin");
        prop.setProperty("mysql_password", "password");
        prop.setProperty("mysql_port", "3306");

        prop.store(output, null);
    }

    public Properties getProperties() throws IOException {
        InputStream input = new FileInputStream(propertyPath);
        prop.load(input);
        return prop;
    }

}
