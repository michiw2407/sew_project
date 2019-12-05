package main.pluginManager.plugins;

import interfaces.Plugin;
import interfaces.Response;
import main.request.RequestClass;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.*;

public class TemperaturePlugin implements Plugin {

    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/tempplugin", "root", "");

            Statement stmt = con.createStatement();
            stmt.executeQuery("INSERT INTO tempentry (date, temperature) VALUES ('27.11.2018', 3.33)");
            ResultSet rs = stmt.executeQuery("select * from tempentry");
            while (rs.next())
                System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3));
            con.close();
        } catch (
                Exception e) {
            System.out.println(e);
        }

    }


    /**
     * Returns a score between 0 and 1 to indicate that the plugin is willing to
     * handle the request. The plugin with the highest score will execute the
     * request.
     *
     * @param req
     * @return A score between 0 and 1
     */
    @Override
    public float canHandle(RequestClass req) throws Exception {
        return 0;
    }

    /**
     * Called by the server when the plugin should handle the request.
     *
     * @param req
     * @return A new response object.
     */
    @Override
    public Response handle(RequestClass req) throws IOException, ParserConfigurationException, SAXException {
        return null;
    }
}
