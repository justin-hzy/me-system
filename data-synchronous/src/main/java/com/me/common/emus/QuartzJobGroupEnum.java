package com.me.common.emus;

/**
 * @author : wangsj
 * @version : 1.0
 * @date : 2020/05/27 22:02
 */
public enum QuartzJobGroupEnum {

    /**
     * 代付文件查正分组
     */
    PAYMENT_FILE_VERIFY_GROUP("Payroll_File_Verify_Group", "T日5次查询批量代发文件的批处理结果"),

    BPWMS_JOB_GROUP("BPWMS_JOB_GROUP","金财通定时任务"),

    ;


    private final String groupName;
    private final String description;

    QuartzJobGroupEnum(String groupName, String description) {
        this.groupName = groupName;
        this.description = description;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getDescription() {
        return description;
    }
}
