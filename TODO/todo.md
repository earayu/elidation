*   现在没有validation.xml配置文件的时候会报错, 需要改掉  DONE
*   让validation.xml配置文件可以改成其他名字 DONE
*   提供注解功能, 而不是实现Callings接口
*   现在没有某个key的时候, 调用validate方法不会提示任何信息。相当于验证成功。可以加个功能, 没有key的时候抛出异常。
    或者有key并且验证成功的话返回true。 没key返回false。有key验证失败抛出异常。
*   递归调用validate来处理JSONObject数据 DONE
*   添加可以继承非publicRegulations集合的功能   DONE
