package com.infoshareacademy.servlet;

import com.infoshareacademy.domain.api.OrganizerJSON;
import com.infoshareacademy.freemarker.TemplateProvider;
import com.infoshareacademy.service.EventViewService;
import com.infoshareacademy.service.OrganizerViewService;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/show-organizers")
public class ShowOrganizersServlet extends HttpServlet {
    private static final Logger STDLOG = LoggerFactory.getLogger(LoginServlet.class.getName());

    @Inject
    TemplateProvider templateProvider;

    @EJB
    OrganizerViewService organizerViewService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Template template = templateProvider.getTemplate(getServletContext(), "showOrganizers.ftlh");
        Map<String, Object> dataModel = new HashMap<>();

        Integer actPage = Integer.parseInt(req.getParameter("page"));
        Integer listSize = organizerViewService.listSize();
        Integer numberOfPages = (listSize % 20 != 0) ? listSize / 20 + 1 : listSize / 20;

        List<OrganizerJSON> listOrganizers = organizerViewService.prepareOrganizersToShow((actPage - 1) * 20);
        req.setCharacterEncoding("UTF-8");

        if (actPage < 1 || actPage > numberOfPages) {
            resp.sendRedirect("/show-organizers?page=1");
        }

        dataModel.put("organizers", listOrganizers);
        dataModel.put("actPage", actPage);
        dataModel.put("numberOfPages", numberOfPages);
        dataModel.put("numberOfOrganizers", listSize);
        dataModel.put("name", "organizers");
        dataModel.put("action","");

        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter pw = resp.getWriter();

        try {
            template.process(dataModel, pw);
        } catch (TemplateException e) {
            STDLOG.error("Template for main page error");
        }
    }
}
