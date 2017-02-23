package elidation.config;

import static elidation.config.Mode.STRICT;

/**
 * Created by earay on 2017/2/23.
 */
public class RegulationManagerAutoConfig implements AutoConfig {

    public final static String DEFAULT_XML_CONFIGUATION_FILE = "validation.xml";
    public final static String DEFAULT_PUBLIC_CONFIGURATION_NAME = "publicRegulations";

    public Mode mode = STRICT;


}
