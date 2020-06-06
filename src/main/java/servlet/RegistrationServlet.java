package servlet;

import exception.DBException;
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

public class RegistrationServlet extends HttpServlet {
    private Map<String, Object> map = new HashMap<>();
    private  BankClientService bcs = new BankClientService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println(PageGenerator.getInstance().getPage("registrationPage.html", map));
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("name");
        String pass = req.getParameter("password");
        Long money = Long.parseLong(req.getParameter("money"));
        BankClient bankClient = new BankClient(login, pass, money);

        if (bcs.addClient(bankClient)) {
            map.put("message", "Add client successful");
        } else {
            map.put("message", "Client not add");
        }
        resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", map));
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);

    }
}
