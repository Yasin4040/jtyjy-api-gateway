package com.jtyjy.gateway.controller;


import com.jtyjy.gateway.dto.RouteDTO;
import com.jtyjy.gateway.infrastructure.web.Result;
import com.jtyjy.gateway.service.GatewayRouteService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.server.context.SecurityContextServerWebExchange;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import java.security.Principal;

/**
 * <p>
 * 网关路由表 前端控制器
 * </p>
 *
 * @author zxw
 * @since 2021-09-24
 */
@RestController
@RequestMapping("/route")
public class GatewayRouteController {

    @Autowired
    private GatewayRouteService gatewayRouteService;

    @PostMapping("/addRoute")
    @ApiOperation(value = "添加路由")
    public Result<Void> addRoute(@Validated @RequestBody RouteDTO routeDTO){
        gatewayRouteService.addRoute(routeDTO);
        return Result.ok();
    }

    @PostMapping("/deleteRoute")
    @ApiOperation(value = "删除路由")
    public Result<Void> deleteRoute(Long id){
        gatewayRouteService.deleteRoute(id);
        return Result.ok();
    }

    @PostMapping("/reloadConfig")
    @ApiOperation(value = "刷新配置")
    public Result<Void> reloadConfig(Principal principal, ServerWebExchange exchange){
        ReactiveSecurityContextHolder.getContext();
        gatewayRouteService.reloadConfig();
        return Result.ok();
    }

}

