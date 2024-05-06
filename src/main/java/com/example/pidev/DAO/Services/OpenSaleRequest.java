package com.example.pidev.DAO.Services;

public  class OpenSaleRequest {
    private Long saleId;

    public OpenSaleRequest(Long saleId) {
        this.saleId = saleId;
    }

    public Long getSaleId() {
        return saleId;
    }

    public void setSaleId(Long saleId) {
        this.saleId = saleId;
    }
}
