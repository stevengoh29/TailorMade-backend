package com.future.tailormade.command.auth.impl;

import com.future.tailormade.command.auth.SignInCommand;
import com.future.tailormade.component.CustomPasswordEncoder;
import com.future.tailormade.component.JwtTokenProvider;
import com.future.tailormade.exceptions.UnauthorizedException;
import com.future.tailormade.model.entity.auth.Token;
import com.future.tailormade.model.entity.user.User;
import com.future.tailormade.payload.request.auth.SignInRequest;
import com.future.tailormade.payload.response.auth.SignInResponse;
import com.future.tailormade.payload.response.user.GetUserByIdResponse;
import com.future.tailormade.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SignInCommandImpl implements SignInCommand {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomPasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public Mono<SignInResponse> execute(SignInRequest request) {
        return findByUsername(request.getUsername()).map(user -> {
            if (passwordEncoder.encode(
                    request.getPassword()).equals(user.getPassword())
            ) {
                Token token = getToken(
                        jwtTokenProvider.generateAccessToken(user),
                        jwtTokenProvider.generateRefreshToken(user)
                );
                return createResponse(token, user);
            }
            throw new UnauthorizedException();
        });
    }

    private Mono<User> findByUsername(String username) {
        return userRepository.findByEmail(username)
                .switchIfEmpty(Mono.error(UnauthorizedException::new));
    }

    private Token getToken(String access, String refresh) {
        return Token.builder()
                .access(access)
                .refresh(refresh)
                .build();
    }

    private GetUserByIdResponse getUser(User user) {
        GetUserByIdResponse response = GetUserByIdResponse.builder().build();
        BeanUtils.copyProperties(user, response);
        return response;
    }

    private SignInResponse createResponse(Token token, User user) {
        return SignInResponse.builder()
                .token(token)
                .user(getUser(user))
                .build();
    }
}
