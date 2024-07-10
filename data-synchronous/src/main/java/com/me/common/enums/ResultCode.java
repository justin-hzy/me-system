package com.me.common.enums;

/**
 * 返回码与返回信息
 *
 * @author lisp
 */
@SuppressWarnings("ALL")
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

    //业务类错误 101
    ACCOUNTQUERYFAIL("103011", "账户信息查询失败"),
    ORGNOMATCHFAIL("103012","撤销机构代码输入错误"),
    SALARYCOMPANYFAIL("103013","查询代发企业数失败"),
    //系统级错误   102*
    UNKNOWEXCEPTION("500", "系统未知异常!"),
    SIGNAGREEMENTFAIL("101001", "商户签约失败"),
    COMPANY_DISABLED_FAIL("101002", "公司停用失败"),
    COMPANY_ENABLED_FAIL("101003", "公司启用失败"),
    COMPANY_CODE_EXIST_NORMAL("101004", "公司启用失败，该组织机构代码号已经在平台存在状态正常的公司"),
    COMPANY_CODE_EXIST_CLOSING("101004", "公司启用失败，该组织机构代码号已经在平台存在正在注销中的公司"),
    ORG_DISABLED_ERROR("101005","该机构下存在启用的机构，不能停用"),
    ORG_DISABLED_UPDATE_EXCEPTION("101006","机构停用更新失败"),
    CUSTOMER_MANAGER_NOT_EXIST("101007", "此客户经理信息不存在，请联系管理员确认"),
    CUSTOMER_CAN_NOT_DISABLE("101008", "该客户经理下有正常使用的企业，请先进行客户经理变更"),
    CUSTOMER_DISABLE_EXCEPTION("101009", "客户经理停用失败"),
    CUSTOMER_ENABLE_EXCEPTION("101010", "客户经理启用失败"),
    CUSTOMER_DELETE_EXCEPTION("101011", "客户经理删除失败"),
    UNKNOWNEXCEPTION("102001", "自定义错误信息"),
    ACCESSDENIED("102002", "访问拒绝,无效的权限"),
    FUNCTIONUNSUPPORTED("102003", "功能暂不支持!"),
    FILEUPLOADERROR("102004", "文件上传失败"),
    FILEEMPTYERROR("102005", "上传文件为空"),
    TOKENCHECKERROR("102006", "登录超时，请重新登录"),
    EXCELDATAERROR("102007", "数据不存在，请重新上传"),
    TOKENNEEDREFRESH("102008", "无效token，待重新刷新"),
    SIGNERROR("102009", "签约异常，请核实后，重新入驻企业"),
    REAPEATREQUEST("102010", "重复请求"),
    INDEXERROR("102011", "查询首页数据异常"),

    //用户类错误   104*
    LOGINMODELERROR("104001", "登录模式错误"),
    USERNOTEXITS("104002", "用户不存在"),
    NAMEORPASSWRRDERROR("104003", "用户名或登录密码错误"),
    VCODEEXPIRE("104004", "验证码超时,请重新获取!"),
    VCODEEEROR("104005", "验证码错误,请重新输入!"),
    USEREXITS("104006", "登录名称已存在"),
    USERREGUNKNOWN("104007", "用户注册失败,结果未知"),
    USERUPDATEERROR("104008", "用户更新失败"),
    USERROlESETERROR("104009", "用户角色设置失败"),
    USERROlNOTFOUNDERROR("104010", "用户角色未设置"),
    ORGNONULL("104011", "机构编号为空"),
    USERPERMISSION("104012", "该用户尚未配置权限"),
    PASSWRRDERROR("104013", "原密码输入错误，请重新输入"),
    USERREMOVEERROR("104014", "用户删除失败"),
    COMPANY_CODE_EXIST("104015", "组织机构代码已存在"),
    COMPANY_EXIST("104016", "该公司已存在"),
    OPERATE_STATUS_ERROR("104017", "操作状态异常"),

    //校验类错误 100*
    EMPLOYEEINFOERROR("100001", "员工信息有误"),
    EMPLOYEENOTEXIST("100002", "员工不存在"),
    EMPLOYEENOWECHAT("100003", "员工尚未绑定微信"),
    EMPLOYEEREPEAT("100004", "存在姓名编码重复的员工，请排查"),
    EMPLOYEXIST("100005", "员工信息已存在"),
    PAGEERROR("100006", "分页条件错误"),
    BATCHNOERROR("100007", "批次号不能为空"),
    QUESTIONIDEMPTYERROR("100008", "问题编号不能为空"),
    REPLYQUESTIONEMPTYERROR("100009", "回复消息不存在"),
    SALARYINFOERROR("100010", "工资表信息有误"),
    SLIPPARAMERROR("100011", "工资条参数不能为空"),
    COMPANYNOTEXIST("100012", "企业信息不存在"),
    DATECONVERSIONERROR("100013", "日期转换异常"),
    SIGNINGINFO("100014", "该公司签约信息尚未维护"),
    SPSERROR("100015", "未找到对应的成员行编码"),
    USERNOTEXIST("100016", "用户不存在"),
    FIELD_TO_LONG("100017", " 字段长度超出限制！"),
    FIELD_IS_EMPTY("100018", " 字段不能为空！"),
    FIELD_VALUE_ERROR("100019", " 字段传值异常！"),
    DNS_IS_NULL("100020", " dns不能为空！"),
    SPSERROR_OF_MORE_THEN_ONE_DNS("100021", "获取成员行编码异常,DNS被重复配置!"),


    //数据库错误 103*
    SAVEBATCHEMPDBERROR("103001", "员工信息导入失败"),
    ADDEMPDBERROR("103002", "添加员工信息失败"),
    UPDATEEMPDBERROR("103003", "更新员工信息失败"),
    DELEMPDBERROR("103004", "删除员工信息失败"),
    ADDSLIPTEMPLATEDBERROR("103005", "添加工资条模板失败"),
    QUERYRESULTNULL("103006", "查询结果为空"),
    COMPANYAREANULL("103007", "入驻企业区域查询结果为空"),
    ADDCOMPANYINFOERROR("103008", "入驻企业信息添加至公司表失败"),
    ADDPAYMENTINFOERROR("103009", "入驻企业信息添加至代发账户表失败"),
    ADDUSERINFOERROR("103010", "入驻企业信息添加至用户表失败"),
    ADDEMPLOYEEINFOERROR("103011", "入驻企业信息添加至员工表失败"),
    ADDSYSROLEERROR("103012", "入驻企业信息添加至角色表失败"),
    ADDUSERROLEERROR("103013", "入驻企业信息添加至用户角色关联表失败"),
    ADDSLIPSETTINGERROR("103014", "入驻企业信息添加至工资条基础设置表失败"),
    QUERYCOMPANYINFOERROR("103015", "该企业信息不存在"),
    QUERYPAYMENTACCTINFOERROR("103016", "该企业信息的代发账户不存在"),
    QUERYORGINFOERROR("103018", "该企业信息的所属机构不存在"),
    QUERYUSERINFOERROR("103019", "该企业信息的用户不存在"),
    QUERYCERTIFICATEINFOERROR("103020", "该企业信息的用户证书不存在"),
    ADDCERTIFICATEINFOERROR("103017", "入驻企业信息添加证书表失败"),
    COMPANYINFOEXIST("103018", "该入驻的企业信息已录入"),
    UPDATECOMPANYSTATUSFAIL("103019", "企业的签约状态和签约编号更新失败"),
    STOP_ROLE_ERROR("103020", "停用角色失败,数据库错误"),
    INSERT_ERROR("103021", "插入数据异常"),
    UPDATE_ERROR("103022", "更新数据异常"),
    SELECTE_ERROR("103023", "查询数据异常"),
    SELECTE_SUPPER("103024", "超管查询失败，有两条以上的超管信息"),
    UPDATEEMPERROR("103025", "变更的超管是审核流程中的一员，不允许该操作，请变更审核流程再操作"),
    ADMINISTRATOR("103026", "输入管理员信息不匹配,或不是管理员"),

    //角色删除错误
    ROLE_IS_ADMIN("100308", "当前角色为超级管理员，不可删除"),
    USER_IN_ROLE("100309", "该角色下有关联用户，请先进行用户角色变更或删除。"),

    //机构错误码106*
    ORGEXITS("103010", "该机构代码已存在，不允许重复添加"),
    ORGADDERROR("103011", "新增机构失败"),
    ORGRIGHTERROR("103021", "无此公司审核权限"),
    ORGUPDATEERROR("103013", "机构更新错误，上级机构不允许为本机构"),
    ORG_LEVEL_ERROR("106005", "机构级别错误，机构级别需低于上级机构级别"),
    GET_NO_ORG_LIST("106006", "获取当前机构及下级机构失败!"),
    FIND_NO_PARSENT_ORG("106007", "新增机构失败，未找到父级机构"),
    GET_ORGID_ERROR("106008", "查询机构错误，请联系管理员"),

    //问题回复类错误
    REPLYADDUNKNOWN("103012", "新增企业问题回复失败"),

    //适配器返回码
    ADAPTOR_RET_SUCCESS("BIZ0000", "处理成功"),
    ADAPTOR_RET_TIMEOUT("BIZ9999", "调用超时"),
    //签约错误码
    SIGNFAILERROR("105001", "签约失败，请核实信息后，重新入驻"),

    //客户经理错误信息
    NO_MANAGER_ERROR("M10001", "无此客户经理"),
    NO_PERMISSIONS_CHANGE("M10002", "无权限修改此客户经理"),
    NO_PERMISSIONS_FOR_ORG("M10003", "无权限指定新机构"),
    NO_MANAGER_NO_ERROR("M20001", "当前经理工号已存在"),
    CHANGE_ORG_ERROR("M30001", "当前客户经理名下存在正在使用企业，不允许修改客户经理所属机构"),
    NO_UPDATE_ERROR("M90001", "更新客户经理信息错误"),

    //IAM接口调用错误
    IAM_TTIMEOUT("300001","调用接口IAM接口超时"),
    IAM_TOKEN_FAIL("300002","获取IAM TOKEN失败"),
    IAM_USER_FAIL("300003","获取IAM用户失败"),
    IAM_CODE_FAIL("300004","code为空，获取code失败"),
    IAM_CLIENT_ID_NULLPOINT("300005","clientId为空"),
    IAM_CLIENT_SECRET_NULLPOINT("300006","clientSecret为空"),
    IAM_URL_NULLPOINT("300007","iamUrl为空"),
    IAM_TOKEN_NULLPOINT("300008","IAM令牌为空")
    ;

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
