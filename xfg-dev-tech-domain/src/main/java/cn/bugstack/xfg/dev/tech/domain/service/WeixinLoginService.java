package cn.bugstack.xfg.dev.tech.domain.service;

import cn.bugstack.xfg.dev.tech.domain.adapter.ILoginAdapter;
import cn.bugstack.xfg.dev.tech.types.exception.AppException;
import com.google.common.cache.Cache;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 微信服务
 * @create 2024-02-25 12:00
 */
@Service
public class WeixinLoginService implements ILoginService {

    @Resource
    private ILoginAdapter loginAdapter;
    @Resource
    private Cache<String, String> openidToken;

    @Override
    public String createQrCodeTicket() {
        try{
            return loginAdapter.createQrCodeTicket();
        } catch (Exception e){
            throw new AppException(e.getMessage());
        }
    }

    @Override
    public String checkLogin(String ticket) {
        // 通过 ticket 判断，用户是否登录。如果登录了，会在内存里写入信息。
        return openidToken.getIfPresent(ticket);
    }

}
