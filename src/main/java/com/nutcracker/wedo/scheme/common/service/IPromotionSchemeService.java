package com.nutcracker.wedo.scheme.common.service;

/**推广方案抽象接口,所有推广方案需要实现该接口并标注SchemeService(schemeType)注解，
 * 以在取得公共方案信息后查询个性方案
 * Created by huh on 2017/3/16.
 */
public interface IPromotionSchemeService<T> {
    /**
     * 新增推广方案
     * @param scheme 推广方案bo
     */
    void insert(T scheme);

    /**
     * 修改推广方案
     * @param scheme 推广方案bo
     */
    void update(T scheme);

    /**
     * 删除推广方案
     * @param schemeId 方案id
     */
    void delete(Long schemeId);

    /**
     * 通过方案id查询
     * @param schemeId 方案id
     */
    T getBySchemeId(Long schemeId);
}

