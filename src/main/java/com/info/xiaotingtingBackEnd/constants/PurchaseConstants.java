package com.info.xiaotingtingBackEnd.constants;

/**
 * Copyright (c) 2017, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2018/4/10 10:34:41
 * Description：采购状态码
 * Email: xiaoting233zhang@126.com
 */
public class PurchaseConstants {
    /**
     * 请假状态 -1收回请求 0 已申请，待审批；1 已审批，通过；2 已审批，不通过
     */
    public static final int STATUS_PURCHASE_RETURN = -1;
    public static final int STATUS_PURCHASE_WAITING = 0;
    public static final int STATUS_PURCHASE_PASS = 1;
    public static final int STATUS_PURCHASE_UNPASS = 2;
}
