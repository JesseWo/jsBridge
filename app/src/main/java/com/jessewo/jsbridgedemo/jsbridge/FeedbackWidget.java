package com.jessewo.jsbridgedemo.jsbridge;

import android.content.Intent;

import com.jessewo.annotations.JsBridgeWidget;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wangzhx on 2018/3/14.
 */
@JsBridgeWidget(methodName = "feedback")
public class FeedbackWidget implements IJsWidget {

    private static final String TAG = "FeedbackWidget";

    public FeedbackWidget(IPageManager pageManager) {
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
            JSONObject jo = new JSONObject(jString);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
