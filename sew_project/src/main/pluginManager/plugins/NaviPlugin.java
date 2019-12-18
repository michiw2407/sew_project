package main.pluginManager.plugins;

import interfaces.Plugin;
import main.request.RequestClass;
import main.response.ResponseClass;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.SortedSet;

public class NaviPlugin implements Plugin {

    boolean xmlParsed = false;
    private SortedSet<String> set;
    private Map<String, SortedSet<String>> navi_data = null;

    private static String getFile() throws FileNotFoundException {
        String filename = "austria-latest.osm";
        String inputFile = System.getProperty("user.home") + "/SEWFiles/mapFiles/" + filename;

        System.out.println("Valid input file");
        return inputFile;

    }

    /**
     * Returns a score between 0 and 1 to indicate that the plugin is willing to
     * handle the request. The plugin with the highest score will execute the
     * request.
     *
     * @param req
     * @return A score between 0 and 1
     */
    public float canHandle(RequestClass req) throws Exception {
        if (req.isValid()) {
            if (req.url.getRawUrl().startsWith("/navi")) {
                System.out.println("NAVI PLUGIN VALID");
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
    public ResponseClass handle(RequestClass req) throws IOException, ParserConfigurationException, SAXException {

        ResponseClass response = new ResponseClass();
        System.out.println(req.getUrl().getRawUrl());

        if (req.getUrl().getRawUrl().equals("/navi")) {
            response.setContent("<html>"
                    + "<head>"
                    + "</head>"
                    + "<body>"
                    + "<form <action='navi' method='get'>"
                    + "Streetname: <input type='text' name='street'>"
                    + "<input type='submit' value='Submit'>"
                    + "</form>"
                    + "</body>"
                    + "</html>");
        }

        if (req.getUrl().getRawUrl().equals("/navi/parseNew")) {
            System.out.println("PARSENEW");
            xmlParsed = false;

            response.setContent("<html>"
                    + "<head>"
                    + "</head>"
                    + "<body>"
                    + "File neu <a href='/navi?street=/'>parsen</a>"
                    + "</body>"
                    + "</html>");
        }


        if (req.getUrl().getRawUrl().startsWith("/navi?")) {

            if (!xmlParsed) {
                System.out.println("PARSING");

                navi_data = xmlRead();
//                xml_got_parsed = true;
                System.out.println("FINISHED PARSING");
                //xml_got_parsed gets TRUE
            }

            if (xmlParsed) {
                System.out.println("SEARCHFOR");
                StringBuilder cities = new StringBuilder();
                if (req.getUrl().getRawUrl().startsWith("/navi?street=")) {
                    String street = req.getUrl().getParameter().get("street");
                    if (!street.equals("")) {
                        street = street.replaceAll("\\+", " ")
                                .toLowerCase()
                                .replaceAll("%df", "ß")
                                .replaceAll("%e4", "ä")
                                .replaceAll("%f6", "ö")
                                .replaceAll("%fc", "ü");


                        if (navi_data.containsKey(street)) {
                            set = navi_data.get(street);
                            for (String s : set) {
                                cities.append(
                                        s.replaceAll("ß", "&szlig")
                                                .replaceAll("ä", "&auml")
                                                .replaceAll("ö", "&ouml")
                                                .replaceAll("ü", "&uuml")
                                                .replaceAll("Ä", "&Auml")
                                                .replaceAll("Ö", "&Ouml")
                                                .replaceAll("Ü", "&Uuml"));
                                cities.append("<br>");
                            }
                        } else {
                            if (req.getUrl().getRawUrl().startsWith("/navi?street=/")) {
                                cities = new StringBuilder("Please enter a city.");
                            } else {
                                cities = new StringBuilder("Street does not exist.");
                            }
                        }
                    }
                }

                response.setContent("<html>"
                        + "<head>"
                        + "</head>"
                        + "<body>"
                        + "<form <action='navi' method='get'>"
                        + "Streetname: <input type='text' name='street'>"
                        + "<input type='submit' value='Submit'>"
                        + "</form>"
                        + "<br><pre>"
                        + cities
                        + "</pre>"
                        + "</body>"
                        + "</html>");
            }

        }

        return response;
    }

    /**
     * Class to load the map for the navi.
     *
     * @return Returns a map with the parsed data from the XML-file
     * @throws ParserConfigurationException
     * @throws IOException
     */
    private Map<String, SortedSet<String>> xmlRead() throws ParserConfigurationException, IOException {
        try {
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            javax.xml.parsers.SAXParser parser = parserFactory.newSAXParser();
            FileReader fileRead = new FileReader(getFile());
            InputSource input = new InputSource(fileRead);
            OSMHandler handler = new OSMHandler();
            parser.parse(input, handler);
            xmlParsed = true;
            return handler.returnData();
        } catch (SAXException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}




