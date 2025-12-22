package org.spring.prophet.config.datasource.mybatis;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;

import java.util.List;

/**
 * 数据plus增强
 **/
public class SqlInjectorPlus extends DefaultSqlInjector {

    /**
     * 获取所有方法, mybatis-plus 增强
     */
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
        return methodList;
    }

}
