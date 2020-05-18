package com.copote.common.util;

import com.copote.common.exception.R;

/**
 * @author Administrator
 * @create 2020/5/14
 * @Description: 快速检验返回是否成功
 * @since 1.0.0
 */
public class RUtil {

    public static boolean checkErro(R r){
        boolean flag = false;
        if("00".equals(r.get("code"))){
            flag = true;
        }
        return flag;
    }
}
