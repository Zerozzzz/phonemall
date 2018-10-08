package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;

public interface AddressService {

    ServerResponse add(Shipping shipping);

    ServerResponse delete(Integer shippingId);

    ServerResponse update(Shipping shipping);
}
