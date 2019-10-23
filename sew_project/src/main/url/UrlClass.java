package main.url;

import at.technikum.Interfaces.Url;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class UrlClass implements Url {

    private String url;
    private String retString = "";

    public static void main(String[] args) {
        System.out.println();
    }

    /**
     * Constructor
     */
    UrlClass(String url) {
        this.url = url;
    }

    /**
     * @return Returns the raw main.url.
     */
    @Override
    public String getRawUrl() {
        return url;
    }

    /**
     * @return Returns the path of the main.url, without parameter.
     */
    @Override
    public String getPath() {
        if (url != null) {
            if (url.contains("//") && url.contains("?")) {
                String[] withoutHttps = url.split("\\?")[0].split("//");
                retString = withoutHttps[1].split("/", 2)[1];
            } else if (url.contains("//")) {
                retString = url.split("//")[1].split("/", 2)[1];
            } else if (url.contains("?")) {
                retString = url.split("\\?")[0].split("/", 2)[1];
            } else {
                retString = url;
            }
        }

        return retString;
    }

    /**
     * @return Returns a dictionary with the parameter of the main.url. Never returns
     * null.
     */
    @Override
    public Map<String, String> getParameter() {
        Map<String, String> params = new HashMap<>();
        String parameters = url.substring(url.lastIndexOf("?") + 1);

        if (parameters.length() > 1) {
            for (String param : parameters.split("&")) {
                String[] pair = param.split("=");
                params.put(pair[0], pair[1]);
            }
        }
        return params;
    }

    /**
     * @return Returns the number of parameter of the main.url. Returns 0 if there are no parameter.
     */
    @Override
    public int getParameterCount() {

        int count = 0;
        String[] rUrl = url.split("\\?");

        if (rUrl.length >= 2) {
            count = rUrl[1].split("=").length - 1;
        }

        return count;
    }

    /**
     * @return Returns the segments of the main.url path. A segment is divided by '/'
     * chars. Never returns null.
     */
    @Override
    public String[] getSegments() {
        String[] parts = url.split("/", 4);

        String rUrl = parts[parts.length - 1];
        String[] segments = rUrl.split("/");

        if (segments[segments.length - 1].contains("?")) {
            segments = Arrays.copyOf(segments, segments.length - 1);
        }
        return segments;
    }

    /**
     * @return Returns the filename (with extension) of the main.url path. If the main.url
     * contains no filename, a empty string is returned. Never returns
     * null. A filename is present in the main.url, if the last segment
     * contains a name with at least one dot.
     */
    @Override
    public String getFileName() {

        String[] name;
        String ext = url.substring(url.lastIndexOf('/'));

        if (ext.contains(".")) {
            if (ext.contains("?")) {
                String file = ext.substring(0, ext.indexOf("?"));
                name = file.split("/");
            } else {
                name = ext.split("/");
            }

            retString = name[1];
        }

        return retString;
    }

    /**
     * @return Returns the extension of the main.url filename, including the leading
     * dot. If the main.url contains no filename, a empty string is returned.
     * Never returns null.
     */
    @Override
    public String getExtension() {

        if (url.substring(url.lastIndexOf('/')).indexOf(".") > 0 ) {
            String ext = url.substring(url.lastIndexOf('.') + 1);
            retString = ext.contains("?") ? ext.substring(0, ext.indexOf('?')) : ext;
        }

        return retString;
    }

    /**
     * @return Returns the main.url fragment. A fragment is the part after a '#' char
     * at the end of the main.url. If the main.url contains no fragment, a empty
     * string is returned. Never returns null.
     */
    @Override
    public String getFragment() {
        return url.contains("#") ? url.split("#")[1] : "";
    }

}
