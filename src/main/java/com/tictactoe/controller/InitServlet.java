package com.tictactoe.controller;

import com.tictactoe.entity.Difficulty;
import com.tictactoe.entity.Game;
import com.tictactoe.entity.Sign;
import com.tictactoe.util.Constant;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import static com.tictactoe.util.Constant.*;

@WebServlet(name = "InitServlet", value = {Constant.EMPTY, Constant.START}, loadOnStartup = 1)
public class InitServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession currentSession = req.getSession(true);

        Game game = new Game(Sign.CROSS, Difficulty.EASY);

        List<Sign> data = game.getFieldData();

        currentSession.setAttribute(FIELD_ATTRIBUTE, game.getField());
        currentSession.setAttribute(FIELD_CLASS_ATTRIBUTE, game);
        currentSession.setAttribute(DATA_ATTRIBUTE, data);
        currentSession.setAttribute(SELECTED_SIGN_ATTRIBUTE, CROSS);
        currentSession.setAttribute(SELECTED_DIFFICULTY_ATTRIBUTE, EASY);

        getServletContext().getRequestDispatcher(INDEX_JSP).forward(req, resp);
    }
}