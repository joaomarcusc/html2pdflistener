package br.com.christ.cdi;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;

public class CustomScopeContextHolder implements Serializable {
 
    private static CustomScopeContextHolder INSTANCE;
    private Map<Class, CustomScopeInstance> beans;//we will have only one instance of a type so the key is a class
 
    private CustomScopeContextHolder() {
        beans = Collections.synchronizedMap(new HashMap<Class, CustomScopeInstance>());
    }
 
    public synchronized static CustomScopeContextHolder getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CustomScopeContextHolder();
        }
        return INSTANCE;
    }
 
    public Map<Class, CustomScopeInstance> getBeans() {
        return beans;
    }
 
    public CustomScopeInstance getBean(Class type) {
        return getBeans().get(type);
    }
 
    public void putBean(CustomScopeInstance customInstance) {
        getBeans().put(customInstance.bean.getBeanClass(), customInstance);
    }
 
    void destroyBean(CustomScopeInstance customScopeInstance) {
        getBeans().remove(customScopeInstance.bean.getBeanClass());
        customScopeInstance.bean.destroy(customScopeInstance.instance, customScopeInstance.ctx);
    }
 
    /**
* wrap necessary properties so we can destroy the bean later:
*
* @see
* CustomScopeContextHolder#destroyBean(CustomScopeContextHolder.CustomScopeInstance)
*/
    public static class CustomScopeInstance<T> {
 
        Bean<T> bean;
        CreationalContext<T> ctx;
        T instance;
    }
}