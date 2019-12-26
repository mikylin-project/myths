package cn.mikylin.myths.common.prop;

import cn.mikylin.myths.common.*;
import cn.mikylin.myths.common.lang.StringUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * properties utils
 *
 * @author mikylin
 * @date 20190817
 */
public final class PropertiesUtils {

    /**
     * load the properties with propFilePath
     *
     * @param propFilePath
     *          could be full path or relative path,
     *          just like '/home/mikylin/test.properties' or 'src/main/resource/test.propertis'
     * @return properties
     */
    public static Properties properties(String propFilePath) {
        return properties(NioLocal.inputStream(propFilePath));
    }

    /**
     * load the properties with classload
     *
     * @param propFilePath
     *          could be 'test.properties' or '/test.properties'
     * @param clz class for get classload
     * @return properties
     */
    public static Properties properties(String propFilePath,Class clz) {

        if(StringUtils.isBlank(propFilePath))
            throw new NullPointerException("file name can not be blank.");
        if(!propFilePath.startsWith(Constants.System.SLASH))
            propFilePath = Constants.System.SLASH + propFilePath;
        if(clz == null)
            clz = PropertiesUtils.class;

        InputStream stream = clz.getResourceAsStream(propFilePath);

        return properties(stream);
    }

    /**
     * load the properties with inputstream
     *
     * @param stream inputstream
     * @return properties
     */
    public static Properties properties(InputStream stream) {
        Properties p = new Properties();
        try{
            p.load(stream);
        }catch (IOException e){
            throw new RuntimeException("load properties exception.");
        }
        return p;
    }

    /**
     * get string value by properties key
     *
     * @param properties properties
     * @param key properties key
     * @return value
     */
    public static String getString(Properties properties,String key) {
        return properties.getProperty(key);
    }

    /**
     * get string value by properties key
     *
     * @param propFilePath properties file path
     * @param key properties key
     * @return value
     */
    public static String getString(String propFilePath,String key) {
        Properties p = properties(propFilePath);
        return getString(p,key);
    }

    /**
     * get string value by properties key
     *
     * @param propFilePath properties file path
     * @param key properties key
     * @param clz class for get classload
     * @return value
     */
    public static String getString(String propFilePath,String key,Class clz) {
        Properties p = properties(propFilePath,clz);
        return getString(p,key);
    }


    /**
     * get int value by properties key
     *
     * @param properties properties
     * @param key properties key
     * @return value
     */
    public static Integer getInt(Properties properties,String key) {
        try{
            return Integer.valueOf(getString(properties,key));
        }catch (Exception e){
            throw new RuntimeException("properties get value exception.");
        }
    }

    /**
     * get double value by properties key
     *
     * @param properties properties
     * @param key properties key
     * @return value
     */
    public static Double getDouble(Properties properties,String key) {
        try{
            return Double.valueOf(getString(properties,key));
        }catch (Exception e){
            throw new RuntimeException("properties get value exception.");
        }
    }



}
