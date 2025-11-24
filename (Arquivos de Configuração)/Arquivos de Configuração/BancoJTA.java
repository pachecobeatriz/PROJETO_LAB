package model.dao;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.mysql.cj.jdbc.MysqlXADataSource;

import java.sql.Connection;
import java.sql.SQLException;

import javax.transaction.UserTransaction;

public class BancoJTA {

    private static AtomikosDataSourceBean dataSource;
    private static UserTransaction userTransaction;
    private static UserTransactionManager transactionManager;

    public static void init() {
        try {
            //Inicializa o Transaction Manager do Atomikos
            transactionManager = new UserTransactionManager();
            transactionManager.init();
            
            //Cria e configura o UserTransaction com timeout de 5 minutos (por padrão é 1 minuto).
			UserTransactionImp userTransactionTimeOut = new UserTransactionImp();
            userTransactionTimeOut.setTransactionTimeout(300); 
            userTransaction = userTransactionTimeOut;

            //Configura o XADataSource (MySQL)
            MysqlXADataSource mysqlXa = new MysqlXADataSource();
            mysqlXa.setUrl("jdbc:mysql://localhost:3306/dblaboratorio");
            mysqlXa.setUser("root");
            mysqlXa.setPassword("rootroot");

            //Cria o AtomikosDataSourceBean
            dataSource = new AtomikosDataSourceBean();
            dataSource.setUniqueResourceName("SenacJTADS");
            dataSource.setXaDataSource(mysqlXa);
            dataSource.setPoolSize(10);
            dataSource.init();
        } catch (Exception e) {
            System.out.println("Erro ao iniciar Atomikos:");
            e.printStackTrace();
            throw new RuntimeException("Erro ao iniciar Atomikos", e);
        }
    }
    
	public static Connection getConnectionJTA(){
		try {

			Connection conn = dataSource.getConnection();
			return conn; 
		} catch (Exception e) {
			System.out.println("Erro ao obter a Connection.");
			System.out.println("Erro: " + e.getMessage());
			return null;
		}
	}
	
	public static void closeConnectionJTA(Connection conn){
		try {
			if(conn != null){
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("Problema no fechamento da conexão.");
			System.out.println("Erro: " + e.getMessage());
		}	
	}
	
    public static UserTransaction getUserTransaction() {
        return userTransaction;
    }

	public static void rollbackJTA() {
		try {
			if(userTransaction != null){
				userTransaction.rollback();
			}
		} catch (Exception e) {
			System.out.println("Problema na transação - Rollback.");
			System.out.println("Erro: " + e.getMessage());
		}	
	}
    
    
}

