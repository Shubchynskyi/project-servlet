package com.tictactoe;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "InitServlet", value = {"", "/start"}, loadOnStartup = 1)
public class InitServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Creating a new session
        HttpSession currentSession = req.getSession(true);

        // Creating a playing field
        Field field = new Field(Sign.CROSS, Difficulty.EASY);
//        Map<Integer, Sign> fieldData = field.getField();

        // Getting a list of field values
        List<Sign> data = field.getFieldData();

        // Adding field parameters to the session (will be needed to store state between requests)
        currentSession.setAttribute("field", field.getField());
        currentSession.setAttribute("fieldClass", field);
        // and field values sorted by index (required for drawing crosses and zeroes)
        currentSession.setAttribute("data", data);
        currentSession.setAttribute("selectedSign", "cross");
        currentSession.setAttribute("selectedDifficulty", "easy");

        // Redirecting request to index.jsp page via server
        getServletContext().getRequestDispatcher("/index.jsp").forward(req, resp);
    }
}