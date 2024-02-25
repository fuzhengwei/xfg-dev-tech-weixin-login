package cn.bugstack.xfg.dev.tech.types.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 通用的枚举值
 * @create 2023-07-22 21:24
 */
public class Constants {

    public final static String SPLIT = ",";

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public enum ResponseCode {
        SUCCESS("0000", "调用成功"),
        UN_ERROR("0001", "调用失败"),
        ILLEGAL_PARAMETER("0002", "非法参数"),
        NO_LOGIN("0002", "未登录"),
        ;

        private String code;
        private String info;

    }

}
