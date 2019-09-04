package com.lxb.mall.core.goods.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lxb.framework.util.JsonUtil;
import com.lxb.mall.core.goods.dos.DraftGoodsSkuDO;
import com.lxb.mall.core.goods.dos.GoodsSkuDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
* @Author: lixubin
* @Date: 2019-08-28
* @Description: 商品sku
*/
@ApiModel
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GoodsSkuVO extends GoodsSkuDO {

    /**
     *
     */
    private static final long serialVersionUID = -666090547834195127L;

    @ApiModelProperty(name = "spec_list", value = "规格列表", required = false)
    private List<SpecValueVO> specList;

    @ApiModelProperty(name = "goods_transfee_charge", value = "谁承担运费0：买家承担，1：卖家承担", hidden = true)
    private Integer goodsTransfeeCharge;

    @ApiModelProperty(value = "是否被删除 0 删除 1 未删除", hidden = true)
    private Integer disabled;

    @ApiModelProperty(value = "上架状态  0下架 1上架", hidden = true)
    private Integer marketEnable;

    @ApiModelProperty(name = "goods_type", value = "商品类型NORMAL普通POINT积分")
    private String goodsType;

    @ApiModelProperty(value = "最后修改时间", hidden = true)
    private Long lastModify;

    public List<SpecValueVO> getSpecList() {

        if (this.getSpecs() != null) {
            return JsonUtil.jsonToList(this.getSpecs(), SpecValueVO.class);
        }

        return specList;
    }

    public void setSpecList(List<SpecValueVO> specList) {
        this.specList = specList;
    }

    public GoodsSkuVO() {
    }

    public GoodsSkuVO(DraftGoodsSkuDO draftSku) {
        this.setCost(draftSku.getCost());
        this.setPrice(draftSku.getPrice());
        this.setQuantity(draftSku.getQuantity());
        this.setSkuId(draftSku.getDraftSkuId());
        this.setSn(draftSku.getSn());
        this.setWeight(draftSku.getWeight());
        this.setSpecList(JsonUtil.jsonToList(draftSku.getSpecs(), SpecValueVO.class));
    }

    public Integer getGoodsTransfeeCharge() {
        return goodsTransfeeCharge;
    }

    public void setGoodsTransfeeCharge(Integer goodsTransfeeCharge) {
        this.goodsTransfeeCharge = goodsTransfeeCharge;
    }

    public Integer getDisabled() {
        return disabled;
    }

    public void setDisabled(Integer disabled) {
        this.disabled = disabled;
    }

    public Integer getMarketEnable() {
        return marketEnable;
    }

    public void setMarketEnable(Integer marketEnable) {
        this.marketEnable = marketEnable;
    }

    public Long getLastModify() {
        return lastModify;
    }

    public void setLastModify(Long lastModify) {
        this.lastModify = lastModify;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    @Override
    public String toString() {
        return "GoodsSkuVO{" +
                "specList=" + specList +
                ", goodsTransfeeCharge=" + goodsTransfeeCharge +
                ", disabled=" + disabled +
                ", marketEnable=" + marketEnable +
                ", goodsType='" + goodsType + '\'' +
                ", lastModify=" + lastModify +
                "} " + super.toString();
    }
}
