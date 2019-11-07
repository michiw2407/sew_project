package interfaces;

import java.util.Map;

public interface Url {

    /**
     * @return Returns the raw main.url.
     */
    String getRawUrl();

    /**
     * @return Returns the path of the main.url, without parameter.
     */
    String getPath();

    /**
     * @return Returns a dictionary with the parameter of the main.url. Never returns
     *         null.
     */
    Map<String, String> getParameter();

    /**
     * @return Returns the number of parameter of the main.url. Returns 0 if there are no parameter.
     */
    int getParameterCount();

    /**
     * @return Returns the segments of the main.url path. A segment is divided by '/'
     *         chars. Never returns null.
     */
    String[] getSegments();

    /**
     * @return Returns the filename (with extension) of the main.url path. If the main.url
     *         contains no filename, a empty string is returned. Never returns
     *         null. A filename is present in the main.url, if the last segment
     *         contains a name with at least one dot.
     */
    String getFileName();

    /**
     * @return Returns the extension of the main.url filename, including the leading
     *         dot. If the main.url contains no filename, a empty string is returned.
     *         Never returns null.
     */
    String getExtension();

    /**
     * @return Returns the main.url fragment. A fragment is the part after a '#' char
     *         at the end of the main.url. If the main.url contains no fragment, a empty
     *         string is returned. Never returns null.
     */
    String getFragment();
}