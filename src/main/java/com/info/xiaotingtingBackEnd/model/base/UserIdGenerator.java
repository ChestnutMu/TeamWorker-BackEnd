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
 * Created by king on 2017/9/22.
 */
public class UserIdGenerator implements Configurable,IdentifierGenerator {

    @Override
    public Serializable generate(SessionImplementor sessionImplementor, Object o) throws HibernateException {
        return EntityUtil.getIdByTimeStampAndRandom();
    }

    @Override
    public void configure(Type type, Properties params, Dialect d) throws MappingException {

    }
}
