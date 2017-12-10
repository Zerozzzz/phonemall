package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2017/12/10.
 */
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService{

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;

    public ServerResponse addCategory(String categoryName,Integer parentId){
        if (StringUtils.isBlank(categoryName) || parentId == null) {
            return ServerResponse.createByErrorMessage("商品管理参数未添加");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);
        int resultRow = categoryMapper.insertSelective(category);
        if (resultRow > 0) {
            return ServerResponse.createBySuccessMessage("分类添加成功");
        }
        return ServerResponse.createByErrorMessage("分类添加失败");
    }

    public ServerResponse updateCategoryName(Integer categoryId,String categoryName){
        if (categoryId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("参数不能为空");
        }

        //仅更新需要的信息
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        int resultRow = categoryMapper.updateByPrimaryKeySelective(category);
        if (resultRow > 0) {
            return ServerResponse.createBySuccessMessage("分类名称更新成功");
        }
        return ServerResponse.createByErrorMessage("分类名称更新失败");
    }

    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId){
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if (CollectionUtils.isEmpty(categoryList)) {
            logger.info("该分类没有子分类");
        }
        return ServerResponse.createBySuccess(categoryList);
    }

    public ServerResponse selectCategoryAndChildrenById(Integer categoryId){
        Set<Category> categorySet = new HashSet<>();
        //调用递归，此时返回的Set就包含了所有子节点及孙子节点
        getDeepChildCategory(categorySet,categoryId);

        List<Integer> categoryIdList = Lists.newArrayList();
        if (categoryId != null) {
            for (Category categoryItem : categorySet) {
                categoryIdList.add(categoryItem.getId());
            }
        }

        return ServerResponse.createBySuccess(categoryIdList);

    }
    /**
     * 递归查询子分类，将所有的子分类装入Set中返回
     */
    private Set<Category> getDeepChildCategory(Set<Category> categorySet,Integer categoryId){
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        //将category和所有的子节点添加进Set中，返还Set（Set自动去除重复）
        if (category != null) {
            categorySet.add(category);
        }
        //递归查找子节点，当查不到子分类时，递归结束
        //当查寻集合时，Mybatis不会返回"null",不用进行非空判断，不会出现空指针异常
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for (Category categoryItem : categoryList) {
            getDeepChildCategory(categorySet,categoryItem.getId());
        }
        return categorySet;
    }
}
