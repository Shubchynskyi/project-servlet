package com.tictactoe;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "NewGameServlet", value = "/newgame")
public class NewGameServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String playerSign = req.getParameter("sign"); // Получение значения параметра sing
        String playerDifficulty = req.getParameter("difficulty"); // Получение значения параметра difficult

        // Creating a new session
        HttpSession currentSession = req.getSession(true);

// Определение знака (Sign)
        Sign sign = Sign.CROSS;
        if (playerSign.equalsIgnoreCase(Sign.NOUGHT.toString())) {
            sign = Sign.NOUGHT;
        }

// Определение сложности (Difficulty)
        Difficulty difficulty = Difficulty.EASY; // По умолчанию устанавливаем EASY
        if (playerDifficulty != null) {
            for (Difficulty d : Difficulty.values()) {
                if (d.getSign().equalsIgnoreCase(playerDifficulty)) {
                    difficulty = d;
                    break;
                }
            }
        }

// Создание нового объекта Field
        Field field = new Field(sign, difficulty);

        // Getting a list of field values
        List<Sign> data = field.getFieldData();

        // Adding field parameters to the session (will be needed to store state between requests)
        currentSession.setAttribute("field", field.getField());
        currentSession.setAttribute("fieldClass", field);
        // and field values sorted by index (required for drawing crosses and zeroes)
        currentSession.setAttribute("data", data);

        req.setAttribute("selectedSign", playerSign);
        req.setAttribute("selectedDifficulty", playerDifficulty);

        // Redirecting request to index.jsp page via server
        getServletContext().getRequestDispatcher("/index.jsp").forward(req, resp);
    }
}