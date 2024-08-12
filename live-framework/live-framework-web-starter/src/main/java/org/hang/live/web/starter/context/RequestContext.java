package org.hang.live.web.starter.context;


import org.hang.live.web.starter.constants.RequestConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * User request context
 *
 * @Author hang
 * @Date: Created in 08:58 2024/8/11
 * @Description
 */
public class RequestContext {

    private static final ThreadLocal<Map<Object, Object>> resources = new InheritableThreadLocalMap<>();

    //Design a set method
    public static void set(Object key, Object value) {
        if (key == null) {
            throw new IllegalArgumentException("key can not be null");
        }
        if (value == null) {
            resources.get().remove(key);
        }
        resources.get().put(key, value);
    }

    public static Long getUserId() {
        Object userId = get(RequestConstants.HANG_USER_ID);
        return userId == null ? null : (Long) userId;
    }

    public static Object get(Object key) {
        if (key == null) {
            throw new IllegalArgumentException("key can not be null");
        }
        return resources.get().get(key);
    }

    public static void clear() {
        resources.remove();
    }

    //When the child threads inherit parent threads, the thread local variable is also cloned to parent threads with deep copy.
    private static final class InheritableThreadLocalMap<T extends Map<Object, Object>> extends InheritableThreadLocal<Map<Object, Object>> {

        @Override
        protected Map<Object, Object> initialValue() {
            return new HashMap();
        }

        @Override
        protected Map<Object, Object> childValue(Map<Object, Object> parentValue) {
            if (parentValue != null) {
                return (Map<Object, Object>) ((HashMap<Object, Object>) parentValue).clone();
            } else {
                return null;
            }
        }
    }

}
