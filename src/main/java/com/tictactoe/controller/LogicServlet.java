package com.tictactoe.controller;

import com.tictactoe.entity.Game;
import com.tictactoe.entity.Sign;
import com.tictactoe.util.Constant;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import static com.tictactoe.util.Constant.*;

@WebServlet(name = "LogicServlet", value = Constant.LOGIC)
public class LogicServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession currentSession = req.getSession();

        String selectedSign = (String) currentSession.getAttribute(Constant.SELECTED_SIGN);

        Sign playerSign = Sign.CROSS;
        if (selectedSign.equalsIgnoreCase(Constant.NOUGHT)) {
            playerSign = Sign.NOUGHT;
        }

        Game game = extractField(currentSession);

        // get the index of the cell that was clicked
        int index = getSelectedIndex(req);
        Sign currentSign = game.getField().get(index);

        // Check that the cell that was clicked is empty.
        // Otherwise, we do nothing and send the user to the same page without changes
        // parameters in the session
        if (Sign.EMPTY != currentSign) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(INDEX_JSP);
            dispatcher.forward(req, resp);
            return;
        }

        // put a sign in the cell that the user clicked on
        game.getField().put(index, playerSign);

        // Checking if the sign has won after adding the user's last click
        if (checkWin(resp, currentSession, game)) {
            return;
        }

        // Get an empty field cell
        int emptyFieldIndex = game.getEmptyFieldIndex();

        if (emptyFieldIndex >= 0) {
            game.makeMove();
            // Checking if the zero wins after adding the last zero
            if (checkWin(resp, currentSession, game)) {
                return;
            }
        }

        // If there is no empty cell and no one has won, then it is a draw
        if (game.getEmptyFieldIndex() < 0) {
            // We add a flag to the session that signals that a draw has occurred
            currentSession.setAttribute(Constant.DRAW, true);
            List<Sign> data = game.getFieldData();
            currentSession.setAttribute(Constant.DATA, data);
            resp.sendRedirect(INDEX_JSP);
            return;
        }

        // Reading a list of icons
        List<Sign> data = game.getFieldData();

        // Update field object and icon list in session
        currentSession.setAttribute(Constant.DATA, data);
        currentSession.setAttribute(FIELD_ATTRIBUTE, game);

        resp.sendRedirect(INDEX_JSP);
    }

    /**
     * The method checks if there are three X/O's in a row.
     * Return true/false
     */
    private boolean checkWin(HttpServletResponse response, HttpSession currentSession, Game game) throws IOException {
        Sign winner = game.checkWin();
        if (Sign.CROSS == winner || Sign.NOUGHT == winner) {
            // Adding a flag that shows that someone has won
            currentSession.setAttribute(WINNER, winner);

            // Reading a list of icons
            List<Sign> data = game.getFieldData();

            // Update this list in session
            currentSession.setAttribute(DATA, data);

            // Send a redirect
            response.sendRedirect(INDEX_JSP);
            return true;
        }
        return false;
    }

    private int getSelectedIndex(HttpServletRequest request) {
        String click = request.getParameter(CLICK);
        boolean isNumeric = click.chars().allMatch(Character::isDigit);
        return isNumeric ? Integer.parseInt(click) : 0;
    }

    private Game extractField(HttpSession currentSession) {
        Object fieldAttribute = currentSession.getAttribute(FIELD_CLASS);
        if (Game.class != fieldAttribute.getClass()) {
            currentSession.invalidate();
            throw new RuntimeException(SESSION_IS_BROKEN_TRY_ONE_MORE_TIME);
        }
        return (Game) fieldAttribute;
    }
}