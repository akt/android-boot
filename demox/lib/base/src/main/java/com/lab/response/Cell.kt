package com.lab.response


import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class Cell(
    @Expose
    @SerializedName("adj_cnt") var adjCnt: Int = 0,
    @Expose
    @SerializedName("adj_scnt") var adjScnt: Any? = Any(),
    @Expose
    @SerializedName("adjust_tip") var adjustTip: String = "",
    @Expose
    @SerializedName("adjusted") var adjusted: String = "",
    @Expose
    @SerializedName("apply_cd") var applyCd: String? = "",
    @Expose
    @SerializedName("bond_id") var bondId: String = "",
    @Expose
    @SerializedName("bond_nm") var bondNm: String = "",
    @Expose
    @SerializedName("bond_value") var bondValue: String = "",
    @Expose
    @SerializedName("bond_value2") var bondValue2: String = "",
    @Expose
    @SerializedName("btype") var btype: String = "",
    @Expose
    @SerializedName("convert_amt_ratio") var convertAmtRatio: String = "",
    @Expose
    @SerializedName("convert_cd") var convertCd: String = "",
    @Expose
    @SerializedName("convert_cd_tip") var convertCdTip: String = "",
    @Expose
    @SerializedName("convert_dt") var convertDt: String = "",
    @Expose
    @SerializedName("convert_price") var convertPrice: String = "",
    @Expose
    @SerializedName("convert_price_valid") var convertPriceValid: String = "",
    @Expose
    @SerializedName("convert_price_valid_from") var convertPriceValidFrom: String? = "",
    @Expose
    @SerializedName("convert_value") var convertValue: String = "",
    @Expose
    @SerializedName("curr_iss_amt") var currIssAmt: String = "",
    @Expose
    @SerializedName("dblow") var dblow: String = "",
    @Expose
    @SerializedName("esc_dt") var escDt: Any? = Any(),
    @Expose
    @SerializedName("force_redeem") var forceRedeem: Any? = Any(),
    @Expose
    @SerializedName("force_redeem_price") var forceRedeemPrice: String = "",
    @Expose
    @SerializedName("full_price") var fullPrice: String = "",
    @Expose
    @SerializedName("fund_rt") var fundRt: String = "",
    @Expose
    @SerializedName("guarantor") var guarantor: String? = "",
    @Expose
    @SerializedName("increase_rt") var increaseRt: String = "",
    @Expose
    @SerializedName("issuer_rating_cd") var issuerRatingCd: String = "",
    @Expose
    @SerializedName("last_time") var lastTime: String = "",
    @Expose
    @SerializedName("left_put_year") var leftPutYear: String = "",
    @Expose
    @SerializedName("margin_flg") var marginFlg: String = "",
    @Expose
    @SerializedName("maturity_dt") var maturityDt: String = "",
    @Expose
    @SerializedName("next_put_dt") var nextPutDt: String? = "",
    @Expose
    @SerializedName("noted") var noted: Int = 0,
    @Expose
    @SerializedName("online_offline_ratio") var onlineOfflineRatio: Any? = Any(),
    @Expose
    @SerializedName("option_tip") var optionTip: String = "",
    @Expose
    @SerializedName("orig_iss_amt") var origIssAmt: String = "",
    @Expose
    @SerializedName("owned") var owned: Int = 0,
    @Expose
    @SerializedName("pb") var pb: String = "",
    @Expose
    @SerializedName("pb_flag") var pbFlag: String = "",
    @Expose
    @SerializedName("pre_bond_id") var preBondId: String = "",
    @Expose
    @SerializedName("premium_rt") var premiumRt: String = "",
    @Expose
    @SerializedName("price") var price: String = "",
    @Expose
    @SerializedName("price_tips") var priceTips: String = "",
    @Expose
    @SerializedName("put_convert_price") var putConvertPrice: String = "",
    @Expose
    @SerializedName("put_convert_price_ratio") var putConvertPriceRatio: String? = "",
    @Expose
    @SerializedName("put_count_days") var putCountDays: Int = 0,
    @Expose
    @SerializedName("put_dt") var putDt: Any? = Any(),
    @Expose
    @SerializedName("put_inc_cpn_fl") var putIncCpnFl: String = "",
    @Expose
    @SerializedName("put_notes") var putNotes: Any? = Any(),
    @Expose
    @SerializedName("put_price") var putPrice: String? = "",
    @Expose
    @SerializedName("put_real_days") var putRealDays: Int = 0,
    @Expose
    @SerializedName("put_total_days") var putTotalDays: Int = 0,
    @Expose
    @SerializedName("qflag") var qflag: String = "",
    @Expose
    @SerializedName("qflag2") var qflag2: String = "",
    @Expose
    @SerializedName("qstatus") var qstatus: String = "",
    @Expose
    @SerializedName("rating_cd") var ratingCd: String = "",
    @Expose
    @SerializedName("ration") var ration: Any? = Any(),
    @Expose
    @SerializedName("ration_cd") var rationCd: String? = "",
    @Expose
    @SerializedName("ration_rt") var rationRt: String? = "",
    @Expose
    @SerializedName("real_force_redeem_price") var realForceRedeemPrice: Any? = Any(),
    @Expose
    @SerializedName("redeem_count_days") var redeemCountDays: Int = 0,
    @Expose
    @SerializedName("redeem_dt") var redeemDt: Any? = Any(),
    @Expose
    @SerializedName("redeem_flag") var redeemFlag: String = "",
    @Expose
    @SerializedName("redeem_icon") var redeemIcon: String = "",
    @Expose
    @SerializedName("redeem_inc_cpn_fl") var redeemIncCpnFl: String = "",
    @Expose
    @SerializedName("redeem_price") var redeemPrice: String = "",
    @Expose
    @SerializedName("redeem_price_ratio") var redeemPriceRatio: String? = "",
    @Expose
    @SerializedName("redeem_real_days") var redeemRealDays: Int = 0,
    @Expose
    @SerializedName("redeem_total_days") var redeemTotalDays: Int = 0,
    @Expose
    @SerializedName("ref_yield_info") var refYieldInfo: String = "",
    @Expose
    @SerializedName("repo_cd") var repoCd: Any? = Any(),
    @Expose
    @SerializedName("repo_discount_rt") var repoDiscountRt: String = "",
    @Expose
    @SerializedName("repo_valid") var repoValid: String = "",
    @Expose
    @SerializedName("repo_valid_from") var repoValidFrom: Any? = Any(),
    @Expose
    @SerializedName("repo_valid_to") var repoValidTo: Any? = Any(),
    @Expose
    @SerializedName("sc_notes") var scNotes: Any? = Any(),
    @Expose
    @SerializedName("short_maturity_dt") var shortMaturityDt: String = "",
    @Expose
    @SerializedName("sincrease_rt") var sincreaseRt: String = "",
    @Expose
    @SerializedName("sprice") var sprice: String = "",
    @Expose
    @SerializedName("sqflg") var sqflg: String = "",
    @Expose
    @SerializedName("ssc_dt") var sscDt: Any? = Any(),
    @Expose
    @SerializedName("stock_cd") var stockCd: String = "",
    @Expose
    @SerializedName("stock_id") var stockId: String = "",
    @Expose
    @SerializedName("stock_net_value") var stockNetValue: String = "",
    @Expose
    @SerializedName("stock_nm") var stockNm: String = "",
    @Expose
    @SerializedName("svolume") var svolume: String = "",
    @Expose
    @SerializedName("total_shares") var totalShares: String = "",
    @Expose
    @SerializedName("turnover_rt") var turnoverRt: String = "",
    @Expose
    @SerializedName("volume") var volume: String = "",
    @Expose
    @SerializedName("year_left") var yearLeft: String = "",
    @Expose
    @SerializedName("ytm_rt") var ytmRt: String = "",
    @Expose
    @SerializedName("ytm_rt_tax") var ytmRtTax: String = ""
)
