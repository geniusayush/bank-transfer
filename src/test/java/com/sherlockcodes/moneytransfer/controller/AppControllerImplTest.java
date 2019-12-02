package com.sherlockcodes.moneytransfer.controller;


import com.sherlockcodes.moneytransfer.TransferApp;
import com.sherlockcodes.moneytransfer.utils.JsonUtils;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AppControllerImplTest {
    private static final String ACCOUNTS_ENDPOINT = "/accounts";
    private static long acc1Id;

    @BeforeAll
    public static void setUp() {
        TransferApp.start();

    }

    @AfterAll
    public static void tearDown() {
        TransferApp.stop();
    }


    @Test
    @Order(1)
    public void errorNameLessThan3Test() {
        AccountInput input = new AccountInput("a", 123, 20);
        given().
                when().
                body(JsonUtils.make().toJson(input)).
                post(ACCOUNTS_ENDPOINT).
                then().
                assertThat().statusCode(400);

    }

    @Test
    @Order(2)
    public void createAccountTest() {
        AccountInput input = new AccountInput("abcd", 123, 100);
        Response response = given().
                body(JsonUtils.make().toJson(input)).
                post(ACCOUNTS_ENDPOINT);
        assertEquals(201, response.statusCode());
        AccountOutput account = JsonUtils.make().fromJson(response.asString(), AccountOutput.class);
        assertEquals("abcd", account.getName());
        acc1Id = account.getId();

    }

    @Test
    @Order(3)
    public void getAccountTest() {
        Response res = get("/accounts/" + acc1Id);
        AccountOutput account = JsonUtils.make().fromJson(res.asString(), AccountOutput.class);
        assertEquals("abcd", account.getName());
    }

    @Test
    @Order(4)
    public void getNonExistingAcountTest() {
        Response res = get("/accounts/" + 2345);
        assertEquals(404, res.statusCode());
    }

    @Test
    @Order(5)
    public void createTransactionTest() {

        AccountInput input = new AccountInput("wxyz", 9632, 100);
        Response response = given().
                body(JsonUtils.make().toJson(input)).
                post(ACCOUNTS_ENDPOINT);
        assertEquals(201, response.statusCode());
        long accId2 = JsonUtils.make().fromJson(response.asString(), AccountOutput.class).getId();

        TransactionInput transaction = new TransactionInput(accId2, 60);
        Response transactionResponse = given().
                body(JsonUtils.make().toJson(transaction)).
                post("/accounts/" + acc1Id + "/transaction");
        assertEquals(204, transactionResponse.statusCode());


        AccountOutput sourceAccount = JsonUtils.make().fromJson(get("/accounts/" + acc1Id).asString(), AccountOutput.class);
        assertEquals(40, sourceAccount.getBalance());
        AccountOutput destAccount = JsonUtils.make().fromJson(get("/accounts/" + accId2).asString(), AccountOutput.class);
        assertEquals(160, destAccount.getBalance());

        given().body(JsonUtils.make().toJson(transaction)).when().post("/accounts/" + acc1Id + "/transaction").then().assertThat().statusCode(400);

    }
}