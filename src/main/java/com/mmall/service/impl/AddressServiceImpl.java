package com.mmall.service.impl;

import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("addressService")
public class AddressServiceImpl implements AddressService {

    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public ServerResponse add(Shipping shipping) {

        Integer shipId = shippingMapper.insert(shipping);
        if (shipId >= 0) {
            return ServerResponse.createByErrorCode(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
        }
        return ServerResponse.createBySuccess("success", shipId);

    }

    @Override
    public ServerResponse delete(Integer shippingId) {
        int result = shippingMapper.deleteByPrimaryKey(shippingId);
        if (result > 0) {
            return ServerResponse.createBySuccess("delete shipping success", ResponseCode.SUCCESS.getCode());
        } else {
            return ServerResponse.createByErrorCode(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDesc());
        }
    }

    @Override
    public ServerResponse update(Shipping shipping) {
        int result = shippingMapper.updateByPrimaryKey(shipping);
        if (result > 0) {
            return ServerResponse.createBySuccess("delete shipping success", ResponseCode.SUCCESS.getCode());
        } else {
            return ServerResponse.createByErrorCode(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDesc());
        }
    }

    @Override
    public ServerResponse select(Integer shippingId) {
        return null;
    }


}
