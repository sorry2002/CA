package com.electricalweb.controllers;

import com.electricalweb.validators.EmailValidator;
import com.electricalweb.validators.StringValidator;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "CustomerController", urlPatterns = "/processcustomer")
public class CustomerController extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) {

        // Get the customer parameters from the request object
        Map<String, String> customerParameters = getCustomerParametersFromRequest(request);

        // Set the customer parameters to the request object
        setCustomerParametersToRequest(request);

        // Set upfront the URL the customer will be redirected to
        String url = "/WEB-INF/views/customerinfo.jsp";

        // Validate the customer parameters
        String firstName = customerParameters.get("firstname");
        String lastName = customerParameters.get("lastname");
        String email = customerParameters.get("email");

        List<String> violations = validateCustomerParameters(firstName, lastName, email);
        if (!violations.isEmpty()) {
            request.setAttribute("violations", violations);
            url = "/";
        }

        try {
            request.getRequestDispatcher(url).forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, String> getCustomerParametersFromRequest(HttpServletRequest request) {
        Map<String, String> customerParameters = new HashMap<>();
        customerParameters.put("firstname", request.getParameter("firstname"));
        customerParameters.put("lastname", request.getParameter("lastname"));
        customerParameters.put("email", request.getParameter("email"));
        return customerParameters;
    }

    private void setCustomerParametersToRequest(HttpServletRequest request) {
        request.setAttribute("firstname", request.getParameter("firstname"));
        request.setAttribute("lastname", request.getParameter("lastname"));
        request.setAttribute("email", request.getParameter("email"));
    }

    private List<String> validateCustomerParameters(String firstName, String lastName, String email) {
        List<String> violations = new ArrayList<>();
        if (!StringValidator.validate(firstName)) {
            violations.add("First Name is mandatory");
        }
        if (!StringValidator.validate(lastName)) {
            violations.add("Last Name is mandatory");
        }
        if (!EmailValidator.validate(email)) {
            violations.add("Email is mandatory and must be a well-formed email address");
        }
        return violations;
    }
}