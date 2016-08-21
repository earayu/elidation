package elidation;

/**
 * Created by Administrator on 2016/8/2.
 */
public interface Rule {

    static Rule createRole(String name, String regex)
    {
        return new RuleImpl(name, regex);
    }

    static Rule createRole(String name)
    {
        return new RuleImpl(name);
    }

    String getName();

    Rule setName(String name);

    String getMsg();

    Rule setMsg(String msg);

    String getRegex();

    Rule setRegex(String regex);

    String getTypeClazzName();

    Rule setTypeClazzName(String typeClazzName);

    boolean isNullable();

    Rule setNullable(boolean nullable);

    int getMinLength();

    Rule setMinLength(int minLength);

    int getMaxLength();

    Rule setMaxLength(int maxLength);

    void validate(String target);

    Rule setCallAble(ValidateFunction callable);

}
