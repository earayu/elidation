package elidation;

import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/8/2.
 */
final class RuleImpl implements Rule {

    private String name;

    private String msg;

    private String regex;

    //规定全类名
    private String typeClazzName;

    private boolean nullable;

    private int minLength;

    private int maxLength;

    private ValidateFunction callable;

    public RuleImpl(){}

    public RuleImpl(String name)
    {
        if(name==null || name.equals(""))
            throw new IllegalArgumentException("规则名不能为空");
        this.name = name;
    }

    public RuleImpl(String name, String regex)
    {
        if(name==null || name.equals(""))
            throw new IllegalArgumentException("规则名不能为空");
        this.name = name;
        this.regex = regex;
    }

    public String getName() {
        return name;
    }

    public Rule setName(String name) {
        if(name==null || name.equals(""))
            throw new IllegalArgumentException("规则名不能为空");
        this.name = name;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public Rule setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public String getRegex() {
        return regex;
    }

    public Rule setRegex(String regex) {
        this.regex = regex;
        return this;
    }

    public Rule setCallAble(ValidateFunction callable)
    {
        this.callable = callable;
        return this;
    }

    public String getTypeClazzName() {
        return typeClazzName;
    }

    public Rule setTypeClazzName(String typeClazzName) {
        this.typeClazzName = typeClazzName;
        return this;
    }

    public boolean isNullable() {
        return nullable;
    }

    public Rule setNullable(boolean nullable) {
        this.nullable = nullable;
        return this;
    }

    public int getMinLength() {
        return minLength;
    }

    public Rule setMinLength(int minLength) {
        this.minLength = minLength;
        return this;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public Rule setMaxLength(int maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    private void validateRegex(String target)
    {
        if(regex==null)
            return;

        if(!Pattern.matches(regex, target))
        {
            if (msg == null)
            {
                msg = name + "不合法";
            }
            throw new IllegalArgumentException(msg);
        }
    }

    private void validateCallAble(String target)
    {
        if(this.callable!=null)
        {
            try {
                callable.function(target);
            } catch (Exception e) {
                if (msg == null)
                {
                    msg = name + "不合法";
                }
                throw new IllegalArgumentException(msg);
            }
        }
    }


    @Override
    public void validate(String target) {
        if(typeClazzName!=null)
        {
            if(!target.getClass().getName().equals(typeClazzName))
                throw new IllegalArgumentException(target + "的类型与" + typeClazzName + "不匹配");
        }
        // TODO: 2016/8/3 minLength maxLength nullable...
        validateRegex(target);
        validateCallAble(target);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RuleImpl role = (RuleImpl) o;

        return name.equalsIgnoreCase(role.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
