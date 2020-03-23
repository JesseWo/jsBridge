package com.jessewo.jsbridgedemo.jsbridge;

import android.content.Intent;

/**
 * Created by wangzhx on 2020/3/23.
 */
public interface IJsWidget {

    int requestCode();

    void parseResult(int resultCode, Intent data);

    void exec(String jString);
}
