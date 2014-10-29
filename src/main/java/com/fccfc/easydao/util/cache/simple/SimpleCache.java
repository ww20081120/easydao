/**************************************************************************************** 
 Copyright © 2003-2012 ZTEsoft Corporation. All rights reserved. Reproduction or       <br>
 transmission in whole or in part, in any form or by any means, electronic, mechanical <br>
 or otherwise, is prohibited without the prior written consent of the copyright owner. <br>
 ****************************************************************************************/
package com.fccfc.easydao.util.cache.simple;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fccfc.easydao.util.cache.CacheException;
import com.fccfc.easydao.util.cache.ICache;

/**
 * <Description> <br>
 * 
 * @author 王伟 <br>
 * @version 1.0 <br>
 * @CreateDate 2014年10月24日 <br>
 * @see com.fccfc.framework.common.cache.simple <br>
 */
public class SimpleCache<T extends Serializable> implements ICache<T> {

    /** cachesMap */
    private Map<String, Map<String, T>> cachesMap;

    public SimpleCache() {
        cachesMap = new ConcurrentHashMap<String, Map<String, T>>();
    }

    /**
     * @see com.fccfc.framework.common.cache.ICache#getNode(java.lang.String)
     */
    public Map<String, T> getNode(String nodeName) throws CacheException {
        return cachesMap.get(nodeName);
    }

    /**
     * @see com.fccfc.framework.common.cache.ICache#putNode(java.lang.String, java.util.Map)
     */
    public void putNode(String nodeName, Map<String, T> node) throws CacheException {
        cachesMap.put(nodeName, node);
    }

    /**
     * @see com.fccfc.framework.common.cache.ICache#removeNode(java.lang.String)
     */
    public boolean removeNode(String nodeName) throws CacheException {
        cachesMap.remove(nodeName);
        return true;
    }

    /**
     * @see com.fccfc.framework.common.cache.ICache#getValue(java.lang.String, java.lang.String)
     */
    public T getValue(String nodeName, String key) throws CacheException {
        Map<String, T> node = getNode(nodeName);
        return node == null ? null : node.get(key);
    }

    /**
     * @see com.fccfc.framework.common.cache.ICache#putValue(java.lang.String, java.lang.String, java.io.Serializable)
     */
    public void putValue(String nodeName, String key, T t) throws CacheException {
        Map<String, T> node = getNode(nodeName);
        if (node == null) {
            node = new ConcurrentHashMap<String, T>();
            putNode(nodeName, node);
        }
        node.put(key, t);
    }

    /**
     * @see com.fccfc.framework.common.cache.ICache#updateValue(java.lang.String, java.lang.String,
     *      java.io.Serializable)
     */
    public void updateValue(String nodeName, String key, T t) throws CacheException {
        putValue(nodeName, key, t);
    }

    /**
     * @see com.fccfc.framework.common.cache.ICache#removeValue(java.lang.String, java.lang.String)
     */
    public T removeValue(String nodeName, String key) throws CacheException {
        Map<String, T> node = getNode(nodeName);
        return node == null ? null : node.remove(key);
    }

    /**
     * @see com.fccfc.framework.common.cache.ICache#clean()
     */
    public void clean() throws CacheException {
        cachesMap.clear();
    }
}
