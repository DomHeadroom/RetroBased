package com.retrobased.market.mappers;

import com.retrobased.market.dtos.SellerDTO;
import com.retrobased.market.entities.Seller;

public class SellerMapper {

    public static Seller toEntity(SellerDTO sellerDTO) {
        Seller seller = new Seller();
        seller.setId(sellerDTO.id());
        seller.setFirstName(sellerDTO.firstName());
        seller.setLastName(sellerDTO.lastName());
        seller.setRegisteredAt(sellerDTO.registeredAt());
        return seller;

    }
}
