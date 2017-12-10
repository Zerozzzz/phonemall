package com.mmall.service.impl;

import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/12/10.
 */
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService{

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
}
