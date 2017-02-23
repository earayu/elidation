package elidation;


import elidation.config.Mode;
import elidation.config.RegulationManagerAutoConfig;
import elidation.io.ResourceParser.ValidationReader;
import elidation.io.resource.Resource;
import elidation.io.resourceloader.ClassPathResourceLoader;
import elidation.io.resourceloader.ResourceLoader;
import net.sf.json.JSONObject;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.*;

import static elidation.config.Mode.SIMPLE;
import static elidation.config.Mode.STRICT;
import static elidation.utils.StringUitls.*;
import static elidation.config.RegulationManagerAutoConfig.*;

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

    private RegulationManagerAutoConfig config;

//    private String validationXml;
    private static final List<String> validationXmlList;


    static
    {
        publicRegulations = Regulations.createRegulations(DEFAULT_PUBLIC_CONFIGURATION_NAME, STRICT);
        regulationsHashMap = new HashMap<>();
        regulationsHashMap.put(DEFAULT_PUBLIC_CONFIGURATION_NAME, publicRegulations);
        validationXmlList = new ArrayList<>();
    }

    public RegulationManager(){
//        validationXml = DEFAULT_XML_CONFIGUATION_FILE;
        validationXmlList.add(DEFAULT_XML_CONFIGUATION_FILE);
    }

    public RegulationManager(String fileName)
    {
//        validationXml = fileName;
        validationXmlList.add(fileName);
        initXml();
    }

    public RegulationManager(String[] fileNames)
    {
        validationXmlList.addAll(Arrays.asList(fileNames));
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
            Mode mode;
            String modeStr = regulationsElem.attributeValue("mode");
            if(modeStr ==null || isEqualsIgnoreCase(modeStr, "strict"))
            {
                mode = STRICT;
            }else if(isEqualsIgnoreCase(modeStr, "simple")) {
                mode = SIMPLE;
            }else
            {
                throw new RuntimeException("unknown mode [" + modeStr + "]");
            }

            if(regulationName==null)
                regulationName = DEFAULT_PUBLIC_CONFIGURATION_NAME;

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
            putIfAbsent(regulationsElem, regulationName, mode);

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
        List<Element> regulationsList = new ArrayList<>();
        for(String validationXml:validationXmlList) {
            try {
                ResourceLoader resourceLoader = new ClassPathResourceLoader(validationXml);
                Document document = ValidationReader.loadDocumentFromString(resourceLoader.getResource().getContentAsString());
                Element root = document.getRootElement();
                regulationsList.addAll(root.elements("regulations"));
            } catch (RuntimeException e) {
                if (e.getLocalizedMessage().equals(Resource.NO_URL))
                    throw new RuntimeException("找不到文件: [" + validationXml + "]");
                return new ArrayList<>(0);
            }
        }
        return regulationsList;
    }

    /**
     * 将规则集合添加进regulationsHashMap
     * @param regulationsElem
     * @param regulationName
     */
    private void putIfAbsent(Element regulationsElem, String regulationName, Mode mode)
    {
        if(!regulationsHashMap.containsKey(regulationName)) {
            Regulations newRegulations;
            if(isEquals(regulationsElem.attributeValue("distinct"), "true"))
                newRegulations = emptyRules(regulationName, mode);
            else
                newRegulations = newRules(regulationName, mode);
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
    public Regulations newRules(String regulationsName, Mode mode) {
        return newRules(regulationsName, DEFAULT_PUBLIC_CONFIGURATION_NAME, mode);
    }

    /**
     * 从from集合继承所有规则
     * @param regulationsName
     * @param from
     * @return
     */
    public final Regulations newRules(String regulationsName, String from, Mode mode) {
        if(!regulationsHashMap.containsKey(from))
            throw new IllegalArgumentException("规则集合 " + from + " 不存在");
        Regulations regulations = Regulations.copyRegulations(regulationsName, regulationsHashMap.get(from), mode);
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
    public final Regulations emptyRules(String regulationsName, Mode mode) {
        Regulations regulations = Regulations.createRegulations(regulationsName, mode);
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










}
