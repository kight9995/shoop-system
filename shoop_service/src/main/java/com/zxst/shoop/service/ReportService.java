package com.zxst.shoop.service;

import com.zxst.shoop.util.JsonResult;

public interface ReportService {
    JsonResult getCustomerReport();

    JsonResult getOrderReport();

    JsonResult getBusinessReport();
}
