package elidation;


import elidation.config.Mode;
import net.sf.json.JSONObject;

import java.util.Map;

/**
 * Created by Administrator on 2016/8/2.
 */
public interface Regulations {

    static Regulations createRegulations(String regulationsName, Mode mode)
    {
        return new RegulationsImpl(regulationsName, mode);
    }

    static Regulations copyRegulations(String rugulationsName, Regulations regulations, Mode mode)
    {
        return new RegulationsImpl(rugulationsName, (RegulationsImpl) regulations, mode);
    }

    Regulations addRule(Rule rule);

    boolean containsRule(Rule rule);

    boolean containsRule(String name);

    Rule getRule(String name);

    void deleteRule(String name);

    void validate(JSONObject jsonObject);

    void validate(String key, String value);

    void validate(String jsonStr);

    boolean isEmpty();

}
