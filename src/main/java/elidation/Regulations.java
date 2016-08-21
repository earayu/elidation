package elidation;


import net.sf.json.JSONObject;

/**
 * Created by Administrator on 2016/8/2.
 */
public interface Regulations {

    static Regulations createRegulations(String regulationsName)
    {
        return new RegulationsImpl(regulationsName);
    }

    static Regulations copyRegulations(String rugulationsName, Regulations regulations)
    {
        return new RegulationsImpl(rugulationsName, (RegulationsImpl) regulations);
    }

    Regulations addRule(Rule rule);

    boolean containsRole(Rule rule);

    boolean containsRole(String name);

    Rule getRule(String name);

    void deleteRule(String name);

    void validate(JSONObject jsonObject);

    void validate(String key, String value);

}
