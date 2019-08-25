package cn.mikylin.myths.litjson.handler;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class DateTypeHandler implements TypeHandler<Date> {

    //option for jwriter to serialize the json string
    private DateFormat writeDateFormat;

    //option for jreader to unserialize the json string
    private List<DateFormat> readParseFormats;

    public void setReadParseFormats(List<DateFormat> readParseFormats) {
        this.readParseFormats = readParseFormats;
    }

    public void setWriteDateFormat(DateFormat format){
        this.writeDateFormat = format;
    }

    @Override
    public Date read(String value) {
        return dateTrans(value);
    }

    @Override
    public String write(Date date) {
        return writeDateFormat.format(date);
    }


    /**
     * deal the string by date formate
     */
    private Date dateTrans(final String dateStr){
        for(DateFormat dateFormat : readParseFormats){
            try {
                return dateFormat.parse(dateStr);
            } catch (ParseException e) { }
        }
        throw new ClassCastException("the string trans to Date exception");
    }
}
