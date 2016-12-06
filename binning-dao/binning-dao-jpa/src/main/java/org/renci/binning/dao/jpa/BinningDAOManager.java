package org.renci.binning.dao.jpa;

import org.renci.binning.dao.BinningDAOBeanService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BinningDAOManager {

    private static BinningDAOManager instance;

    private BinningDAOBeanService daoBean;

    public static BinningDAOManager getInstance() {
        if (instance == null) {
            instance = new BinningDAOManager();
        }
        return instance;
    }

    private BinningDAOManager() {
        super();
        // do not close ctx...will close entity manager
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:/dao-context.xml");
        this.daoBean = ctx.getBean(BinningDAOBeanService.class);
    }

    public BinningDAOBeanService getDAOBean() {
        return daoBean;
    }

    public void setDAOBean(BinningDAOBeanService daoBean) {
        this.daoBean = daoBean;
    }
}
