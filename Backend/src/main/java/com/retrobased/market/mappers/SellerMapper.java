package com.retrobased.market.mappers;

import com.retrobased.market.dtos.SellerDTO;
import com.retrobased.market.entities.Seller;

public class SellerMapper {

    public static Seller toEntity(SellerDTO sellerDTO) {
        Seller seller = new Seller();
        seller.setSellerName(sellerDTO.sellerName());
        seller.setCompany(sellerDTO.company());
        seller.setPhoneNumber(sellerDTO.phoneNumber());
        seller.setAddressLine1(sellerDTO.addressLine1());
        seller.setAddressLine2(sellerDTO.addressLine2());
        seller.setCountry(sellerDTO.country());
        seller.setCity(sellerDTO.city());
        seller.setNote(sellerDTO.note());

        return seller;
    }
}
