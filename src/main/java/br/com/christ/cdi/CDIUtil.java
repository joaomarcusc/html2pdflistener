package br.com.christ.cdi;

import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class CDIUtil {
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> klass) {
        BeanManager beanManager;
        try {
            beanManager = InitialContext.doLookup("java:comp/BeanManager");
        } catch (NamingException e) {
            return null;
        }
        Set<Bean<?>> beans = beanManager.getBeans(klass);
        Bean<?> bean = beanManager.resolve(beans);
        CreationalContext<?> cc = beanManager.createCreationalContext(bean);
        return (T) beanManager.getReference(bean, klass, cc);
    }
}
