package com.info.xiaotingtingBackEnd.repository.base;

import com.info.xiaotingtingBackEnd.pojo.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2017, Chestnut All rights reserved
 * Author: Chestnut
 * CreateTime：at 2017/12/11 16:21:47
 * Description：BaseRepository的实现类
 * Email: xiaoting233zhang@126.com
 */
public class BaseRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
        implements BaseRepository<T, ID> {

    private final EntityManager entityManager;
    private Class<T> clazz;

    //父类没有不带参数的构造方法，这里手动构造父类
    public BaseRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.entityManager = entityManager;
        this.clazz = domainClass;
    }


    /**
     * 通过EntityManager来完成查询
     */
    @Override
    public List<T> listBySQL(String sql) {
        return entityManager.createNativeQuery(sql, clazz).getResultList();
    }

    /**
     * 通过多条件查询单一记录
     *
     * @param searchCondition
     * @return
     */
    @Override
    public T getUniqueBySearchCondition(SearchCondition searchCondition) {
        Specification<T> specification = generateSpecification(searchCondition);
        return findOne(specification);
    }

    /**
     * 通过多条件查询列表结果
     *
     * @param searchCondition
     * @return
     */
    @Override
    public List<T> getListBySearchCondition(SearchCondition searchCondition) {
        Specification<T> specification = generateSpecification(searchCondition);
        List<SearchBean> sortBeans = searchCondition.getSortBeans();
        List<T> entitys = null;
        Sort sort = null;
        if (sortBeans != null) {
            sort = generateSort(sortBeans);
            entitys = findAll(specification, sort);
        } else
            entitys = findAll(specification);
        return entitys;
    }

    /**
     * 通过多条件查询分页结果
     *
     * @param searchCondition
     * @return
     */
    @Override
    public ApiResponse<List<T>> getPageBySearchCondition(SearchCondition searchCondition) {
        // List<SearchBean> searchBeans=searchCondition.getSearchBeans();
        List<SearchBean> sortBeans = searchCondition.getSortBeans();
        Specification<T> specification = generateSpecification(searchCondition);
        Pageable pageable = null;
        if (sortBeans != null) {
            Sort sort = generateSort(sortBeans);
            pageable = new PageRequest(searchCondition.getPageNum() - 1, searchCondition.getSize(), sort);
        } else {
            pageable = new PageRequest(searchCondition.getPageNum() - 1, searchCondition.getSize());
        }
        Page page = findAll(specification, pageable);
        ApiResponse<List<T>> apiResponse = new ApiResponse<List<T>>(0, "成功");
        apiResponse.setCurrentPage(searchCondition.getPageNum());
        apiResponse.setMaxPage(page.getTotalPages());
        apiResponse.setMaxCount((int) page.getTotalElements());
        apiResponse.setPageSize(page.getSize());
        apiResponse.setData(page.getContent());
        return apiResponse;
        //  return page;
    }

    /**
     * 通过多条件查询记录数
     *
     * @param searchCondition
     * @return
     */
    @Override
    public long countBySearchCondition(SearchCondition searchCondition) {
        Specification<T> specification = generateSpecification(searchCondition);
        return count(specification);
    }

    private Specification<T> generateSpecification(SearchCondition searchCondition) {
        List<SearchBean> searchBeans = searchCondition.getSearchBeans();
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> list = new ArrayList<Predicate>();
                Map<String, List<Object>> inConditions = new HashMap<String, List<Object>>();
                try {
                    for (SearchBean searchBean : searchBeans) {
                        Class fieldType = clazz.getDeclaredField(searchBean.getKey()).getType();
                        switch (searchBean.getOperator()) {
                            case SearchBean.OPERATOR_EQ:
                                list.add(criteriaBuilder.equal(root.get(searchBean.getKey()).as(fieldType), searchBean.getValue()));
                                break;
                            case SearchBean.OPERATOR_LIKE:
                                list.add(criteriaBuilder.like(root.get(searchBean.getKey()).as(fieldType), "%" + searchBean.getValue() + "%"));
                                break;
                            case SearchBean.OPERATOR_IS_NULL:
                                list.add(criteriaBuilder.isNull(root.get(searchBean.getKey()).as(fieldType)));
                                break;
                            case SearchBean.OPERATOR_GT:
                                list.add(criteriaBuilder.greaterThan(root.<Object>get(searchBean.getKey()).as(fieldType), (Comparable) searchBean.getValue()));
                                break;
                            case SearchBean.OPERATOR_NE:
                                list.add(criteriaBuilder.notEqual(root.get(searchBean.getKey()).as(fieldType), searchBean.getValue()));
                                break;
                            case SearchBean.OPERATOR_LT:
                                list.add(criteriaBuilder.lessThan(root.<Object>get(searchBean.getKey()).as(fieldType), (Comparable) searchBean.getValue()));
                                break;
                            case SearchBean.OPERATOR_GE:
                                list.add(criteriaBuilder.greaterThanOrEqualTo(root.<Object>get(searchBean.getKey()).as(fieldType), (Comparable) searchBean.getValue()));
                                break;
                            case SearchBean.OPERATOR_LE:
                                list.add(criteriaBuilder.lessThanOrEqualTo(root.<Object>get(searchBean.getKey()).as(fieldType), (Comparable) searchBean.getValue()));
                                break;
                            case SearchBean.OPERATOR_IN:
                                List<Object> objects = inConditions.get(searchBean.getKey());
                                if (objects == null) {
                                    objects = new ArrayList<>();
                                }
                                objects.add(searchBean.getValue());
                                inConditions.put(searchBean.getKey(), objects);
                                break;
                        }
                    }
                    for (Map.Entry<String, List<Object>> entry : inConditions.entrySet()) {
                        Class fieldType = clazz.getDeclaredField(entry.getKey()).getType();
                        List<Object> objects = entry.getValue();
                        Predicate predicate = root.get(entry.getKey()).as(fieldType).in(objects);
                        list.add(predicate);
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
                Predicate[] p = new Predicate[list.size()];
                if (searchCondition.getJoinType().equals(SearchCondition.JOIN_TYPE_AND))
                    return criteriaBuilder.and(list.toArray(p));
                else if (searchCondition.getJoinType().equals(SearchCondition.JOIN_TYPE_OR))
                    return criteriaBuilder.or(list.toArray(p));
                else
                    return null;
            }

        };
    }

    private Sort generateSort(List<SearchBean> sortBeans) {
        List<Order> orders = new ArrayList<>(sortBeans.size());
        List<SearchBean> sortedBeans = new ArrayList<>(sortBeans.size());
        for (int i = 0; i < sortBeans.size(); i++) {
            sortedBeans.add(new SearchBean());
        }
        for (SearchBean sortBean : sortBeans) {
            sortedBeans.set(sortBean.getPriority(), sortBean);
        }
        for (SearchBean searchBean : sortedBeans) {
            String order = (String) searchBean.getValue();
            switch (order) {
                case "desc":
                    orders.add(new Order(Sort.Direction.DESC, searchBean.getKey()));
                    break;
                case "asc":
                    orders.add(new Order(Sort.Direction.ASC, searchBean.getKey()));
                    break;
            }
        }
        Sort sort = new Sort(orders);
        return sort;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}
