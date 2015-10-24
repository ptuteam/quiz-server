package utils;

import com.github.javafaker.Faker;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import model.UserProfile;

import java.util.UUID;

/**
 * alex on 24.10.15.
 */
public class AuthHelper {

    private static final String GET_TOKEN_URL = "https://accounts.google.com/o/oauth2/token";
    private static final String GET_USER_URL = "https://www.googleapis.com/oauth2/v2/userinfo?access_token=%s";

    private static final int AVATAR_SIZE = 200;
    private static final String AVATAR_URL = "http://api.adorable.io/avatars/%d/%s";
    private static final String GUEST_EMAIL = "%s.%s@guest.com";

    public static UserProfile getUserFromSocial(String code) throws UnirestException, NullPointerException {
        HttpResponse<String> accessTokenResponse = Unirest.post(GET_TOKEN_URL)
                .field("code", code)
                .field("client_id", ConfigGeneral.getClientID())
                .field("client_secret", ConfigGeneral.getClientSecret())
                .field("redirect_uri", ConfigGeneral.getRedirectUrl())
                .field("grant_type", ConfigGeneral.getGrantType())
                .asString();

        String accessTokenResponseString = accessTokenResponse.getBody();
        JsonObject accessTokenJson = new JsonParser().parse(accessTokenResponseString).getAsJsonObject();
        String accessToken = accessTokenJson.getAsJsonPrimitive("access_token").getAsString();

        HttpResponse<String> userInfoResponse = Unirest.get(String.format(GET_USER_URL, accessToken))
                .asString();

        String userInfoResponseString = userInfoResponse.getBody();
        JsonObject userInfoJson = new JsonParser().parse(userInfoResponseString).getAsJsonObject();

        return new UserProfile(
                userInfoJson.getAsJsonPrimitive("given_name").getAsString(),
                userInfoJson.getAsJsonPrimitive("family_name").getAsString(),
                userInfoJson.getAsJsonPrimitive("email").getAsString(),
                userInfoJson.getAsJsonPrimitive("picture").getAsString()
        );
    }

    public static UserProfile getGuestUser() {
        Faker faker = new Faker();

        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = String.format(GUEST_EMAIL, firstName, lastName);
        String avatarUrl = getGuestAvatarUrl();

        return new UserProfile(firstName, lastName, email, avatarUrl, true);
    }

    private static String getGuestAvatarUrl() {
        final String id = UUID.randomUUID().toString();
        return String.format(AVATAR_URL, AVATAR_SIZE, id);
    }
}
