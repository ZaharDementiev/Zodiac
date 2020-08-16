package com.example.zodiac.DB;

public class Constants {
    private static final String ROOT_URL = "http://134.0.116.112/";

    // Users
    public static final String URL_USER_REGISTRATION = ROOT_URL + "create_user.php";
    public static final String URL_USER_CHECK_MAIL = ROOT_URL + "unique_email.php";
    public static final String URL_GET_USER_BY_EMAIL_AND_PASSWORD = ROOT_URL + "user_by_email_and_password.php";
    public static final String URL_GET_USER_BY_PHONE_AND_PASSWORD = ROOT_URL + "user_by_phone_and_password.php";
    public static final String URL_GET_USER_BY_EMAIL_ONLY = ROOT_URL + "user_by_email_only.php";
    public static final String URL_UPDATE_USER_BY_EMAIL = ROOT_URL + "update_user.php";
    public static final String URL_UPDATE_USER_BY_ID = ROOT_URL + "update_user_by_id.php";
    public static final String URL_GET_USER_BY_RADIUS = ROOT_URL + "get_users_by_long_lat.php";
    public static final String URL_GET_USER_BY_ID = ROOT_URL + "user_by_id.php";
    public static final String URL_DELETE_USER = ROOT_URL + "delete_user.php";

    // Searching Settings
    public static final String URL_CREATE_SEARCHING_SETTINGS = ROOT_URL + "create_searching_settings.php";
    public static final String URL_GET_SEARCHING_SETTINGS = ROOT_URL + "get_recommendation_settings.php";
    public static final String URL_UPDATE_SEARCHING_SETTINGS = ROOT_URL + "update_searching_settings.php";
    public static final String URL_DELETE_SEARCHING_SETTINGS = ROOT_URL + "delete_swipe_searching_data.php";

    // Photos
    public static final String URL_UPLOAD_PHOTO = ROOT_URL + "upload_photo.php";
    public static final String URL_GET_USERS_PHOTOS = ROOT_URL + "photos_by_user.php";
    public static final String URL_DELETE_PHOTOS = ROOT_URL + "delete_images.php";
    public static final String URL_UPDATE_PHOTOS = ROOT_URL + "update_image_owner.php";

    // Swipes
    public static final String URL_CREATE_SWIPE = ROOT_URL + "create_swipe.php";
    public static final String URL_DELETE_SWIPE = ROOT_URL + "delete_swipes.php";

    // Sympathies
    public static final String URL_CREATE_SYMPATHY = ROOT_URL + "create_sympathy.php";
    public static final String URL_DELETE_SYMPATHY = ROOT_URL + "delete_sympathies.php";

    // Chats
    public static final String URL_CREATE_CHAT = ROOT_URL + "create_chat.php";
    public static final String URL_GET_CHATS = ROOT_URL + "get_chats_by_user.php";
    public static final String URL_DELETE_CHATS = ROOT_URL + "delete_chats.php";
    public static final String URL_GET_CHATS_BY_ID = ROOT_URL + "get_chats_by_id.php";

    // Messages
    public static final String URL_DELETE_MESSAGES = ROOT_URL + "delete_messages.php";
    public static final String URL_DELETE_PHOTO_MESSAGE = ROOT_URL + "delete_last_photo_message.php";
    public static final String URL_UPLOAD_PHOTO_CHAT = ROOT_URL + "upload_photo_chat.php";
    public static final String URL_GET_MESSAGES = ROOT_URL + "get_messages.php";
    public static final String URL_GET_LAST_MESSAGE = ROOT_URL + "get_last_message.php";

    // Recommendations
    public static final String URL_GET_WHO_LIKES_ME = ROOT_URL + "get_likes_users.php";
    public static final String URL_GET_RECOMMENDATIONS = ROOT_URL + "get_recommendations.php";

    // Message
    public static final String URL_CREATE_MESSAGE = ROOT_URL + "create_message.php";
}
