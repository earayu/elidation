package elidation.io.resource;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by earayu on 2016/7/4.
 */
public interface Resource
{
    String NO_URL = "NO_URL";

    InputStream getInputStream();

    String getContentAsString();

    boolean exists();

    String getLocation();

    URL getURL();

}
