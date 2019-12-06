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

    boolean xml_got_parsed = false;
    private SortedSet<String> set;
    private Map<String, SortedSet<String>> navi_data = null;

    private static String getFile() throws FileNotFoundException {
        String filename = "austria-latest.osm";
        String inputFile = System.getProperty("user.dir") + "/src/resources/files/mapFiles/" + filename;

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
        if (req.getUrl().getRawUrl().startsWith("/navi"))
            return 1f;
        else
            return 0;
    }

    /**
     * Called by the server when the plugin should handle the request.
     *
     * @param req
     * @return A new response object.
     */
    @Override
    public ResponseClass handle(RequestClass req) throws IOException, ParserConfigurationException, SAXException {

        ResponseClass _resp = new ResponseClass();
        System.out.println(req.getUrl().getRawUrl());

        if (req.getUrl().getRawUrl().startsWith("/navi")) {
            System.out.println("XMLGOTPARSED: " + xml_got_parsed);

            if (!xml_got_parsed) {
                System.out.println("PARSE");
                navi_data = xmlRead();
                System.out.println("FINISHED");
            } else if(xml_got_parsed){
                System.out.println("SEARCHFOR");
                StringBuilder cities = new StringBuilder();
                if (req.getUrl().getParameter().containsKey("street")) {
                    String street = req.getUrl().getParameter().get("street");
                    street = street.replaceAll("\\+", " ")
                            .toLowerCase()
                            .replaceAll("%df", "ß")
                            .replaceAll("%e4", "ä")
                            .replaceAll("%f6", "ö")
                            .replaceAll("%fc", "ü");

//					System.out.println(street);
//					System.out.println(navi_data.get(street));
                    if (navi_data.containsKey(street)) {
                        set = navi_data.get(street);
//						System.out.println(set.size());
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
                        cities = new StringBuilder("Street does not exist.");
                    }
                }

                _resp.setContent("<html>"
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

        System.out.println("RETURN");
        return _resp;
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
            xml_got_parsed = true;
            return handler.returnData();
        } catch (SAXException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}




