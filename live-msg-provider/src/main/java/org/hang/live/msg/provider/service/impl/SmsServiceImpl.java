package org.hang.live.msg.provider.service.impl;

import com.cloopen.rest.sdk.BodyType;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.hang.live.framework.redis.starter.key.MsgProviderCacheKeyBuilder;
import org.hang.live.common.interfaces.utils.DESUtils;
import org.hang.live.msg.dto.MsgCheckDTO;
import org.hang.live.msg.enums.MsgSendResultEnum;
import org.hang.live.msg.provider.config.ApplicationProperties;
import org.hang.live.msg.provider.config.SmsTemplateIDEnum;
import org.hang.live.msg.provider.config.ThreadPoolManager;
import org.hang.live.msg.provider.dao.mapper.SmsMapper;
import org.hang.live.msg.provider.dao.po.SmsPO;
import org.hang.live.msg.provider.service.ISmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


/**
 * @Author hang
 * @Description
 */
@Service
public class SmsServiceImpl implements ISmsService {

    private static Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private MsgProviderCacheKeyBuilder msgProviderCacheKeyBuilder;
    @Resource
    private SmsMapper smsMapper;
    @Resource
    private ApplicationProperties applicationProperties;
    @Value("${spring.cloud.nacos.config.namespace}")
    private String namespace;

    @Override
    public MsgSendResultEnum sendLoginCode(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return MsgSendResultEnum.MSG_PARAM_ERROR;
        }
        //Generate 4 bits code within 60s expire date, one phone cannot request duplicate code.
        String codeCacheKey = msgProviderCacheKeyBuilder.buildSmsLoginCodeKey(phone);
        if (redisTemplate.hasKey(codeCacheKey)) {
            logger.warn("This phone sends too many requests，phone is {}", phone);
            return MsgSendResultEnum.SEND_FAIL;
        }
        int code = RandomUtils.nextInt(1000, 9999);
        redisTemplate.opsForValue().set(codeCacheKey, code, 60, TimeUnit.SECONDS);
        //Send Code
        ThreadPoolManager.commonAsyncPool.execute(() -> {
            boolean sendStatus = sendSmsToCCP(phone, code);
            if (sendStatus) {
                insertOne(phone, code);
            }
        });
        //Generate
        return MsgSendResultEnum.SEND_SUCCESS;
    }
    // This is used to check user's login code
    @Override
    public MsgCheckDTO checkLoginCode(String phone, Integer code) {
        //Check Code
        if (StringUtils.isEmpty(phone) || code == null || code < 1000) {
            return new MsgCheckDTO(false, "Parameter Errors!");
        }
        //redis Code
        String codeCacheKey = msgProviderCacheKeyBuilder.buildSmsLoginCodeKey(phone);
        Integer cacheCode = (Integer) redisTemplate.opsForValue().get(codeCacheKey);
        if (cacheCode == null || cacheCode < 1000) {
            return new MsgCheckDTO(false, "Validation Code is Expired!");
        }
        if (cacheCode.equals(code)) {
            redisTemplate.delete(codeCacheKey);
            return new MsgCheckDTO(true, "Validation Success");
        }
        return new MsgCheckDTO(false, "Validation Failed!");
    }

    @Override
    public void insertOne(String phone, Integer code) {
        SmsPO smsPO = new SmsPO();
        smsPO.setPhone(DESUtils.encrypt(phone));
        smsPO.setCode(code);
        smsMapper.insert(smsPO);
    }

    /**
     * Send sms message via CCP server
     *
     * @param phone
     * @param code
     */
    private boolean sendSmsToCCP(String phone, Integer code) {
        logger.info("phone is {},code is {}", phone, code);
        //If in the testing environment, do not send
        if(namespace.contains("test")){
            return true;
        }
        try {
            //app.cloopen.com
            String serverIp = applicationProperties.getSmsServerIp();
            //8883
            String serverPort = String.valueOf(applicationProperties.getPort());
            //2c94811c9035ff9f01913dae97773b35
            String accountSId = applicationProperties.getAccountSId();
            //878689ffdd0b4ee0b274e6ad1ce23e2e
            String accountToken = applicationProperties.getAccountToken();
            //2c94811c9035ff9f01913dae98f03b3c
            String appId = applicationProperties.getAppId();
            CCPRestSmsSDK sdk = new CCPRestSmsSDK();
            sdk.init(serverIp, serverPort);
            sdk.setAccount(accountSId, accountToken);
            sdk.setAppId(appId);
            sdk.setBodyType(BodyType.Type_JSON);
            //13141150122
            String to = applicationProperties.getTestPhone();
            String templateId = SmsTemplateIDEnum.SMS_LOGIN_CODE_TEMPLATE.getTemplateId();
            //Use default message template.
            String[] datas = {String.valueOf(code), "1"};
            String subAppend = "1234";
            String reqId = UUID.randomUUID().toString();
            //Get results from that template
            HashMap<String, Object> result = sdk.sendTemplateSMS(to, templateId, datas, subAppend, reqId);
            if ("000000".equals(result.get("statusCode"))) {
                //If the result is success, print it at consule.
                HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
                Set<String> keySet = data.keySet();
                for (String key : keySet) {
                    Object object = data.get(key);
                    logger.info("key is {},object is {}", key, object);
                }
            } else {
                //Return errors
                logger.error("Error code:{}, Error Message:{}", result.get("statusCode"), result.get("statusMsg"));
                return false;
            }
            return true;
        } catch (Exception e) {
            logger.error("[sendSmsToCCP] error is ", e);
            throw new RuntimeException(e);
        } finally {
            return false;
        }
    }

}