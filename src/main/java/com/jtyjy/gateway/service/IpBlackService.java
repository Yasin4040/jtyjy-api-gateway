package com.jtyjy.gateway.service;

import com.jtyjy.gateway.common.web.Result;
import com.jtyjy.gateway.vo.IpBlackVO;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zxw
 * @since 2022-03-15
 */
public interface IpBlackService {

    Mono<Result> getIpList(IpBlackVO blackVO);

    void refreshIpListNoEvent();

    Mono<Result> addIp(String ip, String remark);

    Mono<Result> delIp(String ip);
    void refreshIpList();
    List<IpBlackVO> getIpBlackList();
}
