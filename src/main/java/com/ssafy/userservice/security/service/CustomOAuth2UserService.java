package com.ssafy.userservice.security.service;


import com.ssafy.userservice.security.oauth2.AzureOAuth2UserInfo;

import java.util.Optional;

import com.ssafy.userservice.dto.AuthDto;
import com.ssafy.userservice.entity.Auth;
import com.ssafy.userservice.entity.Role;
import com.ssafy.userservice.repository.AuthRepository;
import com.ssafy.userservice.security.CustomUserDetails;
import com.ssafy.userservice.security.oauth2.GoogleOAuth2UserInfo;
import com.ssafy.userservice.security.oauth2.Oauth2UserInfo;
import com.ssafy.userservice.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final AuthRepository authRepository;
    private final MemberService memberService;
    private final String GOOGLE = "google";
    private final String AZURE = "azure";
    private Auth auth;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Oauth2UserInfo oauth2UserInfo = ofOAuth2UserInfo(registrationId, oAuth2User);

        log.info("oauth2UserInfo: {}", oauth2UserInfo.toString());
        log.info("userRequest: {}", userRequest.toString());

//        assert oauth2UserInfo != null;

        Optional<Auth> optionalUser = authRepository.findByProviderAndProviderId(
                oauth2UserInfo.getProvider(), oauth2UserInfo.getProviderId());
        if (optionalUser.isPresent()) {
            AuthDto authDto = AuthDto.getAuth(optionalUser.get());
            auth = Auth.toDto(authDto);
        } else {
            auth = Auth.builder()
                    .username(oauth2UserInfo.getProviderId())
                    .name(oauth2UserInfo.getName())
                    .role(Role.USER)
                    .provider(oauth2UserInfo.getProvider())
                    .providerId(oauth2UserInfo.getProviderId())
                    .build();
            authRepository.save(auth);
            log.info("auth={}", auth.toString());
        }

        if (optionalUser.isEmpty()) {
            memberService.createMember(auth, oauth2UserInfo.getEmail());
        }

        return new CustomUserDetails(auth, oAuth2User.getAttributes());
    }

    private Oauth2UserInfo ofOAuth2UserInfo(String registrationId, OAuth2User oAuth2User) {
        log.info("registrationId: {}", registrationId);
        log.info("oAuth2User: {}", oAuth2User.toString());
        if (registrationId.equals(GOOGLE)) {
            log.info(" 0 - oAuth2User = {}", oAuth2User.toString());
            return new GoogleOAuth2UserInfo(oAuth2User.getAttributes());
        } else if (registrationId.equals(AZURE)) {
            log.info("0 - oAuth2User = {}", oAuth2User.toString());
            return new AzureOAuth2UserInfo(oAuth2User.getAttributes());
        }
        return null;
    }
}