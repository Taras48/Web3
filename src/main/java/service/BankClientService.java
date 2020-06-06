package service;

import dao.BankClientDAO;
import model.BankClient;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class BankClientService {

    public BankClientService() {
    }

    public BankClient getClientByName(String name) {
        BankClient bankClient = null;
        try {
            bankClient = getBankClientDAO().getClientByName(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bankClient;
    }

    public List<BankClient> getAllClient() throws SQLException {
        return getBankClientDAO().getAllBankClient();
    }

    public boolean addClient(BankClient client) {
        boolean res = false;
        try {
            if (!getBankClientDAO().isNameInDataBase(client.getName())) {
                getBankClientDAO().addClient(client);
                res = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public boolean sendMoneyToClient(BankClient sender, String name, Long value) {
        BankClientDAO dao = getBankClientDAO();
        boolean result = false;
        try {
            if (dao.validateClient(sender.getName(), sender.getPassword()) && dao.isNameInDataBase(name) && dao.isClientHasSum(sender.getName(), value)) {
                dao.updateClientsMoney(sender.getName(), sender.getPassword(), sender.getMoney() - value);
                dao.updateClientsMoney(name, getClientByName(name).getPassword(), getClientByName(name).getMoney() + value);
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void cleanUp() {
        BankClientDAO dao = getBankClientDAO();
        try {
            dao.dropTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTable() {
        BankClientDAO dao = getBankClientDAO();
        try {
            dao.createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Connection getMysqlConnection() {
        try {
            DriverManager.registerDriver((Driver) Class.forName("com.mysql.cj.jdbc.Driver").newInstance());
            StringBuilder url = new StringBuilder();
            url.
                    append("jdbc:mysql://").        //db type
                    append("localhost:").           //host name
                    append("3306/").                //port
                    append("bank_client?").         //db name
                    append("user=root&").          //login
                    append("password=1234").       //password
                    append("&serverTimezone=UTC");
            System.out.println("URL: " + url + "\n");

            Connection connection = DriverManager.getConnection(url.toString());
            return connection;
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }

    private static BankClientDAO getBankClientDAO() {
        return new BankClientDAO(getMysqlConnection());
    }
}
