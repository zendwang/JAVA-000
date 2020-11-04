package io.github.kimmking.gateway.router;

import java.util.List;
import java.util.Random;

/**
 * 随机路由
 */
public class HttpEndpointRandomRouter implements HttpEndpointRouter {
    
    public String route(List<String> endpoints) {
        if(null == endpoints || 0 == endpoints.size() ) {
            throw new IllegalArgumentException("endpoints must be not null or empty");
        }
        Random random = new Random();
        int index = random.nextInt(endpoints.size());
        return endpoints.get(index);
    }
    
}
