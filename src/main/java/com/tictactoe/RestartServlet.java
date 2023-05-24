package com.tictactoe;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

//@WebServlet(name = "RestartServlet", value = "/restart")
//public class RestartServlet extends HttpServlet {
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        req.getSession().invalidate();
//        resp.sendRedirect("/start");
//    }
//}

@WebServlet(name = "RestartServlet", value = "/restart")
public class RestartServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String playerSign = req.getParameter("sign");
        String playerDifficulty = req.getParameter("difficulty");

        HttpSession session = req.getSession();
        session.invalidate();

        req.setAttribute("sign", playerSign);
        req.setAttribute("difficulty", playerDifficulty);

        resp.sendRedirect(req.getContextPath() + "/newgame?sign=" + playerSign + "&difficulty=" + playerDifficulty);
    }
}