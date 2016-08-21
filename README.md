# elidation
### 提供灵活、方便的字符串校验功能



常常要对某个字符串(key)进行校验, 所以写了这样的代码:

* 对于一个key, 对应一条规则。规则可以用正则表达式或者回调函数约束(其他约束待添加, not null etc.)


* 多条规则可以添加进一个集合, 每个集合有一个命名空间。为了方便, 有一个公共的集合叫publicRegulations。如果不指定集合名, 则默认使用公共集合
* 暂时用xml进行配置, 以后可以考虑用代码配置的功能。



```java
使用方便:
RegulationManager regulationManager = new RegulationManager("validation.xml");
//有3个重载的validate方法, 除了key-value形式。还可以校验JSONObject格式的
regulationManager.validate("id", "$%^&*(");
```



配置文件:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<validation>
    <!--regulations没有name的时候默认为publicRegulations-->
    <regulations callings="test.MyCallings">
        <!--rule可以有4个元素 name regex msg call-->
        <rule>
            <name>earayu</name>
            <regex>earayu123</regex>
            <msg>不是earayu123</msg>
        </rule>
        <rule>
            <name>id</name>
            <regex>[0-9a-zA-Z_$]+</regex>
            <call>id</call>
        </rule>
    </regulations>

    <!--distinct=true时生成空的集合, 否则会继承publicRegulations的全部规则-->
    <regulations name="eee" distinct="true">
        <rule>
            <name>eee</name>
            <regex>eeeee</regex>
            <msg>不是afddasasdf</msg>
        </rule>
    </regulations>

</validation>


```

