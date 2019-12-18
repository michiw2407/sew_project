package main.pluginManager.plugins;

import interfaces.Plugin;
import main.request.RequestClass;
import main.response.ResponseClass;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class TemperaturePlugin implements Plugin {

    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

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
        if (req.isValid()) {
            if (req.url.getRawUrl().startsWith("/temp")) {
                System.out.println("TEMPERATURE PLUGIN VALID");
                return 1.0f;
            }
        }
        return 0.0f;
    }

    /**
     * Called by the server when the plugin should handle the request.
     *
     * @param req
     * @return A new response object.
     */
    @Override
    public ResponseClass handle(RequestClass req) throws IOException, ParserConfigurationException, SAXException, SQLException {
        ResponseClass response = new ResponseClass();

        fillDatabase();


        if (req.getUrl().getRawUrl().matches("/temp/\\d\\d\\d\\d/\\d\\d/\\d\\d")) {
            String time =
                    req.getUrl().getRawUrl().split("[/|?]")[2] + "-" +
                            req.getUrl().getRawUrl().split("[/|?]")[3] + "-" +
                            req.getUrl().getRawUrl().split("[/|?]")[4] + " 00:00:00";

            Timestamp ts = Timestamp.valueOf(time);
            response.setContent(getDatabasaDataXML(ts));
        } else if (req.getUrl().getRawUrl().startsWith("/temp/search")) {
            System.out.println("HIER SUCHE: " + req.getUrl().getParameter().get("date"));

            String date = req.getUrl().getParameter().get("date");
            Timestamp ts = Timestamp.valueOf(date + " 00:00:00");

            response.setContent("<html><body>" +
                    "<h1>Temperatures:</h1>" +
                    "<form action=\"/temp/search\">\n" +
                    "  <input type=\"date\" name=\"date\" value=" + date + ">\n" +
                    "  <input type=\"submit\" value=\"Search\">\n" +
                    "</form> " +
                    "<form action=\"/temp\">\n" +
                    "  <input type=\"submit\" value=\"Overview\">\n" +
                    "</form> " +
                    "" +
                    "" +
                    "<br>" + getDatabaseDataByDate(ts) + "</body></html>");
        } else {
            int offset = 0;
            int limit = 10;

            String param = null;

            try {
                param = req.getUrl().getParameter().get("offset");
                if (param != null) {
                    offset = Integer.parseInt(req.getUrl().getParameter().get("offset"));
                    if (offset < 0) offset = 0;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                param = req.getUrl().getParameter().get("limit");
                if (param != null) {
                    limit = Integer.parseInt(req.getUrl().getParameter().get("limit"));
                    if (limit < 0) limit = 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("RAWURL: " + req.getUrl().getRawUrl());

            response.setContent("<html><body>" +
                    "<h1>Temperatures:</h1>" +
                    "<form action=\"/temp/search\">\n" +
                    "  Search date:<br>\n" +
                    "  <input type=\"date\" name=\"date\" value=\"2019-12-05\">\n" +
                    "  <input type=\"submit\" value=\"Submit\">\n" +
                    "</form> " +
                    "" +
                    "" +
                    "<br>" + getDatabaseData(limit, offset) + "</body></html>");
        }

        return response;
    }


    private String getDatabaseDataByDate(Timestamp date) {
        StringBuilder res = new StringBuilder(
                "<table border='1'><tr><td><a>Date&nbsp;&nbsp;</a></td><td><a>Temperature&nbsp;&nbsp;</a></td></tr>");

        try {
            Connection conn = getDbConnection();
            PreparedStatement query = conn.prepareStatement("SELECT * FROM tempentry WHERE date = ? ORDER BY date DESC;");
            query.setTimestamp(1, date);

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                res.append("<tr><td><a>");
                res.append(resultSet.getString("date"));
                res.append("</a></td><td><a>");
                res.append(resultSet.getString("temperature"));
                res.append("</a></td></tr>");
            }
            res.append("</table>");

            resultSet.close();
            query.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res.toString();
    }

    private String getDatabaseData(int limit, int offset) {
        StringBuilder res = new StringBuilder(
                "<table border='1'><tr><td><a>Date&nbsp;&nbsp;</a></td><td><a>Temperature&nbsp;&nbsp;</a></td></tr>");

        try {
            Connection conn = getDbConnection();
            PreparedStatement query = conn.prepareStatement("select * from tempentry order by date desc limit ? offset ?");
            query.setInt(1, limit);
            query.setInt(2, offset);
            ResultSet resultSet = query.executeQuery();

            int i = 0;
            while (resultSet.next()) {
                if (i >= limit) break;
                res.append("<tr><td><a>");
                res.append(resultSet.getString("date"));
                res.append("</a></td><td><a>");
                res.append(resultSet.getString("temperature"));
                res.append("</a></td></tr>");
                i++;
            }

            res.append("</table>");

            if (offset == 0) {
                res.append("<a href='?offset=");
                res.append(offset + limit);
                res.append("'>next</a>");
            } else if (offset > 0) {
                res.append("<a href='?offset=");
                res.append(offset - limit);
                res.append("'>prev</a>&nbsp;&nbsp;");
                res.append("<a href='?offset=");
                res.append(offset + limit);
                res.append("'>next</a>");
            }

            resultSet.close();
            query.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res.toString();
    }

    private Connection getDbConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/tempplugin", "root", "");
    }

    private String getDatabasaDataXML(Timestamp time) {
        StringBuilder res = new StringBuilder("<?xml version='1.0'?><temperatures>");

        try {
            Connection conn = getDbConnection();

            PreparedStatement query = conn.prepareStatement("SELECT * FROM tempentry WHERE date = ? ORDER BY date DESC;");
            query.setTimestamp(1, time);

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next()) {
                res.append("<entry><daytemperature>");
                res.append(resultSet.getString("temperature"));
                res.append("</daytemperature><date>");
                res.append(resultSet.getString("date"));
                res.append("</date></entry>");
            }
            res.append("</temperatures>");

            resultSet.close();
            query.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res.toString();
    }


    private void fillDatabase() throws SQLException {
        Random r = new Random();

        double min = 0.00, max = 40.00, randDouble;
        int dayOfYear;
        BigDecimal bd;

        Connection conn = getDbConnection();
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT COUNT(*) FROM tempentry");
        resultSet.next();
        int rows = resultSet.getInt("count(*)");

        if (rows < 10000) {
            for (int i = 0; i <= 10000; i++) {

                GregorianCalendar gc = new GregorianCalendar();

                int year = ThreadLocalRandom.current().nextInt(1900, 2019 + 1);

                gc.set(Calendar.YEAR, year);

                dayOfYear = ThreadLocalRandom.current().nextInt(1, gc.getActualMaximum(Calendar.DAY_OF_YEAR) + 1);
                bd = new BigDecimal(min + r.nextFloat() * (max - min)).setScale(2, RoundingMode.HALF_UP);
                randDouble = bd.doubleValue();

                gc.set(Calendar.DAY_OF_YEAR, dayOfYear);

                String randDate = gc.get(Calendar.YEAR) + "/" + (gc.get(Calendar.MONTH) + 1) + "/" + gc.get(Calendar.DAY_OF_MONTH);

                stmt.executeUpdate("INSERT INTO tempentry (date, temperature) VALUES ('" + randDate + "'," + randDouble + ")");

            }
        }
        conn.close();
    }
}
