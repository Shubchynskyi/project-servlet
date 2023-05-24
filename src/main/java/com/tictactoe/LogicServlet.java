package com.tictactoe;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "LogicServlet", value = "/logic")
public class LogicServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Get the current session
        HttpSession currentSession = req.getSession();

        String selectedSign = (String) currentSession.getAttribute("selectedSign");

        Sign playerSign = Sign.CROSS;
        if (selectedSign.equalsIgnoreCase("NOUGHT")) {
            playerSign = Sign.NOUGHT;
        }

        // Getting the play field object from the session
        Field field = extractField(currentSession);

        // get the index of the cell that was clicked
        int index = getSelectedIndex(req);
        Sign currentSign = field.getField().get(index);

        // Check that the cell that was clicked is empty.
        // Otherwise, we do nothing and send the user to the same page without changes
        // parameters in the session
        if (Sign.EMPTY != currentSign) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
            dispatcher.forward(req, resp);
            return;
        }

        // put a cross in the cell that the user clicked on
        field.getField().put(index, playerSign);

        // Checking if the cross has won after adding the user's last click
        if (checkWin(resp, currentSession, field)) {
            return;
        }

        // Get an empty field cell
        int emptyFieldIndex = field.getEmptyFieldIndex();

        if (emptyFieldIndex >= 0) {
//            field.getField().put(emptyFieldIndex, Sign.NOUGHT);
            field.makeMove();
            // Checking if the zero wins after adding the last zero
            if (checkWin(resp, currentSession, field)) {
                return;
            }
        }
        // If there is no empty cell and no one has won, then it is a draw
        else {
            // We add a flag to the session that signals that a draw has occurred
            currentSession.setAttribute("draw", true);

            // Reading a list of icons
            List<Sign> data = field.getFieldData();

            // Update this list in session
            currentSession.setAttribute("data", data);

            // Send a redirect
            resp.sendRedirect("/index.jsp");
            return;
        }

        // Reading a list of icons
        List<Sign> data = field.getFieldData();

        // Update field object and icon list in session
        currentSession.setAttribute("data", data);
        currentSession.setAttribute("field", field);



        resp.sendRedirect("/index.jsp");
    }

    /**
     * The method checks if there are three X/O's in a row.
     * Return true/false
     */
    private boolean checkWin(HttpServletResponse response, HttpSession currentSession, Field field) throws IOException {
        Sign winner = field.checkWin();
        if (Sign.CROSS == winner || Sign.NOUGHT == winner) {
            // Adding a flag that shows that someone has won
            currentSession.setAttribute("winner", winner);

            // Reading a list of icons
            List<Sign> data = field.getFieldData();

            // Update this list in session
            currentSession.setAttribute("data", data);

            // Send a redirect
            response.sendRedirect("/index.jsp");
            return true;
        }
        return false;
    }

    private int getSelectedIndex(HttpServletRequest request) {
        String click = request.getParameter("click");
        boolean isNumeric = click.chars().allMatch(Character::isDigit);
        return isNumeric ? Integer.parseInt(click) : 0;
    }

    private Field extractField(HttpSession currentSession) {
        Object fieldAttribute = currentSession.getAttribute("fieldClass");
        if (Field.class != fieldAttribute.getClass()) {
            currentSession.invalidate();
            throw new RuntimeException("Session is broken, try one more time");
        }
        return (Field) fieldAttribute;
    }
}