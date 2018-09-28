package com.github.fromi.openidconnect.security.role;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.util.Set;

public class RoleExtenderFactory {
    private static Logger logger = LoggerFactory.getLogger(RoleExtenderFactory.class);
    private static RoleExtender instance;
    private static String implementationClassString;
    private static String roleExtenderImplClassName;

    public static RoleExtender getRoleExtender() {
        if (instance == null) {
            ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
            provider.addIncludeFilter(new AssignableTypeFilter(RoleExtender.class));
            Set<BeanDefinition> components = provider.findCandidateComponents("com.github");
            components.forEach(component -> {
                implementationClassString = component.getBeanClassName();
            });
            try {
                if (implementationClassString == null) {
                    implementationClassString = "com.github.fromi.openidconnect.security.role.RoleExtenderDefaultImpl";
                }
                Class implementation = ClassLoader.getSystemClassLoader().loadClass(implementationClassString);
                instance = (RoleExtender) implementation.newInstance();
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage());
            }
        }

        return instance;
    }
}
