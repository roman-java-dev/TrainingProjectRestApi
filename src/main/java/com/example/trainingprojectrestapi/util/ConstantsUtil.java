package com.example.trainingprojectrestapi.util;

/**
 * Utility class containing constant values used throughout the application.
 */
public class ConstantsUtil {
    public static final String CUSTOMER_ID_KEY = "customerId";
    public static final String ORDER_DATE_KEY = "orderDate";
    public static final String DESCRIPTION_KEY = "description";
    public static final String TOTAL_PRICE_KEY = "totalPrice";
    public static final String CUSTOMER_JOIN_PROPERTY = "customer";
    public static final String CUSTOMER_ID_PROPERTY = "id";
    public static final String REPORT_FILE_NAME = "orders_report.csv";
    public static final String JSON_FILE_EXTENSION = ".json";
    public static final String ERROR_MESSAGE = "Couldn't find customer";
    public final static String PHONE_NUMBER_PATTERN = "^380[0-9]{9}$";
    public final static String EMAIL_PATTERN = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
}
