package com.cmeza.sdgenerator.util;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.apache.commons.lang3.StringUtils;

/**
 * Project SqlHelper.
 * Package com.ymonnier.sql.help.service.utilities.
 * File ClassUtil.java.
 * Created by Ysee on 15/03/2017 - 21:39.
 * www.yseemonnier.com
 * https://github.com/YMonnier
 */
public class ClassUtil {
    
    public static List<Class<?>> getEntityClassesFromPackage(String packageName) throws ClassNotFoundException, IOException, URISyntaxException {
        List<String> classNames = getClassNamesFromPackage(packageName);
        List<Class<?>> classes = new ArrayList<Class<?>>();
        for (String className : classNames) {
            Class<?> cls = Class.forName(packageName + "." + className);
            Annotation[] annotations = cls.getAnnotations();

            for (Annotation annotation : annotations) {
                System.out.println(cls.getCanonicalName() + ": " + annotation.toString());
                if (annotation instanceof javax.persistence.Entity) {
                    classes.add(cls);
                }
            }
        }

        return classes;
    }

    public static ArrayList<String> getClassNamesFromPackage(String packageName) throws IOException, URISyntaxException, ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ArrayList<String> names = new ArrayList<String>();

        packageName = packageName.replace(".", "/");
        URL packageURL = classLoader.getResource(packageName);

        URI uri = new URI(packageURL.toString());
        File folder = new File(uri.getPath());
        File[] files = folder.listFiles();
        for (File file: files) {
            String name = file.getName();
            name = name.substring(0, name.lastIndexOf('.'));  // remove ".class"
            names.add(name);
        }

        return names;
    }
    
    public static Map<String,String> getEntityColumnsFieldByField(Class<?> baseEntity){
    	Map<String,String> columns = new LinkedHashMap<>();
    	for (Field field : baseEntity.getClass().getFields()) {	             
	        if (field.isAnnotationPresent(Column.class)) {
	            String columnName = field.getAnnotation(Column.class).name();
	            columns.put(field.getName(), columnName);
	        }    
    	}  
    	return columns;
    }
    
    public static Map<String,String> getEntityColumnsFieldByGetter(Class<?> baseEntity){
    	Map<String,String> columns = new LinkedHashMap<>();
    	for (Field field : baseEntity.getClass().getFields()) {    	    
	        String getterName = "get" + StringUtils.capitalize(field.getName());
	        for (Method m : baseEntity.getDeclaredMethods()) {
	        	if (m.isAnnotationPresent(Column.class)) {
			         if (m.getName().equals(getterName)) {
			        	 String columnName = m.getAnnotation(Column.class).name();
				         columns.put(field.getName(), columnName);	            
			         }
	        	}
	        }
    	}   
    	return columns;
    }
    
    public static String getEntityId(Object o) {
	   String retval = null;
	   if (o != null) {
	      String id = null;
	      for (Field f : o.getClass().getDeclaredFields()) {
		      if (f.isAnnotationPresent(Id.class)) {
		         String getterName = "get" + StringUtils.capitalize(f.getName());
		         for (Method m : o.getClass().getDeclaredMethods()) {
			         if (m.getName().equals(getterName)) {
			            try {
				            Object idObj = m.invoke(o, null);
				            if (idObj != null) {
				               id = idObj.toString();
				               break;
				            }
			            } catch (IllegalAccessException e) {
			            throw new RuntimeException("Cannot find appropriate accessor for @Id field ");
			            } catch (InvocationTargetException e) {
			            throw new RuntimeException("Exception thrown w/in accessor");
			            }
			         }
		         }
		         break;
		      }
	      }
	      retval = o.toString().split("@")[0] + ":" + id;//fully.qualified.class.name:primarykey
	   }
	   return retval;
	}

	public static List<Field> getFieldColumns(Map<String, String> merge,Class<?> baseEntity) {
		List<Field> fieldsColumns = new ArrayList<>();
		for (Field f : baseEntity.getDeclaredFields()) {
			if(merge.keySet().contains(f.getName())){
				fieldsColumns.add(f);
			}
		}
		return fieldsColumns;
	}
    
    
}
