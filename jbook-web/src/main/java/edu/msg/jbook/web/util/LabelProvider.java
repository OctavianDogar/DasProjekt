package edu.msg.jbook.web.util;


import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Created by dogaro on 11/08/2016.
 */
public class LabelProvider {

    public static String getLabel(final String key, Locale locale){
        ResourceBundle resourceBundle = ResourceBundle.getBundle("jbook",locale);
        try{
            return new String(resourceBundle.getString(key).getBytes(), "utf-8");
        }catch(final MissingResourceException e){
            return '!'+ key + '!';
        }catch (final UnsupportedEncodingException e){
            return resourceBundle.getString(key);
        }
    }

}
