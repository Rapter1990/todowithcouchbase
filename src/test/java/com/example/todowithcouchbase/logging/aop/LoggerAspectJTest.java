package com.example.todowithcouchbase.logging.aop;

import com.example.todowithcouchbase.auth.exception.UserNotFoundException;
import com.example.todowithcouchbase.base.AbstractBaseServiceTest;
import com.example.todowithcouchbase.logging.entity.LogEntity;
import com.example.todowithcouchbase.logging.service.LogService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LoggerAspectJTest extends AbstractBaseServiceTest {

    @InjectMocks
    private LoggerAspectJ loggerAspectJ;

    @Mock
    private LogService logService;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpServletResponse httpServletResponse;

    @Mock
    private ServletRequestAttributes servletRequestAttributes;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private JoinPoint joinPoint;

    @Mock
    private Signature signature;

    @BeforeEach
    public void setUp() {
        // Initialize mocks and set request attributes
        when(servletRequestAttributes.getRequest()).thenReturn(httpServletRequest);
        when(servletRequestAttributes.getResponse()).thenReturn(httpServletResponse);
        RequestContextHolder.setRequestAttributes(servletRequestAttributes);

        // Mock JoinPoint signature
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getName()).thenReturn("testMethod");
        when(signature.getDeclaringTypeName()).thenReturn("LoggerAspectJ");
        when(signature.getDeclaringType()).thenReturn(LoggerAspectJ.class);
    }

    @Test
    public void testLogAfterThrowing() {

        // Given
        Exception ex = new UserNotFoundException("User not found");

        // When
        when(httpServletRequest.getRequestURL()).thenReturn(new StringBuffer("http://localhost/api/test"));
        when(httpServletRequest.getMethod()).thenReturn("GET");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("testUser");
        SecurityContextHolder.setContext(securityContext);

        // Then
        loggerAspectJ.logAfterThrowing(joinPoint, ex);

        // Verify
        verify(logService, times(1)).saveLogToDatabase(any(LogEntity.class));

    }

    @Test
    public void testLogAfterThrowing_AnonymousUser() {

        // Given
        Exception ex = new UserNotFoundException("User not found");
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ANONYMOUS");
        List<GrantedAuthority> authorities = Collections.singletonList(authority);
        SecurityContextHolder.setContext(securityContext);

        // When
        when(httpServletRequest.getRequestURL()).thenReturn(new StringBuffer("http://localhost/api/test"));
        when(httpServletRequest.getMethod()).thenReturn("GET");
        when(securityContext.getAuthentication()).thenReturn(new AnonymousAuthenticationToken("key", "anonymousUser", authorities));

        // Then
        loggerAspectJ.logAfterThrowing(joinPoint, ex);

        // Verify
        verify(logService, times(1)).saveLogToDatabase(any(LogEntity.class));

    }

    @Test
    public void testLogAfterReturning() throws IOException {

        // When
        when(httpServletRequest.getRequestURL()).thenReturn(new StringBuffer("http://localhost/api/test"));
        when(httpServletRequest.getMethod()).thenReturn("POST");
        when(httpServletResponse.getStatus()).thenReturn(HttpStatus.OK.value());
        when(signature.getName()).thenReturn("testMethod");
        when(joinPoint.getSignature()).thenReturn(signature);

        // Then
        loggerAspectJ.logAfterReturning(joinPoint, "test response");

        // Verify
        verify(logService, times(1)).saveLogToDatabase(any(LogEntity.class));

    }

    @Test
    public void testLogAfterReturning_WithJsonNode() throws IOException {

        // Given
        JsonNode jsonNode = new ObjectMapper().createObjectNode().put("key", "value");

        // When
        when(httpServletRequest.getRequestURL()).thenReturn(new StringBuffer("http://localhost/api/test"));
        when(httpServletRequest.getMethod()).thenReturn("POST");
        when(httpServletResponse.getStatus()).thenReturn(HttpStatus.OK.value());

        // Then
        loggerAspectJ.logAfterReturning(joinPoint, jsonNode);

        // Verify
        verify(logService, times(1)).saveLogToDatabase(any(LogEntity.class));

    }

    @Test
    public void testLogAfterReturning_NoResponseAttributes() throws IOException {

        // Given
        RequestContextHolder.resetRequestAttributes();

        // When
        loggerAspectJ.logAfterReturning(mock(JoinPoint.class), "test response");

        // Then
        verify(logService, never()).saveLogToDatabase(any(LogEntity.class));

    }

}