package utils;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.mashape.unirest.request.body.MultipartBody;
import model.UserProfile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * alex on 02.11.15.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AuthHelper.class, Unirest.class, ConfigGeneral.class})
public class AuthHelperTest {

    @Test
    public void testGetUserFromSocial() throws UnirestException, NullPointerException {
        PowerMockito.mockStatic(ConfigGeneral.class);
        when(ConfigGeneral.getClientID()).thenReturn("id");
        when(ConfigGeneral.getClientSecret()).thenReturn("secret");
        when(ConfigGeneral.getRedirectUrl()).thenReturn("redirect");
        when(ConfigGeneral.getGrantType()).thenReturn("grant");

        @SuppressWarnings("unchecked") HttpResponse<String> response = mock(HttpResponse.class);
        when(response.getBody()).thenReturn(
                '{' +
                        "\"access_token\": 123," + ' ' +
                        "\"given_name\": \"first\"," + ' ' +
                        "\"family_name\": \"last\"," + ' ' +
                        "\"email\": \"email\"," + ' ' +
                        "\"picture\": \"avatar\"" + ' ' +
                        '}'
        );

        MultipartBody body = mock(MultipartBody.class);
        when(body.field(anyString(), anyString())).thenReturn(body);
        when(body.asString()).thenReturn(response);


        HttpRequestWithBody requestWithBody = mock(HttpRequestWithBody.class);
        when(requestWithBody.field(anyString(), anyString())).thenReturn(body);

        GetRequest getRequest = mock(GetRequest.class);
        when(getRequest.asString()).thenReturn(response);

        PowerMockito.mockStatic(Unirest.class);
        when(Unirest.post(anyString())).thenReturn(requestWithBody);
        when(Unirest.get(anyString())).thenReturn(getRequest);

        UserProfile user = AuthHelper.getUserFromSocial("code");
        assertEquals(user.getFirstName(), "first");
        assertEquals(user.getLastName(), "last");
        assertEquals(user.getEmail(), "email");
        assertEquals(user.getAvatarUrl(), "avatar");
    }

    @Test
    public void testGetGuestUser() throws Exception {
        Faker faker = mock(Faker.class);
        Name name = mock(Name.class);

        PowerMockito.whenNew(Faker.class).withNoArguments().thenReturn(faker);
        when(faker.name()).thenReturn(name);
        when(name.firstName()).thenReturn("first");
        when(name.lastName()).thenReturn("last");

        UserProfile guest = AuthHelper.getGuestUser();
        assertTrue(guest.isGuest());
        assertEquals(guest.getFirstName(), "first");
        assertEquals(guest.getLastName(), "last");
        assertEquals(guest.getEmail(), "first.last@guest.com");
    }
}