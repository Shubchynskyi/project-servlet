package com.tictactoe.controller;

import com.tictactoe.entity.Difficulty;
import com.tictactoe.entity.Game;
import com.tictactoe.entity.Sign;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import static com.tictactoe.util.Constant.*;

@WebServlet(name = "NewGameServlet", value = NEW_GAME)
public class NewGameServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String playerSign = req.getParameter(SIGN);
        String playerDifficulty = req.getParameter(DIFFICULTY);

        HttpSession currentSession = req.getSession(true);

        Sign sign = Sign.CROSS;
        if (playerSign.equalsIgnoreCase(NOUGHT)) {
            sign = Sign.NOUGHT;
        }

        Difficulty difficulty = Difficulty.EASY;
        if (playerDifficulty != null) {
            for (Difficulty d : Difficulty.values()) {
                if (d.getSign().equalsIgnoreCase(playerDifficulty)) {
                    difficulty = d;
                    break;
                }
            }
        }

        Game game = new Game(sign, difficulty);

        if (playerSign.equalsIgnoreCase(NOUGHT)) {
            game.makeMove();
        }

        List<Sign> data = game.getFieldData();

        currentSession.setAttribute(FIELD_ATTRIBUTE, game.getField());
        currentSession.setAttribute(FIELD_CLASS, game);
        currentSession.setAttribute(DATA, data);
        currentSession.setAttribute(SELECTED_SIGN_ATTRIBUTE, playerSign);
        currentSession.setAttribute(SELECTED_SIGN_ATTRIBUTE, playerDifficulty);

        getServletContext().getRequestDispatcher(INDEX_JSP).forward(req, resp);
    }
}