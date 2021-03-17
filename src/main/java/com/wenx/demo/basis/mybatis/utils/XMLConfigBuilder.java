package com.wenx.demo.basis.mybatis.utils;

import com.wenx.demo.basis.mybatis.annotations.Select;
import com.wenx.demo.basis.mybatis.session.Configuration;
import com.wenx.demo.basis.mybatis.session.MappedStatement;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 解析XML配置文件
 *
 * @author Wenx
 * @date 2021/3/17
 */
public class XMLConfigBuilder {
    private final InputStream inputStream;

    public XMLConfigBuilder(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    /**
     * 获得Configuration对象
     *
     * @return
     */
    public Configuration parse() {
        return parsingConfiguration(inputStream);
    }

    /**
     * 解析XML配置文件，将配置信息填充到Configuration中
     * 使用的技术：dom4j+xpath
     *
     * @param inputStream 配置文件字节输入流
     * @return 封装数据库连接信息和映射文件信息的Configuration对象
     */
    public static Configuration parsingConfiguration(InputStream inputStream) {
        try {
            // 定义封装数据库连接信息和映射文件信息的配置对象
            Configuration config = new Configuration();

            // 开始解析XML
            // 1.获取SAXReader对象
            SAXReader reader = new SAXReader();
            // 2.根据字节输入流获取Document对象
            Document document = reader.read(inputStream);
            // 3.获取根节点
            Element root = document.getRootElement();
            // 4.获取数据库配置节点，使用xpath中选择指定节点的方式，获取所有property节点
            List<Element> propertyElements = root.selectNodes("//property");
            // 5.遍历节点
            for (Element propertyElement : propertyElements) {
                // 取出节点的name属性的值，判断是否为数据库连接信息
                String name = propertyElement.attributeValue("name");
                if ("driver".equals(name)) {
                    // 表示驱动，获取property标签value属性的值
                    String driver = propertyElement.attributeValue("value");
                    config.setDriver(driver);
                }
                if ("url".equals(name)) {
                    // 表示连接字符串，获取property标签value属性的值
                    String url = propertyElement.attributeValue("value");
                    config.setUrl(url);
                }
                if ("username".equals(name)) {
                    // 表示用户名，获取property标签value属性的值
                    String username = propertyElement.attributeValue("value");
                    config.setUsername(username);
                }
                if ("password".equals(name)) {
                    // 表示密码，获取property标签value属性的值
                    String password = propertyElement.attributeValue("value");
                    config.setPassword(password);
                }
            }
            // 6.获取映射文件信息，取出mappers中的所有mapper标签，判断他们使用了resource还是class属性
            List<Element> mapperElements = root.selectNodes("//mappers/mapper");
            // 7.遍历集合
            for (Element mapperElement : mapperElements) {
                // 判断mapperElement使用的是哪个属性
                Attribute attribute = mapperElement.attribute("resource");
                if (attribute != null) {
                    // 表示有resource属性，使用的是XML，取出属性的值
                    String mapperPath = attribute.getValue();
                    // 把映射配置文件的内容获取出来，封装成一个map
                    Map<String, MappedStatement> mappers = parsingXML(mapperPath);
                    // 给configuration中的mappers赋值
                    config.setMappers(mappers);
                } else {
                    // 表示没有resource属性，使用的是注解，获取class属性的值
                    String daoClassPath = mapperElement.attributeValue("class");
                    // 根据daoClassPath获取封装的必要信息
                    Map<String, MappedStatement> mappers = parsingAnnotation(daoClassPath);
                    // 给configuration中的mappers赋值
                    config.setMappers(mappers);
                }
            }

            // 返回Configuration
            return config;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 解析XML，并且封装到Map中
     *
     * @param mapperPath 映射配置文件的位置
     * @return map中包含了获取的唯一标识（key是由dao的全限定类名和方法名组成）
     * 以及执行所需的必要信息（value是一个MappedStatement对象，里面存放的是执行的SQL语句和要封装的实体类全限定类名）
     * @throws IOException
     */
    private static Map<String, MappedStatement> parsingXML(String mapperPath) throws IOException {
        InputStream in = null;
        try {
            // 定义返回值对象
            Map<String, MappedStatement> mappers = new HashMap<String, MappedStatement>();

            // 开始解析XML
            // 1.根据路径获取字节输入流
            in = Resources.getResourceAsStream(mapperPath);
            // 2.根据字节输入流获取Document对象
            SAXReader reader = new SAXReader();
            Document document = reader.read(in);
            // 3.获取根节点
            Element root = document.getRootElement();
            // 4.获取根节点的namespace属性的值，作为map中key的前半部分
            String namespace = root.attributeValue("namespace");
            // 5.获取所有的select节点
            List<Element> selectElements = root.selectNodes("//select");
            // 6.遍历select节点集合
            for (Element selectElement : selectElements) {
                // 取出id属性的值，作为map中key的后半部分
                String id = selectElement.attributeValue("id");
                // 取出resultType属性的值，作为map中value的一部分
                String resultType = selectElement.attributeValue("resultType");
                // 取出文本内容，作为map中value的一部分
                String sqlStatement = selectElement.getText();
                // 创建Key
                String key = namespace + "." + id;
                // 创建Value
                MappedStatement mapper = new MappedStatement();
                mapper.setSqlStatement(sqlStatement);
                mapper.setResultType(resultType);
                // 把key和value存入mappers中
                mappers.put(key, mapper);
            }

            return mappers;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            in.close();
        }
    }

    /**
     * 解析dao中所有被select注解标注的方法，并且封装到Map中
     * 根据方法名称和类名，以及方法上注解value属性的值，作为MappedStatement的必要信息
     *
     * @param daoClassPath dao类文件的位置
     * @return map中包含了获取的唯一标识（key是由dao的全限定类名和方法名组成）
     * 以及执行所需的必要信息（value是一个MappedStatement对象，里面存放的是执行的SQL语句和要封装的实体类全限定类名）
     * @throws Exception
     */
    private static Map<String, MappedStatement> parsingAnnotation(String daoClassPath) throws Exception {
        // 定义返回值对象
        Map<String, MappedStatement> mappers = new HashMap<String, MappedStatement>();

        // 1.得到dao接口的字节码对象
        Class daoClass = Class.forName(daoClassPath);
        // 2.得到dao接口中的方法数组
        Method[] methods = daoClass.getMethods();
        // 3.遍历Method数组
        for (Method method : methods) {
            // 取出每一个方法，判断是否有select注解
            boolean isAnnotated = method.isAnnotationPresent(Select.class);
            if (isAnnotated) {
                // 创建MappedStatement对象
                MappedStatement mapper = new MappedStatement();
                // 取出注解的value属性值
                Select selectAnnotation = method.getAnnotation(Select.class);
                String sqlStatement = selectAnnotation.value();
                mapper.setSqlStatement(sqlStatement);
                // 获取当前方法的返回值，还要求必须带有泛型信息 List<T>
                Type type = method.getGenericReturnType();
                // 判断type是不是参数化的类型
                if (type instanceof ParameterizedType) {
                    // 强转
                    ParameterizedType ptype = (ParameterizedType) type;
                    // 得到参数化类型中的实际类型参数
                    Type[] types = ptype.getActualTypeArguments();
                    // 取出第一个
                    Class domainClass = (Class) types[0];
                    // 获取domainClass的类名
                    String resultType = domainClass.getName();
                    // 给MappedStatement赋值
                    mapper.setResultType(resultType);
                }
                // 组装key的信息
                // 获取方法的名称
                String methodName = method.getName();
                String className = method.getDeclaringClass().getName();
                String key = className + "." + methodName;
                // 给map赋值
                mappers.put(key, mapper);
            }
        }
        return mappers;
    }
}
