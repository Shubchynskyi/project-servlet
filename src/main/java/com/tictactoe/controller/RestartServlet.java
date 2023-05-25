package com.tictactoe.controller;

import com.tictactoe.util.Constant;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.tictactoe.util.Constant.*;

@WebServlet(name = "RestartServlet", value = RESTART)
public class RestartServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String playerSign = req.getParameter(SIGN);
        String playerDifficulty = req.getParameter(DIFFICULTY);

        HttpSession session = req.getSession();
        session.invalidate();

        req.setAttribute(SIGN, playerSign);
        req.setAttribute(DIFFICULTY, playerDifficulty);

        resp.sendRedirect(req.getContextPath() + "/new-game?sign=" + playerSign + "&difficulty=" + playerDifficulty);
    }
}