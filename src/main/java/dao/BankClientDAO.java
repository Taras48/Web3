package dao;

import model.BankClient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankClientDAO {

    private Connection connection;

    public BankClientDAO(Connection connection) {
        this.connection = connection;
    }

    public List<BankClient> getAllBankClient() throws SQLException {
        List<BankClient> resList = new ArrayList<>();

        Statement stm = connection.createStatement();
        stm.execute("select  * from bank_client");
        ResultSet resultSet = stm.getResultSet();
        while (resultSet.next()) {
            resList.add(new BankClient(Long.parseLong(resultSet.getString("id")),
                    resultSet.getString("name"),
                    resultSet.getString("password"),
                    resultSet.getLong("money"))
            );
        }
        resultSet.close();
        stm.close();
        return resList;
    }

    public boolean validateClient(String name, String password) throws SQLException {
        BankClient bankClient = getClientByName(name);
        if (bankClient != null) {
            return bankClient.getPassword().equals(password);
        } else {
            return false;
        }
    }

    public void updateClientsMoney(String name, String password, Long transactValue) throws SQLException {
        PreparedStatement pstmt = null;
        pstmt = connection.prepareStatement("update bank_client set money = ? where name = ? and password = ?");
        pstmt.setLong(1, transactValue);
        pstmt.setString(2, name);
        pstmt.setString(3, password);
        pstmt.executeUpdate();
        pstmt.close();
    }

    public BankClient getClientById(long id) throws SQLException {
        BankClient bankClient = new BankClient();
        Statement stmt = connection.createStatement();
        stmt.execute("select * from bank_client where id='" + id + "'");
        ResultSet result = stmt.getResultSet();
        if (!result.isLast()) {
            result.next();
            bankClient.setMoney(result.getLong("money"));
            bankClient.setName(result.getString("name"));
            bankClient.setPassword(result.getString("password"));
            bankClient.setId(result.getLong("id"));
        }
        result.close();
        stmt.close();
        return bankClient;
    }

    public boolean isClientHasSum(String name, Long expectedSum) throws SQLException {
        Statement stm = null;
        Long sum = 0L;

        stm = connection.createStatement();
        stm.execute("select  * from bank_client where name ='" + name + "'");
        ResultSet resultSet = stm.getResultSet();
        resultSet.next();
        sum = resultSet.getLong("money");
        resultSet.close();
        stm.close();
        return sum >= expectedSum;
    }

    public BankClient getClientByName(String name) throws SQLException {
        BankClient bankClient = new BankClient();
        Statement stmt = null;

        stmt = connection.createStatement();
        stmt.execute("select * from bank_client where name='" + name + "'");
        ResultSet result = stmt.getResultSet();
        result.next();
        bankClient.setMoney(result.getLong("money"));
        bankClient.setName(result.getString("name"));
        bankClient.setPassword(result.getString("password"));
        bankClient.setId(result.getLong("id"));
        result.close();
        stmt.close();

        return bankClient;
    }

    public void addClient(BankClient client) throws SQLException {
        PreparedStatement pstmt =
                connection.prepareStatement("INSERT INTO bank_client(name, password, money) VALUES (?, ?, ?)");
        pstmt.setString(1, client.getName());
        pstmt.setString(2, client.getPassword());
        pstmt.setLong(3, client.getMoney());
        pstmt.executeUpdate();
        pstmt.close();
    }

    public boolean isNameInDataBase(String name) throws SQLException {
        boolean res = false;
        Statement stmt = connection.createStatement();
        stmt.execute("select * from bank_client where name='" + name + "'");
        ResultSet result = stmt.getResultSet();
        res = result.next();
        result.close();
        stmt.close();
        return res;
    }

    public void createTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("create table if not exists bank_client (id bigint auto_increment, name varchar(256), password varchar(256), money bigint, primary key (id))");
        stmt.close();
    }

    public void dropTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS bank_client");
        stmt.close();
    }
}
