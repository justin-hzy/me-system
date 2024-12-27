package com.me.common.enums;

public enum ResultCode {

    /**
     * 错误码分类,长度为6
     * <p>
     * 校验类错误   100*
     * 业务类错误   101*
     * 系统级错误   102*
     * 数据库错误   103*
     * 用户类错误   104*
     * </p>
     */

    // 接口统一验证成功码
    SUCCESS("200", "处理成功!"),
    UNKNOWEXCEPTION("500", "系统未知异常!请核实"),
    ExcelFail("102","导出失败");

    //业务类错误 101

    private String code;
    private String message;

    ResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
