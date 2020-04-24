package org.webworkbench.web.init;

import org.webworkbench.web.config.WebConfig;

/**
 * WEB初始化
 */
public class WebInit {


    public void init(){
        WebConfig webConfig = new WebConfig();
        webConfig.build();
    }


}
