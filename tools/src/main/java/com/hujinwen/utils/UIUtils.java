package com.hujinwen.utils;

import android.view.View;
import android.widget.TextView;

import com.hujinwen.tools.AndroidUI;


/**
 * Created by hu-jinwen on 12/2/20
 * <p>
 * UI相关工具
 */
public class UIUtils {

    /**
     * 获取当前页面目录树
     */
    public static String viewTree() throws Exception {
        return AndroidUI.viewTree();
    }

    public static String findViewText(int id) throws Exception {
        TextView textView = (TextView) AndroidUI.findViewById(id);
        return textView.getText().toString();
    }

    /**
     * 根据id查找view
     */
    public static String findViewById(int id) throws Exception {
        StringBuilder sb = new StringBuilder();
        View viewById = AndroidUI.findViewById(id);
        sb.append("toString -> ").append(viewById).append("\n");
        if (viewById != null) {
            sb.append("class -> ").append(viewById.getClass().getName()).append("\n");
        }
        return sb.toString();
    }

}
