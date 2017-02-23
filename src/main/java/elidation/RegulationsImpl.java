package elidation;

import elidation.config.Mode;
import net.sf.json.JSONObject;

import java.util.HashSet;

/**
 * Created by Administrator on 2016/8/2.
 */
final class RegulationsImpl implements Regulations {


    private String regulationName;

    private final HashSet<Rule> rules;

    private Mode mode;

    private int size;

    public RegulationsImpl(String regulationName, Mode mode){
        this.regulationName = regulationName;
        rules = new HashSet<>();
        this.mode = mode;
    }

    public RegulationsImpl(String regulationName, HashSet rules, Mode mode)
    {
        this.regulationName = regulationName;
        this.rules = new HashSet<>(rules);
        this.mode = mode;
    }

    public RegulationsImpl(String regulationName, RegulationsImpl regulations, Mode mode)
    {
        this(regulationName, regulations.rules, mode);
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    @Override
    public Regulations addRule(Rule rule) {
        rules.add(rule);
        size++;
        return this;
    }

    @Override
    public boolean containsRule(Rule rule) {
        return rules.contains(rule);
    }

    @Override
    public boolean containsRule(String name) {
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
        size--;
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
            if(containsRule(key))
            {
                Object value = jsonObject.get(key);
                if(value instanceof JSONObject)
                    validate((JSONObject)value);
                getRule(key).validate(String.valueOf(value));
            }else if(mode==Mode.STRICT)
            {
                throw new RuntimeException("不存在key: [" + key + "]");
            }
        }
    }

    @Override
    public void validate(String key, String value) {
        if(containsRule(key))
        {
            getRule(key).validate(value);
        }else if(mode==Mode.STRICT)
        {
            throw new RuntimeException("不存在key: [" + key + "]");
        }
    }

    @Override
    public void validate(String jsonStr) {
        validate(JSONObject.fromObject(jsonStr));
    }


    @Override
    public boolean isEmpty() {
        return size==0;
    }

    @Override
    public String toString() {
        return "RegulationsImpl{" +
                "regulationName='" + regulationName + '\'' +
                ", rules=" + rules +
                '}';
    }
}
