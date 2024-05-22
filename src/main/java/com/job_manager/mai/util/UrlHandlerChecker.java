package com.job_manager.mai.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class UrlHandlerChecker {

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;


    public boolean checkUrl(String url) {
        Map<RequestMappingInfo, HandlerMethod> mappings = requestMappingHandlerMapping.getHandlerMethods();
        boolean isHas = false;
        for (RequestMappingInfo mappingInfo : mappings.keySet()) {
            if (mappingInfo.getPatternsCondition() != null) {
                isHas = mappingInfo.getPatternsCondition().getPatterns().contains(url);
            }
        }
        return isHas;
    }
}