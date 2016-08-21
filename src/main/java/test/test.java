package test;

import elidation.RegulationManager;
import org.junit.Test;

/**
 * Created by earayu on 2016/8/21.
 */
public class test
{

    @Test
    public void test1()
    {
        RegulationManager.validate("limit", "123");
    }

}
