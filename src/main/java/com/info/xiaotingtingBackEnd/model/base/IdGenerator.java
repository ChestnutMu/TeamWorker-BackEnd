package com.info.xiaotingtingBackEnd.model.base;
import com.info.xiaotingtingBackEnd.uitl.EntityUtil;
import org.hibernate.HibernateException;

import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;

/**
 * Copyright (c) 2017, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2017/12/11 16:23:06
 * Description：随机生成UserId
 * Email: xiaoting233zhang@126.com
 */

public class IdGenerator implements Configurable,IdentifierGenerator {

    @Override
    public Serializable generate(SessionImplementor sessionImplementor, Object o) throws HibernateException {
        return EntityUtil.getIdByTimeStampAndRandom();
    }

    @Override
    public void configure(Type type, Properties params, Dialect d) throws MappingException {

    }
}
