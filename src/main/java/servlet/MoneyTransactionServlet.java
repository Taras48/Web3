package servlet;

import model.BankClient;
import service.BankClientService;
import util.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MoneyTransactionServlet extends HttpServlet {

    BankClientService bankClientService = new BankClientService();
    private Map<String, Object> map = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println(PageGenerator.getInstance().getPage("moneyTransactionPage.html", map));
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String send = req.getParameter("senderName");
        BankClient sender = bankClientService.getClientByName(send);
        String pass = req.getParameter("senderPass");
        String name = req.getParameter("nameTo");
        Long value = Long.parseLong(req.getParameter("count"));
        if (sender.getPassword().equals(pass) && bankClientService.sendMoneyToClient(sender, name, value)) {
            map.put("message", "The transaction was successful");
        } else {
            map.put("message", "transaction rejected");
        }
        resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", map));
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
