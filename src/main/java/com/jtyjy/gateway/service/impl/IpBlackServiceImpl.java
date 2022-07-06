package com.jtyjy.gateway.service.impl;

import com.jtyjy.basic.common.web.Result;
import com.jtyjy.gateway.constants.StringConstants;
import com.jtyjy.gateway.dto.PageBody;
import com.jtyjy.gateway.service.IpBlackService;
import com.jtyjy.gateway.vo.IpBlackVO;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.jtyjy.gateway.constants.CacheNameConstants.BLACKLIST_IP_KEY;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zxw
 * @since 2022-03-15
 */
@Service
public class IpBlackServiceImpl implements IpBlackService {

    @Autowired
    private StringRedisTemplate redisTemplate;
    public static FastDateFormat ymd = FastDateFormat.getInstance("yyyy-MM-dd");
    private List<IpBlackVO> ipList = null;
    @Override
    public Mono<Result> getIpList() {
        return Mono.just( Result.ok(new PageBody(getIpBlackList())));
    }

    @Override
    public void refreshIpList() {
        List<IpBlackVO> result = new ArrayList<>();
        redisTemplate.keys(BLACKLIST_IP_KEY + StringConstants.COLON + StringConstants.ASTERISK).stream().
                collect(Collectors.toMap(key -> key.substring(key.indexOf(StringConstants.COLON)+1, key.length()),
                        key -> redisTemplate.opsForValue().get(key)))
                .forEach((k,v)->{
                    IpBlackVO vo = new IpBlackVO();
                    vo.setIp(k);
                    vo.setRemark(v);
                    result.add(vo);
                });
        ipList = result;
    }
    @Override
    public Mono<Result> addIp(String ip, String remark) {
        String format = ymd.format(System.currentTimeMillis());
        redisTemplate.opsForValue().set(BLACKLIST_IP_KEY + StringConstants.COLON + ip,   format+ StringConstants.COMMA + remark, 1, TimeUnit.DAYS);
        return Mono.just(Result.ok());
    }
    @Override
    public Mono<Result> delIp(String ip) {
        redisTemplate.delete(BLACKLIST_IP_KEY + StringConstants.COLON + ip);
        return Mono.just(Result.ok());
    }
    @Override
    public List<IpBlackVO>  getIpBlackList(){
        List<IpBlackVO> result = ipList;
        if(result == null) {
            synchronized (this) {
                result = ipList;
                if(result == null) {
                    refreshIpList();
                    result = ipList;
                }
            }
        }
        return result;
    }
}
