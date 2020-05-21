package com.copote.common.util;

import com.alibaba.fastjson.JSONObject;

import java.rmi.MarshalledObject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 * @create 2020/5/20
 * @Description: map工具类
 * @since 1.0.0
 */
public class MapUtils {

    public static Map<String,Object> getMapParams(String[] names, Object[] values) {
        Map<String,Object> map = new HashMap<>();
        for (int i = 0; i < names.length; i++) {
            map.put(names[i], values[i]);
        }
        return map;
    }
}
