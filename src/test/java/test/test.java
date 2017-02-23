package test;

import elidation.RegulationManager;
import elidation.Regulations;
import elidation.Rule;
import elidation.config.Mode;
import elidation.config.RegulationManagerAutoConfig;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by earayu on 2016/8/21.
 */
public class test
{

    static String jsonStr = "{\n" +
            "    \"name\":\"earayu\",\"cat\":\"cat\",\n" +
            "    \"year\":\n" +
            "    {\n" +
            "        \"birth\":\"1994\",\n" +
            "        \"now\":2016\n" +
            "    }\n" +
            "}";

    @Test
    public void test2()
    {
        RegulationManager regulationManager = new RegulationManager(new String[]{"validation.xml","validation2.xml"});
        regulationManager.getRegulations("shabi").validate("name","dd");

        Regulations regulations = regulationManager.publicRules();
    }

    @Test
    public void test3()
    {
        Mode mode = Mode.STRICT;
        Mode mode2 = Mode.STRICT;
        System.out.println(mode==mode2);
    }





}
