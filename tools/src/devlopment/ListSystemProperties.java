package devlopment;

import java.util.Enumeration;
import java.util.Properties;

public class ListSystemProperties {

	public static void main(String[] args) {
		Properties properties = System.getProperties();
		Enumeration<?> propertyNames = properties.propertyNames();
		String property,value;
		while(propertyNames.hasMoreElements()){
			property = (String) propertyNames.nextElement();
			value = properties.getProperty(property);
			System.out.printf("%-20s   %s%n", property,value);
		}//while
	}//main

}//class ListSystemProperties
