package elidation;


import elidation.io.ResourceParser.ValidationReader;
import elidation.io.resource.Resource;
import elidation.io.resourceloader.ClassPathResourceLoader;
import elidation.io.resourceloader.ResourceLoader;
import net.sf.json.JSONObject;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 字符串校验
 * Rule定义了一条规则. 规则名(key)是必须执行的, 可以通过`正则表达式`和`回调函数`来校验key对应的value是否合法
 * Regulations定义了一个规则集合。可以理解为规则的命名空间。默认有一个publicRegulations, 新创建的Regulations会继承
 * publicRegulations。当然你也可以通过newEmptyRegulations来创建一个空的命名空间。
 * Created by Administrator on 2016/8/2.
 */
public final class RegulationManager {

    //公共的规则集合
    private static final Regulations publicRegulations;

    private static final HashMap<String, Regulations> regulationsHashMap;

    private String validationXml;

//    private RegulationManager(){}

    static
    {
        publicRegulations = Regulations.createRegulations("publicRegulations");
        regulationsHashMap = new HashMap<>();
        regulationsHashMap.put("publicRegulations", publicRegulations);
//        initInternal();
    }

    public RegulationManager(){
        validationXml = "validation.xml";
    }

    public RegulationManager(String fileName)
    {
        validationXml = fileName;
        initXml();
    }

