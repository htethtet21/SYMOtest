package com.example.authjwtdemo.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.MappedCollection;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

@Getter
@Setter
@ToString
public class User {
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

@MappedCollection
private final Set<Token> tokens=new HashSet<>();
@MappedCollection private final Set<PasswordRecovery> passwordRecoveries=new HashSet<>();
@PersistenceConstructor
    private User(Long id, String firstName, String lastName, String email, String password,Collection<Token> tokens ,Collection<PasswordRecovery> passwordRecoveries) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.tokens.addAll(tokens);
        this.passwordRecoveries.addAll(passwordRecoveries);
    }

    public static User of (String firstName, String lastName, String email, String password){
        return new User(null,firstName,lastName,email,password, Collections.emptyList(),Collections.emptyList());
    }


    public void addToken(Token token){
    this.tokens.add(token);

    }

    public Boolean removeToken(Token token){
    return this.tokens.remove(token);
    }


    public Boolean removeTokenIf(Predicate<? super Token> predicate){
    return this.tokens.removeIf(predicate);
    }
    public void addPasswordRecovery(PasswordRecovery passwordRecovery){
    this.passwordRecoveries.add(passwordRecovery);
    }

    public  Boolean removePasswordRecovery(PasswordRecovery passwordRecovery){
    return this.passwordRecoveries.remove(passwordRecovery);
    }
    public Boolean removePasswordRecoveryIf(Predicate<? super PasswordRecovery> predicate){
        return this.passwordRecoveries.removeIf(predicate);
    }



}
