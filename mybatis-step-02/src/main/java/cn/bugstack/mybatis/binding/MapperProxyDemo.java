package cn.bugstack.mybatis.binding;

import cn.bugstack.mybatis.session.SqlSession;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 *
 * 代理里，提供了一个invoke的代理方法
 * @author 小傅哥，微信：fustack
 * @description 映射器代理类
 * @date 2022/3/26
 * @github https://github.com/fuzhengwei
 * @Copyright 公众号：bugstack虫洞栈 | 博客：https://bugstack.cn - 沉淀、分享、成长，让自己和他人都能有所收获！
 */
public class MapperProxyDemo<T> implements InvocationHandler, Serializable {

    private static final long serialVersionUID = -6424540398559729838L;


    private SqlSession sqlSession;
    private final Class<T> mapperInterface;

    public MapperProxyDemo(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public MapperProxyDemo(SqlSession sqlSession, Class<T> mapperInterface) {
        this.sqlSession = sqlSession;
        this.mapperInterface = mapperInterface;
    }


    /**
     * 创建对象方法
     * @param sqlSession
     * @return
     */
    @SuppressWarnings("unchecked")
    public T newInstance(SqlSession sqlSession) {
        final MapperProxyDemo<T> mapperProxy = new MapperProxyDemo<>(sqlSession, mapperInterface);
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface}, mapperProxy);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        } else {
            return sqlSession.selectOne(method.getName(), args);
        }
    }

}
