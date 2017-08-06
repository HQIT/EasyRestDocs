# EasyRestDocs (Rest Docs Plus)

easy rest doc for Spring boot. 依赖spring *AOP* 发现 ``RestController`` 注解的 beans, 对其 ``RequestMapping`` 注解的 methods 进行解析, 生成 **markdown** 格式的文档.

## 用法
使用``@EnableRestDocs``注解启用自动生成, 包含``name``/``version``/``usage``三个属性, 生成在文档最前部分总体说明

## 注解

### @RestDoc
可用于单独指定method的usage内容

### @RestMethod
1. **name** 方法名
1. **requestExampleClass** 如果不为``Void.class``(默认值)则实例化并生成pretty json string作为接口请求body的样例
1. **requestExampleText** 如果``requestExampleClass``不存在,使用该属性作为接口请求body的样例
1. **responseExampleClass** 如果不为``Void.class``(默认值)则实例化并生成pretty json string作为接口输出样例
1. **responseExampleText** 如果``responseExampleClass``不存在,使用该属性作为接口输出样例
1. **scopes**
1. **usage** 用途说明

#### 参数
1. 跳过 ``HttpServletRequest``/``HttpServletRequest`` 等
1. 识别``RequestParam``注解的``name``/``required``等属性

### @RestParam
TBD

## 例子

1. 只扫描@RestController注解的类, 如果存在@RequestMapping并设置path, 会叠加到内部methods的uris

``` java
@RestController
@RequestMapping(value = { "/1", "/2" })
public class DemoRestApi {}
```

2. 生成response样例, 定义样例类, 通过``@RestMethod``注解指定

``` java
static public class ResponseExample {
    	int code;
    	String message;
    	
    	public String getMessage() {
			return message;
		}
}

@RestMethod(responseExampleClass = ResponseExample.class)
@RestDoc(usage = "api used to say hello to someone")
@RequestMapping(value = "/hello", method = {RequestMethod.GET})
public Object get(
        @RestParam @RequestParam(name = "who", defaultValue = "") String who
) {
    return "hello," + who;
}
```

3. 生成response样例, 需要``@RequestBody``和``@RestMethod.requestExampleClass``同时具备

## 输出文件
可通过 **${restdocs.base}** 配置, 默认为 **"/docs.md"**

## TODOs:
1. 支持 ``@PathVariable``
1. 支持 *scopes*
1. 更多完善
