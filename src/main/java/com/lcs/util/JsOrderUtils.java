package com.lcs.util;

import app.yzb.entity.YzbCompany;
import app.yzb.exceptions.GlobleException;
import app.yzb.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@Component
public class JsOrderUtils {


    private static YzbCompanyMapper yzbCompanyMapper;
    private static YzbStatementMapper yzbYzbStatementMapper;
    private static YzbInvoiceMapper yzbInvoiceMapper;
    private static YzbTaskMapper yzbTaskMapper;
    private static YzbIncomeBillMapper incomeBillMapper;
    private static YzbFceWithdrawMapper yzbFceWithdrawMapper;

    @Autowired
    private YzbCompanyMapper companyMapper;
    @Autowired
    private YzbStatementMapper YzbStatementMapper;
    @Autowired
    private YzbTaskMapper taskMapper;
    @Autowired
    private YzbInvoiceMapper invoiceMapper;
    @Autowired
    private YzbIncomeBillMapper yzbIncomeBillMapper;
    @Autowired
    private  YzbFceWithdrawMapper fceWithdrawMapper;

    @PostConstruct
    public void init() {
        yzbCompanyMapper = companyMapper;
        yzbYzbStatementMapper = YzbStatementMapper;
        incomeBillMapper = yzbIncomeBillMapper;
        yzbInvoiceMapper = invoiceMapper;
        yzbTaskMapper = taskMapper;
        yzbFceWithdrawMapper=fceWithdrawMapper;
    }




    /**
     * 生成订单号
     *
     * @param cpyid     公司id
     * @param orderType 订单类型
     * @return
     */
    public static String createOrderNo(Integer cpyid, int orderType) {
        YzbCompany yzbCompany = yzbCompanyMapper.selectByPrimaryKey(cpyid);
        if (yzbCompany == null) throw new GlobleException("公司id无效");
        int serialNumber = 0;
        if (orderType == 1) {
            //结算单
            //查询该公司下当天的订单数量
            serialNumber = yzbYzbStatementMapper.selectOrderSumToday(cpyid);

        } else if (orderType == 2) {
            //充值单
            Map<String, Object> param = new HashMap<>();
            param.put("cpyid", cpyid);
            param.put("day", new Date());
            serialNumber = incomeBillMapper.selectSumByCpyidAndDay(param);

        } else if (orderType == 3) {
            //发票单
            Map<String, Object> param = new HashMap<>();
            param.put("cpyid", cpyid);
            param.put("day", new Date());
            serialNumber = yzbInvoiceMapper.selectSumByCpyidAndDay(param);
        } else if (orderType == 4) {
            //任务单
            Map<String, Object> param = new HashMap<>();
            param.put("cpyid", cpyid);
            param.put("day", new Date());
            serialNumber = yzbTaskMapper.selectSumByDay(param);
        }


        return yzbCompany.getSuoxie() + orderType + getDateString() + getSerialNumber(serialNumber);

    }

    /**
     * 生成提现单号
     */
      public static String createTiXianNo() {
          //提现单
          Map<String, Object> param = new HashMap<>();
          param.put("day", new Date());
          int serialNumber = yzbFceWithdrawMapper.selectSumByDay(param);
          Random random = new Random();
          return getDateString()+getSixSerialNumber(random.nextInt(999998))+getSerialNumber(serialNumber);
      }


    /**
     * 获取六位序列号
     * @param serialNumber
     * @return
     */
    private static String getSixSerialNumber(int serialNumber) {
        serialNumber+=1;
        if (serialNumber>999999) {
            return String.valueOf(serialNumber);
        }else {
            DecimalFormat decimalFormat=new DecimalFormat("000000");
            return decimalFormat.format(serialNumber);
        }
    }


    /**
     *  获取序号
     * @return
     */
    private static String getSerialNumber(Integer serialNumber) {
        serialNumber+=1;
        if (serialNumber>999) {
         return String.valueOf(serialNumber);
        }else {
            DecimalFormat decimalFormat=new DecimalFormat("000");
            return decimalFormat.format(serialNumber);
        }

    }

    /**
     *  8位日期（年月日）
     * @return
     */
    private static String getDateString() {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.format(new Date());
    }




}