    /**
     * 解析xml, 根据配置注册规则和回调函数
     */
    private void initXml(){
        //读取配置文件, 获取xml中的regulations元素
        List<Element> regulationsList = getRegulationsElements();
        for(Element regulationsElem:regulationsList)
        {
            String regulationName = regulationsElem.attributeValue("name");
            if(regulationName==null)
                regulationName = "publicRegulations";
            //生成回调类的实例
            String clazzName = regulationsElem.attributeValue("callings");
            Callings callings = null;
            Class callingClazz = null;
            if(clazzName!=null && !clazzName.trim().equals(""))
            {
                callings = getCallings(clazzName);
                callingClazz = getCallingsClazz(clazzName);
            }
            //生成规则集合
            putIfAbsent(regulationsElem, regulationName);

            Regulations regulations = regulationsHashMap.get(regulationName);

            //添加规则和回调函数
            List<Element> rules = regulationsElem.elements("rule");
            for(Element element:rules)
            {
                try {
                    Rule rule = Rule.createRole(element.elementText("name")).setRegex(element.elementText("regex")).setMsg(element.elementText("msg"));
                    String funcName = element.elementText("call");
                    if(funcName!=null && !funcName.trim().equals(""))
                        rule.setCallAble((ValidateFunction)callingClazz.getMethod(funcName).invoke(callings));
                    regulations.addRule(rule);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private List<Element> getRegulationsElements()
    {
        List<Element> regulationsList;
        try
        {
            ResourceLoader resourceLoader = new ClassPathResourceLoader(validationXml);
            Document document = ValidationReader.loadDocumentFromString(resourceLoader.getResource().getContentAsString());
            Element root = document.getRootElement();
            regulationsList = root.elements("regulations");
        }catch (RuntimeException e)
        {
            if(e.getLocalizedMessage().equals(Resource.NO_URL))
                throw new RuntimeException("找不到文件: ["+validationXml+"]");
            return new ArrayList<>(0);
        }
        return regulationsList;
    }

    private void putIfAbsent(Element regulationsElem, String regulationName)
    {
        if(!regulationsHashMap.containsKey(regulationName)) {
            Regulations newRegulations;
            if(regulationsElem.attributeValue("distinct").equals("true"))
                newRegulations = emptyRules(regulationName);
            else
                newRegulations = newRules(regulationName);
            regulationsHashMap.put(regulationName, newRegulations);
        }
    }

    private Class getCallingsClazz(String clazzName)
    {
        try {
            return Class.forName(clazzName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private Callings getCallings(String clazzName)
    {
        try {
            return (Callings) getCallingsClazz(clazzName).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



//    private static void initInternal()
//    {
//        //RecordController
//        publicRegulations.addRule(Rule.createRole("limit", Validate.maxNNumericText(3)).setMsg("limit超出限制"));
//        publicRegulations.addRule(Rule.createRole("pageIndex", Validate.maxNNumericText(6)).setMsg("pageIndex超出限制"));
//        publicRegulations.addRule(Rule.createRole("opr", Validate.maxNText(30)).setMsg("opr不合法"));// TODO: 2016/8/3
//        publicRegulations.addRule(Rule.createRole("sid", Validate.maxNText(36)).setMsg("sid不符合格式"));
//        publicRegulations.addRule(Rule.createRole("startDate", Validate.isoDateTime()).setMsg("startDate不合法"));
//        publicRegulations.addRule(Rule.createRole("endDate", Validate.isoDateTime()).setMsg("endDate不合法"));
//        publicRegulations.addRule(Rule.createRole("Type", "(1|2|3|4|all)").setMsg("Type不合法"));
//        publicRegulations.addRule(Rule.createRole("Status", "[0-8]?").setMsg("Status不合法"));
//        publicRegulations.addRule(Rule.createRole("Name", ".{0,60}").setMsg("Name不合法"));// TODO: 2016/8/3 更严格一点
//        publicRegulations.addRule(Rule.createRole("order_id", "[0-9]{19}").setMsg("order_id不合法"));
//
//        //BaseController
//        publicRegulations.addRule(Rule.createRole("role", "(operator|audit)").setMsg("role不合法"));
//        publicRegulations.addRule(Rule.createRole("serial_num", Validate.maxNNumericText(20)).setMsg("serial_num不合法"));
//        publicRegulations.addRule(Rule.createRole("account", Validate.maxNText(32)).setMsg("account不合法"));
//        publicRegulations.addRule(Rule.createRole("bank_account", Validate.maxNText(32)).setMsg("bank_account不合法"));
//        publicRegulations.addRule(Rule.createRole("realtime", "(0|1)").setMsg("realtime不合法"));
//        publicRegulations.addRule(Rule.createRole("agreement_id", Validate.maxNNumericText(20)).setMsg("agreement_id不合法"));
//        publicRegulations.addRule(Rule.createRole("bank_account_name", ".{0,60}").setMsg("bank_account_name不合法"));
//        publicRegulations.addRule(Rule.createRole("amount", "\\d*(\\.\\d\\d|\\.\\d)?").setCallAble(amount()).setMsg("amount不合法"));//重要
//        publicRegulations.addRule(Rule.createRole("usage_type", Validate.maxNNumericText(5)).setMsg("usage_type不合法"));
//
//        //bankName
//        publicRegulations.addRule(Rule.createRole("provinceId", Validate.maxNText(5)).setMsg("provinceId不合法"));
//        publicRegulations.addRule(Rule.createRole("cityId", Validate.maxNText(5)).setMsg("cityId不合法"));
//        publicRegulations.addRule(Rule.createRole("bankName", Validate.maxNText(50)).setMsg("bankName不合法"));
//        publicRegulations.addRule(Rule.createRole("bankAccount", Validate.maxNText(32)).setMsg("bankAccount不合法"));
//    }

    private ValidateFunction amount()
    {
        ValidateFunction lamb = s->{
            if(s==null || s.trim().equals(""))
                throw new RuntimeException(s + "不能为空");//非空约束
            Double amount = Double.valueOf(s);
            if(amount > 1_000_000_000_000d) {
                throw new RuntimeException(s + "金额超过最大限制");
            }
        };
        return lamb;
    }

    public Regulations getRegulations(String name)
    {
        return regulationsHashMap.get(name);
    }




    public Regulations publicRules() {
        return publicRegulations;
    }

    /**
     * 从publicRegulations集合继承所有规则
     * @param regulationsName
     * @return
     */
    public Regulations newRules(String regulationsName) {
//        Regulations regulations = Regulations.copyRegulations(regulationsName, publicRegulations);
//        if(regulationsHashMap.containsKey(regulationsName))
//        {
//            throw new IllegalArgumentException("该规则集合已经存在");
//        }
//        regulationsHashMap.put(regulationsName, regulations);
//        return regulations;
        return newRules(regulationsName, "publicRegulations");
    }

    /**
     * 从from集合继承所有规则
     * @param regulationsName
     * @param from
     * @return
     */
    public Regulations newRules(String regulationsName, String from) {
        if(!regulationsHashMap.containsKey(from))
            throw new IllegalArgumentException("规则集合 " + from + " 不存在");
        Regulations regulations = Regulations.copyRegulations(regulationsName, regulationsHashMap.get(from));
        if(regulationsHashMap.containsKey(regulationsName))
        {
            throw new IllegalArgumentException("该规则集合已经存在");
        }
        regulationsHashMap.put(regulationsName, regulations);
        return regulations;
    }

    /**
     * 创建空的规则集合
     * @param regulationsName
     * @return
     */
    public Regulations emptyRules(String regulationsName) {
        Regulations regulations = Regulations.createRegulations(regulationsName);
        if(regulationsHashMap.containsKey(regulationsName))
        {
            throw new IllegalArgumentException("该规则集合已经存在");
        }
        regulationsHashMap.put(regulationsName, regulations);
        return regulations;
    }

    public void validate(String key, String value)
    {
        publicRegulations.validate(key, value);
    }

    public void validate(JSONObject jsonObject)
    {
        publicRegulations.validate(jsonObject);
    }

    public void validate(String jsonStr)
    {
        publicRegulations.validate(JSONObject.fromObject(jsonStr));
    }










    /*****************************************************************************************/


    static String jsonStr = "{\n" +
            "    \"name\":\"earayu\",\n" +
            "    \"year\":\n" +
            "    {\n" +
            "        \"birth\":\"1994\",\n" +
            "        \"now\":2016\n" +
            "    }\n" +
            "}";

    public static void main(String[] args)
    {
        RegulationManager regulationManager = new RegulationManager("validation.xml");
        regulationManager.validate(jsonStr);
    }

}
