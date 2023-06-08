package com.quick.wolf.config;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/08/17:17
 */
public class BasicServicesInterfaceConfig extends AbstractServiceConfig {
    private static final long serialVersionUID = 3042494010865892685L;

    private Boolean isDefault;

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }
}
