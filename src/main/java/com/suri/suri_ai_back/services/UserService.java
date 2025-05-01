package com.suri.suri_ai_back.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import com.suri.suri_ai_back.exceptions.DuplicatedValueException;
import com.suri.suri_ai_back.exceptions.InvalidCredentialsException;
import com.suri.suri_ai_back.exceptions.ObjectNotFoundException;
import com.suri.suri_ai_back.exceptions.PermissionException;
import com.suri.suri_ai_back.models.User;
import com.suri.suri_ai_back.models.dtos.LoginRequestDTO;
import com.suri.suri_ai_back.models.dtos.UserCreateDTO;
import com.suri.suri_ai_back.models.dtos.UserUpdateDTO;
import com.suri.suri_ai_back.repositories.UserRepository;
import com.suri.suri_ai_back.util.Argon2Encoder;
import com.suri.suri_ai_back.util.CPFUtil;
import com.suri.suri_ai_back.util.EmailValidator;
import com.suri.suri_ai_back.util.PasswordValidator;
import com.suri.suri_ai_back.util.TelefoneUtil;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Argon2Encoder argon2Encoder;

    @Transactional
    public User create(User obj) {
        if(this.userRepository.findByCpf(CPFUtil.formatarCPF(obj.getCpf())).isPresent()) {
            throw new DuplicatedValueException("CPF em uso");
        }

        //validações
        obj.setId(null);
        CPFUtil.validarCPF(obj.getCpf());
        EmailValidator.validarEmail(obj.getEmail());
        PasswordValidator.validarSenha(obj.getPassword());
        TelefoneUtil.validarTelefones(obj.getTelefones(), "BR");

        //formatações
        obj.setCpf(CPFUtil.formatarCPF(obj.getCpf())); 
        obj.setTelefones(TelefoneUtil.formatarTelefones(obj.getTelefones(), "BR"));

        //encoder de senha
        obj.setPassword(argon2Encoder.encode(obj.getPassword()));

        return this.userRepository.save(obj);
    }

    public User findById(UUID id) {
        Optional<User> user = this.userRepository.findById(id);
        if(user.isPresent()) {
            return user.get();
        } else {
            throw new ObjectNotFoundException("Usuário com o id: " + id + " não encontrado");
        }
    }

    public User findByCpf(String cpf) {
        CPFUtil.validarCPF(cpf);
        cpf = CPFUtil.formatarCPF(cpf);
        Optional<User> user = this.userRepository.findByCpf(cpf);
        if(user.isPresent()) {
            return user.get();
        } else {
            throw new ObjectNotFoundException("Usuário com o cpf: " + cpf + "não encontrado");
        }
    }

    public User findByEmail(String email) {

        EmailValidator.validarEmail(email);

        Optional<User> user = this.userRepository.findByEmail(email);
        if(user.isPresent()) {
            return user.get();
        } else {
            throw new ObjectNotFoundException("Usuário com o email: " + email + " não encontrado");
        }
    }

    public List<User> findByNome(String nome) {
        List<User> user = this.userRepository.findByNomeContainingIgnoreCase(nome);
        if(user.size() > 0) {
            return user;
        } else {
            throw new ObjectNotFoundException("User não encontrado");
        }

    }

    public User findByCpfOrEmail(String login) {
        try {
            return this.findByCpf(login);
        } catch (Exception e) {
            try {
                return this.findByEmail(login);
            } catch (Exception ex) {
                throw new ObjectNotFoundException("Login inválido");
            }
        }
    }

    public List<User> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users;
    }

    public Set<String> findTelefones(UUID id) {
        User user = this.findById(id);
        return user.getTelefones();
    }

    @Transactional
    public User update(User obj, UserUpdateDTO update) {
        User newobj = this.findById(obj.getId());
        if (update.nome() != null){
            newobj.setNome(update.nome());
        }
        if (update.password() != null){
            PasswordValidator.validarSenha(update.password());
            newobj.setPassword(argon2Encoder.encode(update.password()));
        }
        if (update.telefones() != null){
            TelefoneUtil.validarTelefones(update.telefones(), "BR");
            newobj.setTelefones(TelefoneUtil.formatarTelefones(update.telefones(), "BR"));
        }
        return this.userRepository.save(newobj);
    }

    @Transactional
    public User update(User obj, LoginRequestDTO update) {
        User newobj = this.findById(obj.getId());
        newobj.setPassword(argon2Encoder.encode(update.password()));
        return this.userRepository.save(newobj);
    }

    @Transactional
    public User saveUser(User obj) {
        return this.userRepository.save(obj);
    }

    public void addTelefones(UUID id, Set<String> telefones) {
        User user = this.findById(id);
        TelefoneUtil.validarTelefones(telefones, "BR");
        user.getTelefones().addAll(TelefoneUtil.formatarTelefones(telefones, "BR"));
        this.userRepository.save(user);
    }

    public void deleteUser(UUID id) {
        User user = this.findById(id);
        this.userRepository.delete(user);
    }

    public void deleteTelefones(UUID id, Set<String> telefones) {
        User user = this.findById(id);
        TelefoneUtil.validarTelefones(telefones, "BR");
        user.getTelefones().removeAll(TelefoneUtil.formatarTelefones(telefones, "BR"));
        this.userRepository.save(user);
    }

    public boolean isLoginCorrect(LoginRequestDTO loginRequest, User user) {
        Argon2Encoder argon2Encoder = new Argon2Encoder();
        if (argon2Encoder.matches(loginRequest.password(), user.getPassword())) {
            return true;
        } else {
            throw new InvalidCredentialsException("Usuário ou senha incorreto(s)");
        }
    }

    public void hasPermision(UUID id, JwtAuthenticationToken token) {
        User obj = this.findById(id);
        if (obj.getId().toString().equals(token.getName()) /*|| token.getAuthorities().toString().contains("SCOPE_ADMIN")*/) {
            return;
        } else {
            throw new PermissionException("Sem permissão para executar esta ação");
        }
    }

    public User fromDTO(@Valid UserCreateDTO obj) {
        User user = new User();
        user.setId(null);
        user.setTelefones(obj.telefones());
        user.setCpf(obj.cpf());
        user.setEmail(obj.email());
        user.setNome(obj.nome());
        user.setPassword(obj.password());
        return user;
    }
}
