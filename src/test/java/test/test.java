package test;

import elidation.RegulationManager;
import org.junit.Test;

/**
 * Created by earayu on 2016/8/21.
 */
public class test
{

    static String jsonStr = "{\n" +
            "    \"name\":\"earayu\",\n" +
            "    \"year\":\n" +
            "    {\n" +
            "        \"birth\":\"1994\",\n" +
            "        \"now\":2016\n" +
            "    }\n" +
            "}";

    @Test
    public void test2()
    {
        RegulationManager regulationManager = new RegulationManager("validation.xml");
        regulationManager.validate(jsonStr);
    }



}
