package main.pluginManager.plugins;

import interfaces.Plugin;
import interfaces.Response;
import main.request.RequestClass;
import main.response.ResponseClass;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NaviPlugin implements Plugin {


    Map<String, List<String>> townStreetDictionary = new HashMap<>();
    boolean currentlyUpdating = false;

    //searches for a "monaco-latest.osm" file
    private File getFile() throws FileNotFoundException {

        String filename = "austria-latest.osm";
        File inputFile = new File(System.getProperty("user.dir") + "/src/resources/files/mapFiles/" + filename);

        if (inputFile.exists()) {
            System.out.println("Valid input file");
            return inputFile;
        } else {
            throw new FileNotFoundException("Map not found.");
        }
    }


    // searches and reads the found file. Creates a dictionary which contains all cities and their streets.
    private Map<String, List<String>> createMap() throws IOException, SAXException, ParserConfigurationException {

        System.out.println("CREATE MAP");
        String city = "";
        Map<String, List<String>> cityStreetMap = new HashMap<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        Document document = documentBuilder.parse(getFile());

        //optional, but recommended
        //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
        //document.getDocumentElement().normalize();

        System.out.println("Root element :" + document.getDocumentElement().getNodeName());

//        NodeList nList = document.getElementsByTagName("node");
        NodeList nList = document.getChildNodes();


        for (int i = 0; i < nList.getLength(); i++) {
            System.out.println("search ...");

            Node node = nList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                // do something with the current element
                System.out.println(node.getNodeName());

                String element = node.getNodeName();

                // the name of the current element
                if (element.equals("tag")) {

                    String k = ((Element) node).getAttribute("k");
                    String v = ((Element) node).getAttribute("v");

                    //incase the tag is city
                    if (k.equals("addr:city")) {
                        //adds the city as key with empty value to dictionary
                        if (!cityStreetMap.containsKey(v)) {
                            List<String> newList = new ArrayList<String>();
                            cityStreetMap.put(v, newList);
                        }
                        city = v;
                    }
                    //incase the tag is street
                    else if (k.equals("addr:street")) {
                        //adds the street to the corresponding city
                        if (cityStreetMap.containsKey(city)) {
                            if (!cityStreetMap.get(city).contains(v)) {
                                cityStreetMap.get(city).add(v);
                                city = "";
                            }
                        }
                    }
                }
            }


        }

        return cityStreetMap;

    }


    //receives the filled dictionary & street name and searches for the cities
    private List<String> getCity(Map<String, List<String>> inpMap, String street) {
        List<String> matches = new ArrayList<String>();

        System.out.println("getCity");
        System.out.println("adds cities");

        for (Map.Entry<String, List<String>> match : inpMap.entrySet()) {
            if (match.getKey().equals(street)) {
                System.out.println(match.getKey());
                matches.add(match.getKey());
            }
        }


        return matches;
    }

    // checks if the request is good and a new map is currently loaded

    /**
     * Returns a score between 0 and 1 to indicate that the plugin is willing to
     * handle the request. The plugin with the highest score will execute the
     * request.
     *
     * @param req
     * @return A score between 0 and 1
     */
    public float canHandle(RequestClass req) throws Exception {
        if (currentlyUpdating)
            throw new Exception("A new map is currently being loaded.");
        else if (req.isValid() && !currentlyUpdating) {
            if (req.url.getSegments()[0].equals("navi")) {
                System.out.println("NaviPlugin VALID");
                return 1.0f;
            }
        }
        return 0.0f;
    }


    //Hanlde request. receives from content String a command (either to load map new or search for cities)

    /**
     * Called by the server when the plugin should handle the request.
     *
     * @param req
     * @return A new response object.
     */
    @Override
    public Response handle(RequestClass req) throws IOException, ParserConfigurationException, SAXException {
        ResponseClass res = new ResponseClass();

        if (req.isValid()) {
            res.setStatusCode(200);

            String action = req.getUrl().getRawUrl();
            String street = req.getUrl().getParameter().get("street");

            if(action.equals("/favico.ico"))
                res.setStatusCode(404);

            if (action.contains("street=")) {
                // cut the "street=" from String
                List<String> matches = new ArrayList<String>();
                if (getFile().isFile()) {
                    currentlyUpdating = true;
                    Map<String, List<String>> map = createMap();
                    currentlyUpdating = false;
                    matches.addAll(getCity(map, street));


                    res.setContent("Orte gefunden: " + matches);
                }

            } else {
                res.setContent("Anfrage eingeben");
            }
        } else
            res.setStatusCode(404);

        return res;
    }
}




