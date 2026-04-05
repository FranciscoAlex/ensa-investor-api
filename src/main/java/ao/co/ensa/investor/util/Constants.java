package ao.co.ensa.investor.util;

/**
 * Application-wide constants
 */
public final class Constants {

    private Constants() {} // Prevent instantiation

    // API Versioning
    public static final String API_V1 = "/api/v1";

    // JWT
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_AUTHORIZATION = "Authorization";

    // Pagination defaults
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;
    public static final String DEFAULT_SORT_FIELD = "createdAt";
    public static final String DEFAULT_SORT_DIRECTION = "desc";

    // Cache names
    public static final String CACHE_STATIC_DATA = "staticData";
    public static final String CACHE_USER_SESSIONS = "userSessions";

    // Investor code prefix
    public static final String INVESTOR_CODE_PREFIX = "ENSA-INV-";
}
