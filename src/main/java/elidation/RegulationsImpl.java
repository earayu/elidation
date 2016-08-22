package elidation;

import net.sf.json.JSONObject;

import java.util.HashSet;

/**
 * Created by Administrator on 2016/8/2.
 */
final class RegulationsImpl implements Regulations {


    private String regulationName;

    private final HashSet<Rule> rules;

    public RegulationsImpl(String regulationName){
        this.regulationName = regulationName;
        rules = new HashSet<>();
    }

    public RegulationsImpl(String regulationName, HashSet rules)
    {
        this.regulationName = regulationName;
        this.rules = new HashSet<>(rules);
    }

    public RegulationsImpl(String regulationName, RegulationsImpl regulations)
    {
        this(regulationName, regulations.rules);
    }

    @Override
    public Regulations addRule(Rule rule) {
        rules.add(rule);
        return this;
    }

    @Override
    public boolean containsRole(Rule rule) {
        return rules.contains(rule);
    }

    @Override
    public boolean containsRole(String name) {
        return rules.contains(Rule.createRole(name, ""));
    }

    @Override
    public Rule getRule(String name) {
        for(Rule rule:rules)
        {
            if(rule.getName().equals(name))
                return rule;
        }
        return null;// TODO: 2016/8/2 改成Optional
    }

    @Override
    public void deleteRule(String name) {
        for (Rule rule:rules)
        {
            if(rule.getName().equals(name))
                rules.remove(rule);
        }
    }

    /**
     * 递归校验JSONObject
     * @param jsonObject
     */
    @Override
    public void validate(JSONObject jsonObject) {
        for(Object keyObj:jsonObject.keySet())
        {
            String key = String.valueOf(keyObj);
            if(containsRole(key))
            {
                Object value = jsonObject.get(key);
                if(value instanceof JSONObject)
                    validate((JSONObject)value);
                getRule(key).validate(String.valueOf(value));
            }
        }
    }

    @Override
    public void validate(String key, String value) {
        if(containsRole(key))
        {
            getRule(key).validate(value);
        }
    }


}
