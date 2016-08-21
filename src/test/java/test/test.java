package test;

import elidation.RegulationManager;
import org.junit.Test;

/**
 * Created by earayu on 2016/8/21.
 */
public class test
{

    @Test
    public void test2()
    {
        RegulationManager regulationManager = new RegulationManager("validation.xml");
        regulationManager.getRegulations("eee").validate("id", "$%^&*(");
    }



}
