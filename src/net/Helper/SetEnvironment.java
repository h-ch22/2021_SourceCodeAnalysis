package net.Helper;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;

public class SetEnvironment {
    public static void setEnvironment(Map<String, String> environment) throws ClassNotFoundException, IllegalAccessException, NoSuchFieldException{
        try{
            Class<?> processEnvironment = Class.forName("java.lang.ProcessEnvironment");
            Field environmentField = processEnvironment.getDeclaredField("theEnvironment");
            environmentField.setAccessible(true);

            Map<String, String> env = (Map<String, String>) environmentField.get(null);
            env.putAll(environment);

            Field CaseInsensitiveEnvironmentField = processEnvironment.getDeclaredField("theCaseInsensitiveEnvironment");
            CaseInsensitiveEnvironmentField.setAccessible(true);

            Map<String, String> caseInsensitiveEnv = (Map<String, String>) CaseInsensitiveEnvironmentField.get(null);
            caseInsensitiveEnv.putAll(environment);

        } catch (NoSuchFieldException e) {
            Class[] classes = Collections.class.getDeclaredClasses();
            Map<String, String> env = System.getenv();
            for (Class cl : classes) {
                if ("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
                    Field field = cl.getDeclaredField("m");
                    field.setAccessible(true);
                    Object obj = field.get(env);
                    Map<String, String> map = (Map<String, String>) obj;
                    map.clear();
                    map.putAll(environment);
                }
            }
        }
    }
}
