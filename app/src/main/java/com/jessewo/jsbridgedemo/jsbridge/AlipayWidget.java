package com.jessewo.jsbridgedemo.jsbridge;

import android.content.Intent;

import com.jessewo.annotations.JsBridgeWidget;

import org.json.JSONObject;

/**
 * Created by wangzhx on 2018/1/24.
 * 支付宝支付
 */
@JsBridgeWidget(methodName = "alipay")
public class AlipayWidget implements IJsWidget {

    private static final String TAG = "AlipayWidget";

    public AlipayWidget(IPageManager pageManager) {
    }

    @Override
    public int requestCode() {
        return 0;
    }

    @Override
    public void parseResult(int resultCode, Intent data) {

    }

    @Override
    public void exec(String jString) {
        try {
            JSONObject jsonObject = new JSONObject(jString);
            String jsCallback = jsonObject.getString("action");
            String orderString = jsonObject.getString("orderString");


        } catch (Exception e) {
        }
    }
}
