package org.hang.live.gateway.filter;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.hang.live.common.interfaces.enums.GatewayHeaderEnum;
import org.hang.live.gateway.properties.GatewayApplicationProperties;
import org.hang.live.user.interfaces.IAccountTokenRPC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

import static io.netty.handler.codec.http.cookie.CookieHeaderNames.MAX_AGE;
import static org.springframework.web.cors.CorsConfiguration.ALL;

/**
 * @Author hang
 * @Date: Created in 10:57 2024/8/11
 * @Description
 */
@Component
public class AccountCheckFilter implements GlobalFilter, Ordered {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountCheckFilter.class);

    @DubboReference
    private IAccountTokenRPC accountTokenRPC;
    @Resource
    private GatewayApplicationProperties gatewayApplicationProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //Get request url，check if it is valid，if it is empty then gateway should not pass it.
        ServerHttpRequest request = exchange.getRequest();
        String reqUrl = request.getURI().getPath();
        ServerHttpResponse response = exchange.getResponse();
        if (StringUtils.isEmpty(reqUrl)) {
            return Mono.empty();
        }

        //Add this to avoid CORS
        HttpHeaders headers = response.getHeaders();
        headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, request.getHeaders().getOrigin());
        headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST, GET");
        headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "*");
        headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, ALL);
        headers.add(HttpHeaders.ACCESS_CONTROL_MAX_AGE, MAX_AGE);


        //Check if it is in the whitelist url，if it is in that list，then do not validate token.
        List<String> notCheckUrlList = gatewayApplicationProperties.getNotCheckUrlList();
        for (String notCheckUrl : notCheckUrlList) {
            if (reqUrl.startsWith(notCheckUrl)) {
                LOGGER.info("Do not validate token in whitelist.");
                return chain.filter(exchange);
            }
        }
        //If it is not in whitelist，then check cookie cookie
        List<HttpCookie> httpCookieList = request.getCookies().get("hlivetoken");
        if (CollectionUtils.isEmpty(httpCookieList)) {
            LOGGER.error("Do not find hlivetoken cookie! It is intercepted.");
            return Mono.empty();
        }
        String tokenCookieValue = httpCookieList.get(0).getValue();
        if (StringUtils.isEmpty(tokenCookieValue) || StringUtils.isEmpty(tokenCookieValue.trim())) {
            LOGGER.error("hlivetoken is empty，It is intercepted.");
            return Mono.empty();
        }
        //Use account token rpc to check if token is valid.
        Long userId = accountTokenRPC.getUserIdByToken(tokenCookieValue);
        //If token is invalid, gateway intercept it.
        if (userId == null) {
            LOGGER.error("Token is invalid!");
            return Mono.empty();
        }
        // gateway --(header)--> springboot-web(interceptor-->get header)
        ServerHttpRequest.Builder builder = request.mutate();
        builder.header(GatewayHeaderEnum.USER_LOGIN_ID.getName(), String.valueOf(userId));
        return chain.filter(exchange.mutate().request(builder.build()).build());
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
