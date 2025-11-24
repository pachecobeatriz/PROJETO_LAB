package listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import model.dao.BancoJTA;

@WebListener
public class StartupListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            System.out.println("Inicializando JTA com Atomikos...");
            BancoJTA.init();
            System.out.println("JTA inicializada com sucesso.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao inicializar Atomikos", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Finalizando contexto.");
    }
}
