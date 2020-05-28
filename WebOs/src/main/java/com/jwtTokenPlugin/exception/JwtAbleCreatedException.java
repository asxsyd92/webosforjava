package com.jwtTokenPlugin.exception;

/**
 * 我有故事，你有酒么？
 * JKhaled created by yunqisong@foxmail.com 2017/7/23
 * FOR : IJwtAble对象构建异常
 */
public class JwtAbleCreatedException extends RuntimeException {

    private static String messagePrefix = "构建 IJwtAble 的对象中的 对应的 ";

    private static String messageEnd = "属性不可以为 ";

    /**
     * 组合异常产生原因
     *
     * @param keyWord
     * @param status
     */
    public JwtAbleCreatedException(String keyWord, String status) {
        super(messagePrefix + keyWord + messageEnd + status);
    }
}
