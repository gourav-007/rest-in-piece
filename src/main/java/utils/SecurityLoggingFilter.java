package utils;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SecurityLoggingFilter implements Filter {
    
    @Override
    public Response filter(FilterableRequestSpecification requestSpec, 
                          FilterableResponseSpecification responseSpec, 
                          FilterContext ctx) {
        
        // Log request with sensitive data masked
        logSecureRequest(requestSpec);
        
        Response response = ctx.next(requestSpec, responseSpec);
        
        // Log response with sensitive data masked
        logSecureResponse(response);
        
        return response;
    }
    
    private void logSecureRequest(FilterableRequestSpecification requestSpec) {
        log.info("Request: {} {}", requestSpec.getMethod(), requestSpec.getURI());
        
        // Log headers (mask authorization)
        requestSpec.getHeaders().forEach(header -> {
            if (header.getName().toLowerCase().contains("authorization") || 
                header.getName().toLowerCase().contains("cookie")) {
                log.debug("Header: {} = {}", header.getName(), SecurityUtils.maskToken(header.getValue()));
            } else {
                log.debug("Header: {} = {}", header.getName(), header.getValue());
            }
        });
        
        // Log body with sensitive data masked
        if (requestSpec.getBody() != null) {
            String body = requestSpec.getBody().toString();
            log.debug("Request Body: {}", SecurityUtils.maskSensitiveData(body));
        }
    }
    
    private void logSecureResponse(Response response) {
        log.info("Response: {} {} ({}ms)", 
                response.getStatusCode(), 
                response.getStatusLine(),
                response.getTime());
        
        // Log response body with sensitive data masked
        String responseBody = response.getBody().asString();
        if (SecurityUtils.containsSensitiveData(responseBody)) {
            log.debug("Response Body: {}", SecurityUtils.maskSensitiveData(responseBody));
        } else {
            log.debug("Response Body: {}", responseBody);
        }
    }
}