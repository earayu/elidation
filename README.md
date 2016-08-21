# elidation
### 提供灵活、方便的字符串校验功能



常常要对某个字符串(key)进行校验, 所以写了这样的代码:

* 对于一个key, 对应一条规则。规则可以用正则表达式或者回调函数约束(其他约束待添加, not null etc.)


* 多条规则可以添加进一个集合, 每个集合有一个命名空间。为了方便, 有一个公共的集合叫publicRegulations。如果不指定集合名, 则默认使用公共集合
* 暂时用xml进行配置, 以后可以考虑用代码配置的功能。



```java
最基础用法:
RegulationManager regulationManager = new RegulationManager("validation.xml");
//有3个重载的validate方法, 除了key-value形式。还可以校验JSONObject格式的
regulationManager.validate("id", "$%^&*(");




回调函数:
如果想给key为id的字符串指定一个回调函数。参见以下配置文件
1. 先在regulations TAG中指定一个全类名`<regulations callings="test.MyCallings">`。该类必须实现Callings接口
2. 在rule中添加`<call>id</call>`。
3. 在MyCallings类中实现id函数:
`
  public ValidateFunction id()
    {
        ValidateFunction validateFunction =
                (str)->
                {
                    System.out.println("ads");
                    System.out.println(str);
                };
        return validateFunction;
    }
`


命名空间:
除了publicRegulations, 还可以自定义命名空间。
publicRegulations.getRegulations(String name)方法可以获取相应的命名空间。

新创建的命名空间可以是空的, 也可以继承publicRegulations的全部规则。
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

