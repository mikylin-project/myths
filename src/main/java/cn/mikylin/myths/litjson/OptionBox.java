package cn.mikylin.myths.litjson;

import cn.mikylin.myths.common.CollectionUtils;
import cn.mikylin.myths.common.MapUtils;
import cn.mikylin.myths.litjson.handler.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * user defined config for jreader and jwriter
 * @author mikylin
 */
public class OptionBox {


    //chars cache
    public char openCurly = '{';
    public char closeCurly = '}';
    public char openSquareBrackets = '[';
    public char closeSquareBrackets = ']';
    public char colon = ':';
    public char comma = ',';
    public char doubleQuotationMark = '"';
    public char singleQuotationMark = '\'';

    //option for jwriter to serialize the json string to use " or '
    public char quotationMarks;

    //blank char[]
    public char[] blankChars = new char[0];

    //ignore chars
    public List<Character> readIgnoreChars;

    //type handlers
    public Map<Class, TypeHandler> typeHandlers;

    private OptionBox(List<Character> ignoreChars,
                     boolean writeDoubleQuotationMarks,
                     Map<Class, TypeHandler> typeHandlers){

        //ignore chars for jwriter
        readIgnoreChars = ignoreChars;

        //quotation marks
        if(writeDoubleQuotationMarks)
            quotationMarks = doubleQuotationMark;
        else
            quotationMarks = singleQuotationMark;

        //type handlers
        this.typeHandlers = typeHandlers;

        //type handlers
        this.typeHandlers = typeHandlers;

    }

    //ignore boolean
    public boolean isIgnoreChar(char c){
        for(Character character : readIgnoreChars)
            if(Objects.equals(character.charValue(),c)) return true;
        return false;
    }







    /**
     * builder for option box
     */
    public static class OptionBoxBuilder{

        private List<Character> ignoreChars;
        private boolean writeDoubleQuotationMarks;
        private List<DateFormat> readDateFormats;
        private Map<Class, TypeHandler> typeHandlerMap;

        private OptionBoxBuilder(){
            //add the ignore chars for jreader
            initIgnoreChars();
            //select the quotation marks
            writeDoubleQuotationMarks = Boolean.TRUE;
            //add the dateformat for jreader
            initReadDateFormats();
            //init the type handlers
            initTypeHandlers();

        }

        public static OptionBoxBuilder builder(){
            return new OptionBoxBuilder();
        }

        private void initIgnoreChars(){
            ignoreChars = CollectionUtils.newArrayList();
            ignoreChars.add('\u0000');
            ignoreChars.add('\n');
            ignoreChars.add(' ');
        }


        private void initReadDateFormats(){
            readDateFormats = CollectionUtils.newArrayList();
            readDateFormats.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            readDateFormats.add(new SimpleDateFormat("yyyy-MM-dd"));
        }


        private void initTypeHandlers(){
            typeHandlerMap = MapUtils.newHashMap();

            //int
            typeHandlerMap.put(Integer.class,new IntegerTypeHandler());

            //float
            typeHandlerMap.put(Float.class,new FloatTypeHandler());

            //long
            typeHandlerMap.put(Long.class,new LongTypeHandler());

            //double
            typeHandlerMap.put(Double.class,new DoubleTypeHandler());

            //date
            DateTypeHandler dateHandler = new DateTypeHandler();
            dateHandler.setReadParseFormats(readDateFormats);
            dateHandler.setWriteDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            typeHandlerMap.put(Date.class,dateHandler);

            //string and object
            StringTypeHandler stringHandler = new StringTypeHandler();
            typeHandlerMap.put(String.class,stringHandler);
            typeHandlerMap.put(Object.class,stringHandler);

        }


        /**
         * build over
         */
        public OptionBox over(){
            return new OptionBox(ignoreChars,
                                writeDoubleQuotationMarks,
                                typeHandlerMap);
        }


        /**
         * set options
         */



        public OptionBoxBuilder writeDateFormat(String format){
            ((DateTypeHandler)typeHandlerMap.get(Date.class)).setWriteDateFormat(new SimpleDateFormat(format));
            return this;
        }

        public OptionBoxBuilder addIgnoreChar(char c){
            ignoreChars.add(c);
            return this;
        }

        public OptionBoxBuilder addIgnoreChars(List<Character> clist){
            ignoreChars.addAll(clist);
            return this;
        }

        public OptionBoxBuilder isQoubleQuotationMarks(boolean bool){
            writeDoubleQuotationMarks = bool;
            return this;
        }

        public OptionBoxBuilder addReadDateFormat(String dateFormat){
            readDateFormats.add(new SimpleDateFormat(dateFormat));
            return this;
        }

        public OptionBoxBuilder addReadDateFormats(List<DateFormat> dateFormatList){
            readDateFormats.addAll(dateFormatList);
            return this;
        }

        public OptionBoxBuilder addTypeHandlers(Map<Class, TypeHandler> handlers){
            handlers.forEach((k,v)-> typeHandlerMap.put(k,v));
            return this;
        }
    }

}
