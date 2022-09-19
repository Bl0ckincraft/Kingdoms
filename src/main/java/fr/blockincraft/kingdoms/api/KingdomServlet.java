package fr.blockincraft.kingdoms.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.blockincraft.kingdoms.Kingdoms;
import fr.blockincraft.kingdoms.Registry;
import fr.blockincraft.kingdoms.api.objects.KingdomAO;
import fr.blockincraft.kingdoms.core.dto.KingdomFullDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KingdomServlet extends HttpServlet {
    private final Registry registry = Kingdoms.getInstance().getRegistry();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String[] args = req.getRequestURI().substring("/kingdoms/".length()).split("/");

        if (args.length == 1) {
            if (args[0].equals("all")) {
                List<KingdomFullDTO> kingdoms = registry.getKingdoms();

                List<KingdomAO> kingdomAOList = new ArrayList<>();
                for (KingdomFullDTO kingdom : kingdoms) {
                    kingdomAOList.add(KingdomAO.fromDTO(kingdom));
                }

                ObjectMapper mapper = new ObjectMapper();
                resp.getOutputStream().println("{\"success\":\"true\", \"kingdoms\":" + mapper.writeValueAsString(kingdomAOList) + "}");
                resp.setStatus(HttpStatus.OK_200);
                resp.getOutputStream().flush();
                return;
            }
        } else if (args.length == 2) {
            if (args[0].equals("ranking")) {
                int max;

                try {
                    max = Integer.parseInt(args[1]);

                    if (max < 1) {
                        resp.getOutputStream().println("{\"success\":\"false\", \"error\":\"Integer must be greater than 0\"}");
                        resp.setStatus(HttpStatus.BAD_REQUEST_400);
                        resp.getOutputStream().flush();
                        return;
                    }
                } catch (Exception e) {
                    resp.getOutputStream().println("{\"success\":\"false\", \"error\":\"Cannot parse integer\"}");
                    resp.setStatus(HttpStatus.BAD_REQUEST_400);
                    resp.getOutputStream().flush();
                    return;
                }

                List<KingdomFullDTO> kingdoms = registry.getRanking();

                List<KingdomAO> kingdomAOList = new ArrayList<>();
                for (int i = 0; i < kingdoms.size() && i < max; i++) {
                    kingdomAOList.add(KingdomAO.fromDTO(kingdoms.get(i)));
                }

                ObjectMapper mapper = new ObjectMapper();
                resp.getOutputStream().println("{\"success\":\"true\", \"ranking\":" + mapper.writeValueAsString(kingdomAOList) + "}");
                resp.setStatus(HttpStatus.OK_200);
                resp.getOutputStream().flush();
                return;
            }
        }

        resp.setStatus(HttpStatus.BAD_REQUEST_400);
        resp.getOutputStream().println("{\"success\":\"false\", \"error\":\"Invalid request\"}");
        resp.getOutputStream().flush();
    }
}
