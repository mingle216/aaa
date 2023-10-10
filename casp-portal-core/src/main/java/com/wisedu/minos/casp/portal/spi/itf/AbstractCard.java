package com.wisedu.minos.casp.portal.spi.itf;

import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.exception.NoLoginException;
import com.wisedu.minos.casp.portal.common.utils.RequestUtil;
import com.wisedu.minos.casp.portal.i18n.I18nInitializer;
import com.wisedu.minos.casp.portal.model.CardAjaxRequest;
import com.wisedu.minos.casp.portal.model.CardConfigReq;
import com.wisedu.minos.casp.portal.model.MinosSearchForPortalRequest;
import com.wisedu.minos.casp.portal.model.UserInfo;
import com.wisedu.minos.casp.portal.model.configcomponent.ComponentContainer;
import com.wisedu.minos.casp.portal.service.CardApiAdapter;
import com.wisedu.minos.casp.portal.service.impl.CardService;
import com.wisedu.minos.casp.portal.service.impl.MinosSearchServiceImpl;
import com.wisedu.minos.casp.portal.service.impl.ServiceApiService;
import com.wisedu.minos.casp.portal.utils.ScriptDefenseFormat;
import com.wisedu.minos.casp.portal.utils.UserUtil;
import com.wisedu.minos.casp.portal.utils.XSSDefenseFormat;
import com.wisedu.minos.casp.portal.utils.beetlfunction.PortalFunction;
import com.wisedu.minos.config.ApplicationContextUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 抽象card类，封装基础模板方法
 *
 * @author kaisir
 */
public abstract class AbstractCard implements ICard {
    private static final Logger logger = LogManager.getLogger(AbstractCard.class);


    private GroupTemplate groupTemplate;

    public GroupTemplate getGroupTemplate() {
        return groupTemplate;
    }

    public void setGroupTemplate(GroupTemplate groupTemplate) {
        this.groupTemplate = groupTemplate;
    }

    @Autowired
    private CardApiAdapter adapter;

    /**
     * 初始化方法，初始化模板引擎
     */
    @Override
    public void initialize(Map<String, Object> param) {
        String cardId = getCardId();
        logger.info("initialize card..{}", cardId);
        try {
            //卡片国际化初始化加载
//            Map<String, Object> langMessagesMap = I18nInitializer.load(param, Global.SpiPluginType.CARD, cardId);
            ApplicationContextUtil.get(CardService.class).updateCardStatus(cardId);
        } catch (Exception e) {
            logger.warn("卡片{}初始化失败", cardId, e);
            throw new RuntimeException("卡片初始化失败", e);
        }
    }

    @Override
    public ComponentContainer getConfig(CardConfigReq cardConfigReq) {
        return cardConfigReq.getComponentContainer();
    }

    protected boolean checkLogin(){
        UserInfo currentUser = ApplicationContextUtil.get(UserUtil.class).getCurrentUser();
        boolean isLogin;
        if (null == currentUser) {
            isLogin = false;
        }else {
            isLogin = true;
        }
        return isLogin;
    }
}
