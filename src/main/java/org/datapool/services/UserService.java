package org.datapool.services;

import org.datapool.core.jwt.TokenObject;
import org.datapool.core.jwt.impl.JwtService;
import org.datapool.db.Project;
import org.datapool.db.ProjectJpaRepository;
import org.datapool.db.UserRepository;
import org.datapool.db.Users;
import org.datapool.dto.AuthRq;
import org.datapool.dto.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public String secret;
    @Autowired
    private ProjectJpaRepository projectJpaRepository;
    @Autowired
    private JwtService jwtService;

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public List<String> getProjectsList(long userId){
        List<Project> projectList = projectJpaRepository.findByOwnerId(userId);
        ArrayList<String> listForToken = new ArrayList<>();
        for (Project project : projectList){
            listForToken.add(project.getId());
        }
        return listForToken;
    }

    public UserService setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
        return this;
    }

    public String getSecret() {
        return secret;
    }

    public UserService setSecret(String secret) {
        this.secret = secret;
        return this;
    }

    public TokenObject decryptToken(String token){
        TokenObject tokenObject = jwtService.decryptToken(token);
        return tokenObject;
    }

    public Token auth(AuthRq auth){
        if (auth.getLogin()!=null && auth.getPass()!=null){
            if (!auth.getLogin().equals("") && !auth.getPass().equals("")){
                Optional<Users> user = Optional.ofNullable(userRepository.findByLogin(auth.getLogin()));
                if (user.isPresent()){
                    Users account = user.get();
                    if (auth.getPass().equals(account.getPassword()) && auth.getLogin().equals(account.getLogin())){
                        TokenObject tokenObject = new TokenObject();
                        tokenObject.setEmail(account.getEmail());
                        tokenObject.setLogin(account.getLogin());
                        tokenObject.setCreateDate(new Date().getTime());
                        tokenObject.setUserId(account.getId());

                        String token = jwtService.createToken(tokenObject);
                        Token authResponse = new Token(token);
                        authResponse.setEmail(account.getEmail());
                        authResponse.setLogin(account.getLogin());
                        return authResponse;
                    } else throw new InvalidParameterException();
                }
            }
        }
        throw new InvalidParameterException();
    }

    public String updateRemoteToken(AuthRq auth){
        if (auth.getLogin()!=null && auth.getPass()!=null){
            if (!auth.getLogin().equals("") && !auth.getPass().equals("")){
                Optional<Users> user = Optional.ofNullable(userRepository.findByLogin(auth.getLogin()));
                if (user.isPresent()){
                    Users account = user.get();
                    if (auth.getPass().equals(account.getPassword()) && auth.getLogin().equals(account.getLogin())){
                        TokenObject tokenObject = new TokenObject();
                        tokenObject.setEmail(account.getEmail());
                        tokenObject.setLogin(account.getLogin());
                        tokenObject.setCreateDate(new Date().getTime());
                        tokenObject.setUserId(account.getId());
                        List<String> projects = getProjectsList(tokenObject.getUserId());
                        String[] projectsForToken = projects.toArray(new String[0]);
                        String remoteToken = jwtService.remoteTokenBuilder(tokenObject, projectsForToken);
                        return remoteToken;
                    } else throw new InvalidParameterException();
                }
            }
        }
        throw new InvalidParameterException();
    }
    public String updateRemoteToken(String token){
        TokenObject tokenData = decryptToken(token);
        Optional<Users> user = userRepository.findById(tokenData.getUserId());
        if (user.isPresent()){
            Users account = user.get();
            TokenObject tokenObject = new TokenObject();
            tokenObject.setEmail(account.getEmail());
            tokenObject.setLogin(account.getLogin());
            tokenObject.setCreateDate(new Date().getTime());
            tokenObject.setUserId(account.getId());
            List<String> projects = getProjectsList(tokenObject.getUserId());
            String[] projectsForToken = projects.toArray(new String[0]);
            String remoteToken = jwtService.remoteTokenBuilder(tokenObject, projectsForToken);
            return remoteToken;
        }
        throw new InvalidParameterException();
    }
}
