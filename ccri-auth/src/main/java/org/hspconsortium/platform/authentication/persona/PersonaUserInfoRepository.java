package org.hspconsortium.platform.authentication.persona;

import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.mitre.openid.connect.model.DefaultUserInfo;
import org.mitre.openid.connect.model.UserInfo;
import org.mitre.openid.connect.repository.UserInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * This class only handles the fetching of persona users.  Different user auth methods must implement their own
 * subclass to fetch user info for non-persona users.
 */
public abstract class PersonaUserInfoRepository implements UserInfoRepository {

    public static final String ANONYMOUS_USER = "anonymousUser";
    public static final String PERSONA_USERNAME_REGEX = "\\A[a-zA-Z0-9]{1,50}@[a-zA-Z0-9]{1,20}\\Z";

    private static final Logger log = LoggerFactory.getLogger(PersonaUserInfoRepository.class);

    Pattern personaPattern = Pattern.compile(PERSONA_USERNAME_REGEX);

    protected LoadingCache<String, Optional<UserInfo>> userInfoCache;

    @Value("${hspc.platform.sandbox.api.host}${hspc.platform.sandbox.personaInfoPath}")
    private String personaInfoEndpoint;

    @Value("${hspc.platform.userInfoCacheTimeout:2}")
    private Integer userInfoCacheTimeout;

    @PostConstruct
    private void validatePersonaInfoEndpoint() {
        if (!personaInfoEndpoint.endsWith("/")) {
            personaInfoEndpoint += "/";
        }
    }

    @PostConstruct
    private void initializeCache() {
        this.userInfoCache = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterAccess(userInfoCacheTimeout, TimeUnit.SECONDS)
                .build(cacheLoader);
    }

    @Override
    public UserInfo getByUsername(String username) {
        try {
            return userInfoCache.get(username).orNull();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private CacheLoader<String, Optional<UserInfo>> cacheLoader = new CacheLoader<String, Optional<UserInfo>>() {
        @Override
        public Optional<UserInfo> load(String username) throws Exception {
            // anonymous users will always return null
            if (ANONYMOUS_USER.equals(username))
                return Optional.absent();

            UserInfo userInfo = null;

            // if this looks like it could be a persona, check the persona API endpoint
            if(matchesPersonaPattern(username))
                userInfo = fetchUserInfoForPersona(username);

            // if the user isn't anonymous and isn't a persona, pass it off to the child class to check for a user
            if(userInfo == null)
                userInfo = getRealUserByUsername(username);

            return Optional.fromNullable(userInfo);
        }
    };

    private UserInfo fetchUserInfoForPersona(String username) {
        RestTemplate restTemplate = new RestTemplate();
        UserPersonaDto userPersonaDto;
        String personaInfoUrl = personaInfoEndpoint + username;

        try {
            log.info("Attempting to fetch user info for persona: " + username + " at URL " + personaInfoUrl);
            userPersonaDto = restTemplate.getForObject(personaInfoUrl, UserPersonaDto.class);
        } catch (Exception ex) {
            return null;
        }

        if (userPersonaDto == null) {
            log.info("Unable to fetch info for persona: " + username) ;
            return null;
        } else {
            log.info("Successfully fetched user info for persona: " + username + ", with name: " + userPersonaDto.getName());
        }

        UserInfo userInfo = new DefaultUserInfo();

        userInfo.setName(userPersonaDto.getName());
        userInfo.setPreferredUsername(userPersonaDto.getUsername());
        userInfo.setEmail(userPersonaDto.getUsername());
        userInfo.setSub(userPersonaDto.getUsername());
        userInfo.setProfile(userPersonaDto.getResourceUrl());

        return userInfo;
    }

    private boolean matchesPersonaPattern(String username) {
        // validation appied in controllers.js line 1843:
        //     id !== undefined && id !== "" && id.length <= 50 && /^[a-zA-Z0-9]*$/.test(id)
        // and for the sandbox id
        //     id !== undefined && id !== "" && id.length <= 20 && /^[a-zA-Z0-9]*$/.test(id)
        return personaPattern.matcher(username).matches();
    }

    /**
     * Child classes will implement this method for fetching actual users via LDAP or whatever other implementation.
     */
    public abstract UserInfo getRealUserByUsername(String username);

    @Override
    public UserInfo getByEmailAddress(String s) {
        return null;
    }
}
