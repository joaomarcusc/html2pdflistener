package br.com.christ.cdi;

import java.io.Serializable;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;

public class CustomScopeExtension implements Extension, Serializable {
    public void addScope(@Observes final BeforeBeanDiscovery event) {
        event.addScope(ViewScopedWithPdf.class, true, false);
    }
    public void registerContext(@Observes final AfterBeanDiscovery event) {
        event.addContext(new CustomScopeContext());
    }
}

