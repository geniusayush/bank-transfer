package com.sherlockcodes.moneytransfer;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sherlockcodes.moneytransfer.config.GuiceConfig;
import com.sherlockcodes.moneytransfer.controller.AppController;
import com.sherlockcodes.moneytransfer.controller.AppControllerImpl;
import com.sherlockcodes.moneytransfer.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Filter;
import spark.Response;
import spark.Spark;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.NoSuchElementException;


public class TransferApp {

     private  static final int MAIN_PORT = 8080;

    private static final String ADD_DATA = "add_data";
    private static final Logger logger = LoggerFactory.getLogger(TransferApp.class);
    private static final HashMap<String, String> corsHeaders = new HashMap<>();


    static {
        corsHeaders.put("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
        corsHeaders.put("Access-Control-Allow-Origin", "*");
        corsHeaders.put("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");
        corsHeaders.put("Access-Control-Allow-Credentials", "true");
    }

    private static void setupPortNumber() {
        Spark.port(MAIN_PORT);
    }


    /**
     * use this to start spark app for endpoint testing
     */
    public static void start() {

        TransferApp.main(null);
        Spark.awaitInitialization();
    }
    /**
     * stop spark server
     */
    public static void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    public static void main(String[] args) {

        setupPortNumber();
        applyCORSFilter();
        initializeRoutes();

        Spark.after((req, res) -> res.type("application/json"));
        initExceptionsHandling();
    }

    private static void initExceptionsHandling() {
        Spark.exception(IllegalArgumentException.class, (e, req, res) ->
                fillErrorInfo(res, e, HttpServletResponse.SC_BAD_REQUEST));

        Spark.exception(NullPointerException.class, (e, req, res) ->
                fillErrorInfo(res, e, HttpServletResponse.SC_BAD_REQUEST));

        Spark.exception(NumberFormatException.class, (e, req, res) ->
                fillErrorInfo(res, e, HttpServletResponse.SC_BAD_REQUEST));

        Spark.exception(NoSuchElementException.class, (e, req, res) ->
                fillErrorInfo(res, e, HttpServletResponse.SC_NOT_FOUND));
    }

    private static void fillErrorInfo(Response res, Exception err, int errCode) {
        res.type("application/json");
        res.status(errCode);
        res.body(JsonUtils.toJson(err, errCode));
    }


    private static void initializeRoutes() {
        Injector injector = Guice.createInjector(new GuiceConfig());
        AppController accountController = injector.getInstance(AppControllerImpl.class);
        Spark.post("/accounts", accountController::createAccount);
        Spark.get("/accounts/:id", accountController::getAccountById);
        Spark.post("/accounts/:id/transaction", accountController::createAccountTransaction);

    }

    /**
     * apply right before routes initialization to prevent error.
     */
    private static void applyCORSFilter() {
        Filter filter = (request, response) -> corsHeaders.forEach(response::header);
        Spark.after(filter);
    }


}
