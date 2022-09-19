package fr.blockincraft.kingdoms.api;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import java.io.IOException;

public class RequestHandler extends AbstractHandler {
    private final KingdomServlet kingdomServlet = new KingdomServlet();

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String uri = request.getRequestURI();

        if (uri.startsWith("/kingdoms/")) {
            kingdomServlet.service(request, response);
            return;
        }

        response.setStatus(HttpStatus.BAD_REQUEST_400);
        response.getOutputStream().println("{\"success\":\"false\", \"error\":\"Invalid request\"}");
        response.getOutputStream().flush();
    }
}
